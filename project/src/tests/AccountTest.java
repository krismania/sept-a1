package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Account;

public class AccountTest
{
	
	// test valid passwords. -kg
	
	@Test
	public void testValidPassword1()
	{		
		String password = "SomePassword123";
		assertTrue(Account.passwordAccepted(password));
	}
	
	@Test
	public void testValidPassword2()
	{		
		String password = "5ApwWith5omeNumb3rs!";
		assertTrue(Account.passwordAccepted(password));
	}
	
	@Test
	public void testValidPassword3()
	{		
		String password = "1234aB";
		assertTrue(Account.passwordAccepted(password));
	}
	
	@Test
	public void testValidPassword4()
	{		
		String password = "Aaaaa9";
		assertTrue(Account.passwordAccepted(password));
	}
	
	@Test
	public void testValidPassword5()
	{		
		String password = "1111hhhhhXXXXX";
		assertTrue(Account.passwordAccepted(password));
	}
	
	
	// test passwords with invalid length
	
	@Test
	public void testInvalidLengthPassword1()
	{		
		String password = "2Shrt";
		assertFalse(Account.passwordAccepted(password));
	}
	
	@Test
	public void testInvalidLengthPassword2()
	{		
		String password = "aA1";
		assertFalse(Account.passwordAccepted(password));
	}

	@Test
	public void testInvalidLengthPassword3()
	{		
		String password = "1XYab";
		assertFalse(Account.passwordAccepted(password));
	}

	@Test
	public void testInvalidLengthPassword4()
	{		
		String password = "!@6Xg";
		assertFalse(Account.passwordAccepted(password));
	}

	@Test
	public void testInvalidLengthPassword5()
	{		
		String password = "b7*U";
		assertFalse(Account.passwordAccepted(password));
	}
	
	
	// test some specific cases
	
	@Test
	public void testPasswordNoNumbers()
	{		
		String password = "NoNumbersHere";
		assertFalse(Account.passwordAccepted(password));
	}
	
	@Test
	public void testPasswordNoUppercase()
	{		
		String password = "alllowercasepassword123";
		assertFalse(Account.passwordAccepted(password));
	}
	
	@Test
	public void testPasswordNoLowercase()
	{		
		String password = "111CAPSLOCK1SSTUCK";
		assertFalse(Account.passwordAccepted(password));
	}
	
	@Test
	public void testPasswordNoLetters()
	{		
		String password = "123456";
		assertFalse(Account.passwordAccepted(password));
	}
}
