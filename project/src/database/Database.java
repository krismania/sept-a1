package database;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Logger;

import model.Account;
import model.Booking;
import model.BusinessOwner;
import model.Customer;
import model.Employee;
import model.Shift;

public abstract class Database {
	
	/**
	 * DB Connection object
	 */
	protected Connection c = null;

	/**
	 * The name of the DB file, excluding it's extension
	 */
	private String dbName;
	
	protected Logger logger;
	
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
	
	public void close()
	{
		closeConnection();
	}

	//***PUBLIC API***


	/**
	 * @author James
	 * @author krismania
	 * @deprecated Controller method has been deprecated.
	 */
	 @Deprecated
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
	 @Deprecated
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
	protected boolean insert(String table, String...values)
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
	protected boolean openConnection()
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
	protected boolean closeConnection()
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
	 * Checks if master DB
	 * @author krismania
	 * @author James
	 */
	protected void createTables()
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
		booking.addColumn("End", "int");
		booking.setPrimary("BookingID");
		booking.addForeignKey("Customer", "Customer(Username)");
		booking.addForeignKey("EmpID", "Employee(EmpID)");
		
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
			}
		}
		catch (SQLException e)
		{
			logger.severe("SQL Exception in table creation: " + e);
		}
	}
}
