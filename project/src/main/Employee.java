package main;

/**
 * An employee in the system. Each employee is identified by their ID and has
 * personal information as specified in {@link PersonalDetails}
 * @author krismania
 */
public class Employee implements PersonalDetails
{
	public final int ID;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	
	public Employee(int ID, String firstName, String lastName, String email, String phoneNumber)
	{
		this.ID = ID;
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
}