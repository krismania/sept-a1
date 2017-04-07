package main;

/**
 * Business owner is a basic type of {@link Account}, that additionally stores
 * the name of the owner's business.
 * @author krismania
 */
public class BusinessOwner extends Account
{
	private String businessName;
	private String name;
	private String address;
	private String phoneNumber;

	public BusinessOwner(String username, String businessName)
	{
		super(username);
		
		this.businessName = businessName;
	}
	
	public String getBusinessName()
	{
		return businessName;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public void setBusinessName(String businessName)
	{
		this.businessName = businessName;
	}
}
