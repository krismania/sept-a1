package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.sun.media.jfxmedia.logging.Logger;

import database.model.Account;
import database.model.Booking;
import database.model.BusinessOwner;
import database.model.Customer;
import database.model.Employee;
import database.model.Service;
import database.model.Shift;
import database.model.TimeSpan;

public class BusinessDatabase extends Database implements DBInterface
{

	public BusinessDatabase(String dbName) {
		super(dbName);
	}


	/**PUBLIC API**/

	/**
	 * Write an account to the DB - appropriately decides which table to write to.
	 * @author James
	 * @author krismania
	 */
	@Override
	public boolean addAccount(Account account, String password)
	{
		// first, check the username
		if (getAccount(account.username) == null)
		{
			// if it doesn't exist, add it.
			if(account instanceof Customer)
			{			
				return insert((Customer) account, password);
			}
			else if(account instanceof BusinessOwner)
			{
				logger.fine("Added business owner to business: " + account.username );
				return insert((BusinessOwner) account, password);
			}
		}

		return false;
	}

	@Override
	public boolean addEmployee(Employee employee)
	{
		return insert(employee);
	}

	/**
	 * Takes LocalTime objects representing the start and end of 2 time periods,
	 * and returns true if those periods overlap. Arguments should be supplied in
	 * the following order: {@code start1, end1, start2, end2}.
	 * @author krismania
	 */
	private boolean overlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2)
	{
		if (end1.compareTo(start2) <= 0)
		{
			// period 1 ends before period 2 starts
			return false;
		}
		else if (start1.compareTo(end2) >= 0)
		{
			// period 1 starts after period 2 ends
			return false;
		}
		else
		{
			// overlap
			return true;
		}
	}

	/**
	 * Checks if there is already a shift for this employee with an overlapping
	 * time before adding it to the db.
	 * @author krismania
	 */
	@Override
	public boolean addShift(Shift newShift)
	{
		ArrayList<Shift> shifts = new ArrayList<Shift>();
		// get all other shifts for this employee on this day
		try (Statement stmt = c.createStatement())
		{
			String sql = "SELECT * FROM Shift WHERE EmpID = %d AND Day = '%s'";
			try (ResultSet rs = stmt.executeQuery(String.format(sql, newShift.employeeID, newShift.getDay().toString())))
			{
				while (rs.next())
				{
					int id = rs.getInt("ShiftID");
					int empId = rs.getInt("EmpID");
					DayOfWeek day = DayOfWeek.valueOf(rs.getString("Day"));
					LocalTime start = LocalTime.ofSecondOfDay(rs.getInt("Start"));
					LocalTime end = LocalTime.ofSecondOfDay(rs.getInt("End"));

					shifts.add(new Shift(id, empId, day, start, end));
				}
				logger.info("Found " + shifts.size() + " shifts for emp " + newShift.employeeID + " on " + newShift.getDay().toString());
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}

		// check that the shift being added does not overlap with any of these
		for (Shift shift: shifts)
		{
			logger.info("old shift: " + shift.getStart() + " " + shift.getEnd() + 
					"\nnew shift: " + newShift.getStart() + " " + newShift.getEnd());

			if (overlap(newShift.getStart(), newShift.getEnd(), shift.getStart(), shift.getEnd()))
			{
				// shifts overlap, this employee already has a shift at this time
				return false;
			}
		}
		return insert(newShift);
	}

	/**
	 * Add a booking to the DB. Checks if this customer already has a booking on
	 * the same date at an overlapping time, or if the employee is already booked.
	 * @author krismania
	 */
	@Override
	public boolean addBooking(Booking b)
	{
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		// get all other bookings for this customer on this date
		try (Statement stmt = c.createStatement())
		{			
			String sql = "SELECT * FROM Booking WHERE Date = '%s' AND (Customer = '%s' OR EmpID = %d)";
			try (ResultSet rs = stmt.executeQuery(String.format(sql, b.getDate(), b.getCustomer(), b.getEmployeeID())))
			{
				while (rs.next())
				{
					int id = rs.getInt("BookingID");
					String customer = rs.getString("Customer");
					int employeeID = rs.getInt("EmpID");
					LocalDate date = LocalDate.parse(rs.getString("Date"));
					LocalTime start = LocalTime.ofSecondOfDay((rs.getInt("Start")));
					Service service = getService(rs.getInt("ServiceID"));
					
					// construct the object & add to list. -kg
					bookings.add(new Booking(id, customer, employeeID, date, start, service));
				}
				logger.info("Found " + bookings.size() + " bookings for " + b.getCustomer() + " on " + b.getDate());
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		// check that the booking being added does not overlap with any of these
		for (Booking booking: bookings)
		{
			if (overlap(b.getStart(), b.getEnd(), booking.getStart(), booking.getEnd()))
			{
				// bookings overlap, this customer or employee already has a booking at this time
				return false;
			}
		}
		
		return insert(b);
	}
	
	@Override
	public boolean addService(Service service)
	{
		return insert(service);
	}
	
	/**
	 * @author krismania
	 */
	@Override
	public boolean updateService(Service service)
	{
		// update the given service in the db
		try (Statement stmt = c.createStatement())
		{
			String sql = String.format("UPDATE Service SET Name = '%s', Duration = %d WHERE ServiceID = %d", 
							service.getName(), service.getDuration().toMinutes(), service.ID);
			
			if (stmt.executeUpdate(sql) == 1) return true; // only 1 row should be affected.
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		return false;
	}

	/**
	 * @author krismania
	 */
	@Override
	public boolean deleteService(Service s)
	{
		try (Statement stmt = c.createStatement())
		{
			String sql = String.format("DELETE FROM Service WHERE ServiceID = %d", s.ID);
			if (stmt.executeUpdate(sql) == 1) return true;
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		return false;
	}

	/**
	 * @author krismania
	 */
	@Override
	public Employee buildEmployee()
	{
		// find the highest current ID
		int maxID = 0;

		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(EmpID) AS id FROM Employee"))
			{
				if (rs.next())
				{
					maxID =  rs.getInt("id");
				}
			}

		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}

		return new Employee(maxID+1,"", "", "", "");
	}

	/**
	 * @author krismania
	 */
	@Override
	public Shift buildShift(int employeeID)
	{
		// find the highest ID
		int maxID = 0;

		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(ShiftID) AS id FROM Shift"))
			{
				if (rs.next())
				{
					maxID =  rs.getInt("id");
				}
			}

		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}

		return new Shift(maxID+1, employeeID, null, null, null);
	}

	/**
	 * find the highest ID and create a new service with the next one.
	 * @author krismania
	 */
	@Override
	public Service buildService()
	{
		int maxID = 0;
		
		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(ServiceID) AS id FROM Service"))
			{
				if (rs.next())
				{
					maxID = rs.getInt("id");
				}
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		// add 1 to the max ID for the new ID
		int newID = maxID + 1;
		
		logger.info("Building service with ID " + newID);
		return new Service(newID, null, null);
	}
	
	/**
	 * @author James
	 */
	@Override
	public Booking buildBooking() {
		// find the highest ID
		int maxID = 0;

		try {
			Statement stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(BookingID) AS id FROM Booking")) {
				if (rs.next()) {
					maxID = rs.getInt("id");
				}
			}

		} catch (SQLException e) {
			logger.warning(e.toString());
		}

		return new Booking(maxID + 1, null, 0, null, null, null);
	}

	/**
	 * @author krismania
	 *//*
	@Override
	public Account getAccount(String username)
	{		
		Class<? extends Account> type = validateUsername(username);

		// check if type is null
		if (type == null)
		{
			return null;
		}

		try
		{
			Statement stmt = c.createStatement();
			if (type.equals(Customer.class))
			{
				try (ResultSet customerQuery = stmt.executeQuery(
						String.format("SELECT * FROM Customer WHERE Username = '%s'", username)))
				{
					if (customerQuery.next())
					{
						// get info
						String first = customerQuery.getString("Firstname");
						String last = customerQuery.getString("Lastname");
						String email = customerQuery.getString("Email");
						String phone = customerQuery.getString("Phone");
						String usr = customerQuery.getString("Username");

						// create customer obj and return it
						return new Customer(usr, first, last, email, phone);
					}
				}
			}
			else if (type.equals(BusinessOwner.class))
			{
				try (ResultSet boQuery = stmt.executeQuery(
						String.format("SELECT * FROM BusinessOwner WHERE Username = '%s'", username)))
				{
					if (boQuery.next())
					{
						// get info
						String usr = boQuery.getString("Username");
						String businessName = boQuery.getString("BusinessName");
						String ownerName = boQuery.getString("Name");
						String address = boQuery.getString("Address");
						String phone = boQuery.getString("Phone");

						// create obj and return
						return new BusinessOwner(usr, businessName, ownerName, address, phone);
					}
				}
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}

		return null;
	}*/
	
	/**
	 * @author krismania
	 */
	@Override
	public Service getService(int id)
	{
		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM Service WHERE ServiceID = " + id))
			{
				while (rs.next())
				{
					String name = rs.getString("Name");
					Duration duration = Duration.ofMinutes(rs.getInt("Duration"));
									
					return new Service(id, name, duration);
				}
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		return null;
	}
	
	/**
	 * @author krismania
	 */
	@Override
	public ArrayList<Service> getServices()
	{
		ArrayList<Service> services = new ArrayList<Service>();
		
		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM Service"))
			{
				while (rs.next())
				{
					int id = rs.getInt("ServiceID");
					String name = rs.getString("Name");
					Duration duration = Duration.ofMinutes(rs.getInt("Duration"));
									
					services.add(new Service(id, name, duration));
				}
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		return services;
	}

	/**
	 * @author krismania
	 */
	@Override
	public Employee getEmployee(int id)
	{
		try
		{
			Statement stmt = c.createStatement();

			try (ResultSet rs = stmt.executeQuery(
					String.format("SELECT * FROM Employee WHERE EmpID = '%s'", id)))
			{
				if (rs.next())
				{
					String first = rs.getString("Firstname");
					String last = rs.getString("Lastname");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");

					return new Employee(id, first, last, email, phone);
				}
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}

		return null;
	}

	/**
	 * @author James
	 * @author krismania
	 */
	@Override
	public ArrayList<Employee> getAllEmployees()
	{
		ArrayList<Employee> roster = new ArrayList<Employee>();

		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM Employee"))
			{
				while(rs.next())
				{
					String first = rs.getString("Firstname");
					String last = rs.getString("Lastname");
					String email = rs.getString("Email");
					String phone = rs.getString("Phone");
					int EmpID = rs.getInt("EmpID");

					Employee current = new Employee(EmpID, first, last, email, phone);

					roster.add(current);
				}
			}

		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		return roster;
	}
	
	/**
	 * Returns an ArrayList of bookings in the database, restricted by the given
	 * {@code constraint}. The constraint arg is added after the {@code WHERE}
	 * clause in the SQL query.
	 * @author krismania
	 */
	private ArrayList<Booking> getBookings(String constraint)
	{
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		try (Statement stmt = c.createStatement())
		{
			try (ResultSet bookingQuery = stmt.executeQuery(
							"SELECT * FROM Booking WHERE " + constraint))
			{
				while (bookingQuery.next())
				{
					int id = bookingQuery.getInt("BookingID");
					String customer = bookingQuery.getString("Customer");
					int employeeID = bookingQuery.getInt("EmpID");
					LocalDate date = LocalDate.parse(bookingQuery.getString("Date"));
					LocalTime start = LocalTime.ofSecondOfDay((bookingQuery.getInt("Start")));
					Service service = getService(bookingQuery.getInt("ServiceID"));
					
					// construct the object & add to list. -kg
					bookings.add(new Booking(id, customer, employeeID, date, start, service));
				}
			}
			
			stmt.close();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		return bookings;
	}

	@Override
	public ArrayList<Booking> getPastBookings()
	{
		return getBookings("Date < DATE('now')");
	}

	@Override
	public ArrayList<Booking> getFutureBookings()
	{
		return getBookings("Date >= DATE('now')");
	}

	/**
	 * Joint login function, may return either a Customer or BusinessOwner.
	 * @author krismania
	 *//*
	@Override
	public Account login(String username, String password)
	{
		boolean valid = false;
		Account account = getAccount(username);

		if (account instanceof Customer)
		{
			valid = validatePassword(username, password, "Customer");
		}
		else if (account instanceof BusinessOwner)
		{
			valid = validatePassword(username, password, "BusinessOwner");
		}

		if (valid) return account;
		else return null;
	}*/

	/**
	 * Returns a shift by it's ID
	 * @author krismania
	 */
	@Override
	public Shift getShift(int shiftID)
	{
		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery(
					String.format("SELECT * FROM Shift WHERE ShiftID = '%s'", shiftID)))
			{
				if (rs.next())
				{
					String day = rs.getString("Day");
					int empId = rs.getInt("EmpID");
					LocalTime start = LocalTime.ofSecondOfDay(rs.getInt("Start"));
					LocalTime end = LocalTime.ofSecondOfDay(rs.getInt("End"));

					return new Shift(shiftID, empId, DayOfWeek.valueOf(day), start, end);
				}
			}

		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}

		return null;
	}

	@Override
	public ArrayList<Shift> getShifts(DayOfWeek onDay)
	{
		ArrayList<Shift> shifts= new ArrayList<Shift>();

		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM Shift WHERE Day = '" + onDay + "'"))
			{
				while (rs.next())
				{
					int id = rs.getInt("ShiftID");
					int empId = rs.getInt("EmpID");
					DayOfWeek day = DayOfWeek.valueOf(rs.getString("Day"));
					LocalTime start = LocalTime.ofSecondOfDay(rs.getInt("Start"));
					LocalTime end = LocalTime.ofSecondOfDay(rs.getInt("End"));

					Shift shift = new Shift(id, empId, day, start, end);
					shifts.add(shift);
				}
			}
		}
		catch (SQLException e)
		{
			logger.warning("SQL Exception in getShifts: " + e);
		}

		return shifts;
	}
	
	public BusinessOwner getBusinessOwner()
	{
		BusinessOwner businessOwner = null;
		
		try
		{
			Statement stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT * FROM BusinessOwner";
			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
		        //Retrieve by column name         
	         	String usr = rs.getString("Username");
				String businessName = rs.getString("BusinessName");
				String ownerName = rs.getString("Name");
				String address = rs.getString("Address");
				String phone = rs.getString("Phone");

				// build obj and add to list. -kg
				businessOwner = new BusinessOwner(usr, businessName, ownerName, address, phone);
			}
		}
		catch(SQLException e)
		{
			//JM Handle errors for JDBC
		    logger.warning(e.toString());
		}
		catch(Exception e)
		{
		    //JM Handle errors for Class.forName
			logger.warning(e.toString());
		}
		
		return businessOwner;
	}
	
	/**
	 * Returns a {@link TimeSpan} containing the opening and closing hours on a given day.
	 * @author krismania
	 */
	public TimeSpan getHours(DayOfWeek day)
	{
		try (Statement stmt = c.createStatement())
		{
			String sql = String.format("SELECT * FROM Hours WHERE Day = '%s'", day.toString());
			try (ResultSet rs = stmt.executeQuery(sql))
			{
				if (rs.next())
				{
					LocalTime open = LocalTime.ofSecondOfDay(rs.getInt("Open"));
					LocalTime close = LocalTime.ofSecondOfDay(rs.getInt("Close"));
					
					TimeSpan hours = new TimeSpan(open, close);
					
					logger.info("Open hours: " + hours);
					return hours;
				}
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		return null;
	}
	
	/**
	 * Sets the opening and closing hours for a given day using a {@link TimeSpan}.
	 * The {@code hours} argument may be null to set the business as being closed
	 * @author krismania
	 */
	public boolean setHours(DayOfWeek day, TimeSpan hours)
	{
		try (Statement stmt = c.createStatement())
		{
			// Remove the old record
			String sql = String.format("DELETE FROM Hours WHERE Day = '%s'", day.toString());
			stmt.execute(sql);
			
			// add a new one if hours is not null
			if (hours != null)
			{
				sql = String.format("INSERT INTO Hours (Day, Open, Close) VALUES ('%s', %d, %d)",
								day.toString(), hours.start.toSecondOfDay(), hours.end.toSecondOfDay());
				stmt.execute(sql);
				
				return true; // on success
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		return false;
	}

	/**
	 * Helper method for inserting a {@link Booking} object into the db
	 * @author krismania
	 */
	private boolean insert(Booking b)
	{
		return insert("Booking", Integer.toString(b.ID), b.getCustomer(), 
						Integer.toString(b.getEmployeeID()), b.getDate().toString(), 
						Integer.toString(b.getStart().toSecondOfDay()),
						Integer.toString(b.getService().ID));
	}

	/**
	 * Helper method for inserting a {@link Service} object into the db
	 * @author krismania
	 */
	private boolean insert(Service s)
	{
		return insert("Service", Integer.toString(s.ID), s.getName(),
						Long.toString(s.getDuration().toMinutes()));
	}
	
	/**
	 * Helper method for inserting a {@link BusinessOwner} object into the db
	 * @author krismania
	 */
	private boolean insert(BusinessOwner bo, String password)
	{
		return insert("BusinessOwner", bo.username, password, bo.getBusinessName(), 
				bo.getName(), bo.getAddress(), bo.getPhoneNumber(), 
				"BusinessOwner");
	}

	/**
	 * Helper method for inserting a {@link Employee} object into the db
	 * @author krismania
	 */
	private boolean insert(Employee e)
	{
		return insert("Employee", Integer.toString(e.ID), e.getFirstName(), 
				e.getLastName(), e.getEmail(), e.getPhoneNumber());
	}

	/**
	 * Helper method for inserting a {@link Shift} object into the db
	 * @author krismania
	 */
	private boolean insert(Shift s)
	{
		return insert("Shift", Integer.toString(s.ID), 
				Integer.toString(s.employeeID), s.getDay().toString(), 
				Integer.toString(s.getStart().toSecondOfDay()),
				Integer.toString(s.getEnd().toSecondOfDay()));
	}

	/**
	 * Helper method for inserting a {@link Customer} object into the db
	 * @author krismania
	 */
	private boolean insert(Customer c, String password)
	{
		return insert("Customer", c.username, password, c.getFirstName(), 
				c.getLastName(), c.getEmail(), c.getPhoneNumber(), 
				"Customer");
	}

	//***VALIDATION METHODS***

	/**
	 * Returns a class object describing which type of user {@code username} is,
	 * or null if the username is not found.
	 * @author James
	 * @author krismania
	 *//*
	private Class<? extends Account> validateUsername(String username) 
	{		
		String query = "SELECT Username, Type "
				+ "FROM (SELECT Username, Type from Customer "
				+ "UNION "
				+ "SELECT Username, Type from BusinessOwner"
				+ ") a "
				+ "WHERE Username = '"+username+"'";

		try 
		{
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			if(rs.next())
			{
				String type = rs.getString("Type");

				if(type.equals("BusinessOwner"))
				{
					return BusinessOwner.class;
				}
				else if (type.equals("Customer"))
				{
					return Customer.class;
				}
			}			

		} catch (SQLException e) {
			//JM Catch if table already exists
			logger.warning(e.toString());
		} catch (Exception e) {
			//JM Handles errors for Class.forName
			logger.warning(e.toString());
		}
		return null;
	}

	*//**
	 * returns true if the username & password match in the given table.
	 * @author krismania
	 * @author James
	 *//*
	private boolean validatePassword(String username, String password, String tableName)
	{
		String sql = String.format("SELECT password FROM %s WHERE username='%s'", tableName, username);

		try
		{
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			if (rs.getString(1).equals(password)) {
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}

		return false;
	}*/
}
