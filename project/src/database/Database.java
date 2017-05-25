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
	protected Connection c;

	/**
	 * The name of the DB file, excluding it's extension
	 */
	private String dbName;
	//private boolean isAdmin = false;
	
	protected Logger logger;
	
	/**
	 * Instantiates the database, which will read to and from the .db file with
	 * the given name. If the database doesn't exist, it is created and seeded.
	 * @author James
	 * @author krismania
	 */
	public Database(String dbName)
	{
		// get the logger
		logger = Logger.getLogger(getClass().getName());
		
		// set up db
		this.dbName = dbName;
		open();
		CreateDatabase();

		logger.info("Instantiated DB (" + getClass().getName() + ")");
	}
	
	/**
	 * TODO: Attempts to open a database connection, storing the connection
	 * object as a class variable.
	 * @author James
	 */
	protected boolean open()
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
	 * Calls the close function on the database connection and sets it to null.
	 * @author James
	 */
	protected boolean close()
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
	
	/**
	 * Create the database
	 * TODO: better documentation
	 * @author James
	 * @author krismania
	 */
	private void CreateDatabase()
	{
		//JM Initialize a connection
		try (Statement stmt = c.createStatement())
		{
			// test if the db is empty. -kg
			try (ResultSet rs = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type = 'table'"))
			{
				if (rs.next() && rs.getInt(1) == 0)
				{
					insertTables(createTables());
				}
			}
		}
		catch (Exception e)
		{
			logger.severe(e.toString());
			System.exit(0);
		}
	}

	/**
	 * Attempt to log into an account with the provided credentials. If the login
	 * is successful, am {@link Account} object will be returned, otherwise
	 * the return value is null.
	 * @author krismania
	 */
	public Account login(String username, String password)
	{
		Account account = getAccount(username);
		
		if (validatePassword(account, password))
		{
			return account;
		}
		
		return null;
	}
	
	public abstract Account getAccount(String username);
	protected abstract boolean validatePassword(Account account, String password);
	
//	/**
//	 * Returns a class object describing which type of user {@code username} is,
//	 * or null if the username is not found.
//	 * @author James
//	 * @author krismania
//	 */
//	private Class<? extends Account> validateUsername(String username) 
//	{		
//		if(!isAdmin)
//		{
//			String query = "SELECT Username, Type "
//				+ "FROM (SELECT Username, Type from Customer "
//				+ "UNION "
//				+ "SELECT Username, Type from BusinessOwner"
//				+ ") a "
//				+ "WHERE Username = '"+username+"'";
//
//			try 
//			{
//				Statement stmt = c.createStatement();
//				ResultSet rs = stmt.executeQuery(query);
//	
//				if(rs.next())
//				{
//					String type = rs.getString("Type");
//	
//					if(type.equals("BusinessOwner"))
//					{
//						return BusinessOwner.class;
//					}
//					else if (type.equals("Customer"))
//					{
//						return Customer.class;
//					}
//				}			
//	
//			} catch (SQLException e) {
//				//JM Catch if table already exists
//				logger.warning(e.toString());
//			} catch (Exception e) {
//				//JM Handles errors for Class.forName
//				logger.warning(e.toString());
//			}
//		}
//		else
//		{
//			String query = "SELECT Username "
//					+ "FROM Admin";
//
//				try 
//				{
//					Statement stmt = c.createStatement();
//					ResultSet rs = stmt.executeQuery(query);
//		
//					if(rs.next())
//					{
//						String user = rs.getString("Username");
//						if(user.equals("Admin"))
//						{
//							return Admin.class;
//						}
//					}			
//		
//				} catch (SQLException e) {
//					//JM Catch if table already exists
//					logger.warning(e.toString());
//				} catch (Exception e) {
//					//JM Handles errors for Class.forName
//					logger.warning(e.toString());
//				}
//		}
//		return null;
//	}
//
//	/**
//	 * returns true if the username & password match in the given table.
//	 * @author krismania
//	 * @author James
//	 */
//	private boolean validatePassword(String username, String password, String tableName)
//	{
//		String sql = String.format("SELECT password FROM %s WHERE username='%s'", tableName, username);
//
//		try
//		{
//			Statement stmt = c.createStatement();
//			ResultSet rs = stmt.executeQuery(sql);
//			rs.next();
//			if (rs.getString(1).equals(password)) {
//				return true;
//			}
//		}
//		catch (SQLException e)
//		{
//			logger.warning(e.toString());
//		}
//
//		return false;
//	}
	
	/**
	 * Perform a query on the database and return the objects found.
	 * @author krismania
	 */
	protected <T> ArrayList<T> query(String sql, ModelBuilder<T> builder)
	{
		ArrayList<T> resultArray = new ArrayList<T>();
		
		try (Statement stmt = c.createStatement())
		{
			try (ResultSet rs = stmt.executeQuery(sql))
			{
				while (rs.next())
				{
					resultArray.add(builder.build(rs));
				}
			}
		}
		catch (SQLException e)
		{
			logger.warning("Query failed :" + e);
		}
		
		return resultArray;
	}
	
	/**
	 * Queries the database and returns the first object found.
	 * @author krismania
	 */
	protected <T> T querySingle(String sql, ModelBuilder<T> builder)
	{
		ArrayList<T> resultArray = query(sql, builder);
		
		if (resultArray != null && resultArray.size() > 0)
		{
			return resultArray.get(0);
		}
		
		return null;
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
	 * Database-specific list of table objects.
	 */
	protected abstract ArrayList<Table> createTables();
	
	/**
	 * Uses the abstract {@link #createTables()} method to generate this database's
	 * tables, then inserts them.
	 * @author krismania
	 */
	private void insertTables(ArrayList<Table> tables)
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
		catch (SQLException e)
		{
			logger.severe("SQL Exception in table creation: " + e);
		}
	}

}
