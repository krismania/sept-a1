package main;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller
{
	Scanner sc = new Scanner(System.in);
	
	Database db = new Database("awesomeSauce");
	
	private static Controller instance = null;
	
	public static Controller getInstance()
	{
		if (instance == null)
		{
			instance = new Controller();
		}
		return instance;
	}
	
	/**
	 * Creates an instance of the controller class & opens the database.
	 */
	private Controller()
	{
		db.CreateDatabase();
	}

	public ArrayList<Customer> getAllCustomers()
	{
		return new ArrayList<Customer>();
	}
	
	public ArrayList<BusinessOwner> getAllBusinessOwners()
	{
		return new ArrayList<BusinessOwner>();
	}
	
	public ArrayList<Employee> getAllEmployees()
	{
		return new ArrayList<Employee>();
	}
	
	public ArrayList<Shift> getAllOpenShifts()
	{
		return db.getShiftsNotBooked();
	}
	
	/**
	 * Add a customer to the database.
	 * @author krismania
	 */
	public boolean addCustomer(String username, String password, String firstName,
					String lastName, String email, String phoneNumber)
	{
		// create the Customer instance
		Customer customer = new Customer(username, firstName, lastName, email, phoneNumber);
		
		// store customer in db
		return db.addAccount(customer, password);
	}
	
	/**
	 * Create an employee in the database.
	 * @author krismania
	 */
	public boolean addEmployee(String firstName, String lastName, String email, String phoneNumber)
	{		
		Employee employee = db.buildEmployee();
		
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employee.setPhoneNumber(phoneNumber);
		
		return db.addEmployee(employee);
	}

	/**
	 * Returns true if there is an employee in the DB with the given ID.
	 * @author krismania
	 */
	public boolean employeeExists(int id)
	{
		return db.getEmployee(id) == null;
	}
	
	public boolean addShift(int employeeID, DayOfWeek day, ShiftTime time)
	{
		Shift shift = db.buildShift(employeeID);
		shift.setDay(day);
		shift.setTime(time);
		
		return db.addShift(shift);
	}
	
	public Account login(String username, String password)
	{
		return db.login(username, password);
	}
	
	/**
	 * Returns true if a valid name is input.
	 * @author RK
	 */
	public boolean validateName(String input)
	{
		if(!input.isEmpty()){
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check that email is valid and return true if it is.
	 * @author RK
	 */
	public boolean validateEmail(String input)
	{
		if(!input.isEmpty() && input.contains("@") && input.contains(".")){
			return true;
		}
		return false;
	}
	
	/**
	 * Check that phone number is valid and return true if it is.
	 * @author RK
	 */
	public boolean validatePhoneNumber(String input)
	{
		if(input != null && !input.isEmpty() && input.length() <= 10){
			return true;
		}
		return false;
	}
}
