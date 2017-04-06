package main;

/*
 * Abstract account class; serves as a base for the different types of accounts
 * that will be available in our solution. -kg
 */

public abstract class Account
{
	public final String username;
	
	
	public Account(String username)
	{
		this.username = username;
	}
	
	@Override
	public String toString()
	{
		return username;
	}
	
	/**
	 * Validates account passwords based on some rules
	 * TODO: document the rules
	 * @author krismania
	 */
	public static boolean passwordAccepted(String password)
	{
		//check length
		if (password.length() >= 6)
		{
			// loop through each character and check if there is (at least) 1 upper case, 1 lower case and 1 number
			// the following vars are set to true once an occurrence of each is found. -kg
			boolean upper = false;
			boolean lower = false;
			boolean num = false;
			
			for (int i = 0; i < password.length(); i++)
			{
				// the surrounding if statements prevent extra checks once criteria have been met. -kg
				if (!upper) if (Character.isUpperCase(password.charAt(i))) upper = true;
				if (!lower) if (Character.isLowerCase(password.charAt(i))) lower = true;
				if (!num) if (Character.isDigit(password.charAt(i))) num = true;
			}
			
			// return true if all criteria are met. -kg
			return upper && lower && num;
		}
		return false;
	}
}
