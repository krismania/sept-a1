package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import database.model.BusinessOwner;

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
		businesses.addColumn("businessOwnerUsername", "varchar(255)");
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
				insert();
			}
		}
		catch (SQLException e)
		{
			logger.severe("SQL Exception in table creation: " + e);
		}
	}
	
	public boolean newBusiness(String businessName, BusinessOwner owner, String password)
	{
		if(!validateBusiness(businessName))
		{
			insert("Businesses", businessName, owner.username);
			logger.fine("Added business to master: " + owner.username + " owner of " + businessName );
			return insert(businessName, owner, password);
		}
		return false;
	}
	
	public boolean removeBusiness(String businessName)
	{
		boolean businessRemoved = false;
		try
		{
			Statement stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "DELETE FROM Businesses WHERE businessName = '" + businessName + "'";
			
			stmt.executeUpdate(sql);
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
		return businessRemoved;
	}
	
	private boolean insert(String businessName, BusinessOwner owner, String password)
	{
		//TODO: Allow admin to specify details.
		BusinessDatabase business = new BusinessDatabase(businessName);
		return business.addAccount(owner, password);
	}
	
	private boolean insert()
	{
		//Table, Username, Password
		return insert("Admin", "Admin", "admin");
	}
	
	private boolean validateBusiness(String businessName)
	{
		boolean businessExists = false;
		try
		{
			Statement stmt = c.createStatement();
			
			//JM Selected all constraints for a customer
			String sql = "SELECT businessName FROM Businesses WHERE businessName = '" + businessName + "'";
			
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next())
			{
				businessExists = true;
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
		return businessExists;
	}
}
