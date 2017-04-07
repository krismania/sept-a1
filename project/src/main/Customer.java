package main;

/**
 * A Customer in the system. Customer is an {@link Account} that also implements
 * {@link PersonalDetails}.
 * @author krismania
 */
public class Customer extends Account implements PersonalDetails
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
	
	@Override
	public String getFirstName()
	{
		return firstName;
	}

	@Override
	public String getLastName()
	{
		return lastName;
	}

	@Override
	public String getEmail()
	{
		return email;
	}
	
	@Override
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	/*
	
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
	
	*/
}
