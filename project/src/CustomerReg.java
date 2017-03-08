// s3563242 Richard Kuoch Contribution
public class CustomerReg {
	
	//declare variables
	private String firstName;
	private String lastName;
	private String emailAddress;
	private int contactNumber;
	
	
	//constructor
	public CustomerReg(String firstName, String lastName, String emailAddress, int contactNumber){
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.contactNumber = contactNumber;
	}
	
	// getters
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public String getEmailAddress(){
		return emailAddress;
	}
	
	public int getContactNumber(){
		return contactNumber;
	}
	
	

}
