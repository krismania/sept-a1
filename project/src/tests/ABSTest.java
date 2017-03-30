package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.ABS;

public class ABSTest
{
	// test static validator methods. -kg
	
	@Test
	public void testValidName()
	{
		assertTrue(ABS.validateName("John"));
	}
	
	@Test
	public void testInvalidName()
	{
		assertFalse(ABS.validateName(""));
	}
	
	@Test
	public void testValidEmail()
	{
		assertTrue(ABS.validateEmail("some-email@gmail.com"));
	}
	
	@Test
	public void testInvalidEmail1()
	{
		// no @ symbol
		assertFalse(ABS.validateEmail("myemail.website.com"));
	}
	
	@Test
	public void testInvalidEmail2()
	{
		// no . symbol
		assertFalse(ABS.validateEmail("emailwithout@dots"));
	}
	
	@Test
	public void testInvalidEmail3()
	{
		// no . or @ symbols
		assertFalse(ABS.validateEmail("thisisdefinitelynotanemail"));
	}
}
