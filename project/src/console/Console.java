package console;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.*;

/**
 * Handles the applications interaction with the console. Any input
 * collected from the user, as well as any output printed to them, should go
 * through this class.
 * @deprecated Moved to the GUI, console is no longer maintained.
 * @author krismania
 */
@Deprecated
public class Console
{
	private Scanner sc;
	private Controller c;
	
	private Locale locale;
	DateFormat dateFormat;
	
	private Logger logger;
	
	public Console(Scanner sc)
	{
		// get the logger & set level
		logger = Logger.getLogger(getClass().getName());
		logger.setLevel(Level.ALL);
		
		// get the system's locale & set date format
		locale = Locale.getDefault();
		dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		
		// set the scanner & get controller instance
		this.sc = sc;
		this.c = Controller.getInstance();
	}
	
	public void start()
	{
		mainMenu();
	}
	
	
	// **** MENUS ****
	
	/**
	 * Main menu.
	 * @author krismania
	 */
	public void mainMenu()
	{
		String[] options = {"Log in", "Register", "Exit"};
		Menu menu = new Menu(sc, options, "Appointment Booking System");
		
		// main loop
		boolean exit = false;
		while (!exit)
		{
			switch (menu.prompt())
			{
			case "Log in":
				login();
				break;
			case "Register":
				customerRegister();
				break;
			case "Exit":
				exit = true;
				break;
			}
		}
	}
	
	/**
	 * Business owner sub-menu.
	 * @author krismania
	 */
	private void businessOwnerMenu()
	{
		String[] options = {"Add a new employee", "View employees", "Add working times/dates", 
						"View summary of future bookings", "View past bookings", 
						"View employee availability (next 7 days)", "Log out"};
		Menu menu = new Menu (sc, options, "Business Owner Menu");
		
		// main loop
		boolean exit = false;
		while (!exit)
		{
			switch (menu.prompt())
			{
			case "Add a new employee":
				addEmployee();
				break;
			case "View employees":
				displayEmployees(c.getAllEmployees());
				break;
			case "Add working times/dates":
				addShifts();
				break;
            case "View employee availability (next 7 days)":
				alert("Staff Availability - Days and Times:");
				displayShifts(c.getShiftBookings());
				break;
            case "View summary of future bookings":
            	alert("Future bookings (sorted by Date [asc]):");
            	displayBookings(c.getFutureBookings());
            	break;
            case "View past bookings":
            	alert("Past bookings (sorted by Date [desc]):");
            	displayBookings(c.getPastBookings());
            	break;
			case "Log out":
				exit = true;
				break;
			}
		}
	}
	
	/**
	 * Customer sub-menu.
	 * @author krismania
	 */
	private void customerMenu()
	{
		String[] options = {"View available days/times", "Log out"};
		Menu menu = new Menu (sc, options, "Customer Menu");
		
		// main loop
		boolean exit = false;
		while (!exit)
		{
			switch (menu.prompt())
			{
			case "View available days/times":
				alert("Available Days and Times:");
				displayShifts(c.getShiftBookings());
				break;
			case "Log out":
				exit = true;
				break;
			}
		}
	}
	
	
	// **** VIEWS ****
	
	/**
	 * Joint login function for both account types. This method decides whether
	 * the specified account is a Customer or B.O. and displays the appropriate
	 * sub-menu.
	 * @author krismania
	 */
	private void login()
	{
		String username;
		String password;
		
		HashMap<String, String> accountInput = accountPrompt();
		username = accountInput.get("username");
		password = accountInput.get("password");
		
		// attempt login
		Account account = c.login(username, password);
		
		if (account instanceof Customer)
		{
			customerMenu();
		}
		else if (account instanceof BusinessOwner)
		{
			businessOwnerMenu();
		}
		else 
		{
			alert("Invalid username or password");
		}
	}
	
	/**
	 *  Add new employee
	 *  @author RK
	 */
	private void addEmployee()
	{
		// declare variables
		String firstName, lastName, email, phoneNumber;
		
		// prompt user for input
		HashMap<String, String> employeeInfo;
		boolean accepted = false;
		
		// prompt
		alert("Please enter Employee first name, last name, email and phone number.\n"
				+ "Email must contain '@' and a '.'\n"
				+ "Phone number must be 10 digits long.\n");
		employeeInfo = accountInfoPrompt();
			
		// copy fields into local variables
		firstName = employeeInfo.get("firstName");
		lastName = employeeInfo.get("lastName");
		email = employeeInfo.get("email");
		phoneNumber = employeeInfo.get("phoneNumber");
			
		// verify each field
		if (!c.validateName(firstName))
		{
			alert("Employee could not be added, a first name must be entered");
		}
		else if (!c.validateName(lastName))
		{
			alert("Employee could not be added, a last name must be entered");
		}
		else if (!c.validateEmail(email))
		{
			alert("Employee could not be added, invalid email address format. Must contain @ and .");
		}
		else if (!c.validatePhoneNumber(phoneNumber))
		{
			alert("Employee could not be added, a contact number must be entered.");
		}
		else 
		{
			accepted = true;
		}

		if(accepted)
		{
			if (c.addEmployee(firstName, lastName, email, phoneNumber))
			{
				alert("Employee successfully added!");
			}
			else
			{
				alert("Employee could not be added.");
			}
		}
	}
	
	/**
	 *  Add new shifts. Does not allow for duplicate shifts
	 *  for the same Employee.
	 *  @author RK
	 *  @author James
	 */
	private void addShifts(){
		
		// declare variables
		int employeeID = -1;
		DayOfWeek shiftDay;
		ShiftTime shiftTime;
		int result1, result2;
		String testAddDay = " ", testAddTime = " ";
		
		// prompt user for input
		HashMap<String, String> shiftInfo = addShiftPrompt();
		
		// try/catch to prevent NumberFormatException. -kg
		try { employeeID = Integer.parseInt(shiftInfo.get("employeeID")); }
		catch (NumberFormatException e) { /* log this */ };
		
		//TN - ternary expression to validate input lengths  prior to acceptance
		testAddDay = String.valueOf(shiftInfo.get("shiftDay").toUpperCase());
		testAddTime = String.valueOf(shiftInfo.get("shiftTime").toUpperCase());
		
		result1 = (((testAddDay.length() < 6)||(testAddDay.length() > 9)) ? 1:0);
		
		result2 = (((testAddTime.length() < 7)||(testAddTime.length() > 9)||
				(testAddTime.length() == 8)) ? 1:0);
		
		if ((result1 != 0)||(result2 != 0))
		{
		    alert("Shift could not be added due to invalid input.");
		}
		else
		{
			shiftDay = DayOfWeek.valueOf(testAddDay.toUpperCase());
            shiftTime = ShiftTime.valueOf(testAddTime.toUpperCase());
            
    	    // check if employee exists
    	    if (c.employeeExists(employeeID))
    	        alert("Employee ID cannot be found in database");
    	    else
    	    {
    	    	//Check if the shift already exists
    	    	if(!c.shiftExists(shiftDay.toString(), shiftTime.toString(), employeeID))
    	    	{// employee found, add the shift
	                if (c.addShift(employeeID, shiftDay, shiftTime))
	                	alert("Shift has been successfully added");
			        else
			        	alert("Shift could not be added.");
		     	}
    	    	else
    	    		alert("Shift cannot be added as employee is already working at this time.");
    	    }
        }
	}
	
	/**
	 * Interface for registering a new customer. Adapts code from Richard's
	 * original menu.
	 * @author krismania
	 * @author RK
	 */
	private void customerRegister()
	{
		String username;
		String password;
		String firstName;
		String lastName;
		String email;
		String phoneNumber;
		boolean created;
		
		alert("Please enter Username, password, first name, last name, email and phone number.\n"
				+ "Password must be greater than 6 characters and contain a digit, an upper case and lower case\n"
				+ "Email must contain '@' and a '.'\n"
				+ "Phone number must be 10 digits long.");
		
		// get username/password -kg
		HashMap<String, String> accountInput = accountPrompt();
		username = accountInput.get("username");
		password = accountInput.get("password");
		
		// test password -kg
		// TN Added username null check in password length validation
		if ((Account.passwordAccepted(password)) && (username != null))
		{
			
		}
		else
		{
			// if password is unacceptable, end account creation here. -RK
			alert("*Password must be greater than 6 characters and"
 				+ " contain a digit, an upper case and lower case ");
			return;
		}
		
		// collect customer info -kg
		HashMap<String, String> accountInfoInput;
		boolean accepted = false;
		
		do
		{
			// prompt
			accountInfoInput = accountInfoPrompt();
			
			// copy fields into local variables
			firstName = accountInfoInput.get("firstName");
			lastName = accountInfoInput.get("lastName");
			email = accountInfoInput.get("email");
			phoneNumber = accountInfoInput.get("phoneNumber");
			
			// verify each field
			if (!c.validateName(firstName))
			{
				alert("Error: A first name must be entered");
			}
			else if (!c.validateName(lastName))
			{
				alert("Error: A last name must be entered");
			}
			else if (!c.validateEmail(email))
			{
				alert("Error: Invalid email address format. Must contain @ and .");
			}
			else if (!c.validatePhoneNumber(phoneNumber))
			{
				alert("Error: A contact number must be entered");
			}
			else {
				accepted = true;
			}
		}
		while (!accepted);

		created = c.addCustomer(username, password, firstName, lastName, email, phoneNumber);
		
		//JM Check if customer was created successfully
		if(created) 
		{
			alert("Account Created!");
		}
		else 
		{
			alert("Username already exists. Please try again.");
		}
	}
	
	/**
	 * Display the provided shifts in a table
	 * @author krismania
	 */
	private void displayShifts(TreeMap<Shift, Booking> shiftBookings)
	{
		if(shiftBookings.isEmpty())
		{
			// if there are no shifts, display a message and exit.
			alert("There are currently no available timeslots.");
			return;
		}
		
		// print each shift in the list
		String formatString = "%-10s %3s   %-25s %-10s %-9s\n";
		DayOfWeek currentDay = null; // store the day we're up to
		
		// print header
		printHeader(formatString, "Day", "ID", "Employee", "Time", "");
		
		for (Map.Entry<Shift, Booking> i : shiftBookings.entrySet())
		{
			// get the employee obj of this shift
			Employee employee = c.getEmployee(i.getKey().employeeID);
			
			// set up some helper variables
			String employeeName = employee.getFirstName() + " " + employee.getLastName();
			String printDay = "";
			String booked = (i.getValue() == null ? "" : "[booked]");
			
			// print the current day if it's changed
			if (currentDay != i.getKey().getDay())
			{
				currentDay = i.getKey().getDay();
				printDay = currentDay.getDisplayName(TextStyle.FULL, locale);
			}
			
			// print the shift
			System.out.printf(formatString, printDay, i.getKey().ID, employeeName,
							i.getKey().getTime().toString(), booked);
		}
		System.out.println();
	}
	
	private void displayBookings(ArrayList<Booking> bookings)
	{
		if(bookings.isEmpty())
		{
			// if there are no bookings, display a message and exit.
			alert("No past bookings found.");
			return;
		}
		
		// print the bookings in a table
		String formatString = "%-12s %-10s %3s   %-20s %-25s\n";
		LocalDate currentDate = null; // store the date we're up to
		
		// print header
		printHeader(formatString, "Date", "Time", "ID", "Employee", "Customer");
		
		for (Booking booking : bookings)
		{
			// set up variables
			String printDate = "";
			Employee employee = c.getEmployee(booking.getEmployeeID());
			String employeeName = employee.getFirstName() + " " + employee.getLastName();
			
			// Print the date if we haven't yet done so
			if (currentDate == null || !currentDate.equals(booking.getDate()))
			{
				currentDate = booking.getDate();
				printDate = dateFormat.format(currentDate);
			};
			
			System.out.printf(formatString, printDate, booking.getTime().toString(), booking.ID, employeeName, booking.getCustomer());
		}
		System.out.println();
	}

	/**
	 * Display all current employees
	 * @author James
	 */
	private void displayEmployees(ArrayList<Employee> roster)
	{
		if(roster.isEmpty())
		{
			//If there are no employees, display message and exit.
			alert("There are currently no employees.");
			return;
		}
		
		String formatString = "%-3s %-15s %-40s %-20s\n";
		
		printHeader(formatString, "ID", "Name", "Email", "Phone Number");
		
		for(Employee employee : roster)
		{
			String employeeName = employee.getFirstName() + " " + employee.getLastName();
			
			System.out.printf(formatString, employee.ID, employeeName, 
					employee.getEmail(), employee.getPhoneNumber());
		}
		System.out.println();
	}

	// **** CLASS FUNCTIONALITY ****
	
	
	/**
	 * Prints an alert message to the console, with a trailing line for spacing.
	 * @param message The message to print
	 */
	
	private void alert(String message)
	{
		System.out.println(message + "\n");
	}
	
	
	/**
	 * Sequentially displays each item in {@code fields}, prompting the user for
	 * an input with each field.
	 * @param fields A {@link LinkedHashMap} containing value/prompt pairs; for
	 * example, {@code <"firstName", "First Name">}.
	 * @return {@link HashMap} containing the keys from {@code fields}, paired
	 * with the users responses to them.
	 */
	
	private HashMap<String, String> prompt(LinkedHashMap<String, String> fields)
	{
		HashMap<String, String> input = new HashMap<String, String>();
		
		for (Map.Entry<String, String> field : fields.entrySet())
		{
			System.out.print(field.getValue() + ": ");
			input.put(field.getKey(), sc.nextLine());
		}
		
		System.out.println(); // add space under last field -kg
		return input;
	}
	
	
	/**
	 * Prompts the user for a username & password using {@link #prompt(LinkedHashMap) prompt}.
	 * @return {@link HashMap} containing the keys {@code "username"} and {@code "password"}.
	 */
	
	private HashMap<String, String> accountPrompt()
	{
	 	HashMap<String, String> input = new HashMap<String, String>();
        
        System.out.print("Username: ");
        input.put("username", sc.nextLine());
        
        // try to use io.Console for password input. -kg
        java.io.Console cons = System.console();
        
        if (cons != null)
        {
        	// password is masked if we can use io.Console. -kg
        	input.put("password", new String(cons.readPassword("Password: ")));
        }
        else
        {
        	System.out.print("Password: ");
        	input.put("password", sc.nextLine());
        }
 
        System.out.println(); // add space under last field -kg
        
        return input;
	}
	
	
	/**
	 * Prompts the user for customer information using {@link #prompt(LinkedHashMap) prompt}.
	 * @return {@link HashMap} containing the keys {@code "firstName"}, {@code "lastName"},
	 * {@code "email"} and {@code "phoneNumber"}.
	 */
	
	private HashMap<String, String> accountInfoPrompt()
	{
		LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("firstName", "First Name");
		fields.put("lastName", "Last Name");
		fields.put("email", "Email Address");
		fields.put("phoneNumber", "Contact Number");
		
		return prompt(fields);
	}
	
	
	/**
	 * Prompts the user for shift information using {@link #prompt(LinkedHashMap) prompt}.
	 * @return {@link HashMap} containing the keys {@code "employeeID"}, {@code "shiftDay"}
	 * and {@code "shiftTime"}.
	 */
	
	private HashMap<String, String> addShiftPrompt()
	{
		LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("employeeID", "Employee ID");
		fields.put("shiftDay", "Shift Day (eg. monday/tuesday/.../saturday/sunday)");
		fields.put("shiftTime", "Shift Time (eg. morning/afternoon/evening)");
		
		return prompt(fields);
	}
	
	/**
	 * Helper method that prints the given headers in the provided format, and
	 * adds an underline the same length as the header row.
	 * @author krismania
	 */
	private void printHeader(String format, Object... titles)
	{
		String header = String.format(format, titles);
		String divider = "";
		
		for (int i = 0; i < header.length(); i++)
		{
			divider += "-";
		}
		
		divider += "\n";
		
		System.out.print(header + divider);
	}

}
