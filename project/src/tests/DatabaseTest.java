package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import database.Database;
import model.Booking;
import model.BusinessOwner;
import model.Customer;
import model.Employee;
import model.Shift;

/**
 * Tests on the Database class.
 * @author krismania
 */
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
		
		assertTrue(db.addEmployee(new Employee(1, "Hideo", "Kojima", "some@email.com", "12345678")));
		assertTrue(db.addEmployee(new Employee(2, "Steven", "Suptic", "another@email.com", "12345678")));
		assertTrue(db.addEmployee(new Employee(3, "Woolington", "Madden", "yet-another@email.com", "55555555")));
		
		assertTrue(db.addShift(new Shift(1, 1, DayOfWeek.MONDAY, LocalTime.parse("10:30"))));
		assertTrue(db.addShift(new Shift(2, 1, DayOfWeek.MONDAY, LocalTime.parse("12:30"))));
		assertTrue(db.addShift(new Shift(3, 1, DayOfWeek.MONDAY, LocalTime.parse("15:30"))));
		assertTrue(db.addShift(new Shift(4, 3, DayOfWeek.MONDAY, LocalTime.parse("10:30"))));
		
		assertTrue(db.addBooking(new Booking(1, "krismania", 1, LocalDate.parse("2017-05-01"), LocalTime.parse("12:30"))));
		assertTrue(db.addBooking(new Booking(2, "jamesRulez", 1, LocalDate.parse("2017-05-08"), LocalTime.parse("12:30"))));
	}
	
	
	@Test
	public void testCustomerAccountData()
	{
		Customer c = new Customer("bobby-tables", "Robert", "Table", "drop@table.star", "12345678");
		Customer dbResult = (Customer) db.getAccount(c.username);
		
		assertEquals(dbResult.username, c.username);
		assertEquals(dbResult.getFirstName(), c.getFirstName());
		assertEquals(dbResult.getLastName(), c.getLastName());
		assertEquals(dbResult.getEmail(), c.getEmail());
		assertEquals(dbResult.getPhoneNumber(), c.getPhoneNumber());
	}
	
	@Test
	public void testBOAccountData()
	{
		BusinessOwner bo = new BusinessOwner("owner", "Some Business", "Ava Gordy", "123 Some St", "1300 123 456");
		BusinessOwner dbResult = (BusinessOwner) db.getAccount(bo.username);
		
		assertEquals(dbResult.username, bo.username);
		assertEquals(dbResult.getBusinessName(), bo.getBusinessName());
		assertEquals(dbResult.getName(), bo.getName());
		assertEquals(dbResult.getAddress(), bo.getAddress());
		assertEquals(dbResult.getPhoneNumber(), bo.getPhoneNumber());
	}
	
	@Test
	public void testAccountExists1()
	{
		assertTrue(db.accountExists("bobby-tables"));
	}
	
	@Test
	public void testAccountExists2()
	{
		assertTrue(db.accountExists("owner"));
	}
	
	@Test
	public void testAccountNotExists()
	{
		assertFalse(db.accountExists("notauser"));
	}
	
	@Test
	public void testBookingBuilder()
	{
		db.buildBooking();
	}
	
	@Test
	public void testBuildEmployee()
	{
		assertEquals(db.buildEmployee().ID, 4);
	}
	
	@Test
	public void testBuildShift()
	{
		assertEquals(db.buildShift(1).ID, 5);
	}
	
	@Test
	public void testBuildBooking()
	{
		assertEquals(db.buildBooking().ID, 3);
	}
	
	@Test
	public void testGetAllBusinessOwners()
	{
		ArrayList<BusinessOwner> owners = db.getAllBusinessOwners();
		
		assertEquals(owners.size(), 1);
	}
	
	@Test
	public void testGetAllCustomers()
	{
		ArrayList<Customer> customers = db.getAllCustomers();
		
		assertEquals(customers.size(), 3);
	}
	
	@Test
	public void testGetAllEmployees()
	{
		ArrayList<Employee> employees = db.getAllEmployees();
		
		assertEquals(employees.size(), 3);
	}
	
	@Test
	public void testGetEmployeeByID()
	{
		Employee e = new Employee(1, "Hideo", "Kojima", "some@email.com", "12345678");
		
		Employee dbResult = db.getEmployee(1);
		
		assertEquals(dbResult.ID, e.ID);
		assertEquals(dbResult.getFirstName(), e.getFirstName());
		assertEquals(dbResult.getLastName(), e.getLastName());
		assertEquals(dbResult.getEmail(), e.getEmail());
		assertEquals(dbResult.getPhoneNumber(), e.getPhoneNumber());
	}
	
	@Test
	public void testGetEmployeeByDate()
	{
		assertEquals(db.getEmployeeWorkingOnDay(LocalDate.parse("2017-05-01")).size(), 2);
	}
	
	@Test
	public void testGetShiftByID()
	{
		Shift s = new Shift(1, 1, DayOfWeek.MONDAY, LocalTime.parse("10:30"));
		Shift dbResult = db.getShift(1);
		
		assertEquals(dbResult.ID, s.ID);
		assertEquals(dbResult.employeeID, s.employeeID);
		assertEquals(dbResult.getDay(), s.getDay());
		assertEquals(dbResult.getTime(), s.getTime());
	}
	
	@Test
	public void testGetShiftBookings()
	{
		// TODO: Write test
		fail("test not prepared");
	}
	
	@Test
	public void testGetShifts()
	{
		// TODO: Write test
		fail("test not prepared");
	}
	
	@Test
	public void testLogin1()
	{
		assertTrue(db.login("krismania", "correcthorsebatterystaple") instanceof Customer);
	}
	
	@Test
	public void testLogin2()
	{
		assertTrue(db.login("bobby-tables", "SomePassword5") instanceof Customer);
	}
	
	@Test
	public void testLogin3()
	{
		assertTrue(db.login("owner", "Password1") instanceof BusinessOwner);
	}
	
	@Test
	public void testLoginFail1()
	{
		assertNull(db.login("krismania", "wrongpw"));
	}
	
	@Test
	public void testLoginFail2()
	{
		assertNull(db.login("badaccount", "correcthorsebatterystaple"));
	}
}
