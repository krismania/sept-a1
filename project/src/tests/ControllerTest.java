package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Controller;

public class ControllerTest
{
	public Controller controller;
	
	@Before
	public void setUp()
	{
		controller = Controller.getInstance();
	}

	// test static validator methods. -kg	
	@Test
	public void testValidName()
	{
		assertTrue(controller.validateName("John"));
	}
	
	@Test
	public void testInvalidName()
	{
		assertFalse(controller.validateName(""));
	}
	
	@Test
	public void testValidEmail()
	{
		assertTrue(controller.validateEmail("some-email@gmail.com"));
	}
	
	@Test
	public void testInvalidEmail1()
	{
		// no @ symbol
		assertFalse(controller.validateEmail("myemail.website.com"));
	}
	
	@Test
	public void testInvalidEmail2()
	{
		// no . symbol
		assertFalse(controller.validateEmail("emailwithout@dots"));
	}
	
	@Test
	public void testInvalidEmail3()
	{
		// no . or @ symbols
		assertFalse(controller.validateEmail("thisisdefinitelynotanemail"));
	}
	
	// @author - RK
	@Test
	public void testInvalidEmail4(){
		assertFalse(controller.validateEmail("richard@kuoch@"));
	}
	
	//@author -RK
	@Test
	public void testPhoneNumber(){
		assertFalse(controller.validatePhoneNumber(""));
	}
	
	@Test
	public void testPhoneNumber2(){
		assertTrue(controller.validatePhoneNumber("98029744"));
	}
	
}
