package database.model;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import database.model.Booking;
import database.model.Service;

public class BookingTest
{
	public DateFormat dateFormat;
	public Booking booking1, booking1Clone, booking2, booking3, booking4;
	
	@Before
	public void setUp() throws ParseException
	{
		dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		// variables for constructor
		int bookingID, employeeID;
		String customer;
		LocalDate date;
		LocalTime start;
		Service service;
		
		// create some test objects
		service = new Service(1, "Test Service", Duration.ofMinutes(30));
		
		bookingID = 1;
		customer = "customer";
		employeeID = 5;
		date = LocalDate.of(2017, 3, 1);
		start = LocalTime.of(10, 30);
		
		booking1 = new Booking(bookingID, customer, employeeID, date, start, service);
		booking1Clone = new Booking(bookingID, customer, employeeID, date, start, service);
		
		bookingID = 2;
		customer = "customer";
		employeeID = 3;
		date = LocalDate.of(2017, 6, 12);
		start = LocalTime.of(17, 00);
		
		booking2 = new Booking(bookingID, customer, employeeID, date, start, service);
		
		bookingID = 3;
		customer = "someoneelse";
		employeeID = 7;
		date = LocalDate.of(2017, 6, 12);
		start = LocalTime.of(14, 45);
		
		booking3 = new Booking(bookingID, customer, employeeID, date, start, service);
		
		bookingID = 3;
		customer = "someoneelse";
		employeeID = 7;
		date = LocalDate.of(2017, 2, 21);
		start = LocalTime.of(14, 45);
		
		booking4 = new Booking(bookingID, customer, employeeID, date, start, service);
	}
	
	@Test
	public void testCompare1()
	{
		assertTrue(booking1.compareTo(booking2) < 0);
	}
	
	@Test
	public void testCompare2()
	{
		assertTrue(booking1.compareTo(booking4) > 0);
	}
	
	@Test
	public void testCompare3()
	{
		assertTrue(booking3.compareTo(booking2) < 0);
	}
	
	@Test
	public void testCompareEqual()
	{
		assertTrue(booking1.compareTo(booking1Clone) == 0);
	}
}
