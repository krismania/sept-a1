package main;
import java.sql.*;

/* JM User Database implementation. 
 * Loads database connection to that specified in line 19. Will then invoke 
 * scripts to create users automatically
*/
public class UserDatabase {
	Connection c = null;
	Statement stmt = null;
	
	public void CreateDatabase()
	{
		//JM Initialize a connection
		System.out.println("Attempting to connect to the database...");
		try
		{
			Class.forName("org.sqlite.JDBC");
			//JM Attempts to get the connection to DB file after 'sqlite:<name here>'
			c = DriverManager.getConnection("jdbc:sqlite:awesomeSauce.db");
		}
		catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		//JM Success message means DB is found, or created.
		System.out.println("Opened database successfully\n");
		
		//Customer Table
		CreateDatabaseTable("Customers", "Firstname varchar(255)", "Lastname varchar(255)",
				"Email varchar(255)", "Phone varchar(10)", "Username varchar(15)",
				"Password varchar(15)", "Username");
		//BusinessOwner Table
		CreateDatabaseTable("BusinessOwner", "Firstname varchar(255)", "Lastname varchar(255)",
				"Email varchar(255)", "Phone varchar(10)", "Username varchar(15)",
				"Password varchar(15)", "Username");
		
		CreateDataEntry("Customers", "James", "McLennan", "testing@testing.com", 
				"0400000000", "JamesRulez", "james");
		
		CreateDataEntry("BusinessOwner", "John", "Doe", "rabbits@rocks.com",
				"0400000000", "JohnRulez", "john");
		
		getCustomerDataEntries();
		getBusinessOwnerDataEntries();
		
		System.out.println();
	}
	
	//JM CreateDatabaseTable() will create a table within the database.
	//JM Param = Variable number of Strings (Array)
	public void CreateDatabaseTable(String... strings)
	{
		System.out.println("Creating table in Database...");
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
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			System.out.println("Created " + strings[0] +" table in Database!\n");
		
		} catch (SQLException e) {
			//JM Catch if table already exists
			System.out.println("Table " + strings[0] +" already exists!\n");
			
		} catch (Exception e) {
			//JM Handles errors for Class.forName
			e.printStackTrace();
		}
		
	}
	
	//JM Automatically generate customers into database.
	public void CreateDataEntry(String...strings) 
	{
		System.out.println("Inserting Data...");
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
			stmt = c.createStatement();
			//JM Insert a customer with generic values and details.
			stmt.executeUpdate(sql);
			System.out.println("Data Inserted: New " + strings[0] + "! Welcome, " + strings[5]+"\n");
		} catch(SQLException e) {
			//JM Handle errors for JDBC
			System.out.println("Data failed to insert: " +strings[0] + " " + strings[5] + " already exists!\n");
		} catch(Exception e) {
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
		   
	}
	
	// Insert customer into db. -kg
	public void insert(Customer customer)
	{
		String sql = String.format("INSERT INTO Customers VALUES('%s', '%s', '%s', '%s', '%s', '')",
						customer.getFirstName(), customer.getLastName(), customer.getEmail(),
						customer.getPhoneNumber(), customer.getUsername());
		try
		{
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Set customer password in db. -kg
	public void setPassword(String username, String password)
	{
		String sql = String.format("UPDATE Customers SET password='%s' WHERE username='%s'", password, username);
		try
		{
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	//JM Obtain Data values from tables
	public void getCustomerDataEntries() 
	{
		System.out.println("Fetching Customer Data Entires...");
		
		try{
			c = DriverManager.getConnection("jdbc:sqlite:awesomeSauce.db");
			stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT Firstname, Lastname, Email, Phone,"
					+ "Username, Password FROM Customers";
			
			ResultSet rs = stmt.executeQuery(sql);
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
		      rs.close();
		} catch(SQLException e) {
			//JM Handle errors for JDBC
		    e.printStackTrace();
		} catch(Exception e) {
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
		System.out.println("All data presented.");		
	}
	
	public void getBusinessOwnerDataEntries() 
	{
		System.out.println("Fetching Business Owner Data Entires...");
		
		try{
			c = DriverManager.getConnection("jdbc:sqlite:awesomeSauce.db");
			stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT Firstname, Lastname, Email, Phone,"
					+ "Username, Password FROM BusinessOwner";
			
			ResultSet rs = stmt.executeQuery(sql);
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
		      rs.close();
		} catch(SQLException e) {
			//JM Handle errors for JDBC
		    e.printStackTrace();
		} catch(Exception e) {
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
		System.out.println("All data presented.");		
	}
}
