package main;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;
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
	
	public String loggedUser = null;
	
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
	
	public ArrayList<String> getEmpByDay(LocalDate day)
	{
		return db.getEmployeeWorkingOnDay(day);
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
	
	public ArrayList<Booking> getPastBookings()
	{
		ArrayList<Booking> bookings = db.getPastBookings();
		
		bookings.sort(Comparator.reverseOrder());
		
		for(Booking booked : bookings) 
		{
			System.out.println("Booking: " + booked.ID + ", Time: " + booked.getTime() + ", Date: " + booked.getDate().toString() + ", Customer: " + booked.getCustomer());	
		}
		
		return bookings;
	}
	
	public ArrayList<Booking> getFutureBookings()
	{
		ArrayList<Booking> bookings = db.getFutureBookings();
		
		bookings.sort(Comparator.naturalOrder());
		
		for(Booking booked : bookings) 
		{
			System.out.println("Booking: " + booked.ID + ", Time: " + booked.getTime() + ", Date: " + booked.getDate().toString() + ", Customer: " + booked.getCustomer());		
		}
		
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
		if(validateName(firstName) && validateName(lastName)
				&& validateEmail(email) && validatePhoneNumber(phoneNumber)){
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
	
	public boolean addShift(int employeeID, String day, String time, String duration)
	{
		Shift shift = db.buildShift(employeeID);
		shift.setTime(convertTime(time));
		shift.setDay(DayOfWeek.valueOf(day.toUpperCase()));
		
		return db.addShift(shift);
	}
	
	public boolean addBooking(LocalDate localDate, LocalTime time, int empID) 
	{
		Booking booking = db.buildBooking();
		booking.setCustomer(loggedUser);
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
		Account loggedAccount = db.login(username, password);
		if (loggedAccount != null)
		{
			loggedUser = loggedAccount.username;
		}
		
		logger.info("Logged in user: " + loggedUser);
		
		return loggedAccount;
	}
	
	/**
	 * Sets the logged user to null
	 * @author krismania
	 */
	public void logout()
	{
		logger.info("Logged out user: " + loggedUser);
		loggedUser = null;
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
		System.out.println(time);
		LocalTime newTime = LocalTime.parse(time);
		return newTime;
  }
}
