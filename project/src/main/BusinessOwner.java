package main;

/**
 * Business owner is a basic type of {@link Account}, that additionally stores
 * the name of the owner's business.
 * @author krismania
 */
public class BusinessOwner extends Account
{
	private String businessName;
	
	public BusinessOwner(String username, String businessName)
	{
		super(username);
		
		this.businessName = businessName;
	}
	
	public String getBusinessName()
	{
		return businessName;
	}
	
}
