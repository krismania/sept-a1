package main;

public class Customer extends Account
{
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	
	public Customer(String username, String firstName, String lastName, String email, String phoneNumber)
	{
		super(username);

		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
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
	
	// methods for validating information
	
	public static boolean validateName(String input)
	{
		// check that name is valid (i.e. it exists) and return true if it is.
		if(!input.isEmpty()){
			return true;
		}
		
		return false;
	}
	
	public static boolean validateEmail(String input)
	{
		// check that email is valid and return true if it is.
		if(!input.isEmpty() && input.contains("@") && input.contains(".")){
			return true;
		}
		return false;
	}
	
	public static boolean validatePhoneNumber(String input)
	{
		// check that phone number is valid and return true if it is
		if(!input.isEmpty()){
			return true;
		}
		return false;
	}
}
