package main;
import java.sql.*;

/* JM User Database implementation. 
 * Loads database connection to that specified in line 19. Will then invoke 
 * scripts to create users automatically
*/
public class UserDatabase {
	Connection c = null;
	Statement stmt = null;
	ResultSet rs = null;
	String dbName;
	
	//JM Constructor
	public UserDatabase(String nameOfDatabase) {
		dbName = nameOfDatabase;
	}
	
	public void CreateDatabase()
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
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	//JM CreateDatabaseTable() will create a table within the database.
	//JM Param = Variable number of Strings (Array)
	public void CreateDatabaseTable(String... strings)
	{

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
			else if(i+1 == strings.length)
			{
				//JM Insert Primary Key element and close bracket [!].
				strBuilder.append("PRIMARY KEY (" + strings[i] + "))");			
			}
			//JM If any element in between first and last
			else
			{
				//JM Split with a comma
				strBuilder.append(strings[i] + ", ");
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
		
		} catch (SQLException e) {
			//JM Catch if table already exists

			
		} catch (Exception e) {
			//JM Handles errors for Class.forName
			e.printStackTrace();
		}
		
	}
	
	//JM Insert data into database.
	public boolean CreateDataEntry(String...strings) 
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

			return false;
		} catch(Exception e) {
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
		return false;
		   
	}
	
	//***VALIDATION METHODS***JM
	//Customers/Business Owner JM
	public int validateUsername(String username) 
	{
		String query = "SELECT Username "
				+ "FROM (SELECT Username from Customers "
				+ "UNION "
				+ "SELECT Username from BusinessOwner"
				+ ") a "
				+ "WHERE Username = '"+username+"'";
		try 
		{
			openConnection();
			stmt = c.createStatement();
			rs = stmt.executeQuery(query);

			//JM If rs contains anything, the name exists.
			rs.next();
			if (rs.getString(1).equals(username)) {
				closeConnection();
				openConnection();
				query = "SELECT Username "
						+ "FROM BusinessOwner"
						+ "WHERE Username = '"+username+"'";
				
				stmt = c.createStatement();
				rs = stmt.executeQuery(query);
				if (rs.getString(1).equals(username)) {
					return 2;
				}
				return 1;
			}
			
		} catch (SQLException e) {
			//JM Catch if table already exists
			
		} catch (Exception e) {
			//JM Handles errors for Class.forName
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean checkPassword(String username, String password, String tableName)
	{
		String sql = String.format("SELECT password FROM %s WHERE username='%s'", tableName, username);
		
		try
		{
			openConnection();
			stmt = c.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			if (rs.getString(1).equals(password)) {
				return true;
			}
			closeConnection();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	//Employee JM
	public boolean validateEmpID(String empID) 
	{
		boolean duplicated = false;
		
		String query = String.format("SELECT EmpID FROM %s WHERE EmpID='%s'", "Employee", empID);
		
		try 
		{
			openConnection();
			stmt = c.createStatement();
			rs = stmt.executeQuery(query);
			if(rs != null) {
				while(rs.next()){
				
					closeConnection();
					duplicated = true;
				}
			}
		}
		catch (SQLException e) {
			
			
		} catch (Exception e) {
			//JM Handles errors for Class.forName
			
		}
		
		return duplicated;
	}
	
	/*JM Enabled generic update to specific data, depending on Username.
	* Params = table, the table you wish to update data in
	* userName = Username of specific user
	* dataToInput = the actual string you wish to insert as the update
	* valueToUpdate = the value you wish to update. ie. Name, Password, Username etc.
	*/
	public boolean updateDataEntry(String table, String userName, String dataToInput, String valueToUpdate)
	{
		String sql = String.format("UPDATE " + table + " SET " + valueToUpdate 
				+ "='%s' WHERE Username='%s'", dataToInput, userName);
		int exists = 0;
		try
		{
			openConnection();
			if(userName != null)
			{
				exists = validateUsername(userName);
			}
			
			if(exists!=0)
			{
				
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
	}
	
	//JM Obtain Data values from tables
	public void getCustomerDataEntries() 
	{		
		try{
			openConnection();
			stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT Firstname, Lastname, Email, Phone,"
					+ "Username, Password FROM Customers";
			
			rs = stmt.executeQuery(sql);
			while(rs.next()){
		         //Retrieve by column name
		         String first = rs.getString("Firstname");
		         String last = rs.getString("Lastname");
		         String email = rs.getString("Email");
		         String phone = rs.getString("Phone");
		         String Username = rs.getString("Username");
		         String Password = rs.getString("Password");

		         //Display values
		         System.out.println("\nFirst: " + first);
		         System.out.println("Last: " + last);
		         System.out.println("Email: " + email);
		         System.out.println("Phone: " + phone);
		         System.out.println("Username: " + Username);
		         System.out.println("Password: " + Password + "\n");
		      }
			closeConnection();
		} catch(SQLException e) {
			//JM Handle errors for JDBC
		    e.printStackTrace();
		} catch(Exception e) {
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
	}
	
	public void getBusinessOwnerDataEntries() 
	{		
		try{
			openConnection();
			stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT Firstname, Lastname, Email, Phone,"
					+ "Username, Password FROM BusinessOwner";
			
			rs = stmt.executeQuery(sql);
			while(rs.next()){
		         //Retrieve by column name
		         String first = rs.getString("Firstname");
		         String last = rs.getString("Lastname");
		         String email = rs.getString("Email");
		         String phone = rs.getString("Phone");
		         String Username = rs.getString("Username");
		         String Password = rs.getString("Password");

		         //Display values
		         System.out.println("First: " + first);
		         System.out.println("Last: " + last);
		         System.out.println("Email: " + email);
		         System.out.println("Phone: " + phone);
		         System.out.println("Username: " + Username);
		         System.out.println("Password: " + Password);
		      }
			closeConnection();
		} catch(SQLException e) {
			//JM Handle errors for JDBC
		    e.printStackTrace();
		} catch(Exception e) {
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
	}
	
	public boolean openConnection() throws SQLException {
		c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
		if(c != null) 
		{
			return true;
		}
		return false;
	}
	
	public boolean closeConnection() throws SQLException {
		
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
			System.out.println("Connection failed to close.");
			return false;
		}
	}
	
	private void setupScript() {
		//Customer Table
		CreateDatabaseTable("Customers", "Firstname varchar(255)", "Lastname varchar(255)",
				"Email varchar(255)", "Phone varchar(10)", "Username varchar(15)",
				"Password varchar(15)", "Username");
		
		//BusinessOwner Table
		CreateDatabaseTable("BusinessOwner", "Firstname varchar(255)", "Lastname varchar(255)",
				"Email varchar(255)", "Phone varchar(10)", "Username varchar(15)",
				"Password varchar(15)", "Username");
		
		//Employee Table
		CreateDatabaseTable("Employee", "Firstname varchar(255)", "Lastname varchar(255)",
				"Email varchar(255)", "Phone varchar(10)", "EmpID varchar(20)", "EmpID");
		
		CreateDataEntry("Customers", "James", "McLennan", "testing@testing.com", 
				"0400000000", "JamesRulez", "james");
		
		CreateDataEntry("BusinessOwner", "John", "Doe", "rabbits@rocks.com",
				"0400000000", "JohnRulez", "john");
		
		CreateDataEntry("Employee", "Fred", "Cutshair", "fred.cutshair@thebesthairshop.com", 
				"0400000000", "E001");
	}
}
