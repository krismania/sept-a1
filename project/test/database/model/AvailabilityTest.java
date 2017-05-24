package database.model;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import database.model.Availability;
import database.model.Booking;
import database.model.Service;
import database.model.Shift;

/**
 * Tests of the Availability class
 * @author krismania
 */
public class AvailabilityTest
{
	static Service s1, s2;
	
	private Availability availability;
	
	@Before
	public void setUp() throws Exception
	{
		availability = new Availability();
				
		s1 = new Service(1, "Service1", Duration.ofMinutes(15));
		s2 = new Service(2, "Service2", Duration.ofMinutes(30));
		
		availability.addShift(new Shift(1, 1, DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(15, 00)));
		availability.addBooking(new Booking(1, "Customer", 1, LocalDate.of(2017, 5, 29), LocalTime.of(11, 00), s1));
	}
	
	
	@Test
	public void testGetAvailability()
	{
		TreeSet<TimeSpan> expected = new TreeSet<TimeSpan>();
		expected.add(new TimeSpan(LocalTime.of(10, 30), LocalTime.of(11, 00)));
		expected.add(new TimeSpan(LocalTime.of(11, 15), LocalTime.of(15, 00)));
		
		assertEquals(expected, availability.getAvailability());
	}
	
	
//	@Test(expected = IllegalArgumentException.class)
//	public void testAddInvalidTimeSpan1()
//	{
//		availability.addTimeSpan(LocalTime.of(8, 30), LocalTime.of(8, 15));
//	}
//	
//	@Test(expected = IllegalArgumentException.class)
//	public void testAddInvalidTimeSpan2()
//	{
//		availability.addTimeSpan(LocalTime.of(8, 30), LocalTime.of(8, 30));
//	}
//	
//	@Test
//	public void testAddValidTimeSpan()
//	{
//		availability.addTimeSpan(LocalTime.of(8, 30), LocalTime.of(8, 35));
//	}
}
