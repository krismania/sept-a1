package main;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.DBInterface;
import database.Database;
import database.model.Account;
import database.model.Booking;
import database.model.BusinessOwner;
import database.model.Customer;
import database.model.Employee;
import database.model.Service;
import database.model.Shift;

/**
 * Controller class, which drives interaction between the UI and the database.
 * This class is a singleton, and can be accessed via {@code Controller.getInstance()}
 */
public class Controller
{
	/**
	 * Singleton instance of the Controller
	 */
	private static Controller instance = null;
	
	private Logger logger;
	private DBInterface db;
  
	/**
	 * {@link Account} of the currently logged in user. If no user is logged in, this
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
		db = new Database("awesomeSauce");
		
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
	@Deprecated
	public ArrayList<Customer> getAllCustomers()
	{
		return db.getAllCustomers();
	}
	
	/**
	 * @see DBInterface#getAllBusinessOwners()
	 */
	@Deprecated
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
	 * @deprecated
	 */
	@Deprecated
	public ArrayList<String> getEmpByDay(LocalDate day)
	{
		// return db.getEmployeeWorkingOnDay(day);
		return new ArrayList<String>();
	}
	
	/**
	 * Returns a list of employees that are working on a given date.
	 * @author krismania
	 */
	public ArrayList<Employee> getEmployeesWorkingOn(LocalDate date)
	{
		ArrayList<Shift> shifts = getShiftsByDate(date);
		ArrayList<Employee> employees = new ArrayList<Employee>();
		HashSet<Integer> empIds = new HashSet<Integer>();
		
		for (Shift shift : shifts)
		{
			empIds.add(shift.employeeID);
		}
		
		for (Integer empId : empIds)
		{
			employees.add(db.getEmployee(empId));
		}
		
		return employees;
	}
	
	/**
	 * @see DBInterface#getShiftBookings()
	 */
	@Deprecated
	public TreeMap<Shift, Booking> getShiftBookings()
	{
		//return db.getShiftBookings();
		return new TreeMap<Shift, Booking>();
	}
	
	/**
	 * Gets a list of all shifts occurring on a given date
	 * @author krismania
	 */
	public ArrayList<Shift> getShiftsByDate(LocalDate date)
	{
		return db.getShifts(date.getDayOfWeek());
	}
	
	/**
	 * Gets a list of all shifts, filtered by the desired employee ID.
	 * @author krismania
	 */
	public ArrayList<Shift> getShiftsByDate(LocalDate date, int employeeID)
	{
		ArrayList<Shift> shifts = getShiftsByDate(date);
		
		// filter based on employee id
		Iterator<Shift> shiftIterator = shifts.iterator();
		while (shiftIterator.hasNext())
		{
			if (shiftIterator.next().employeeID != employeeID)
			{
				// if ID doesn't match our desired ID, remove this shift
				shiftIterator.remove();
			}
		}
		
		return shifts;
	}
	
	/**
	 * Returns a list of times that this employee is available, as strings
	 * TODO: need a more robust solution for this.
	 * @author krismania
	 */
	public ArrayList<String> getEmployeeAvailability(LocalDate date, int employeeID)
	{
		ArrayList<Shift> shifts = getShiftsByDate(date, employeeID);
		ArrayList<String> times = new ArrayList<String>();
		
		logger.info("Getting employee availability");
		
		// iterate over the day's shifts
		for (Shift shift : shifts)
		{
			logger.info("Shift: " + shift.ID);
			
			LocalTime currentTime = shift.getStart(); // keep track of time we're up to
						
			while (currentTime.isBefore(shift.getEnd()))
			{
				logger.info("Current time is: " + currentTime);
				
				times.add(currentTime.format(DateTimeFormatter.ofPattern("h:mm a")).toLowerCase());
				currentTime = currentTime.plusMinutes(30);
			}
		}
		
		return times;
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
	 * Returns a list of business services.
	 * @see DBInterface#getServices()
	 */
	public ArrayList<Service> getServices()
	{
		return db.getServices();
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
	public boolean addShift(int employeeID, String day, String start, String end)
	{
		Shift shift = db.buildShift(employeeID);
		shift.setStart(convertTime(start));
		shift.setEnd(convertTime(end));
		shift.setDay(DayOfWeek.valueOf(day.toUpperCase()));
		
		return db.addShift(shift);
	}
	
  /** Add a booking to the DB.
	 * @author James
	 * @author krismania
	 * TODO: fix the inputs for this method
	 */
	public boolean addBooking(LocalDate localDate, String time, Service service, int empID, String customerUsername) 
	{
		LocalTime start = convertTime(time);
		
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
		booking.setStart(start);
		booking.setService(service);
		
		return db.addBooking(booking);
	}
	
	/**
	 * Creates a new service with a placeholder name & 30m duration in the DB,
	 * and returns the ID of the new service.
	 * @author krismania
	 */
	public int addNewService()
	{
		Service s = db.buildService();
		s.setName("New Service");
		s.setDuration(Duration.ofMinutes(30));
		
		if (db.addService(s))
		{
			return s.ID;
		}
		
		return 0; // if creation fails, ID 0 is returned
	}
	
	public boolean updateService(Service s)
	{
		return db.updateService(s);
	}

	public boolean deleteService(Service s)
	{
		return db.deleteService(s);
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
	
	/**
	 * TODO: update this controller method
	 * It should get all shifts on a day, then iterate over them and check for
	 * a duplicate manually
	 */
	public boolean shiftExists(String dayString, String timeString, int empID)
	{
//		DayOfWeek day = DayOfWeek.valueOf(dayString.toUpperCase());
//		ShiftTime time = ShiftTime.valueOf(timeString.toUpperCase());
		return true;
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
