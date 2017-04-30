package main;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeMap;


public interface DBInterface
{
	
	//***PUBLIC API***
	/**
	 * Writes the given account to the database, with the given password
	 * @param account Account to be written
	 * @param password Account does not store the user's password, so it must
	 * be passed separately.
	 */
	boolean addAccount(Account account, String password);
	
	
	/**
	 * Generates a new empty employee object with the next valid ID.
	 */
	Employee buildEmployee();
	
	
	/**
	 * addEmployee will instantiate an employee into the database.
	 */
	boolean addEmployee(Employee employee);
	
	
	/**
	 * Generates a new Shift object with the next valid ID and the supplied
	 * Employee ID.
	 */
	Shift buildShift(int employeeID);
	
	
	/**
	 * addShift will instantiate a shift into the database, which
	 * is connected to an employee.
	 */
	boolean addShift(Shift shift);
	
	
	/**
	 * Returns true if a user with the specified username exists in the database
	 */
	boolean accountExists(String username);
	
	/**
	 * Check if shift exists, returns true if it does.
	 */
	boolean shiftExists(DayOfWeek day, ShiftTime time, int empID);
	
	/**
	 * Returns the account specified by the given username, or null if none
	 * is found.
	 */
	Account getAccount(String username);
	
	/**
	 * Get a list of all customers in the DB.
	 */
	ArrayList<Customer> getAllCustomers();
	
	/**
	 * Get a list of all Business owners in the database;
	 */
	ArrayList<BusinessOwner> getAllBusinessOwners();
	
	/**
	 * Returns the employee specified by the given ID, or null if none is found.
	 */
	Employee getEmployee(int id);
	
	
	/**
	 * Returns all employees that have been registered, otherwise returns null.
	 */
	ArrayList<Employee> getAllEmployees();
	
	/**
	 * Returns an ArrayList of employee IDs working on a given day
	 */
	ArrayList<String> getEmployeeWorkingOnDay(LocalDate day);
	
	/**
	 * Returns the shift specified by the given ID
	 */
	Shift getShift(int shiftID);
	
	
	/**
	 * Returns an ArrayList of shifts for a given employee on a given day
	 */
	ArrayList<Shift> getShifts(int EmpID, String Day);
	
	/**
	 * Returns a hash map of shifts and bookings
	 */
	public TreeMap<Shift, Booking> getShiftBookings();
	
	/**
	 * Builds a booking object with the next available ID
	 */
	Booking buildBooking();

	/**
	 * Write a booking to the database
	 */
	boolean addBooking(Booking booking);
	
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

}
