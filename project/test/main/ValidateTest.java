package main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import main.Validate;


public class ValidateTest
{
	// test validation on username -RK
	@Test
	public void testValidUserName()
	{
		assertTrue(Validate.username("Richard"));
	}
	
	
	@Test
	public void testValidUserName2()
	{
		assertTrue(Validate.username("Alex123"));
	}
	
	
	// boundary test. 14 characters
	@Test
	public void testValidUserName3()
	{
		assertTrue(Validate.username("ABCDEFGH123456"));
	}
	
	
	// testing for null input
	@Test
	public void testInvalidUserName()
	{
		assertFalse(Validate.username(""));
	}
	
	
	// testing for special characters
	@Test
	public void testInvalidUserName2()
	{
		assertFalse(Validate.username("Abc12@"));
	}
	
	
	// testing for greater or equal to character limit 15
	@Test
	@Ignore("No longer applicable")
	public void testInvalidUserName3()
	{
		assertFalse(Validate.username("AbCdEfGh12345A1"));
	}
	
	
	// testing for white space
	@Test
	public void testInvalidUserName4()
	{
		assertFalse(Validate.username("Hello World"));
	}
	
	
	// test static validator methods. -kg
	@Test
	public void testValidName()
	{
		assertTrue(Validate.name("John"));
	}
	
	
	@Test
	public void testInvalidName()
	{
		assertFalse(Validate.name(""));
	}
	
	
	// test emails -RK
	
	@Test
	public void testValidEmail()
	{
		// characters mixed with -
		assertTrue(Validate.email("some-email@gmail.com"));
	}
	
	
	@Test
	public void testValidEmail1()
	{
		// characters mixed with _
		assertTrue(Validate.email("jay_n@hotmail.com"));
	}
	
	
	@Test
	public void testValidEmail2()
	{
		// characters mixed with .
		assertTrue(Validate.email("amy.shh@hotmail.com"));
	}
	
	
	@Test
	public void testValidEmail3()
	{
		// characters mixed with numbers
		assertTrue(Validate.email("hamroll22@hotmail.com"));
	}
	
	
	@Test
	public void testInvalidEmail1()
	{
		// no characters between @ and .
		assertFalse(Validate.email("emailwithout@."));
	}
	
	
	@Test
	public void testInvalidEmail2()
	{
		// no @ symbol
		assertFalse(Validate.email("myemail.website.com"));
	}
	
	
	@Test
	public void testInvalidEmail3()
	{
		// no . symbol
		assertFalse(Validate.email("emailwithout@dots"));
	}
	
	
	@Test
	public void testInvalidEmail4()
	{
		// no . or @ symbols
		assertFalse(Validate.email("thisisdefinitelynotanemail"));
	}
	
	
	// @author - RK
	@Test
	public void testInvalidEmail5()
	{
		// incorrect format. should be format characters@characters.characters
		assertFalse(Validate.email("richard@kuoch@"));
	}
	
	
	// @author -RK
	@Test
	public void testPhoneNumber()
	{
		// null input
		assertFalse(Validate.phone(""));
	}
	
	
	@Test
	public void testPhoneNumber2()
	{
		// meets 10 numbers
		assertTrue(Validate.phone("0398029744"));
	}
	
	
	@Test
	public void testPhoneNumber3()
	{
		assertFalse(Validate.phone("042515267485516654951"));
	}
	
	
	@Test
	public void testPhoneNumber4()
	{
		assertFalse(Validate.phone("++61 412 123 456"));
	}
	
	
	@Test
	public void testPhoneNumber5()
	{
		// must be 10 numbers
		assertFalse(Validate.phone("1234567"));
	}
	
	
	@Test
	public void testPhoneNumber6()
	{
		assertTrue(Validate.phone("+61 400 123 456"));
	}
	
	
	@Test
	public void testPhoneNumber7()
	{
		assertTrue(Validate.phone("0418-1234"));
	}
	
	
	@Test
	public void testPhoneNumber8()
	{
		assertTrue(Validate.phone("1800.456.678"));
	}
	
	
	@Test
	public void testPhoneNumber9()
	{
		assertTrue(Validate.phone("(03) 9876 5432"));
	}
	
	
	// test valid passwords. -kg
	
	@Test
	public void testValidPassword1()
	{
		String password = "SomePassword12";
		assertTrue(Validate.password(password));
	}
	
	
	@Test
	public void testValidPassword3()
	{
		String password = "1234aBcd";
		assertTrue(Validate.password(password));
	}
	
	
	@Test
	public void testValidPassword4()
	{
		String password = "Aaaaaaa9";
		assertTrue(Validate.password(password));
	}
	
	
	@Test
	public void testValidPassword5()
	{
		String password = "1111hhhhhXXXXX";
		assertTrue(Validate.password(password));
	}
	
	
	// test passwords with invalid length. -kg
	
	@Test
	public void testInvalidLengthPassword1()
	{
		String password = "2Shrt";
		assertFalse(Validate.password(password));
	}
	
	
	@Test
	public void testInvalidLengthPassword2()
	{
		String password = "aA1";
		assertFalse(Validate.password(password));
	}
	
	
	@Test
	public void testInvalidLengthPassword3()
	{
		String password = "1XYab";
		assertFalse(Validate.password(password));
	}
	
	
	@Test
	public void testInvalidLengthPassword4()
	{
		String password = "!@6Xg";
		assertFalse(Validate.password(password));
	}
	
	
	@Test
	public void testInvalidLengthPassword5()
	{
		String password = "b7*U";
		assertFalse(Validate.password(password));
	}
	
	
	// test some specific cases. -kg
	
	@Test
	public void testPasswordNoNumbers()
	{
		String password = "NoNumbersHere";
		assertFalse(Validate.password(password));
	}
	
	
	@Test
	public void testPasswordNoUppercase()
	{
		String password = "alllowercasepassword123";
		assertFalse(Validate.password(password));
	}
	
	
	@Test
	public void testPasswordNoLowercase()
	{
		String password = "111CAPSLOCK1SSTUCK";
		assertFalse(Validate.password(password));
	}
	
	
	@Test
	public void testPasswordNoLetters()
	{
		String password = "123456";
		assertFalse(Validate.password(password));
	}
}
