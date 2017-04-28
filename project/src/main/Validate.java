package main;

public final class Validate
{
	// prevent instantiation
	private Validate() {}

	/**
	 * Returns true if a valid username is input.
	 * @author RK
	 */
	
	public boolean validateUserName(String input)
	{
		if(!input.isEmpty() && (input.length()<15) && !input.contains(" ") && input.matches("[a-zA-Z0-9]+")){
			return true;
		}
		
		return false;
	}

	/**
	 * Returns true if a valid name is input.
	 * @author RK
	 */
	public boolean validateName(String input)
	{
		if(!input.isEmpty() && input.matches("[a-zA-Z ,]+")){
			return true;
		}
		
		return false;
	}

	/**
	 * Check that email is valid and return true if it is.
	 * @author RK
	 */
	public boolean validateEmail(String input)
	{
		//if(!input.isEmpty() && input.contains("@") && input.contains(".")){
		//	return true;
		//}
		
		if(!input.isEmpty() && input.matches("^[A-Za-z0-9+_.-]+@+[A-Za-z0-9]+[.]+[a-z]+$")){
			return true;
		}
		
		return false;
	}

	/**
	 * Check that phone number is valid and return true if it is.
	 * @author RK
	 */
	public boolean validatePhoneNumber(String input)
	{
		input = input.replaceAll("[ \\-.()]", "");
		return input.matches("\\+?(\\d{8}|\\d{10,11})");
	}

	/**
	 * Validates account passwords based on some rules
	 * TODO: document the rules
	 * @author krismania
	 */
	public boolean passwordAccepted(String password)
	{
		//check length
		if (password.length() >= 6 && password.length() < 15 && password.matches("[a-zA-Z0-9]+"))
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
