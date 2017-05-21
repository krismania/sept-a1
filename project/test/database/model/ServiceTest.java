package database.model;

import static org.junit.Assert.*;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import database.model.Service;

/**
 * @author krismania
 */
public class ServiceTest
{
	Service service1;
	Service service2;
	
	@Before
	public void setUp() throws Exception
	{
		service1 = new Service(1, "Haircut", Duration.ofMinutes(30));
		service2 = new Service(2, "Colour", Duration.ofMinutes(60));
	}
	
	@Test
	public void testGetEnd1()
	{
		LocalTime start = LocalTime.of(10, 30);
		LocalTime end = service1.getEnd(start);
		
		assertEquals(LocalTime.of(11, 00), end);
	}
	
	@Test
	public void testGetEnd2()
	{
		LocalTime start = LocalTime.of(15, 45);
		LocalTime end = service2.getEnd(start);
		
		assertEquals(LocalTime.of(16, 45), end);
	}
}
