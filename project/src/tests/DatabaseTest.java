package tests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.Database;
import model.BusinessOwner;
import model.Customer;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseTest
{
	private static String dbName;
	private static Database db;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		// delete test db if it already exists
		Files.deleteIfExists(Paths.get("test.db"));
		
		// set db name
		dbName = "test";
		
		// Create a new test database, and populate it with data
		db = new Database(dbName);
		
		assertTrue(db.addAccount(new BusinessOwner("owner", "Some Business", "Ava Gordy", "123 Some St", "1300 123 456"), "Password1"));
		assertTrue(db.addAccount(new Customer("bobby-tables", "Robert", "Table", "drop@table.star", "12345678"), "SomePassword5"));
		assertTrue(db.addAccount(new Customer("krismania", "Kristian", "Giglia", "s3543819@rmit.edu.au", "8888 123 456"), "correcthorsebatterystaple"));
		assertTrue(db.addAccount(new Customer("jamesRulez", "James", "McLennan", "some@email.com", "5555 5555"), "Password2"));
	}
	
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		// TODO: implement
	}
	
	
	@Test
	public void testCustomerAccountExists()
	{
		assertEquals(db.getAccount("bobby-tables").username, "bobby-tables");
	}
	
	@Test
	public void testBusinessAccountExists()
	{
		assertEquals(db.getAccount("owner").username, "owner");
	}
	
}
