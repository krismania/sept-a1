package database.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import database.model.BusinessOwner;

public class BusinessOwnerTest
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
