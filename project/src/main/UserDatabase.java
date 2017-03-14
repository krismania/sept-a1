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
		System.out.println("Opened database successfully");
		CreateDatabaseTable();
		CreateDataEntries();
		getCustomerDataEntries();
		getBusinessOwnerDataEntries();
		
		System.out.println();
	}
	
	//JM CreateDatabaseTable() will create a table within the database.
	public void CreateDatabaseTable()
	{
		System.out.println("Creating table in Database...");
		try {
			/*JM Create a table for Customers with
			 * Firstname, Lastname, Email, Phone, Username
			 * and Password. Primary Key being Username.
			 * Primary Key = Unique ID 
			 */
			stmt = c.createStatement();
			String sql = "CREATE TABLE Customers " +
						 "(Firstname varchar(255),"
						 + "Lastname varchar(255),"
						 + "Email varchar(255),"
						 + "Phone varchar(10),"
						 + "Username varchar(15),"
						 + "Password varchar(15),"
						 + "PRIMARY KEY (Username))";
			
			stmt.executeUpdate(sql);
			System.out.println("Created Customers table in Database!");
			
			/*JM Create a table for Business Owners with
			 * Firstname, Lastname, Email, Phone, Username
			 * and Password. Primary Key being Username.
			 * Primary Key = Unique ID 
			 */
			/*sql = "CREATE TABLE BusinessOwner " +
					 "(Firstname varchar(255),"
					 + "Lastname varchar(255),"
					 + "Email varchar(255),"
					 + "Phone varchar(10),"
					 + "Username varchar(15),"
					 + "Password varchar(15),"
					 + "PRIMARY KEY (Username))";
		
			stmt.executeUpdate(sql);
			System.out.println("Created Business Owners table in Database!");
		
		*/
		} catch (SQLException e) {
			//JM Handles errors for JDBC
			System.out.println("Table already exists!");
			String sql = "DROP TABLE Customers";
			try {
				stmt = c.createStatement();
				stmt.executeUpdate(sql);
				CreateDatabaseTable();
			} catch (SQLException e1) {
				System.out.println("SQL Errors! Please contact admin.");
			}
			
		} catch (Exception e) {
			//JM Handles errors for Class.forName
			e.printStackTrace();
		}
		
	}
	
	//JM Automatically generate customers into database.
	public void CreateDataEntries() 
	{
		System.out.println("Inserting Data...");
		
		try
		{
			stmt = c.createStatement();
			//JM Insert a customer with generic values and details.
			String sql = "INSERT INTO Customers " +
						 "VALUES ('James', 'McLennan', 'testing'"
						 + ", '0400000000', 'JamesRulez', 'james')";
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO BusinessOwner " +
			      "VALUES ('John', 'Doe', 'thegreat@jdo.com'"
			      + ", '0400000000', 'JohnRox', 'Password')";
			
			stmt.executeUpdate(sql);
					
		} catch(SQLException e) {
			//JM Handle errors for JDBC
			System.out.println("Customer already exists!");
		} catch(Exception e) {
		    //JM Handle errors for Class.forName
		    e.printStackTrace();
		}
		System.out.println("Data Inserted!");
		   
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
			System.out.println(stmt.executeUpdate(sql));
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
