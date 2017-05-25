package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ModelBuilder<T>
{
	public T build(ResultSet rs) throws SQLException;
}
