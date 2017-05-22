package main;

import static org.junit.Assert.*;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

public class AvailabilityTest
{
	private Availability availability;
	
	@Before
	public void setUp() throws Exception
	{
		availability = new Availability();
		
		availability.addTimeSpan(LocalTime.of(10, 30), LocalTime.of(11, 45));
		availability.addTimeSpan(LocalTime.of(12, 30), LocalTime.of(15, 00));
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddInvalidTimeSpan1()
	{
		availability.addTimeSpan(LocalTime.of(8, 30), LocalTime.of(8, 15));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddInvalidTimeSpan2()
	{
		availability.addTimeSpan(LocalTime.of(8, 30), LocalTime.of(8, 30));
	}
	
	@Test
	public void testAddValidTimeSpan()
	{
		availability.addTimeSpan(LocalTime.of(8, 30), LocalTime.of(8, 35));
	}
}
