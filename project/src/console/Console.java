package console;

import java.sql.Time;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import main.Account;
import main.BusinessOwner;
import main.Controller;
import main.Customer;

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
		String shiftTime;		
		
		// prompt user for input
		HashMap<String, String> shiftInfo = addShiftPrompt();
		employeeID = Integer.parseInt(shiftInfo.get("employeeID"));
		shiftDay = DayOfWeek.valueOf(shiftInfo.get("shiftDay").toUpperCase());
		shiftTime = shiftInfo.get("shiftTime");
		
		// check if employee exists
		if (c.employeeExists(employeeID))
		{
			alert("Employee ID cannot be found in database");
		}
		else
		{
			// employee found, add the shift
			if (c.addShift(employeeID, shiftDay, new Time(0)))
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
			alert("Password OK!");
		}
		else
		{
			// if password is unacceptable, end account creation here. -kg
			alert("Invalid password.");
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
		LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("username", "Username");
		fields.put("password", "Password");
		
		return prompt(fields);
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
		fields.put("shiftDay", "Shift Day (mon/tue/wed/thu/fri/sat/sun)");
		fields.put("shiftTime", "Shift Time (morning/afternoon/evening)");
		
		return prompt(fields);
	}
}
