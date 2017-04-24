package main;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller
{
	// if true, use DummyDB instead of real db connection
	public static boolean debugDB = false;
	
	private static Controller instance = null;
	
	private Logger logger;
	private DBInterface db;
	
	/**
	 * Creates an instance of the controller class & opens the database.
	 */
	private Controller()
	{
		// get the logger
		logger = Logger.getLogger(getClass().getName());
		logger.setLevel(Level.ALL);
		
		// instantiate DB
		if (debugDB) db = new DummyDatabase();
		else db = new Database("awesomeSauce");
		
		logger.info("Instantiated Controller");
	}
	
	/**
	 * Returns the singleton instance of of the Controller class.
	 * @author krismania
	 */
	public static Controller getInstance()
	{
		if (instance == null)
		{
			instance = new Controller();
		}
		return instance;
	}

	public ArrayList<Customer> getAllCustomers()
	{
		return db.getAllCustomers();
	}
	
	public ArrayList<BusinessOwner> getAllBusinessOwners()
	{
		return db.getAllBusinessOwners();
	}
	
	public Employee getEmployee(int id)
	{
		return db.getEmployee(id);
	}
	
	public ArrayList<Employee> getAllEmployees()
	{
		return db.getAllEmployees();
	}
	
	/**
	 * Returns a hash map of shifts and bookings
	 * @author James
	 * @author krismania
	 */
	public TreeMap<Shift, Booking> getShiftBookings()
	{
		TreeMap<Shift, Booking> shifts = db.getShiftBookings();
		
//		// comparator to sort based on day and time
//		Comparator<Shift> byDayAndTime = new Comparator<Shift>()
//		{
//			@Override
//			public int compare(Shift s1, Shift s2)
//			{
//				// sort on day
//				int byDay = s1.getDay().getValue() - s2.getDay().getValue();
//				
//				// if same day, compare time
//				if (byDay == 0)
//				{
//					return s1.getTime().getValue() - s2.getTime().getValue();
//				}
//				else
//				{
//					return byDay;
//				}
//			}
//		};
//		
//		logger.info("Sorting shift list");
//		shifts.sort(byDayAndTime);
		
		return shifts;
	}
	
	public ArrayList<Booking> getPastBookings()
	{
		ArrayList<Booking> bookings = db.getPastBookings();
		
		bookings.sort(Comparator.reverseOrder());
		
		return bookings;
	}
	
	public ArrayList<Booking> getFutureBookings()
	{
		ArrayList<Booking> bookings = db.getFutureBookings();
		
		bookings.sort(Comparator.naturalOrder());
		
		return bookings;
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
	
	public boolean shiftExists(String dayString, String timeString, int empID)
	{
		DayOfWeek day = DayOfWeek.valueOf(dayString.toUpperCase());
		ShiftTime time = ShiftTime.valueOf(timeString.toUpperCase());
		
		return db.shiftExists(day, time, empID);
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
		input = input.replaceAll("[ \\-.()]", "");
		return input.matches("\\+?(\\d{8}|\\d{10,11})");
	}
}
