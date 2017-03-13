package main;

public class Customer extends Account
{
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	
	public Customer(String firstName, String lastName, String email, String phoneNumber, String username)
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

}
