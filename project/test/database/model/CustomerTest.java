package database.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import database.model.Customer;

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
		assertEquals("johndoe", customer.username);
	}
}
