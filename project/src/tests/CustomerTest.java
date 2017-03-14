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
	
	/*
	 * Tests the getter methods of Customer
	 */
	@Test
	public void getterTest()
	{
		assertEquals("John", customer.getFirstName());
		assertEquals("Doe", customer.getLastName());
		assertEquals("johndoe@gmail.com", customer.getEmail());
		assertEquals("0418 123 456", customer.getPhoneNumber());
		assertEquals("johndoe", customer.getUsername());
	}

}
