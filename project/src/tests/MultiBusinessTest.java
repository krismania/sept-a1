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
import model.BusinessOwner;
import model.ShiftTime;

public class MultiBusinessTest
{
	public BusinessOwner businessOwner;
	
	@Before
	public void setUp() throws Exception
	{
		businessOwner = new BusinessOwner("", "Businesses", 
				"", "", "");
	}	
		//TN test business owner info getters.
		
		@Test
		public void testGetBusinessName()
		{
			assertEquals("Businesses", businessOwner.getBusinessName());
		}
		
}
