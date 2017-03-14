package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Account;

public class AccountTest
{
	
	@Test
	public void testValidPassword()
	{
		String[] validPasswords = {"SomePassword123", "5ApwWith5omeNumb3rs!", "1234aB", "Aaaaa9", "1111hhhhhXXXXX"};
		
		for (String password : validPasswords)
		{
			assertTrue(Account.passwordAccepted(password));
		}
	}
	
	@Test
	public void testInvalidLengthPassword()
	{
		String[] InvalidPasswords = {"2Shrt", "aA1", "1XYab", "!@6Xg", "b7*U"};
		
		for (String password : InvalidPasswords)
		{
			assertFalse(Account.passwordAccepted(password));
		}
	}
	
	@Test
	public void testInvalidPassword()
	{
		String[] InvalidPasswords = {"2Shrt", "NoNumbersHere", "alllowercasepassword", "CAPSLOCKISSTUCK", "123456"};
		
		for (String password : InvalidPasswords)
		{
			assertFalse(Account.passwordAccepted(password));
		}
	}
	
}
