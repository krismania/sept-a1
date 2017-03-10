
public class Bookings {
	
	private String custName;
	private String custAddr;
	private String custPhn;

	//TN Constructor for Bookings class
	public Bookings() { }
	
	//TN Calls Key customer data field from class for access by getters
	public Bookings(String custName, String custAddr, 
			String businessPhn)
	{
		this.custName = custName;
		this.custAddr = custAddr;
		this.custPhn = custPhn;
	}
	
	//TN getters
	public String getCustName() 
	{
		return custName;
	}
	
	public String getCustAddr() 
	{
		return custAddr;
	}
	
	public String getCustPhn() 
	{
		return custPhn;
	}
	
	/* TN METHOD: printDetails -
	 * Display bookings for particular customer. Keeping the implementation general for
	 * use elsewhere
	*/	
    public void printDetails()
	   {
	      // print basic custormer booking details
	      System.out.printf("%-18s%s\n", "Welcome back " + custName);
	 
	      // use the accessor for name so that the overridden version can
	      // be invoked polymorphically for a Business later on - May be able 
	      // to implement customer level access details and business level for
	      // same user depending on function
	      System.out.printf("%-18s%s\n", "Customer Address: ", getCustAddr());
	      System.out.printf("%-18s%s\n", "Please wait while we pull up "
	      		+ "your bookings...");
	 
	   }
}