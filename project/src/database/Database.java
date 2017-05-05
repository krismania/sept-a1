package database;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Account;
import model.Booking;
import model.BusinessOwner;
import model.Customer;
import model.Employee;
import model.Shift;
import model.ShiftTime;

public class Database implements DBInterface {
	Connection c = null;
	Statement stmt = null;
	ResultSet rs = null;
	String dbName;
	
	private Logger logger;
	
	/**
	 * Instantiates the database, which will read to and from the .db file with
	 * the given name. If the database doesn't exist, it is created and seeded.
	 * @author James
	 * @author krismania
	 */
	public Database(String nameOfDatabase)
	{
		// get the logger & set level
		logger = Logger.getLogger(getClass().getName());
		logger.setLevel(Level.ALL);
		
		logger.info("Instantiated DB");
		
		// set up db
		dbName = nameOfDatabase;
		CreateDatabase();
	}

	//***PUBLIC API***

	/**
	 * @author James
	 */
	@Override
	public boolean addAccount(Account account, String password)
	{
		if(account instanceof Customer)
		{
			Customer c = (Customer) account;
			
			return CreateDataEntry("Customer", c.getFirstName(),
							c.getLastName(), c.getEmail(), c.getPhoneNumber(),
							c.username, password, "Customer");
		}
		else if(account instanceof BusinessOwner)
		{
			BusinessOwner bo = (BusinessOwner) account;
			
			return insert("BusinessOwner", bo.username, bo.getBusinessName(),
							bo.getName(), bo.getAddress(), bo.getPhoneNumber(),
							password, "BusinessOwner");
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
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(EmpID) AS id FROM Employee"))
			{
				if (rs.next())
				{
					maxID =  rs.getInt("id");
				}
			}
			
			closeConnection();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		return new Employee(maxID+1,"", "", "", "");
	}
  
	/**
	 * @author James
	 */
	@Override
	public boolean addEmployee(Employee employee)
	{
		if(CreateDataEntry("Employee", employee.getFirstName(), employee.getLastName(), employee.getEmail(), 
				employee.getPhoneNumber(), Integer.toString(employee.ID)))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @author krismania
	 */
	@Override
	public Shift buildShift(int employeeID)
	{
		// find the highest ID
		int maxID = 0;
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(Shift_ID) AS id FROM Shift"))
			{
				if (rs.next())
				{
					maxID =  rs.getInt("id");
				}
			}
			
			closeConnection();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		
		return new Shift(maxID+1, employeeID, null, null);
	}
	
	/**
	 * @author James
	 */
	public Booking buildBooking() {
		// find the highest ID
		int maxID = 0;

		try {
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(Booking_ID) AS id FROM Booking")) {
				if (rs.next()) {
					maxID = rs.getInt("id");
				}
			}

			closeConnection();
		} catch (SQLException e) {
			logger.warning(e.toString());
		}

		return new Booking(maxID + 1, null, 0, null, null);
	}
	
	/**
	 * @author James
	 */
	@Override
	public boolean addShift(Shift shift)
	{
		if(CreateShift(shift.getDay(), shift.getTime(), shift.ID, shift.employeeID))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @author James
	 */
	/** 
	 * TODO: Refactor SQL to check all details rather than doing if/else's
	 */
	@Override
	public boolean addBooking(Booking booking)
	{
		boolean noDuplicate = true;
		LocalTime timer;
		try {
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM Booking WHERE Date ='" +booking.getDate().toString()+"'")) {
				while (rs.next()) {
					if(rs.getString("customerID").equals(booking.getCustomer()))
					{
						timer =  LocalTime.ofSecondOfDay((rs.getInt("Time")));
						if(timer.equals(booking.getTime()))
						{
							logger.info("Duplicate booking found.");
							noDuplicate = false;
							break;
						}
						else
						{
							noDuplicate = true;
						}
					}
					else
					{
						noDuplicate = true;
					}
				}
			}

			closeConnection();
		} catch (SQLException e) {
			logger.warning(e.toString());
		}
		
		if(noDuplicate)
		{
			if(CreateDataEntry("Booking", Integer.toString(booking.ID), booking.getCustomer(),
				Integer.toString(booking.getEmployeeID()),
				booking.getDate().toString(), Integer.toString(booking.getTime().toSecondOfDay())))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @author krismania
	 */
	@Override
	public boolean accountExists(String username)
	{
		return validateUsername(username) != null;
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
			openConnection();
			stmt = c.createStatement();
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
				        closeConnection();
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
						closeConnection();
						// create obj and return
						return new BusinessOwner(usr, businessName, ownerName, address, phone);
					}
				}
			}
			closeConnection();
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
	/**
	 * Controller method has been @Deprecated.  
	 */
	@Override
	public ArrayList<Customer> getAllCustomers()
	{
		ArrayList<Customer> customers = new ArrayList<Customer>();
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT * FROM Customer";
			
			rs = stmt.executeQuery(sql);
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
			closeConnection();
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
	 */
	/**
	 * Controller has been Deprecated
	 */
	@Override
	public ArrayList<BusinessOwner> getAllBusinessOwners()
	{
		ArrayList<BusinessOwner> businessOwners = new ArrayList<BusinessOwner>();
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT * FROM BusinessOwner";
			
			rs = stmt.executeQuery(sql);
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
			closeConnection();
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
	public Employee getEmployee(int id)
	{
		try
		{
			openConnection();
			stmt = c.createStatement();
			
			try (ResultSet rs = stmt.executeQuery(
							String.format("SELECT * FROM Employee WHERE EmpID = '%s'", id)))
			{
				if (rs.next())
				{
					String first = rs.getString("Firstname");
			        String last = rs.getString("Lastname");
			        String email = rs.getString("Email");
			        String phone = rs.getString("Phone");
			        closeConnection();
			        return new Employee(id, first, last, email, phone);
				}
			}
			
			closeConnection();
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
		
		try
		{
			openConnection();
			stmt = c.createStatement();
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
			
			closeConnection();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		return roster;
	}
	
	@Override
	public ArrayList<String> getEmployeeWorkingOnDay(LocalDate date)
	{
		String day = date.getDayOfWeek().toString();
		ArrayList<String> Workers = new ArrayList<String>();
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			
			String sql = String.format("SELECT * FROM Shift WHERE Day = '%s'", day);
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
		         //JM Retrieve by column name
		         
				String empID = Integer.toString(rs.getInt("EmpID"));
		         
		         // add it to the list
		         Workers.add(empID);
		    }
			closeConnection();
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
		return Workers;
	}
	
	/**
	 * @author James
	 */
	/**
	 * TODO: Update method to remove ShiftTime
	 */
	@Override
	public boolean shiftExists(DayOfWeek day, ShiftTime time, int empID)
	{
		boolean shiftExists = false;
		try
		{
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery(
							String.format("SELECT * FROM Shift WHERE EmpID = '%s' AND"
									+ " Day = '%s' AND Time = '%s'", empID, day.toString(),
									time.toString().toUpperCase())))
			{
				while (rs.next())
				{
					shiftExists = true;
				}
			}
			
			closeConnection();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		return shiftExists;
	}
	
	@Override
	public Shift getShift(int shiftID)
	{
		try
		{
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery(
							String.format("SELECT * FROM Shift NATURAL JOIN Schedule WHERE Shift_ID = '%s'", shiftID)))
			{
				if (rs.next())
				{
					String day = rs.getString("Day");
			        int time = rs.getInt("Time");
			        int empID = rs.getInt("EmpID");
			        closeConnection();
			        LocalTime convertTime = LocalTime.ofSecondOfDay(time);
			        return new Shift(shiftID, empID, DayOfWeek.valueOf(day), convertTime);
				}
			}
			
			closeConnection();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		return null;
	}
	
	@Override
	public ArrayList<Shift> getShifts(int EmpID, String Day)
	{
		ArrayList<Shift> Shifts = new ArrayList<Shift>();
		try
		{
			openConnection();
			stmt = c.createStatement();
			
			String sql = String.format("SELECT * FROM Shift WHERE EmpID = '%s' AND Day = '%s'", EmpID, Day);
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
		         //JM Retrieve by column name
		         DayOfWeek day = DayOfWeek.valueOf(rs.getString("Day").toUpperCase());
		         int time = rs.getInt("Time");
		         int shiftID = rs.getInt("Shift_ID");
		         LocalTime convertTime = LocalTime.ofSecondOfDay(time);
		         // create shift object. -kg
		         Shift shift = new Shift(shiftID, EmpID, day, convertTime);
		         
		         // add it to the list
		         Shifts.add(shift);
		    }
			closeConnection();
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
		return Shifts;
	}

	/**
	 * @author krismania
	 */
	/**
	 * TODO: Check if method is used or depreciated. 
	 */
	@Override
	public TreeMap<Shift, Booking> getShiftBookings()
	{
		// get the list of employees
		logger.info("getting employees");
		ArrayList<Employee> employees = getAllEmployees();
		
		// build the list of all shifts
		logger.info("getting shifts");
		ArrayList<Shift> shifts = new ArrayList<Shift>();
		for (Employee employee : employees)
		{
			//shifts.addAll(getShifts(employee.ID));
		}
		
		// get the list of bookings in the next 7 days
		logger.info("getting upcoming bookings");
		ArrayList<Booking> bookings = getBookings("Date >= DATE('now') AND Date < DATE('now', '7 days')");
		
		// create hashmap to decide which shifts are booked within the next 7 days
		TreeMap<Shift, Booking> shiftBookings = new TreeMap<Shift, Booking>();
		
		// iterate over each shift and decide if it's been booked
		for (Shift shift : shifts)
		{
			logger.fine("looping shift " + shift.ID);
			// find a booking with matching details
			boolean found = false;
			for (Booking booking : bookings)
			{
				logger.fine(shift.toString() + " | " + booking.toString());
				// figure out weekday of booking
				if (booking.getDay() == shift.getDay() && booking.getTime() == shift.getTime() &&
								booking.getEmployeeID() == shift.employeeID)
				{
					// booking and shift match, to hash map
					shiftBookings.put(shift, booking);
					found = true;
					break;
				}
			}
			// otherwise, enter null as the booking
			if (!found) shiftBookings.put(shift, null);
			logger.fine("found match: " + found);
		}
		
		return shiftBookings;
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
	 * Returns an ArrayList of bookings in the database, restricted by the given
	 * {@code constraint}. The constraint arg is added after the {@code WHERE}
	 * clause in the SQL query.
	 * @author krismania
	 */
	private ArrayList<Booking> getBookings(String constraint)
	{
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			
			try (ResultSet bookingQuery = stmt.executeQuery(
							"SELECT * FROM Booking WHERE " + constraint))
			{
				while (bookingQuery.next())
				{
					int id = bookingQuery.getInt("Booking_ID");
					String customer = bookingQuery.getString("customerID");
					int employeeID = bookingQuery.getInt("EmpID");
					LocalDate date = LocalDate.parse(bookingQuery.getString("Date"));
					LocalTime timer = LocalTime.ofSecondOfDay((bookingQuery.getInt("Time")));
					
					// construct the object & add to list. -kg
					bookings.add(new Booking(id, customer, employeeID, date, timer));
				}
			}
			
			stmt.close();
			closeConnection();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		return bookings;
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
	 */
	private void CreateDatabase()
	{
		//JM Initialize a connection
		try
		{
			Class.forName("org.sqlite.JDBC");
			//JM Attempts to get the connection to DB file after 'sqlite:<name here>'
			openConnection();
			
			// test if the db is empty. -kg
			boolean empty;
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type = 'table'");
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
			
			closeConnection();
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
	 */
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
			openConnection();
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			closeConnection();
		
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
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			stmt.execute(query);
			closeConnection();
			return true;
		}
		catch (SQLException e)
		{
			logger.warning("SQL Exception: " + e.toString());
			return false;
		}
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
			openConnection();
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			closeConnection();
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
	
	/**
	 * TODO: better documentation
	 * @author James
	 */
	private boolean CreateShift(DayOfWeek day, LocalTime time, int iD, int employeeID) 
	{
		String sql = "INSERT INTO Shift VALUES ('"
				+ day.name() + "', '" + time.toSecondOfDay() + "', '" + iD + "'"
						+ ", '" + employeeID + "')";
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			closeConnection();
			logger.info("Shift Created - Day: " +day +", Time: " +time.toSecondOfDay() + ", ID: " + iD+", EmpID: " +employeeID);
			return true;
		} catch(SQLException e) {
			//JM Handle errors for JDBC
			logger.warning(e.toString());
			return false;
		} catch(Exception e) {
			logger.warning(e.toString());
		    e.printStackTrace();
		}
		return false;
	}
	
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
			openConnection();
			stmt = c.createStatement();
			rs = stmt.executeQuery(query);
			
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
			closeConnection();
			
			
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
			openConnection();
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			if (rs.getString(1).equals(password)) {
				closeConnection();
				return true;
			}
			closeConnection();
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
			openConnection();
			stmt = c.createStatement();
			
			String sql = "SELECT EmpID FROM Employee ORDER BY EmpID DESC";
			rs = stmt.executeQuery(sql);
			
			// we only care about the first result. -kg
			rs.next();
			id = rs.getString("EmpID");
			
			closeConnection();
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
			openConnection();
			stmt = c.createStatement();
			
			String sql = "SELECT Shift_ID FROM Schedule ORDER BY Shift_ID DESC";
			rs = stmt.executeQuery(sql);
			
			// we only care about the first result. -kg
			rs.next();
			id = rs.getString("Shift_ID");
			
			closeConnection();
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
	private boolean openConnection() throws SQLException {
		c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
		if(c != null) 
		{
			return true;
		}
		return false;
	}
	
	/**
	 * TODO: document this
	 * @author James
	 */
	private boolean closeConnection() throws SQLException {
		
		if(stmt != null)
		{
			stmt.close();
			stmt = null;
		}
		if(rs != null)
		{
			rs.close();
			rs = null;
		}
		if(c != null) {
			c.close();
			c = null;
			return true;
		}
		else
		{
			logger.warning("DB Connection failed to close");
			return false;
		}
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
				
		//Customer Table
		CreateDatabaseTable("Customer", "Firstname varchar(255)", "Lastname varchar(255)",
				"Email varchar(255)", "Phone varchar(10)", "Username varchar(15)",
				"Password varchar(15)","Type varchar(13)", "Username");
		
		//BusinessOwner Table
		CreateDatabaseTable("BusinessOwner", "Username varchar(15)", "BusinessName varchar(30)",
				"Name varchar(255)", "Address varchar(255)", "Phone varchar(10)",
				"Password varchar(15)", "Type varchar(13)", "Username");
		
		//Employee Table
		CreateDatabaseTable("Employee", "Firstname varchar(255)", "Lastname varchar(255)",
				"Email varchar(255)", "Phone varchar(10)", "EmpID int", "EmpID");

		//Shift Table
		CreateDatabaseTable("Shift", "Day varchar(9)", "Time int", "Shift_ID int",
				"EmpID int", "Shift_ID"); //Schedule also has a foreign key for EmpID.
		
		//Booking Table
		CreateDatabaseTable("Booking", "Booking_ID int", "customerID varchar(15)", "EmpID int", 
				"Date DATE", "Time int", "Booking_ID");
	}
	
	/**
	 * Seeds the database.
	 * @author James
	 */
	private void createTestData()
	{
		logger.info("Creating DB test data...");
		
		CreateDataEntry("Customer", "sept", "customer", "sept@customer.test", 
				"0400000000", "septC", "septCust1", "Customer");
		
		CreateDataEntry("BusinessOwner", "septB", "SomeBusiness", "John S.",
						"10 Some St, Some Town", "(03) 5555 5555", "septBus1", "BusinessOwner");
		
		CreateDataEntry("Employee", "Fred", "Cutshair", "fred.cutshair@thebesthairshop.com", 
				"0400000000", "1");
		
		CreateDataEntry("Employee", "Bob", "Shaveshair", "bob.shaveshair@thebesthairshop.com", 
				"0400000000", "2");
		

		CreateDataEntry("Shift", "MONDAY", Integer.toString(LocalTime.parse("10:00").toSecondOfDay()), "1", "1");
		CreateDataEntry("Shift", "TUESDAY", Integer.toString(LocalTime.parse("11:00").toSecondOfDay()), "2", "1");
		CreateDataEntry("Shift", "WEDNESDAY",Integer.toString(LocalTime.parse("12:00").toSecondOfDay()), "3", "1");
		CreateDataEntry("Shift", "SUNDAY", Integer.toString(LocalTime.parse("13:00").toSecondOfDay()), "4", "2");

		CreateDataEntry("Booking", "1", "JamesRulez", "1", "2017-05-03", Integer.toString(LocalTime.parse("10:00").toSecondOfDay()));
		CreateDataEntry("Booking", "2", "JamesRulez", "2", "2017-05-02", Integer.toString(LocalTime.parse("11:00").toSecondOfDay()));
		CreateDataEntry("Booking", "3", "krismania", "2", "2017-05-10", Integer.toString(LocalTime.parse("12:00").toSecondOfDay()));
		CreateDataEntry("Booking", "4", "JamesRulez", "1", "2017-03-29", Integer.toString(LocalTime.parse("13:00").toSecondOfDay()));
		CreateDataEntry("Booking", "5", "krismania", "2", "2017-04-17", Integer.toString(LocalTime.parse("14:00").toSecondOfDay()));

		logger.info("DB created.");
	}
}
