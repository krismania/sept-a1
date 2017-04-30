package tests;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import model.Booking;
import model.ShiftTime;

public class BookingTest
{
	public DateFormat dateFormat;
	public Booking booking1, booking2, booking3, booking4;
	
	@Before
	public void setUp() throws ParseException
	{
		dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		// variables for constructor
		int bookingID, employeeID;
		String customer;
		Date date;
		ShiftTime time;
		
		// create some test objects
		
		bookingID = 1;
		customer = "customer";
		employeeID = 5;
		date = dateFormat.parse("01-03-2017");
		time = ShiftTime.MORNING;
		
		booking1 = new Booking(bookingID, customer, employeeID, date, time);
		
		bookingID = 2;
		customer = "customer";
		employeeID = 3;
		date = dateFormat.parse("12-6-2017");
		time = ShiftTime.EVENING;
		
		booking2 = new Booking(bookingID, customer, employeeID, date, time);
		
		bookingID = 3;
		customer = "someoneelse";
		employeeID = 7;
		date = dateFormat.parse("12-6-2017");
		time = ShiftTime.AFTERNOON;
		
		booking3 = new Booking(bookingID, customer, employeeID, date, time);
		
		bookingID = 3;
		customer = "someoneelse";
		employeeID = 7;
		date = dateFormat.parse("21-2-2017");
		time = ShiftTime.AFTERNOON;
		
		booking4 = new Booking(bookingID, customer, employeeID, date, time);
	}
	
	
	@Test
	public void testID()
	{
		assertEquals(booking1.ID, 1);
	}

	@Test
	public void testCustomer()
	{
		assertEquals(booking1.getCustomer(), "customer");
	}

	@Test
	public void testEmployeeID()
	{
		assertEquals(booking1.getEmployeeID(), 5);
	}

	@Test
	public void testDate() throws ParseException
	{
		assertEquals(booking1.getDate(), dateFormat.parse("01-03-2017"));
	}

	@Test
	public void testDay()
	{
		assertEquals(booking1.getDay(), DayOfWeek.WEDNESDAY);
	}

	@Test
	public void testTime()
	{
		assertEquals(booking1.getTime(), ShiftTime.MORNING);
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
		assertTrue(booking1.compareTo(booking1) == 0);
	}
}
