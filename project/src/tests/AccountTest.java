package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Account;

public class AccountTest
{
	
	@Test
	public void testCreate()
	{
		// Attempt to create an account with the following credentials.
		// Note: the database needs to be cleared before these tests run. -kg
		assertTrue(Account.createAccount("testusername", "hunter4"));
	}
	
	@Test
	public void testLogin()
	{
		assertTrue(Account.attemptLogin("testusername", "hunter4"));
	}
	
}
