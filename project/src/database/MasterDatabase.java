package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.model.Account;
import database.model.BusinessOwner;
import database.model.Customer;

public class MasterDatabase extends Database{

	public MasterDatabase(String dbName) {
		super(dbName);
	}
	
	/**
	 * Returns an arrayList of all businessNames
	 * Used for master DB
	 * @author James
	 */
	public ArrayList<String> getAllBusinesses()
	{
		ArrayList<String> businessNames = new ArrayList<String>();
		try
		{
			openConnection();
			Statement stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT * FROM Businesses";
			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
		        //Retrieve by column name         
	         	String name = rs.getString("BusinessName");

				// build obj and add to list. -kg
	         	businessNames.add(name);
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
		
		return businessNames;
	}
	
	@Override
	protected void createTables()
	{
		logger.info("Creating master database tables...");

		Table businesses = new Table("Businesses");
		businesses.addColumn("businessName", "varchar(255)");
		businesses.setPrimary("businessName");
		System.out.println("Business Table Created!");
		
		Table admin = new Table("Admin");
		admin.addColumn("Username", "varchar(255)");
		admin.addColumn("Password", "varchar(255)");
		admin.setPrimary("Username");
		System.out.println("Admin Table Created!");
		
		try
		{
			try (Statement stmt = c.createStatement())
			{
				// Customer Table
				logger.fine("Creating table: " + businesses);
				stmt.execute(businesses.toString());
				stmt.execute(admin.toString());
				insert("testDB");
				insert("awesomeSauce");
				insert();
			}
		}
		catch (SQLException e)
		{
			logger.severe("SQL Exception in table creation: " + e);
		}
	}
	
	private boolean insert(String businessName)
			logger.fine("Added business to master: " + owner.username + " owner of " + businessName );
	{
		//TODO: Allow admin to specify details.
		BusinessDatabase bDb = new BusinessDatabase(businessName);
		BusinessOwner bo = new BusinessOwner("septB", businessName, "septB", "123 ABC Street", "0400000000");
		bDb.addAccount(bo, "septBus1");
		logger.fine("Added business Owner: " + bo.username );
		return insert("Businesses", businessName);
	}
	
	private boolean insert()
	{
		//Table, Username, Password
		return insert("Admin", "Admin", "admin");
	}
}
