package main;
import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database implements DBInterface {
	Connection c = null;
	Statement stmt = null;
	ResultSet rs = null;
	String dbName;
	
	private Logger logger;
	
	//JM Constructor, reads the name of the database file to work with.
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
			
			return CreateDataEntry("BusinessOwner", bo.username, password, "BusinessOwner");
		}

		return false;
	}
	
	@Override
	public Employee buildEmployee()
	{
		// find the highest current ID
		int currentHighestID = 0;
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(EmpID) AS id FROM Employee"))
			{
				if (rs.next())
				{
					currentHighestID =  rs.getInt("id");
				}
			}
			
			closeConnection();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		// create the object and return it
		int id = currentHighestID + 1;
		
		return new Employee(id, "", "", "", "");
	}
	
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
	
	@Override
	public Shift buildShift(int employeeID)
	{
		// find the highest ID
		int currentHighestID = 0;
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery("SELECT MAX(Shift_ID) AS id FROM Shift"))
			{
				if (rs.next())
				{
					currentHighestID =  rs.getInt("id");
				}
			}
			
			closeConnection();
		}
		catch (SQLException e)
		{
			logger.warning(e.toString());
		}
		
		// create the object and return it
		int id = currentHighestID + 1;
		
		return new Shift(id, employeeID, null, null);
	}
	
	@Override
	public boolean addShift(Shift shift)
	{
		if(CreateShift(shift.getDay(), shift.getTime(), shift.ID, shift.employeeID))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean accountExists(String username)
	{
		return validateUsername(username) != null;
	}
	
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
		         String Password = rs.getString("Password");

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
	
	@Override
	public Employee getEmployee(int id)
	{
		try
		{
			openConnection();
			stmt = c.createStatement();
			// TODO: use int IDs instead of strings
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
	
	@Override
	public ArrayList<Employee> getAllEmployees()
	{
		ArrayList<Employee> completeTeam = new ArrayList<Employee>();
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			try (ResultSet rs = stmt.executeQuery("SELECT * FROM Employee"))
			{
				if (rs.next())
				{
					String first = rs.getString("Firstname");
			        String last = rs.getString("Lastname");
			        String email = rs.getString("Email");
			        String phone = rs.getString("Phone");
			        int EmpID = rs.getInt("EmpID");
			        
			        Employee current = new Employee(EmpID, last, email, phone, first);
			        
			        completeTeam.add(current);
				}
				else
				{
					if(!completeTeam.isEmpty())
					{
						return completeTeam;
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
			        ShiftTime time = ShiftTime.valueOf(rs.getString("Time"));
			        int empID = rs.getInt("EmpID");
			        closeConnection();
			        return new Shift(shiftID, empID, DayOfWeek.valueOf(day), time);
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
	public ArrayList<Shift> getShifts(int EmpID)
	{
		ArrayList<Shift> Shifts = new ArrayList<Shift>();
		try
		{
			openConnection();
			stmt = c.createStatement();
			
			String sql = String.format("SELECT * FROM Shift WHERE EmpID = '%s'", EmpID);
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
		         //JM Retrieve by column name
		         DayOfWeek day = DayOfWeek.valueOf(rs.getString("Day").toUpperCase());
		         ShiftTime time = ShiftTime.valueOf(rs.getString("Time"));
		         int shiftID = rs.getInt("Shift_ID");
		         
		         // create shift object. -kg
		         Shift shift = new Shift(shiftID, EmpID, day, time);
		         
		         // add it to the list
		         Shifts.add(shift);
		         
		         // TODO: debug print shift
		         System.out.println(shift.toString());
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
	
	@Override
	public ArrayList<Shift> getShiftsNotBooked()
	{
		ArrayList<Shift> openShifts = new ArrayList<Shift>();
		try
		{
			openConnection();
			stmt = c.createStatement();
			
			String sql = String.format("SELECT * FROM Shift WHERE Shift_ID NOT IN"
					+ "(SELECT SHIFT_ID FROM Booking)");
			
			rs = stmt.executeQuery(sql);
			
			while(rs.next())
			{
		         //JM Retrieve by column name
		         DayOfWeek day = DayOfWeek.valueOf(rs.getString("Day").toUpperCase());
		         ShiftTime time = ShiftTime.valueOf(rs.getString("Time").toUpperCase());
		         int shiftID = rs.getInt("Shift_ID");
		         int EmpID = rs.getInt("EmpID");
		         
		         // create shift object. -kg
		         Shift shift = new Shift(shiftID, EmpID, day, time);
		         
		         // add it to the list
		         openShifts.add(shift);
		         
		         // TODO: debug print shift
		         System.out.println(shift.toString());
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
		return openShifts;
	}

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

	
	//***CREATE METHODS***JM
	private void CreateDatabase()
	{
		//JM Initialize a connection
		try
		{
			Class.forName("org.sqlite.JDBC");
			//JM Attempts to get the connection to DB file after 'sqlite:<name here>'
			openConnection();
			setupScript();
			closeConnection();
		}
		catch (Exception e)
		{
			logger.severe(e.toString());
			System.exit(0);
		}
	}
	
	
	//JM CreateDatabaseTable() will create a table within the database.
	//JM Param = Variable number of Strings (Array)
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
					+ " Employee (EmpID), FOREIGN KEY (Shift_ID) "
					+ "references Shift(Shift_ID))");
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
	
	//JM Insert data into database.
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
	
	private boolean CreateShift(DayOfWeek day, ShiftTime time, int iD, int employeeID) 
	{
		String sql = "INSERT INTO Shift VALUES ('"
				+ day.name() + "', '" + time.name() + "', '" + iD + "'"
						+ ", '" + employeeID + "')";
		
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
			logger.warning(e.toString());
		    e.printStackTrace();
		}
		return false;
	}
	
//***VALIDATION METHODS***JM

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
			
			while(rs.next())
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
	
//***RETRIEVE METHODS***JM
	
	
	/**
	 * Temp method to find the highest ID
	 * TODO: this may need to be removed
	 * @author krismania
	 */
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
	 */
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
	
	//***CONNECTION METHODS***JM
	private boolean openConnection() throws SQLException {
		c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
		if(c != null) 
		{
			return true;
		}
		return false;
	}
	
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

//***SCRIPT METHODS***JM
	private void setupScript()
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
		

		//Schedule Table
		CreateDatabaseTable("Shift", "Day varchar(9)", "Time varchar(10)", "Shift_ID int",

				"EmpID int", "Shift_ID"); //Schedule also has a foreign key for EmpID.
		
		//Booking Table
		CreateDatabaseTable("Booking", "Booking_ID int", "customerID varchar(15)", "EmpID int", 
				"Shift_ID int", "day varchar(9)", "Booking_ID");
		
		logger.info("Creating DB test data...");
		
		CreateDataEntry("Customer", "James", "McLennan", "testing@testing.com", 
				"0400000000", "JamesRulez", "james", "Customer");
		
		CreateDataEntry("BusinessOwner", "JohnRulez", "SomeBusiness", "John S.",
						"10 Some St, Some Town", "(03) 5555 5555", "john", "BusinessOwner");
		
		CreateDataEntry("Employee", "Fred", "Cutshair", "fred.cutshair@thebesthairshop.com", 
				"0400000000", "1");
		
		CreateDataEntry("Employee", "Bob", "Shaveshair", "bob.shaveshair@thebesthairshop.com", 
				"0400000000", "2");
		

		CreateDataEntry("Shift", "MONDAY", "MORNING", "1", "1");
		CreateDataEntry("Shift", "TUESDAY", "AFTERNOON", "2", "1");
		CreateDataEntry("Shift", "WEDNESDAY", "EVENING", "3", "1");
		CreateDataEntry("Shift", "SUNDAY", "AFTERNOON", "4", "2");

		CreateDataEntry("Booking", "1", "JamesRulez", "1", "1", "MONDAY");
		CreateDataEntry("Booking", "2", "JamesRulez", "2", "4", "SUNDAY");

		logger.info("DB created.");
	}
	
//*** Future Dev Requirements. No longer needed***
	

	/**Update data entry
	 * @author James
	 */
	/*private boolean updateDataEntry(String table, String userName, String dataToInput, String valueToUpdate)
	{
		String sql = String.format("UPDATE " + table + " SET " + valueToUpdate 
				+ "='%s' WHERE Username='%s'", dataToInput, userName);
		
		boolean exists = false;
		
		try
		{
			if(userName != null)
			{
				exists = accountExists(userName);
			}
			
			if(exists)
			{
				openConnection();
				stmt = c.createStatement();
				stmt.executeUpdate(sql);
				System.out.println("Value has been updated for: " + userName);
				closeConnection();
				return true;
			}
			else 
			{
				closeConnection();
				return false;
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (NullPointerException s)
		{
			s.printStackTrace();
		}
		return false;
	}*/
}
