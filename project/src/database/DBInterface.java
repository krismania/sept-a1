package database;

import java.time.DayOfWeek;
import java.util.ArrayList;
import database.model.Account;
import database.model.Booking;
import database.model.BusinessOwner;
import database.model.Customer;
import database.model.Employee;
import database.model.Service;
import database.model.Shift;


public interface DBInterface
{
	/**
	 * Writes the given account to the database, with the given password
	 * @param account Account to be written
	 * @param password Account does not store the user's password, so it must
	 * be passed separately.
	 */
	boolean addAccount(Account account, String password);
	
	
	/**
	 * addEmployee will add an employee to the database.
	 */
	boolean addEmployee(Employee employee);


	/**
	 * addShift will add a shift to the database, which
	 * is connected to an employee.
	 */
	boolean addShift(Shift shift);

	/**
	 * Adds the given service to the database.
	 */
	boolean addService(Service service);
	
	/**
	 * Updates the given service in the database.
	 */
	boolean updateService(Service service);

	/**
	 * Removes the given service from the database.
	 */
	boolean deleteService(Service s);

	/**
	 * Write a booking to the database.
	 */
	boolean addBooking(Booking booking);


	/**
	 * Returns the account specified by the given username, or null if none
	 * is found.
	 */
	Account getAccount(String username);


	/**
	 * Returns the employee specified by the given ID, or null if none is found.
	 */
	Employee getEmployee(int id);


	/**
	 * Returns the shift specified by the given ID
	 */
	Shift getShift(int shiftID);
	
	
	/**
	 * Returns a service object by it's ID
	 */
	Service getService(int id);


	/**
	 * Generates a new empty employee object with the next valid ID.
	 */
	Employee buildEmployee();
	
	
	/**
	 * Generates a new Shift object with the next valid ID and the supplied
	 * Employee ID.
	 */
	Shift buildShift(int employeeID);
	
	
	/**
	 * Builds a booking object with the next available ID
	 */
	Booking buildBooking();
	
	/**
	 * Builds a service object with the next available ID
	 */
	Service buildService();

	/**
	 * Get a list of all customers in the DB.
	 * @deprecated -kg
	 */
	@Deprecated
	ArrayList<Customer> getAllCustomers();
	
	
	/**
	 * Get a list of all Business owners in the database;
	 * @deprecated -kg
	 */
	@Deprecated
	ArrayList<BusinessOwner> getAllBusinessOwners();
	
	
	/**
	 * Returns all employees that have been registered, otherwise returns null.
	 * @deprecated -kg
	 */
	@Deprecated
	ArrayList<Employee> getAllEmployees();
	
	
	/**
	 * Returns a list of services available in the business
	 */
	ArrayList<Service> getServices();


	/**
	 * Returns an ArrayList of shifts for a given day
	 */
	ArrayList<Shift> getShifts(DayOfWeek day);
	
	
	/**
	 * Returns a list of all bookings that occurred before the current date.
	 */
	ArrayList<Booking> getPastBookings();
	
	
	/**
	 * Returns a list of all bookings that have not yet occurred (including
	 * today's bookings).
	 */
	ArrayList<Booking> getFutureBookings();
	
	
	/**
	 * Attempt to log into an account with the provided credentials. If the login
	 * is successful, a Customer or BusinessOwner object will be returned, otherwise
	 * the return value is null.
	 */
	Account login(String username, String password);

	
	/**
	 * Close the database connection in preparation for closing or switching
	 */
	public void close();

}
