package console;

import java.sql.Time;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Locale;

import main.*;

/**
 * Handles the applications interaction with the console. Any input
 * collected from the user, as well as any output printed to them, should go
 * through this class.
 * @author krismania
 */

public class Console
{
	private Scanner sc;
	private Controller c;
	
	
	public Console(Scanner sc)
	{
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
		String[] options = {"Log in", "Register", "[debug] print customer db", "Exit"};
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
			case "[debug] print customer db":
				alert("Customers:");
				// c.getAllCustomers(); // TODO: print customers
				alert("\nBusiness Owners:");
				// c.getAllBusinessOwners(); // TODO: print b.o.s
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
						"View summary of bookings", "View employee availability for next 7 days", "Log out"};
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
				// c.getAllEmployees(); // TODO: print employees
				break;
			case "Add working times/dates":
				addShifts();
				break;
                        case "Staff Availability - Days and Times:":
				alert("Staff Availability - Days and Times:");
				viewDaysAndTime(c.getAllOpenShifts());
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
				viewDaysAndTime(c.getAllOpenShifts());
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
		String firstName;
		String lastName;
		String email;
		String phoneNumber;
		
		// prompt user for input
		HashMap<String, String> employeeInfo;
		boolean accepted = false;
		
		// prompt
		do
		{
			employeeInfo = accountInfoPrompt();
			
			// copy fields into local variables
			firstName = employeeInfo.get("firstName");
			lastName = employeeInfo.get("lastName");
			email = employeeInfo.get("email");
			phoneNumber = employeeInfo.get("phoneNumber");
			
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

		if (c.addEmployee(firstName, lastName, email, phoneNumber))
		{
			alert("Employee successfully added!");
		}
		else
		{
			alert("Employee could not be added.");
		}
	}
	
	/**
	 *  Add new shifts.
	 *  @author RK
	 */
	private void addShifts(){
		
		// declare variables
		int employeeID;
		DayOfWeek shiftDay;
		ShiftTime shiftTime;		
		
		// prompt user for input
		HashMap<String, String> shiftInfo = addShiftPrompt();
		employeeID = Integer.parseInt(shiftInfo.get("employeeID"));
		shiftDay = DayOfWeek.valueOf(shiftInfo.get("shiftDay").toUpperCase());
		shiftTime = ShiftTime.valueOf(shiftInfo.get("shiftTime").toUpperCase());
		
		// check if employee exists
		if (c.employeeExists(employeeID))
		{
			alert("Employee ID cannot be found in database");
		}
		else
		{
			// employee found, add the shift
			if (c.addShift(employeeID, shiftDay, shiftTime))
			{
				// TODO: success
			}
			else
			{
				// TODO: failure
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
			alert("Password must be greater than 6 characters and"
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

		created = c.addCustomer(username, firstName, lastName, email, phoneNumber, password);
		
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
	
	private void viewDaysAndTime(ArrayList<Shift> timeSlots)
	{
		for(Shift current : timeSlots)
		{
			int shiftID = current.ID;
			int empID = current.employeeID;
			DayOfWeek day = current.getDay();
			ShiftTime time = current.getTime();
			Locale locale = new Locale("au", "AU");
			System.out.println("***");
			System.out.println("Option: " + Integer.toString(shiftID));
			System.out.println("Employee: " + Integer.toString(empID) 
					+ ", Day: " + day.getDisplayName(TextStyle.SHORT, locale) 
					+ ", Time: " + time.toString());
		}
		System.out.println();
		
		if(timeSlots.isEmpty())
		{
			alert("There are currently no available timeslots.");
		}
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

}
