package database;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test table building helper functionality
 * @author krismania
 */
public class TableTest
{
	private Table t;
	
	@Before
	public void setUp() throws Exception
	{
		t = new Table("TestTable");
		
		t.addColumn("SomeCol", "int");
		t.addColumn("someOtherCol", "varchar(15)");
	}
	
	
	@Test
	public void testAddColumn()
	{
		t.addColumn("Column", "varchar(10)");
		
		assertEquals("CREATE TABLE `TestTable` (`SomeCol` int, `someOtherCol` varchar(15), `Column` varchar(10))", 
						t.toString());
	}
	
	
	@Test
	public void testSetPrimary()
	{
		t.setPrimary("SomeCol");
		
		assertEquals("CREATE TABLE `TestTable` (`SomeCol` int, `someOtherCol` varchar(15), PRIMARY KEY (`SomeCol`))", 
						t.toString());
	}
	
	
	@Test
	public void testAddForeignKey()
	{
		t.addForeignKey("SomeCol", "Table(SomeCol)");
		
		assertEquals("CREATE TABLE `TestTable` (`SomeCol` int, `someOtherCol` varchar(15), FOREIGN KEY (`SomeCol`) REFERENCES Table(SomeCol))",
						t.toString());
	}
	
	
	@Test
	public void testToString()
	{
		assertEquals("CREATE TABLE `TestTable` (`SomeCol` int, `someOtherCol` varchar(15))", 
						t.toString());
	}
	
}
