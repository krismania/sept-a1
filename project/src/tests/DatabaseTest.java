package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.UserDatabase;

public class DatabaseTest {

	public UserDatabase db;

	@Before
	public void setUp() throws Exception 
	{
		db = new UserDatabase("JUnitDataBase");
		db.CreateDatabase();
		db.CreateDatabaseTable("testTable", "Firstname varchar(255)", "Lastname varchar(255)",
				"Email varchar(255)", "Phone varchar(10)", "Username varchar(15)",
				"Password varchar(15)", "Username");
		db.CreateDataEntry("testTable", "Test", "Test", "testing@testing.com", 
				"0400000000", "JamesTest", "Test1");
	}

	@Test
	public void test() 
	{
		assertEquals(true, db.checkUsername("Test", "testTable"));
	}

}
