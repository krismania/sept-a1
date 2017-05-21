package database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.BusinessDatabase;
import database.model.Booking;
import database.model.BusinessOwner;
import database.model.Customer;
import database.model.Employee;
import database.model.Service;
import database.model.Shift;

/**
 * Tests on the Database class.
 * @author krismania
 */
public class BusinessDatabaseTest
{
	private static int COUNTER = 1;
	
	private String dbName;
	private BusinessDatabase db;
	
	private void removeDB() throws Exception
	{
		if (db != null)
		{
			db.close();
			db = null;
		}
		
		Path dbPath = Paths.get(dbName + ".db");
		
		if (Files.exists(dbPath))
		{
			try
			{
				Files.delete(dbPath);
				System.out.println("Deleted db file " + dbPath.toAbsolutePath());
			}
			catch (IOException e)
			{
				System.out.println("db file couldn't be deleted: " + e);
			}
		}

	}
	
	@Before
	public void setUp() throws Exception
	{
		// set db name
		dbName = "test" + COUNTER++;
		
		// delete test db if it already exists
		removeDB();
		
		// Create a new test database, and populate it with data
		db = new BusinessDatabase(dbName);
		
		assertTrue(db.addAccount(new BusinessOwner("owner", "Some Business", "Ava Gordy", "123 Some St", "1300 123 456"), "Password1"));
		assertTrue(db.addAccount(new Customer("bobby-tables", "Robert", "Table", "drop@table.star", "12345678"), "SomePassword5"));
		assertTrue(db.addAccount(new Customer("krismania", "Kristian", "Giglia", "s3543819@rmit.edu.au", "8888 123 456"), "correcthorsebatterystaple"));
		assertTrue(db.addAccount(new Customer("jamesRulez", "James", "McLennan", "some@email.com", "5555 5555"), "Password2"));
		
		assertTrue(db.addEmployee(new Employee(1, "Hideo", "Kojima", "some@email.com", "12345678")));
		assertTrue(db.addEmployee(new Employee(2, "Steven", "Suptic", "another@email.com", "12345678")));
		assertTrue(db.addEmployee(new Employee(3, "Woolington", "Madden", "yet-another@email.com", "55555555")));
		
		assertTrue(db.addShift(new Shift(1, 1, DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(18, 00))));
		assertTrue(db.addShift(new Shift(2, 1, DayOfWeek.WEDNESDAY, LocalTime.of(10, 30), LocalTime.of(18, 00))));
		assertTrue(db.addShift(new Shift(3, 1, DayOfWeek.FRIDAY, LocalTime.of(10, 30), LocalTime.of(16, 00))));
		assertTrue(db.addShift(new Shift(4, 3, DayOfWeek.MONDAY, LocalTime.of(8, 45), LocalTime.of(11, 30))));
		assertTrue(db.addShift(new Shift(5, 3, DayOfWeek.MONDAY, LocalTime.of(12, 00), LocalTime.of(16, 00))));
		
		assertTrue(db.addService(new Service(1, "Haircut", Duration.ofMinutes(15))));
		assertTrue(db.addService(new Service(2, "Colour", Duration.ofMinutes(30))));
		
		assertTrue(db.addBooking(new Booking(1, "krismania", 1, LocalDate.parse("2017-05-01"), LocalTime.of(12, 30), db.getService(2))));
		assertTrue(db.addBooking(new Booking(2, "jamesRulez", 1, LocalDate.parse("2017-05-08"), LocalTime.of(12, 30), db.getService(2))));
	}
	
	@After
	public void tearDown() throws Exception
	{
		removeDB();
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
		assertNotNull(db.getAccount("bobby-tables"));
	}
	
	@Test
	public void testAccountExists2()
	{
		assertNotNull(db.getAccount("owner"));
	}
	
	@Test
	public void testAccountNotExists()
	{
		assertNull(db.getAccount("notauser"));
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
		assertEquals(db.buildShift(1).ID, 6);
	}
	
	@Test
	public void testBuildBooking()
	{
		assertEquals(db.buildBooking().ID, 3);
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
	public void testGetShiftByID()
	{
		Shift s = new Shift(4, 3, DayOfWeek.MONDAY, LocalTime.of(8, 45), LocalTime.of(11, 30));
		Shift dbResult = db.getShift(s.ID);
		
		assertEquals(dbResult.ID, s.ID);
		assertEquals(dbResult.employeeID, s.employeeID);
		assertEquals(dbResult.getDay(), s.getDay());
		assertEquals(dbResult.getStart(), s.getStart());
		assertEquals(dbResult.getEnd(), s.getEnd());
	}
	
	@Test
	public void testGetShifts()
	{
		assertEquals(db.getShifts(DayOfWeek.MONDAY).size(), 3);
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
	
	@Test
	public void testDuplicateShift1()
	{
		// same start/end
		Shift s = db.buildShift(1);
		s.setDay(DayOfWeek.MONDAY);
		s.setStart(LocalTime.of(10, 30));
		s.setEnd(LocalTime.of(18, 00));
		
		assertFalse(db.addShift(s));
	}
	
	@Test
	public void testDuplicateShift2()
	{
		// starts before another shift ends
		Shift s = db.buildShift(1);
		s.setDay(DayOfWeek.MONDAY);
		s.setStart(LocalTime.of(17, 00));
		s.setEnd(LocalTime.of(18, 00));
		
		assertFalse(db.addShift(s));
	}
	
	@Test
	public void testDuplicateShift3()
	{
		// ends after another shift starts
		Shift s = db.buildShift(1);
		s.setDay(DayOfWeek.MONDAY);
		s.setStart(LocalTime.of(8, 15));
		s.setEnd(LocalTime.of(10, 45));
		
		assertFalse(db.addShift(s));
	}
	
	@Test
	public void testBackToBackShift1()
	{
		// starts when another shift ends
		Shift s = db.buildShift(1);
		s.setDay(DayOfWeek.MONDAY);
		s.setStart(LocalTime.of(18, 00));
		s.setEnd(LocalTime.of(22, 30));
		
		assertTrue(db.addShift(s));
	}
	
	@Test
	public void testBackToBackShift2()
	{
		// ends when another shift starts
		Shift s = db.buildShift(1);
		s.setDay(DayOfWeek.MONDAY);
		s.setStart(LocalTime.of(8, 00));
		s.setEnd(LocalTime.of(10, 30));
		
		assertTrue(db.addShift(s));
	}
	
	@Test
	public void testDuplicateBooking1()
	{
		// exact same booking
		Booking b = db.buildBooking();
		b.setCustomer("krismania");
		b.setEmployee(1);
		b.setDate(LocalDate.of(2017, 5, 1));
		b.setStart(LocalTime.of(12, 30));
		b.setService(db.getService(2));
		
		System.out.println(db.getService(2));
		
		assertFalse(db.addBooking(b));
	}
	
	@Test
	public void testDuplicateBooking2()
	{
		// different customer
		Booking b = db.buildBooking();
		b.setCustomer("jamesRulez");
		b.setEmployee(1);
		b.setDate(LocalDate.of(2017, 5, 1));
		b.setStart(LocalTime.of(12, 30));
		b.setService(db.getService(2));
		
		assertFalse(db.addBooking(b));
	}
	
	@Test
	public void testUpdateServiceValid()
	{
		assertTrue(db.updateService(new Service(2, "Hair Colour", Duration.ofMinutes(40))));
	}
	
	@Test
	public void testUpdateServiceInvalid()
	{
		// attempt to update a service that doesn't exist
		assertFalse(db.updateService(new Service(4, "Hair Colour", Duration.ofMinutes(40))));
	}
}
