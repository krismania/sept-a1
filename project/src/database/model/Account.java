package database.model;

/**
 * @author Krismaia
 * 
 *         Abstract account class; serves as a base for the different types of
 *         accounts that will be available in our solution. -kg
 */

public abstract class Account {
    public final String username;

    public Account(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username;
    }
}
