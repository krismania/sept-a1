package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import database.model.Account;
import database.model.Admin;
import database.model.BusinessOwner;

public class MasterDatabase extends Database {

    public MasterDatabase(String dbName) {
        super(dbName);
    }

    /**
     * Returns an arrayList of all businessNames Used for master DB
     * 
     * @author James
     */
    public ArrayList<String> getAllBusinesses() {
        ArrayList<String> businessNames = new ArrayList<String>();
        try {
            Statement stmt = c.createStatement();

            // JM Selected all constraints for a customer
            String sql = "SELECT * FROM Businesses";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Retrieve by column name
                String name = rs.getString("BusinessName");

                // build obj and add to list. -kg
                businessNames.add(name);
            }
        } catch (SQLException e) {
            // JM Handle errors for JDBC
            logger.warning(e.toString());
        } catch (Exception e) {
            // JM Handle errors for Class.forName
            logger.warning(e.toString());
        }

        return businessNames;
    }

    /**
     * Generates the tables required for the business database
     * 
     * @author James
     */
    @Override
    protected ArrayList<Table> createTables() {
        ArrayList<Table> tables = new ArrayList<Table>();

        Table businesses = new Table("Businesses");
        businesses.addColumn("businessName", "varchar(255)");
        businesses.addColumn("businessOwnerUsername", "varchar(255)");
        businesses.setPrimary("businessName");
        tables.add(businesses);

        Table admin = new Table("Admin");
        admin.addColumn("Username", "varchar(255)");
        admin.addColumn("Password", "varchar(255)");
        admin.setPrimary("Username");
        tables.add(admin);

        return tables;
    }

    /**
     * Adds a new business entity to the DB
     * 
     * @author James
     * @author Krismania
     */
    public boolean newBusiness(String businessName, BusinessOwner owner, String password) {
        if (!validateBusiness(businessName)) {
            insert("Businesses", businessName, owner.username);
            logger.fine("Added business to master: " + owner.username + " owner of " + businessName);
            return insert(businessName, owner, password);
        }
        return false;
    }

    /**
     * Removes a business entity from the DB
     * 
     * @author James
     * @author Krismania
     */
    public boolean removeBusiness(String businessName) {
        boolean businessRemoved = false;
        try {
            Statement stmt = c.createStatement();

            // JM Selected all constraints for a customer
            String sql = "DELETE FROM Businesses WHERE businessName = '" + businessName + "'";

            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            // JM Handle errors for JDBC
            logger.warning(e.toString());
        } catch (Exception e) {
            // JM Handle errors for Class.forName
            logger.warning(e.toString());
        }
        return businessRemoved;
    }

    /**
     * Adds business login to DB
     * 
     * @author James
     * @author Krismania
     */
    private boolean insert(String businessName, BusinessOwner owner, String password) {
        // TODO: Allow admin to specify details.
        BusinessDatabase business = new BusinessDatabase(businessName);
        return business.addAccount(owner, password);
    }

    /**
     * Checks the DB business record
     * 
     * @author James
     * @author Krismania
     */
    private boolean validateBusiness(String businessName) {
        boolean businessExists = false;
        try {
            Statement stmt = c.createStatement();

            // JM Selected all constraints for a customer
            String sql = "SELECT businessName FROM Businesses WHERE businessName = '" + businessName + "'";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                businessExists = true;
            }
        } catch (SQLException e) {
            // JM Handle errors for JDBC
            logger.warning(e.toString());
        } catch (Exception e) {
            // JM Handle errors for Class.forName
            logger.warning(e.toString());
        }
        return businessExists;
    }

    /**
     * account type from DB
     * 
     * @author James
     * @author Krismania
     */
    @Override
    public Account getAccount(String username) {
        String sql = String.format("SELECT * FROM Admin WHERE Username = '%s'", username);
        return querySingle(sql, new ModelBuilder<Admin>() {
            @Override
            public Admin build(ResultSet rs) throws SQLException {
                return new Admin(rs.getString("Username"));
            }
        });
    }

    /**
     * Checks password entry agains DB record
     * 
     * @author James
     * @author Krismania
     */
    @Override
    protected boolean validatePassword(Account account, String password) {
        String sql = String.format("SELECT * FROM Admin WHERE Username = '%s'", account.username);

        try (Statement stmt = c.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    if (rs.getString("Password").equals(password)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            logger.warning(e.toString());
        }

        return false;
    }

    @Override
    protected boolean seed() {
        return insert("Admin", "Admin", "admin");
    }
}
