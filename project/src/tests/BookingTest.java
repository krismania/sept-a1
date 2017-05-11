package tests;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.Booking;
import model.ShiftTime;

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
		LocalTime end;
		
		// create some test objects
		
		bookingID = 1;
		customer = "customer";
		employeeID = 5;
		date = LocalDate.of(2017, 3, 1);
		start = LocalTime.of(10, 30);
		end = LocalTime.of(11, 30);
		
		booking1 = new Booking(bookingID, customer, employeeID, date, start, end);
		booking1Clone = new Booking(bookingID, customer, employeeID, date, start, end);
		
		bookingID = 2;
		customer = "customer";
		employeeID = 3;
		date = LocalDate.of(2017, 6, 12);
		start = LocalTime.of(17, 00);
		end = LocalTime.of(17, 15);
		
		booking2 = new Booking(bookingID, customer, employeeID, date, start, end);
		
		bookingID = 3;
		customer = "someoneelse";
		employeeID = 7;
		date = LocalDate.of(2017, 6, 12);
		start = LocalTime.of(14, 45);
		end = LocalTime.of(15, 15);
		
		booking3 = new Booking(bookingID, customer, employeeID, date, start, end);
		
		bookingID = 3;
		customer = "someoneelse";
		employeeID = 7;
		date = LocalDate.of(2017, 2, 21);
		start = LocalTime.of(14, 45);
		end = LocalTime.of(14, 55);
		
		booking4 = new Booking(bookingID, customer, employeeID, date, start, end);
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
