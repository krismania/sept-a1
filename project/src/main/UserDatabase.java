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
		System.out.println();
	}
	
	public void CreateDatabaseTable()
	{
		System.out.println("Creating table in Database...");
		try {
			stmt = c.createStatement();
			String sql = "CREATE TABLE CUSTOMERS " +
						 "(FirstName varchar(255)," +
						 "LastName varchar (255))";
			
			stmt.executeUpdate(sql);
			System.out.println("Created table in Database!");
		} catch (SQLException e) {
			//JM Handles errors for JDBC
			e.printStackTrace();
		} catch (Exception e) {
			//JM Handles errors for Class.forName
			e.printStackTrace();
		}
		
		
	}
}
