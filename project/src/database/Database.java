package database;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import database.model.Account;
import database.model.Admin;
import database.model.BusinessOwner;
import database.model.Customer;

public abstract class Database {
	
	/**
	 * DB Connection object
	 */
	protected Connection c = null;

	/**
	 * The name of the DB file, excluding it's extension
	 */
	private String dbName;
	private boolean isAdmin = false;
	
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
	 */
	@Deprecated
	public ArrayList<BusinessOwner> getAllBusinessOwners()
	{
		ArrayList<BusinessOwner> businessOwners = new ArrayList<BusinessOwner>();
		
		try
		{
			Statement stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT * FROM Businesses";
			
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
	 * Joint login function, may return either a Customer or BusinessOwner.
	 * @author krismania
	 */
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
		else if(account instanceof Admin)
		{
			valid = validatePassword(username, password, "Admin");
		}

		if (valid) return account;
		else return null;
	}
	
	/**
	 * @author krismania
	 */
	public Account getAccount(String username)
	{		
		if(username.equals("Admin"))
			isAdmin = true;
		else
			isAdmin = false;
		
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
			else if(type.equals(Admin.class))
			{
				try (ResultSet adminQuery = stmt.executeQuery(
						String.format("SELECT * FROM Admin WHERE Username = '%s'", username)))
				{
					if (adminQuery.next())
					{
						// get info
						String usr = adminQuery.getString("Username");
						// create obj and return
						return new Admin(usr);
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
	 * Returns a class object describing which type of user {@code username} is,
	 * or null if the username is not found.
	 * @author James
	 * @author krismania
	 */
	private Class<? extends Account> validateUsername(String username) 
	{		
		if(!isAdmin)
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
		}
		else
		{
			String query = "SELECT Username "
					+ "FROM Admin";

				try 
				{
					Statement stmt = c.createStatement();
					ResultSet rs = stmt.executeQuery(query);
		
					if(rs.next())
					{
						String user = rs.getString("Username");
						if(user.equals("Admin"))
						{
							return Admin.class;
						}
					}			
		
				} catch (SQLException e) {
					//JM Catch if table already exists
					logger.warning(e.toString());
				} catch (Exception e) {
					//JM Handles errors for Class.forName
					logger.warning(e.toString());
				}
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
		
		ArrayList<Table> tables = new ArrayList<Table>();
		
		Table hours = new Table("Hours");
		hours.addColumn("Day", "varchar(9)");
		hours.addColumn("Open", "int");
		hours.addColumn("Close", "int");
		tables.add(hours);

		Table customer = new Table("Customer");
		customer.addColumn("Username", "varchar(30)");
		customer.addColumn("Password", "varchar(255)");
		customer.addColumn("Firstname", "varchar(255)");
		customer.addColumn("Lastname", "varchar(255)");
		customer.addColumn("Email", "varchar(255)");
		customer.addColumn("Phone", "varchar(10)");
		customer.addColumn("Type", "varchar(13)");
		customer.setPrimary("Username");
		tables.add(customer);
		
		Table bo = new Table("BusinessOwner");
		bo.addColumn("Username", "varchar(30)");
		bo.addColumn("Password", "varchar(255)");
		bo.addColumn("BusinessName", "varchar(255)");
		bo.addColumn("Name", "varchar(255)");
		bo.addColumn("Address", "varchar(255)");
		bo.addColumn("Phone", "varchar(10)");
		bo.addColumn("Type", "varchar(13)");
		bo.setPrimary("Username");
		tables.add(bo);
		
		Table employee = new Table("Employee");
		employee.addColumn("EmpID", "int");
		employee.addColumn("FirstName", "varchar(255)");
		employee.addColumn("Lastname", "varchar(255)");
		employee.addColumn("Email", "varchar(255)");
		employee.addColumn("Phone", "varchar(10)");
		employee.setPrimary("EmpID");
		tables.add(employee);
		
		Table shift = new Table("Shift");
		shift.addColumn("ShiftID", "int");
		shift.addColumn("EmpID", "int");
		shift.addColumn("Day", "varchar(9)");
		shift.addColumn("Start", "int");
		shift.addColumn("End", "int");
		shift.setPrimary("ShiftID");
		shift.addForeignKey("EmpID", "Employee(EmpID)");
		tables.add(shift);
		
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
		tables.add(booking);
		
		Table service = new Table("Service");
		service.addColumn("ServiceID", "int");
		service.addColumn("Name", "varchar(30)");
		service.addColumn("Duration", "int");
		service.setPrimary("ServiceID");
		tables.add(service);
		
		try
		{
			try (Statement stmt = c.createStatement())
			{
				// add all tables to the db
				for (Table table : tables)
				{
					logger.fine("Creating table: " + table);
					stmt.execute(table.toString());
				}
			}
		}
		catch (SQLException e)
		{
			logger.severe("SQL Exception in table creation: " + e);
		}
	}
}
