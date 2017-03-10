package main;

/*
 * Abstract account class; serves as a base for the different types of accounts
 * that will be available in our solution. -kg
 */

public abstract class Account
{
	private String firstName;
	private String lastName;
	
	private String email;
	private String phoneNumber;
	
	private String username;
	
	public Account(String firstName, String lastName, String email, String phoneNumber,
			String username)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.username = username;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public String getUsername()
	{
		return username;
	}
	
	/*
	 * Test the given username/password combination against stored accounts. -kg
	 */
	public static boolean attemptLogin(String username, String password)
	{
		//TODO: Implement login -kg
		return false;
	}
	
	/*
	 * Add an account with the provided username/password to storage. -kg
	 */
	public static boolean createAccount(String username, String password)
	{
		//TODO: Implement account creation -kg
		return false;
	}
}
