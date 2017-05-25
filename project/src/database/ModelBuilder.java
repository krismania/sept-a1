package database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Functional interface to help build model objects from the database.
 * 
 * @author krismania
 */
interface ModelBuilder<T> {
    /**
     * Takes a ResultSet argument, and produces an object of type T.
     * 
     * @author krismania
     */
    T build(ResultSet rs) throws SQLException;
}
