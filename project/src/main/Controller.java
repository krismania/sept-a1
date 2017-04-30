package main;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class, which drives interaction between the UI and the database.
 * This class is a singleton, and can be accessed via {@code Controller.getInstance()}
 */
public class Controller
{
	/**
	 * Decides whether or not the debug database is used. Defaults to {@code False}.
	 * If the debug DB should be used, this value must be set before any calls to
	 * {@code Controller.getInstance()}.
	 */
	public static boolean debugDB = false;
	
	/**
	 * Singleton instance of the Controller
	 */
	private static Controller instance = null;
	
	private Logger logger;
	private DBInterface db;
  
	/**
	 * Username of the currently logged in user. If no user is logged in, this
	 * will be {@code null}.
	 */
	public Account loggedUser = null;
	
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
	
	/**
	 * Creates an instance of the controller class & opens the database.
	 * @author krismania
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
	 * Returns customer object based on supplied username, or null if none
	 * is found
	 * @author krismania
	 */
	public Customer getCustomer(String username)
	{
		Account account = db.getAccount(username);
		if (account instanceof Customer)
		{
			return (Customer) account;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @see DBInterface#getAllCustomers()
	 */
	public ArrayList<Customer> getAllCustomers()
	{
		return db.getAllCustomers();
	}
	
	/**
	 * @see DBInterface#getAllBusinessOwners()
	 */
	public ArrayList<BusinessOwner> getAllBusinessOwners()
	{
		return db.getAllBusinessOwners();
	}
	
	/**
	 * @see DBInterface#getEmployee(int)
	 */
	public Employee getEmployee(int id)
	{
		return db.getEmployee(id);
	}
	
	/**
	 * @see DBInterface#getAllEmployees()
	 */
	public ArrayList<Employee> getAllEmployees()
	{
		return db.getAllEmployees();
	}
	
	/**
	 * @see DBInterface#getEmployeeWorkingOnDay(LocalDate)
	 */
	public ArrayList<String> getEmpByDay(LocalDate day)
	{
		return db.getEmployeeWorkingOnDay(day);
	}
	
	/**
	 * @see DBInterface#getShiftBookings()
	 */
	public TreeMap<Shift, Booking> getShiftBookings()
	{
		return db.getShiftBookings();
	}
	
	/**
	 * TODO: document this
	 */
	public ArrayList<String> getShiftsByEmp(String emp, LocalDate date) {
		int empID = Integer.parseInt(emp);
		DayOfWeek day = date.getDayOfWeek();
		ArrayList<Shift> shifts = db.getShifts(empID, day.toString());
		ArrayList<String> availableTimes = new ArrayList<String>();
		
		for (Shift shift : shifts) {
			String time = shift.getTime().toString();
			availableTimes.add(time);
		}
		return availableTimes;
	}
	
	/**
	 * Returns a sorted list of past bookings. Does not include bookings
	 * on the current date.
	 * @see DBInterface#getPastBookings()
	 * @author krismania
	 */
	public ArrayList<Booking> getPastBookings()
	{
		ArrayList<Booking> bookings = db.getPastBookings();
		
		bookings.sort(Comparator.reverseOrder());
		
		return bookings;
	}
	
	/**
	 * Returns a sorted list of future bookings. Includes bookings on the
	 * current date.
	 * @see DBInterface#getFutureBookings()
	 * @author krismania
	 */
	public ArrayList<Booking> getFutureBookings()
	{
		ArrayList<Booking> bookings = db.getFutureBookings();
		
		bookings.sort(Comparator.naturalOrder());
		return bookings;
	}
	
	/**
	 * Returns a sorted list of future bookings filtered by the
	 * customer's username.
	 * @see #getFutureBookings
	 * @author krismania
	 */
	public ArrayList<Booking> getFutureBookings(String username)
	{
		ArrayList<Booking> bookings = getFutureBookings();
		
		Iterator<Booking> i = bookings.iterator();
		while (i.hasNext())
		{
			if (!i.next().getCustomer().equals(username))
			{
				i.remove();
			}
		}
		
		return bookings;
	}
	
	/**
	 * Add a customer to the database.
	 * @see DBInterface#addAccount(Account, String)
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
	 * @see DBInterface#addEmployee(Employee)
	 * @author krismania
	 */
	public boolean addEmployee(String firstName, String lastName, String email, String phoneNumber)
	{		
		if(Validate.name(firstName) && Validate.name(lastName)
				&& Validate.email(email) && Validate.phone(phoneNumber)){
			Employee employee = db.buildEmployee();
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setEmail(email);
			employee.setPhoneNumber(phoneNumber);
			
			return db.addEmployee(employee);
		}
		else {
			return false;
		}
	}

	/**
	 * Returns true if there is an employee in the DB with the given ID.
	 * @author krismania
	 */
	public boolean employeeExists(int id)
	{
		return db.getEmployee(id) == null;
	}
	
	/**
	 * Add a shift to the DB.
	 * @see DBInterface#addShift(Shift)
	 * @author TN
	 * @author krismania
	 */
	public boolean addShift(int employeeID, String day, String time, String duration)
	{
		Shift shift = db.buildShift(employeeID);
		shift.setTime(convertTime(time));
		shift.setDay(DayOfWeek.valueOf(day.toUpperCase()));
		
		return db.addShift(shift);
	}
	
  /** Add a booking to the DB.
	 * @author James
	 */
	public boolean addBooking(LocalDate localDate, LocalTime time, int empID, String customerUsername) 
	{
		Booking booking = db.buildBooking();
		if(customerUsername.isEmpty())
		{
			booking.setCustomer(loggedUser.username);
		}
		else
		{
			booking.setCustomer(customerUsername);
		}
		booking.setDate(localDate);
		booking.setEmployee(empID);
		booking.setTime(time);
		
		return db.addBooking(booking);
	}
	
	/**
	 * Validates a username & password, and if valid, returns the account
	 * object & also sets the loggedUser property.
	 * @author krismania
	 */
	public Account login(String username, String password)
	{
		loggedUser = db.login(username, password);
		
		if (loggedUser != null)
		{
			logger.info("Logged in user: " + loggedUser.username);
			logger.info("User type: " + loggedUser.getClass().toString().replaceAll("class main.", ""));
		}
		
		return loggedUser;
	}
	
	/**
	 * Sets the logged user to null
	 * @author krismania
	 */
	public void logout()
	{
		logger.info("Logged out user: " + loggedUser.username);
		loggedUser = null;
	}
	
	/**
	 * Returns the currently logged in user's {@link Account} object.
	 * @author krismania
	 */
	public Account getLoggedUser()
	{
		return loggedUser;
	}
	
	public boolean shiftExists(String dayString, String timeString, int empID)
	{
		DayOfWeek day = DayOfWeek.valueOf(dayString.toUpperCase());
		ShiftTime time = ShiftTime.valueOf(timeString.toUpperCase());
		
		return db.shiftExists(day, time, empID);
	}
	
	/**
	 * TODO: document this
	 * @author James
	 */
	private LocalTime convertTime(String time) {
		if(time.matches("\\d:\\d\\d am"))
		{
			time = "0".concat(time);
			time = time.replaceAll("\\sam", "");
		} 
		else if(time.matches("\\d\\d:\\d\\d am"))
		{
			time = time.replaceAll("\\sam", "");
		}
		else if(time.matches("\\d:\\d\\d pm"))
		{
			int hour = Character.getNumericValue(time.charAt(0));
			hour = hour + 12;
			
			time = time.replaceAll("\\d:", hour + ":");
			time = time.replaceAll("\\spm", "");
		}
		else if(time.matches("\\d\\d:\\d\\d pm"))
		{
			int hour = Integer.parseInt(time.substring(0, 2));
			if(hour != 12){
				hour = hour + 12;
			}
			time = time.replaceAll("\\d\\d:", hour + ":");
			time = time.replaceAll("\\spm", "");
		}
		LocalTime newTime = LocalTime.parse(time);
		return newTime;
  }
}
