package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import database.model.Account;

public abstract class Database {

    /**
     * DB Connection object
     *
     * @author James
     * @author Krismania
     */
    protected Connection c;

    /**
     * The name of the DB file, excluding it's extension
     *
     * @author James
     * @author Krismania
     */
    private String dbName;
    // private boolean isAdmin = false;

    protected Logger logger;

    /**
     * Instantiates the database, which will read to and from the .db file with
     * the given name. If the database doesn't exist, it is created and seeded.
     *
     * @author James
     * @author krismania
     */
    public Database(String dbName) {
        // get the logger
        logger = Logger.getLogger(getClass().getName());

        // set up db
        this.dbName = dbName;
        open();
        CreateDatabase();

        logger.info("Instantiated DB (" + getClass().getName() + ")");
    }

    /**
     * Attempt to log into an account with the provided credentials. If the
     * login is successful, am {@link Account} object will be returned,
     * otherwise the return value is null.
     *
     * @author krismania
     */
    public Account login(String username, String password) {
        Account account = getAccount(username);

        if (validatePassword(account, password)) {
            return account;
        }

        return null;
    }

    /**
     * Perform a query on the database and return the objects found.
     *
     * @author krismania
     */
    protected <T> ArrayList<T> query(String sql, ModelBuilder<T> builder) {
        ArrayList<T> resultArray = new ArrayList<T>();

        try (Statement stmt = c.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    resultArray.add(builder.build(rs));
                }
            }
        } catch (SQLException e) {
            logger.warning("Query failed :" + e);
        }

        return resultArray;
    }

    /**
     * Queries the database and returns the first object found.
     *
     * @author krismania
     */
    protected <T> T querySingle(String sql, ModelBuilder<T> builder) {
        ArrayList<T> resultArray = query(sql, builder);

        if (resultArray != null && resultArray.size() > 0) {
            return resultArray.get(0);
        }

        return null;
    }

    /**
     * Insert the given values into the specified table.
     *
     * @param table
     *            The database table to insert into
     * @param values
     *            Values to insert
     * @author krismania
     */
    protected boolean insert(String table, String... values) {
        // prepare values
        for (int i = 0; i < values.length; i++) {
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

        try {
            Statement stmt = c.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            logger.warning("SQL Exception: " + e.toString());
            return false;
        }
    }

    /**
     * Attempts to open a database connection, storing the connection object as
     * a class variable.
     *
     * @author James
     */
    protected boolean open() {
        // added try-catch to capture sqlException here. -kg
        try {
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
            if (c != null) {
                return true;
            }
        } catch (SQLException e) {
            logger.severe("DB Could not open: SQLException");
        }
        return false;
    }

    /**
     * Calls the close function on the database connection and sets it to null.
     *
     * @author James
     */
    protected boolean close() {
        try {
            if (c != null) {
                c.close();
                c = null;
                logger.info("Closed connection");
            }
        } catch (SQLException e) {
            logger.warning("DB Connection failed to close");
            return false;
        }
        return true;
    }

    /**
     * Get the account with the given username from this database.
     *
     * @author James
     * @author Krismania
     */
    public abstract Account getAccount(String username);

    /**
     * Returns true if the given password matches the account.
     *
     * @author James
     * @author Krismania
     */
    protected abstract boolean validatePassword(Account account, String password);

    /**
     * Database-specific list of table objects.
     *
     * @author James
     * @author Krismania
     */
    protected abstract ArrayList<Table> createTables();

    /**
     * Add required initial dat
     *
     * @author James
     * @author Krismania
     */
    protected abstract boolean seed();

    /**
     * Initialises the database's tables if the database is empty.
     *
     * @author James
     * @author krismania
     */
    private void CreateDatabase() {
        // JM Initialize a connection
        try (Statement stmt = c.createStatement()) {
            // test if the db is empty. -kg
            try (ResultSet rs = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type = 'table'")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    logger.info("Database empty, initialising tables");
                    insertTables(createTables());
                    logger.info("Seeding database");
                    seed();
                }
            }
        } catch (Exception e) {
            logger.severe(e.toString());
            System.exit(0);
        }
    }

    /**
     * Uses the abstract {@link #createTables()} method to generate this
     * database's tables, then inserts them.
     *
     * @author krismania
     */
    private void insertTables(ArrayList<Table> tables) {
        try (Statement stmt = c.createStatement()) {
            // add all tables to the db
            for (Table table : tables) {
                logger.fine("Creating table: " + table);
                stmt.execute(table.toString());
            }
        } catch (SQLException e) {
            logger.severe("SQL Exception in table creation: " + e);
        }
    }

    public String getName()
    {
        return dbName;
    }

}
