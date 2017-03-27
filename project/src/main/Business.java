package main;
//TN Made Business an extension of Bookings
public class Business extends Bookings {
	

	//TN Can you initial your work please
	//TN We should put all this code into Booking class
	//and make Business extend Booking
	//Variables
	private String businessName;
	private String businessAddr;
	private String businessPhn;
	
	//TN added boolean for checking correct access set to default of false
	private boolean isEmployee = false;
	//Constructor
	public Business() { }
	
	public Business(String businessName, String businessAddr, 
			String businessPhn)
	{
		this.businessName = businessName;
		this.businessAddr = businessAddr;
		this.businessPhn = businessPhn;
	}
	
	public String getBusinessName() 
	{
		return businessName;
	}
	
	public String getBusinessAddr() 
	{
		return businessAddr;
	}
	
	public String getBusinessPhn() 
	{
		return businessPhn;
	}
	
	/* METHOD: displayBusinessBookings
	 * Display bookings for particular business. Needs to have an option to look at new bookings
	 * need to specify what a new booking is vs old booking.(JM)
	*/
	public void displayBusinessBookings(String businessName) 
	{
		System.out.printf("%-18s%s\n", "Welcome back " + businessName);
		System.out.printf("%-18s%s\n", "Please wait while we pull up your bookings...");
		
		//Fetch bookings from bookings database/file
	}
}
