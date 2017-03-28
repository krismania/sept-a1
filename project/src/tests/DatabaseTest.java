package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

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
		//Customer Table
		db.CreateDatabaseTable("Customers", "Firstname varchar(255)", "Lastname varchar(255)",
						"Email varchar(255)", "Phone varchar(10)", "Username varchar(15)",
						"Password varchar(15)", "Username");
				
		//BusinessOwner Table
		db.CreateDatabaseTable("BusinessOwner", "Firstname varchar(255)", "Lastname varchar(255)",
						"Email varchar(255)", "Phone varchar(10)", "Username varchar(15)",
						"Password varchar(15)", "Username");
				
	    //Employee Table
		db.CreateDatabaseTable("Employee", "Firstname varchar(255)", "Lastname varchar(255)",
						"Email varchar(255)", "Phone varchar(10)", "EmpID varchar(20)", "EmpID");
				
		db.CreateDataEntry("Customers", "James", "McLennan", "testing@testing.com", 
						"0400000000", "JamesRulez", "james");
				
		db.CreateDataEntry("BusinessOwner", "John", "Doe", "rabbits@rocks.com",
						"0400000000", "JohnRulez", "john");
				
		db.CreateDataEntry("Employee", "Fred", "Cutshair", "fred.cutshair@thebesthairshop.com", 
						"0400000000", "E001");
	}

	@Test
	public void openConnectionTest() throws SQLException 
	{
		assertEquals(true, db.openConnection());
	}
	
	@Test
	public void validateUserDoesExistByUsername() throws SQLException
	{
		assertEquals(true, db.validateUsername("JamesRulez"));
	}
	
	@Test
	public void validateUserDoesNotExistByUsername() throws SQLException
	{
		assertEquals(false, db.validateUsername("Blehasdji123"));
	}
	
	@Test
	public void validateEmployeeDoesExistByID() throws SQLException
	{
		assertEquals(true, db.validateEmpID("E001"));
	}
	
	@Test
	public void validateEmployeeDoesNotExistByID() throws SQLException
	{
		assertEquals(false, db.validateEmpID("abc001"));
	}

	@Test
	public void validatePasswordDoesMatchCustomersSetPassword() throws SQLException
	{
		assertEquals(true, db.checkPassword("JamesRulez", "james", "Customers"));
	}
	
	@Test
	public void validatePasswordDoesNotMatchCustomersSetPassword() throws SQLException
	{
		assertEquals(false, db.checkPassword("JamesRulez", "ksA1jdlksa", "Customers"));
	}
	
	@Test
	public void validatePasswordDoesMatchBusinessOwnerSetPassword() throws SQLException
	{
		assertEquals(true, db.checkPassword("JohnRulez", "john", "BusinessOwner"));
	}
	
	@Test
	public void validatePasswordDoesNotMatchBusinessOwnerSetPassword() throws SQLException
	{
		assertEquals(false, db.checkPassword("JohnRulez", "jkosadJ1", "BusinessOwner"));
	}
	
	@Test
	public void closeConnectionTest() throws SQLException 
	{
		assertEquals(true, db.closeConnection());
	}
}
