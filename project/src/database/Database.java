package database;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Account;
import model.Booking;
import model.BusinessOwner;
import model.Customer;
import model.Employee;
import model.Service;
import model.Shift;

public class Database implements DBInterface {
	
	/**
	 * DB Connection object
	 */
	private Connection c = null;

	/**
	 * The name of the DB file, excluding it's extension
	 */
	private String dbName;
	
	private Logger logger;
	
	/**
	 * Instantiates the database, which will read to and from the .db file with
	 * the given name. If the database doesn't exist, it is created and seeded.
	 * @author James
	 * @author krismania
	 */
	public Database(String dbName)
	{
		// get the logger & set level
		logger = Logger.getLogger(getClass().getName());
		
		// set up db
		this.dbName = dbName;
		openConnection();
		
		CreateDatabase();

		logger.info("Instantiated DB");
	}
	
	/**
	 * Close the DB Connection.
	 * @author krismania
	 */
	@Override
	public void close()
	{
		closeConnection();
	}

	//***PUBLIC API***

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
	 */
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
	 * @deprecated Controller method has been deprecated.
	 */
	@Override @Deprecated
	public ArrayList<Customer> getAllCustomers()
	{
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		try
		{
			Statement stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT * FROM Customer";
			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
		         //Retrieve by column name
		         String first = rs.getString("Firstname");
		         String last = rs.getString("Lastname");
		         String email = rs.getString("Email");
		         String phone = rs.getString("Phone");
		         String Username = rs.getString("Username");

		         // create Customer object & add to list. -kg
		         customers.add(new Customer(Username, first, last, email, phone));
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
		
		return customers;
	}
	
	/**
	 * @author James
	 * @author krismania
	 * @deprecated Controller has been deprecated
	 */
	@Override @Deprecated
	public ArrayList<BusinessOwner> getAllBusinessOwners()
	{
		ArrayList<BusinessOwner> businessOwners = new ArrayList<BusinessOwner>();
		
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
				businessOwners.add(new BusinessOwner(usr, businessName, ownerName, address, phone));
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
		
		return businessOwners;
	}
	
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

//	TODO: remove this -kg
//	@Override
//	public ArrayList<String> getEmployeeWorkingOnDay(LocalDate date)
//	{
//		String day = date.getDayOfWeek().toString();
//		ArrayList<String> Workers = new ArrayList<String>();
//		
//		try
//		{
//			Statement stmt = c.createStatement();
//			
//			String sql = String.format("SELECT * FROM Shift WHERE Day = '%s'", day);
//			
//			ResultSet rs = stmt.executeQuery(sql);
//			
//			while(rs.next())
//			{
//		         //JM Retrieve by column name
//		         
//				String empID = Integer.toString(rs.getInt("EmpID"));
//		         
//		         // add it to the list
//		         Workers.add(empID);
//		    }
//		}
//		catch(SQLException e)
//		{
//			//JM Handle errors for JDBC
//			logger.warning(e.toString());
//		}
//		catch(Exception e)
//		{
//		    //JM Handle errors for Class.forName
//			logger.warning(e.toString());
//		}
//		return Workers;
//	}
	
//	/**
//	 * @author James
//	 * TODO: Remove this -kg
//	 */
//	@Override
//	public boolean shiftExists(DayOfWeek day, ShiftTime time, int empID)
//	{
//		boolean shiftExists = false;
//		try
//		{
//			Statement stmt = c.createStatement();
//			try (ResultSet rs = stmt.executeQuery(
//							String.format("SELECT * FROM Shift WHERE EmpID = '%s' AND"
//									+ " Day = '%s' AND Time = '%s'", empID, day.toString(),
//									time.toString().toUpperCase())))
//			{
//				while (rs.next())
//				{
//					shiftExists = true;
//				}
//			}
//			
//		}
//		catch (SQLException e)
//		{
//			logger.warning(e.toString());
//		}
//		return shiftExists;
//	}
	
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

//	TODO: remove this -kg
//	@Override
//	public ArrayList<Shift> getShifts(int EmpID, String Day)
//	{
//		ArrayList<Shift> Shifts = new ArrayList<Shift>();
//		try
//		{
//			Statement stmt = c.createStatement();
//			
//			String sql = String.format("SELECT * FROM Shift WHERE EmpID = '%s' AND Day = '%s'", EmpID, Day);
//			
//			ResultSet rs = stmt.executeQuery(sql);
//			
//			while(rs.next())
//			{
//		         //JM Retrieve by column name
//		         DayOfWeek day = DayOfWeek.valueOf(rs.getString("Day").toUpperCase());
//		         int time = rs.getInt("Time");
//		         int shiftID = rs.getInt("Shift_ID");
//		         LocalTime convertTime = LocalTime.ofSecondOfDay(time);
//		         // create shift object. -kg
//		         Shift shift = new Shift(shiftID, EmpID, day, convertTime);
//		         
//		         // add it to the list
//		         Shifts.add(shift);
//		    }
//		}
//		catch(SQLException e)
//		{
//			//JM Handle errors for JDBC
//			logger.warning(e.toString());
//		}
//		catch(Exception e)
//		{
//		    //JM Handle errors for Class.forName
//			logger.warning(e.toString());
//		}
//		return Shifts;
//	}

//	/**
//	 * @author krismania
//	 * TODO: Check if method is used or depreciated. 
//	 * TODO: Remove this -kg
//	 */
//	@Override
//	public TreeMap<Shift, Booking> getShiftBookings()
//	{
//		// get the list of employees
//		logger.info("getting employees");
//		ArrayList<Employee> employees = getAllEmployees();
//		
//		// build the list of all shifts
//		logger.info("getting shifts");
//		ArrayList<Shift> shifts = new ArrayList<Shift>();
//		for (Employee employee : employees)
//		{
//			//shifts.addAll(getShifts(employee.ID));
//		}
//		
//		// get the list of bookings in the next 7 days
//		logger.info("getting upcoming bookings");
//		ArrayList<Booking> bookings = getBookings("Date >= DATE('now') AND Date < DATE('now', '7 days')");
//		
//		// create hashmap to decide which shifts are booked within the next 7 days
//		TreeMap<Shift, Booking> shiftBookings = new TreeMap<Shift, Booking>();
//		
//		// iterate over each shift and decide if it's been booked
//		for (Shift shift : shifts)
//		{
//			logger.fine("looping shift " + shift.ID);
//			// find a booking with matching details
//			boolean found = false;
//			for (Booking booking : bookings)
//			{
//				logger.fine(shift.toString() + " | " + booking.toString());
//				// figure out weekday of booking
//				if (booking.getDay() == shift.getDay() && booking.getTime() == shift.getTime() &&
//								booking.getEmployeeID() == shift.employeeID)
//				{
//					// booking and shift match, to hash map
//					shiftBookings.put(shift, booking);
//					found = true;
//					break;
//				}
//			}
//			// otherwise, enter null as the booking
//			if (!found) shiftBookings.put(shift, null);
//			logger.fine("found match: " + found);
//		}
//		
//		return shiftBookings;
//	}
	
	
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
					String customer = bookingQuery.getString("customerID");
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
	 */
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
	}

	
	//***CREATE METHODS***
	
	/**
	 * Create the database
	 * TODO: better documentation
	 * @author James
	 * @author krismania
	 */
	private void CreateDatabase()
	{
		//JM Initialize a connection
		try
		{

			// test if the db is empty. -kg
			boolean empty;
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type = 'table'");
			rs.next();
			empty = (rs.getInt(1) == 0);
			rs.close();
			
			if (empty)
			{
				// temporarily commenting out test data creation. -kg
				// if DB is empty, create the required tables.
				createTables();
				// createTestData();
			}
			
		}
		catch (Exception e)
		{
			logger.severe(e.toString());
			System.exit(0);
		}
	}
	
	
	/**
	 * Creates a table within the database
	 * TODO: better documentation
	 * @param strings a variable number of strings
	 * @author James
	 * @deprecated Method too complicated, use native SQL table creation instead. -kg
	 */
	@Deprecated
	private void CreateDatabaseTable(String... strings)
	{
		int primaryKeyId = 1;
		StringBuilder strBuilder = new StringBuilder();
		
		for(int i = 0; i < strings.length; i++)
		{
			
			//JM If first element of array
			if(i==0) 
			{
				//JM Insert create table statement, and open bracket.
				strBuilder.append("CREATE TABLE " + strings[i] + "(");			
			}
			//JM If last element of array
			else if(i+primaryKeyId == strings.length)
			{
				//JM Insert Primary Key element and close bracket [!].
				if(i+1 == strings.length)
				{
					strBuilder.append("PRIMARY KEY (" + strings[i] + "))");			
				}
			}
			//JM If any element in between first and last
			else
			{
				//JM Split with a comma
				strBuilder.append(strings[i] + ", ");
			}
		}
		
		//JM Temporary work around - may need to change in Assignment 2
		//JM If table is schedule
		if(strings[0].equals("Shift"))
		{
			//Delete previous ) and add foreign key.
			strBuilder.deleteCharAt(strBuilder.length() - 1);
			strBuilder.append(", FOREIGN KEY (EmpID) references"
					+ " Employee (EmpID))");	
		}
		
		else if(strings[0].equals("Booking"))
		{
			strBuilder.deleteCharAt(strBuilder.length() - 1);
			strBuilder.append(", FOREIGN KEY (EmpID) references"
					+ " Employee (EmpID))");
		}
		
		String sql = strBuilder.toString();
		
		try 
		{
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sql);
		
		} catch (SQLException e) {
			//JM Catch if table already exists
			logger.warning(e.toString());
			
		} catch (Exception e) {
			//JM Handles errors for Class.forName
			logger.warning(e.toString());
		}
		
	}
	
	/**
	 * Insert the given values into the specified table.
	 * @param table The database table to insert into
	 * @param values Values to insert
	 * @author krismania
	 */
	private boolean insert(String table, String...values)
	{
		// prepare values
		for (int i = 0; i < values.length; i++)
		{
			// double up existing single quotes to escape them
			values[i] = values[i].replaceAll("'", "''");
			// add single quotes around each value
			values[i] = "'" + values[i] + "'";
		}
		
		// create value string
		String valueString = String.join(",", values);
		
		// create the query
		String query = "INSERT INTO " + table + " VALUES(" + valueString + ")";
		
		logger.fine("Executing query: " + query);
		
		try
		{
			Statement stmt = c.createStatement();
			stmt.execute(query);
			return true;
		}
		catch (SQLException e)
		{
			logger.warning("SQL Exception: " + e.toString());
			return false;
		}
	}
	
	
	/* INSERT HELPERS */
	
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
	
	private boolean insert(Service s)
	{
		return insert("Service", Integer.toString(s.ID), s.getName(),
						Long.toString(s.getDuration().toMinutes()));
	}
	
	/**
	 * Insert data into the database
	 * @author James
	 * @deprecated Use {@link #insert(String, String...)} instead.
	 */
	@Deprecated
	private boolean CreateDataEntry(String...strings) 
	{

		StringBuilder strBuilder = new StringBuilder();
		
		for(int i = 0; i < strings.length; i++)
		{
			//JM If first element of array
			if(i==0) 
			{
				//JM Insert into statement, with values and open bracket.
				strBuilder.append("INSERT INTO " + strings[i] + " VALUES (");			
			}
			//JM If last element of array
			else if(i+1 == strings.length)
			{
				//JM Add close bracket.
				strBuilder.append("'" + strings[i] + "')");			
			}
			//JM If any element in between first and last
			else
			{
				//JM Split with a comma
				strBuilder.append("'"+ strings[i] + "', ");
			}
		}
		
		//JM Convert string array, into a string.
		String sql = strBuilder.toString();
		try
		{
			Statement stmt = c.createStatement();
			stmt.executeUpdate(sql);
			return true;
		} catch(SQLException e) {
			//JM Handle errors for JDBC
			logger.warning(e.toString());
			return false;
		} catch(Exception e) {
		    //JM Handle errors for Class.forName
			logger.warning(e.toString());
		}
		return false;
	}
	
//	/**
//	 * TODO: better documentation
//	 * @author James
//	 */
//	private boolean CreateShift(DayOfWeek day, LocalTime time, int iD, int employeeID) 
//	{
//		String sql = "INSERT INTO Shift VALUES ('"
//				+ day.name() + "', '" + time.toSecondOfDay() + "', '" + iD + "'"
//						+ ", '" + employeeID + "')";
//		
//		try
//		{
//			Statement stmt = c.createStatement();
//			stmt.executeUpdate(sql);
//
//			logger.info("Shift Created - Day: " +day +", Time: " +time.toSecondOfDay() + ", ID: " + iD+", EmpID: " +employeeID);
//			return true;
//		} catch(SQLException e) {
//			//JM Handle errors for JDBC
//			logger.warning(e.toString());
//			return false;
//		} catch(Exception e) {
//			logger.warning(e.toString());
//		    e.printStackTrace();
//		}
//		return false;
//	}
	
//***VALIDATION METHODS***

	/**
	 * Returns a class object describing which type of user {@code username} is,
	 * or null if the username is not found.
	 * @author James
	 * @author krismania
	 */
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
	
	/**
	 * returns true if the username & password match in the given table.
	 * @author krismania
	 * @author James
	 */
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
	}
	
//***RETRIEVE METHODS***
	
	/**
	 * Temp method to find the highest ID
	 * TODO: this may need to be removed
	 * @author krismania
	 * @deprecated
	 */
	@Deprecated
	private String getLastEmployeeID()
	{
		String id = "E000"; // if no employee is found, E000 will be returned
		try
		{
			Statement stmt = c.createStatement();
			
			String sql = "SELECT EmpID FROM Employee ORDER BY EmpID DESC";
			ResultSet rs = stmt.executeQuery(sql);
			
			// we only care about the first result. -kg
			rs.next();
			id = rs.getString("EmpID");
			
		}
		catch(SQLException e)
		{
			//JM Handle errors for JDBC
		    e.printStackTrace();
		}
		catch(Exception e)
		{
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
		
		return id;
	}
	
	/**
	 * Temp method to find the highest ID
	 * TODO: this WILL need to be removed
	 * @author krismania
	 * @deprecated
	 */
	@Deprecated
	private String getLastShiftID()
	{
		String id = "S000"; // if no employee is found, E000 will be returned
		try
		{
			Statement stmt = c.createStatement();
			
			String sql = "SELECT Shift_ID FROM Schedule ORDER BY Shift_ID DESC";
			ResultSet rs = stmt.executeQuery(sql);
			
			// we only care about the first result. -kg
			rs.next();
			id = rs.getString("Shift_ID");
			
		}
		catch(SQLException e)
		{
			//JM Handle errors for JDBC
		    e.printStackTrace();
		}
		catch(Exception e)
		{
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
		
		return id;
	}
	
	//***CONNECTION METHODS***

	/**
	 * TODO: document this
	 * @author James
	 */
	private boolean openConnection()
	{
		// added try-catch to capture sqlException here. -kg
		try
		{
			c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
			if(c != null) 
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.severe("DB Could not open: SQLException");
		}
		return false;
	}
	
	/**
	 * TODO: document this
	 * @author James
	 */
	private boolean closeConnection()
	{
		try
		{
			if(c != null)
			{
				c.close();
				c = null;
				logger.info("Closed connection");
			}
		}
		catch (SQLException e)
		{
			logger.warning("DB Connection failed to close");
			return false;
		}
		return true;
	}

//***SCRIPT METHODS***
	
	/**
	 * Attempt to create the required DB tables
	 * @author krismania
	 * @author James
	 */
	private void createTables()
	{
		logger.info("Creating database tables...");
		
		Table customer = new Table("Customer");
		customer.addColumn("Username", "varchar(30)");
		customer.addColumn("Password", "varchar(255)");
		customer.addColumn("Firstname", "varchar(255)");
		customer.addColumn("Lastname", "varchar(255)");
		customer.addColumn("Email", "varchar(255)");
		customer.addColumn("Phone", "varchar(10)");
		customer.addColumn("Type", "varchar(13)");
		customer.setPrimary("Username");
		
		Table bo = new Table("BusinessOwner");
		bo.addColumn("Username", "varchar(30)");
		bo.addColumn("Password", "varchar(255)");
		bo.addColumn("BusinessName", "varchar(255)");
		bo.addColumn("Name", "varchar(255)");
		bo.addColumn("Address", "varchar(255)");
		bo.addColumn("Phone", "varchar(10)");
		bo.addColumn("Type", "varchar(13)");
		bo.setPrimary("Username");
		
		Table employee = new Table("Employee");
		employee.addColumn("EmpID", "int");
		employee.addColumn("FirstName", "varchar(255)");
		employee.addColumn("Lastname", "varchar(255)");
		employee.addColumn("Email", "varchar(255)");
		employee.addColumn("Phone", "varchar(10)");
		employee.setPrimary("EmpID");
		
		Table shift = new Table("Shift");
		shift.addColumn("ShiftID", "int");
		shift.addColumn("EmpID", "int");
		shift.addColumn("Day", "varchar(9)");
		shift.addColumn("Start", "int");
		shift.addColumn("End", "int");
		shift.setPrimary("ShiftID");
		shift.addForeignKey("EmpID", "Employee(EmpID)");
		
		Table booking = new Table("Booking");
		booking.addColumn("BookingID", "int");
		booking.addColumn("Customer", "varchar(30)");
		booking.addColumn("EmpID", "int");
		booking.addColumn("Date", "DATE");
		booking.addColumn("Start", "int");
		booking.addColumn("ServiceID", "int");
		booking.setPrimary("BookingID");
		booking.addForeignKey("Customer", "Customer(Username)");
		booking.addForeignKey("EmpID", "Employee(EmpID)");
		booking.addForeignKey("ServiceID", "Service(ServiceID)");
		
		Table service = new Table("Service");
		service.addColumn("ServiceID", "int");
		service.addColumn("Name", "varchar(30)");
		service.addColumn("Duration", "int");
		service.setPrimary("ServiceID");
		
		try
		{
			try (Statement stmt = c.createStatement())
			{
				// Customer Table
				logger.fine("Creating table: " + customer);
				stmt.execute(customer.toString());
				logger.fine("Creating table: " + bo);
				stmt.execute(bo.toString());
				logger.fine("Creating table: " + employee);
				stmt.execute(employee.toString());
				logger.fine("Creating table: " + shift);
				stmt.execute(shift.toString());
				logger.fine("Creating table: " + booking);
				stmt.execute(booking.toString());
				logger.fine("Creating table: " + service);
				stmt.execute(service.toString());
			}
		}
		catch (SQLException e)
		{
			logger.severe("SQL Exception in table creation: " + e);
		}
	}
}
