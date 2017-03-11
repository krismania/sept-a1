package main;
import java.sql.*;

//JM User Database implementation. Currently just CREATES a DB File.

public class UserDatabase {
	public void CreateDatabase()
	{
		//JM Initialize a connection
		Connection c = null;
		System.out.println("Attempting to connect to the database");
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
	}
}
