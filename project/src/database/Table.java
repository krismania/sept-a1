package database;

import java.util.ArrayList;

/**
 * Representative object of an SQL table. The {@code toString()} method gives
 * the SQL required to create the represented table.
 *
 * @author krismania
 */
class Table {
    public final String name;

    private ArrayList<String> columns;
    private String primaryKey;
    private ArrayList<String> foreignKey;

    /**
     * @author James
     * @author Krismania
     *
     *         Create a new Table, with the given name.
     * @param name
     */
    protected Table(String name) {
        this.name = name;

        // init lists
        columns = new ArrayList<String>();
        foreignKey = new ArrayList<String>();
    }

    /**
     * Add a column with the given name, of given type. Type may me any SQL
     * type, such as {@code varchar(30)} or {@code date}
     *
     * @author James
     * @author Krismania
     */
    void addColumn(String name, String type) {
        columns.add("`" + name + "` " + type);
    }

    /**
     * Sets the primary column of the table. If the string passed is not the
     * name of a column in the table, it will cause an SQL error.
     *
     * @author James
     * @author Krismania
     */
    void setPrimary(String columnName) {
        primaryKey = "PRIMARY KEY (`" + columnName + "`)";
    }

    /**
     * Add a foreign key constraint. In terms of SQL, each fk adds an additional
     * foreign key clause that looks like {@code FOREIGN KEY (`[columnName]`)
     * REFERENCES [reference]}.
     *
     * @param columnName
     *            the column to constrain
     * @param reference
     *            the column to constrain based on.
     */
    void addForeignKey(String columnName, String reference) {
        foreignKey.add("FOREIGN KEY (`" + columnName + "`) REFERENCES " + reference);
    }

    /**
     * Returns the table specified by this object an SQL {@code CREATE TABLE}
     * statement.
     *
     * @author James
     * @author Krismania
     */
    @Override
    public String toString() {
        // get columns as string
        String colString = String.join(", ", columns);

        // get foreign keys as string
        String fkString = String.join(", ", foreignKey);

        // join it all together
        String tableString;
        if (primaryKey != null)
            tableString = colString + ", " + primaryKey;
        else
            tableString = colString;

        // only join fk if there are any
        if (!fkString.equals("")) {
            tableString += ", " + fkString;
        }

        return "CREATE TABLE `" + name + "` (" + tableString + ")";
    }
}