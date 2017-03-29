package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Customer;

public class CustomerTest
{
	
	public Customer customer;

	@Before
	public void setUp() throws Exception
	{
		customer = new Customer("johndoe", "John", "Doe", "johndoe@gmail.com",
				"0418 123 456");
	}
	
	
	// test customer info getters. -kg
	
	@Test
	public void testGetFirstName()
	{
		assertEquals("John", customer.getFirstName());
	}
	
	@Test
	public void testGetLastName()
	{
		assertEquals("Doe", customer.getLastName());
	}
	
	@Test
	public void testGetEmail()
	{
		assertEquals("johndoe@gmail.com", customer.getEmail());
	}
	
	@Test
	public void testGetPhoneNumber()
	{
		assertEquals("0418 123 456", customer.getPhoneNumber());
	}
	
	@Test
	public void testGetUsername()
	{
		assertEquals("johndoe", customer.getUsername());
	}
	
	
	// test static validator methods. -kg
	
	@Test
	public void testValidName()
	{
		assertTrue(Customer.validateName("John"));
	}
	
	@Test
	public void testInvalidName()
	{
		assertFalse(Customer.validateName(""));
	}
	
	@Test
	public void testValidEmail()
	{
		assertTrue(Customer.validateEmail("some-email@gmail.com"));
	}
	
	@Test
	public void testInvalidEmail1()
	{
		// no @ symbol
		assertFalse(Customer.validateEmail("myemail.website.com"));
	}
	
	@Test
	public void testInvalidEmail2()
	{
		// no . symbol
		assertFalse(Customer.validateEmail("emailwithout@dots"));
	}
	
	@Test
	public void testInvalidEmail3()
	{
		// no . or @ symbols
		assertFalse(Customer.validateEmail("thisisdefinitelynotanemail"));
	}
}
