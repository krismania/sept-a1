package main;
import java.io.File;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.model.Account;
import database.model.Availability;
import database.model.Booking;
import database.model.BusinessOwner;
import database.model.Customer;
import database.model.Employee;
import database.model.Service;
import database.model.Shift;
import database.model.TimeSpan;
import database.BusinessDatabase;
import database.MasterDatabase;

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
	private MasterDatabase masterDB;
	private BusinessDatabase businessDB;
  
	/**
	 * {@link Account} of the currently logged in user. If no user is logged in, this
	 * will be {@code null}.
	 */
	public Account loggedUser = null;
	public String currentDB;
	
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
	 * Creates an instance of the controller class
	 * @author krismania
	 */
	private Controller()
	{
		// get the logger
		logger = Logger.getLogger(getClass().getName());
		logger.setLevel(Level.ALL);

		loadDatabase("master");
		logger.info("Instantiated Controller");
	}
	
	/**
	 * Abstracted out of constructor. 
	 * Required to be public for reconnection back to master @signup and @login fail.
	 * @author James
	 * @param dbName
	 */
	public void loadDatabase(String dbName) 
	{
		currentDB = dbName;
		// instantiate DB
		if(dbName.equals("master"))
		{
			masterDB = new MasterDatabase(dbName);
		}
		else
		{
			businessDB = new BusinessDatabase(dbName);
		}
	}
	
	public void disconnectDB()
	{
		businessDB = null;
	}
	public void disconnectMaster()
	{
		masterDB = null;
	}
	
	/**
	 * Returns customer object based on supplied username, or null if none
	 * is found
	 * @author krismania
	 */
	public Customer getCustomer(String username)
	{
		Account account = businessDB.getAccount(username);
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
	 * @see DBInterface#getAllBusinessOwners()
	 */
	public ArrayList<String> getAllBusinessOwners()
	{
		return masterDB.getAllBusinesses();
	}
	
	public BusinessOwner getBusinessOwner()
	{
		return businessDB.getBusinessOwner();
	}
	
	public boolean removeBusiness()
	{
		return masterDB.removeBusiness(currentDB);
	}
	
	/**
	 * @see DBInterface#getEmployee(int)
	 */
	public Employee getEmployee(int id)
	{
		return businessDB.getEmployee(id);
	}
	
	/**
	 * @see DBInterface#getAllEmployees()
	 */
	public ArrayList<Employee> getAllEmployees()
	{
		return businessDB.getAllEmployees();
	}
	
	/**
	 * @see DBInterface#getEmployeeWorkingOnDay(LocalDate)
	 * @deprecated
	 */
	@Deprecated
	public ArrayList<String> getEmpByDay(LocalDate day)
	{
		// return businessDB.getEmployeeWorkingOnDay(day);
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
			employees.add(businessDB.getEmployee(empId));
		}
		
		return employees;
	}
	
	/**
	 * @see DBInterface#getShiftBookings()
	 */
	@Deprecated
	public TreeMap<Shift, Booking> getShiftBookings()
	{
		//return businessDB.getShiftBookings();
		return new TreeMap<Shift, Booking>();
	}
	
	/**
	 * Gets a list of all shifts occurring on a given date
	 * @author krismania
	 */
	public ArrayList<Shift> getShiftsByDate(LocalDate date)
	{
		return businessDB.getShifts(date.getDayOfWeek());
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
	 * Returns an {@code Availability} object describing the given employee's availability
	 * for the given date.
	 * @author krismania
	 */
	public Availability getEmployeeAvailability(LocalDate date, int employeeID)
	{
		ArrayList<Shift> shifts = getShiftsByDate(date, employeeID);
		ArrayList<Booking> bookings = businessDB.getBookingsByDate(date, employeeID);
		
		Availability avail = new Availability();
		
		avail.addAllShifts(shifts);
		avail.addAllBookings(bookings);
		
		System.out.println(avail);
		return(avail);
	}
	
	/**
	 * Returns a sorted list of past bookings. Does not include bookings
	 * on the current date.
	 * @see DBInterface#getPastBookings()
	 * @author krismania
	 */
	public ArrayList<Booking> getPastBookings()
	{
		ArrayList<Booking> bookings = businessDB.getPastBookings();
		
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
		ArrayList<Booking> bookings = businessDB.getFutureBookings();
		
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
	 * Query Master DB for All Business Names
	 * @author James
	 * @return ArrayList of Strings
	 */
	public ArrayList<String> getAllBusinessNames()
	{
		return masterDB.getAllBusinesses();
	}
	
	/**
	 * Returns a list of business services.
	 * @see DBInterface#getServices()
	 */
	public ArrayList<Service> getServices()
	{
		return businessDB.getServices();
	}
	
	/**
	 * Add a businessOwner and business to the database.
	 * @author James
	 */
	
	public boolean addBusiness(String username, String password, String firstName,
			String lastName, String address, String phoneNumber, String businessName)
	{
		BusinessOwner owner = new BusinessOwner(username, firstName, lastName, address, phoneNumber);
		return masterDB.newBusiness(businessName, owner, password);
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
		return businessDB.addAccount(customer, password);
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
			Employee employee = businessDB.buildEmployee();
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setEmail(email);
			employee.setPhoneNumber(phoneNumber);
			
			return businessDB.addEmployee(employee);
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
		return businessDB.getEmployee(id) == null;
	}
	
	/**
	 * Add a shift to the DB.
	 * @see DBInterface#addShift(Shift)
	 * @author TN
	 * @author krismania
	 */
	public boolean addShift(int employeeID, String day, LocalTime start, LocalTime end)
	{
		Shift shift = businessDB.buildShift(employeeID);
		shift.setStart(start);
		shift.setEnd(end);
		shift.setDay(DayOfWeek.valueOf(day.toUpperCase()));
		
		return businessDB.addShift(shift);
	}
	
	/** Add a booking to the DB.
	 * @author James
	 * @author krismania
	 */
	public boolean addBooking(LocalDate localDate, LocalTime start, Service service, int empID, String customerUsername) 
	{		
		Booking booking = businessDB.buildBooking();
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
		
		// check if the booking fits in the available times
        Availability avail = getEmployeeAvailability(localDate, empID);
        
        if (avail.contains(booking))
        {
            // if it does, try to add it
            return businessDB.addBooking(booking);
        }
        return false;
	}
	
	/**
	 * Creates a new service with a placeholder name & 30m duration in the DB,
	 * and returns the ID of the new service.
	 * @author krismania
	 */
	public int addNewService()
	{
		Service s = businessDB.buildService();
		s.setName("New Service");
		s.setDuration(Duration.ofMinutes(30));
		
		if (businessDB.addService(s))
		{
			return s.ID;
		}
		
		return 0; // if creation fails, ID 0 is returned
	}
	
	public boolean updateService(Service s)
	{
		return businessDB.updateService(s);
	}

	public boolean deleteService(Service s)
	{
		return businessDB.deleteService(s);
	}
	
	/**
	 * Get hours that the business is opened on current day.
	 * @author krismania
	 */
	public TimeSpan getHours(DayOfWeek day)
	{
		return businessDB.getHours(day);
	}
	
	/**
	 * Set opening hours for the given day.
	 * @author krismania
	 */
	public boolean setHours(DayOfWeek day, TimeSpan hours)
	{
		return businessDB.setHours(day, hours);
	}
	
	/**
	 * Set opening hours for all days.
	 * @author krismania
	 */
	public boolean setAllHours(TimeSpan hours)
	{
		// iterate over all days
		for (DayOfWeek day : DayOfWeek.values())
		{
			// if setHours returns false, something went wrong so return false
			if (!businessDB.setHours(day, hours))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Validates a username & password, and if valid, returns the account
	 * object & also sets the loggedUser property.
	 * @author krismania
	 */
	public Account login(String username, String password, String businessName)
	{
		if(username.equals("Admin") && businessName.equals("Administration"))
		{
			loggedUser = masterDB.login(username, password);
		}
		else
		{
			loggedUser = businessDB.login(username, password);
		}
		
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
		businessDB = null;
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
	 * Returns a string containing the path to the given businesses header image.
	 * If the given business has no header image, {@code null} is returned.
	 * @author krismania
	 */
	public String getImageForBusiness(String businessName)
	{
		String imagePath;
		
		File imageFile = new File(Paths.get("images/" + businessName + ".jpg").toString());
		
		// if the file exists, return it's path
		// otherwise, return null
		if (imageFile.exists())
		{
			imagePath = imageFile.toURI().toString();
			logger.info("Business header exists: " + imagePath);
			return imagePath;
		}
		else
		{
			logger.info("No business header exists.");
			return null;
		}
	}
}
