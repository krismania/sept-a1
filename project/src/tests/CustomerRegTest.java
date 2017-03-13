package tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import main.CustomerReg;

public class CustomerRegTest
{
	@Test
	public void testingTest()
	{
		CustomerReg customerReg = new CustomerReg("first", "last", "email@some.com", 12345678);
		
		assertEquals("This is a testing test", customerReg.getFirstName(), "first");
	}
}
