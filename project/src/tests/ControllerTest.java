package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Controller;

public class ControllerTest
{

	// test static validator methods. -kg	
	@Test
	public void testValidName()
	{
		assertTrue(Controller.validateName("John"));
	}
	
	@Test
	public void testInvalidName()
	{
		assertFalse(Controller.validateName(""));
	}
	
	@Test
	public void testValidEmail()
	{
		assertTrue(Controller.validateEmail("some-email@gmail.com"));
	}
	
	@Test
	public void testInvalidEmail1()
	{
		// no @ symbol
		assertFalse(Controller.validateEmail("myemail.website.com"));
	}
	
	@Test
	public void testInvalidEmail2()
	{
		// no . symbol
		assertFalse(Controller.validateEmail("emailwithout@dots"));
	}
	
	@Test
	public void testInvalidEmail3()
	{
		// no . or @ symbols
		assertFalse(Controller.validateEmail("thisisdefinitelynotanemail"));
	}
	
	// @author - RK
	@Test
	public void testInvalidEmail4(){
		assertFalse(Controller.validateEmail("richard@kuoch@"));
	}
	
	//@author -RK
	@Test
	
	public void testPhoneNumber(){
		assertFalse(Controller.validatePhoneNumber(""));
	}
	
	public void testPhoneNumber2(){
		assertTrue(Controller.validatePhoneNumber("98029744"));
	}
	
}
