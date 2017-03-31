package main;
import java.util.HashMap;
import java.util.Scanner;

import console.Console;
import console.Menu;

public class ABS
{
	Scanner sc = new Scanner(System.in);
	Console console = new Console(sc);
	
	UserDatabase db = new UserDatabase("awesomeSauce");
	
	
	/**
	 * Creates an instance of the controller class & opens the database.
	 */
	
	public ABS()
	{
		db.CreateDatabase();
		
	}
	
	
	/**
	 * The main menu loop.
	 */
	
	public void mainMenu() {
		String[] options = {"Log in", "Register", "[debug] print customer db", "Exit"};
		Menu menu = new Menu(sc, options, "Appointment Booking System");
		
		// main loop
		boolean exit = false;
		while (!exit)
		{
			switch (menu.prompt())
			{
//			case "Business Owner":
//				businessOwnerMenu();
//				break;
//			case "Customer":
//				customerMenu();
//				break;
			case "Log in":
				login();
				break;
			case "Register":
				customerRegister();
				break;
			case "[debug] print customer db":
				console.alert("Customers:");
				db.getCustomerDataEntries();
				console.alert("\nBusiness Owners:");
				db.getBusinessOwnerDataEntries();
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
				db.getEmployeeDataEntries();
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
	
	/**
	 *  Add new employee
	 *  @author RK
	 */
	
	private void addEmployee(){
		
		// declare variables
		String firstName;
		String lastName;
		String email;
		String phoneNumber;
		
		// prompt user for input
		HashMap<String, String> employeeInfo;
		boolean accepted = false;
		
		do
		{
			// prompt
			employeeInfo = console.accountInfoPrompt();
			
			// copy fields into local variables
			firstName = employeeInfo.get("firstName");
			lastName = employeeInfo.get("lastName");
			email = employeeInfo.get("email");
			phoneNumber = employeeInfo.get("phoneNumber");
			
			// verify each field
			if (!validateName(firstName))
			{
				console.alert("Error: A first name must be entered");
			}
			else if (!validateName(lastName))
			{
				console.alert("Error: A last name must be entered");
			}
			else if (!validateEmail(email))
			{
				console.alert("Error: Invalid email address format. Must contain @ and .");
			}
			else if (!validatePhoneNumber(phoneNumber))
			{
				console.alert("Error: A contact number must be entered");
			}
			else {
				accepted = true;
			}
		}
		while (!accepted);
		
//		do{
//			System.out.print("Enter Employees First Name: "); firstName = sc.nextLine();
//			
//			// output error if no first name is entered
//			if(!validateName(firstName)){
//				System.out.println("Error: A first name must be entered");
//			}
//			
//		}while(!validateName(firstName));
//		
//		do{
//			System.out.println("Enter Employees Last Name: "); lastName = sc.nextLine();
//			
//			// output error if no last name is entered
//			if(!validateName(lastName)){
//				System.out.println("Error: A first name must be entered");
//			}
//			
//		}while(!validateName(lastName));
//		
//		do{
//			System.out.println("Enter Employees Email Address: "); email = sc.nextLine();
//			
//			if(!validateEmail(email)){
//				System.out.println("Error: Invalid email address format. Must contain @ and .");
//			}
//			
//		}while(!validateEmail(email));

		
		// employee number to be generated not provided. -kg
//		System.out.println("Enter Employees Employee Number: ");
//		employeeID = sc.nextLine();
		
		// TODO: fix this mess. -kg
		String newID = String.format("E%03d", Integer.parseInt(db.getLastEmployeeID().substring(1))+1);

		db.CreateDataEntry("Employee", firstName, lastName, email, phoneNumber, newID);

		console.alert("Employee " + newID + " successfully added!");
	}
	
	
	/**
	 *  Add new shifts
	 *  @author RK
	 */
	
	private void addShifts(){
		
		// declare variables
		String employeeID;
		String shiftDay;
		String shiftTime;		
		
		// prompt user for input
		HashMap<String, String> shiftInfo = console.addShiftPrompt();
		employeeID = shiftInfo.get("shiftInfo");
		shiftDay = shiftInfo.get("shiftDay");
		shiftTime = shiftInfo.get("shiftTime");
		
		// check if employee exists
		if (!db.validateEmpID(employeeID))
		{
			console.alert("Employee ID cannot be found in database");
		}
		else
		{
			// employee found, add the shift
			// TODO: generate a Shift ID
			db.CreateDataEntry("Schedule", shiftDay, shiftTime, "S001", employeeID);
		}
		
//		do{
//			System.out.println("Enter employee ID: ");
//			employeeID = sc.nextLine();
//			
//			if(employeeDB = true){
//				System.out.println("Employee ID cannot be found in database");
//			}
//		}while(false);
//		
//		System.out.println("Enter shift day: ");
//		shiftDay = sc.nextLine();
//		
//		System.out.println("Shift times: Morning  Afternoon Evening");
//		System.out.println("Select a shift time:");
//		shiftTime = sc.nextLine();
		
	}
	
	/**
	 * Interface for registering a new customer. Adapts code from Richard's
	 * original menu.
	 * @author krismania
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
		HashMap<String, String> accountInput = console.accountPrompt();
		username = accountInput.get("username");
		password = accountInput.get("password");
		
		// test password -kg
		// TN Added username null check in password length validation
		if ((Account.passwordAccepted(password)) && (username != null))
		{
			console.alert("Password OK!");
		}
		else
		{
			// if password is unacceptable, end account creation here. -kg
			console.alert("Invalid password.");
			return;
		}
		
		// collect customer info -kg
		HashMap<String, String> accountInfoInput;
		boolean accepted = false;
		
		do
		{
			// prompt
			accountInfoInput = console.accountInfoPrompt();
			
			// copy fields into local variables
			firstName = accountInfoInput.get("firstName");
			lastName = accountInfoInput.get("lastName");
			email = accountInfoInput.get("email");
			phoneNumber = accountInfoInput.get("phoneNumber");
			
			// verify each field
			if (!validateName(firstName))
			{
				console.alert("Error: A first name must be entered");
			}
			else if (!validateName(lastName))
			{
				console.alert("Error: A last name must be entered");
			}
			else if (!validateEmail(email))
			{
				console.alert("Error: Invalid email address format. Must contain @ and .");
			}
			else if (!validatePhoneNumber(phoneNumber))
			{
				console.alert("Error: A contact number must be entered");
			}
			else {
				accepted = true;
			}
		}
		while (!accepted);
		
		// collect customer info @author -RK
		// moved this code above to utilise the prompts that are already printed. -kg
		
		// reprompt customer until valid first name
//		do{
//			System.out.print("Enter your first name: "); firstName = sc.nextLine();
//			
//			// output error if no first name is entered
//			if(!Customer.validateName(firstName)){
//				System.out.println("Error: A first name must be entered");
//			}
//			
//		}while(!Customer.validateName(firstName));
//		
//		
//		// reprompt customer until valid last name
//		do{
//			System.out.print("Enter your last name: "); lastName = sc.nextLine();
//			
//			// output error if no last name is entered
//			if(!Customer.validateName(lastName)){
//				System.out.println("Error: A last name must be entered");
//			}
//			
//		}while(!Customer.validateName(lastName));
//		
//		do{
//			System.out.print("Enter an email address: "); email = sc.nextLine();
//			
//			if(!Customer.validateEmail(email)){
//				System.out.println("Error: Invalid email address format. Must contain @ and .");
//			}
//			
//		}while(!Customer.validateEmail(email));
//			
//		do{
//			System.out.print("Enter a contact number: "); phoneNumber = sc.nextLine();
//			
//			if(!Customer.validatePhoneNumber(phoneNumber)){
//				System.out.println("Error: A contact number must be entered");
//			}
//			
//		}while(!Customer.validatePhoneNumber(phoneNumber));

		
		// create the Customer instance -kg
		// Customer customer = new Customer(username, firstName, lastName, email, phoneNumber);
		
		// store customer in db -kg
		created = db.CreateDataEntry("Customer", firstName, lastName, email, phoneNumber, username, password, "Customer");
		
		//JM Check if customer was created successfully
		if(created) 
		{
			console.alert("Account Created!");
		}
		else 
		{
			console.alert("Username already exists. Please try again.");
		}
//		db.insert(customer);
//		db.setPassword(username, password);
	}
	
	
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
		
		HashMap<String, String> accountInput = console.accountPrompt();
		username = accountInput.get("username");
		password = accountInput.get("password");
		
		//TODO: Login function should check both customer & b.o. accounts
		
		// check if username exists
		if (db.validateUsername(username) == 1)
		{
			// test the password
			if (db.validatePassword(username, password, "Customer"))
			{
				customerMenu();
			}
			else {
				console.alert("Invalid password.");
			}
		}
		else if (db.validateUsername(username) == 2)
		{
			// test the password
			if (db.validatePassword(username, password, "BusinessOwner"))
			{
				businessOwnerMenu();
			}
			else
			{
				console.alert("Invalid password.");
			}
		}
		else {
			console.alert("Username Not Found.");
		}
	}
	
//	private boolean customerLogin()
//	{
//		String username;
//		String password;
//		
//		HashMap<String, String> accountInput = console.accountPrompt();
//		username = accountInput.get("username");
//		password = accountInput.get("password");
//		
//		// check if username exists
//		if (db.validateUsername(username, "Customer"))
//		{
//			if (db.validatePassword(username, password, "Customer"))
//			{
//				return true;
//			}
//			else {
//				console.alert("Invalid password.");
//			}
//		}
//		else {
//			console.alert("Invalid username.");
//		}
//		return false;
//	}
//	
//	private boolean businessOwnerLogin()
//	{
//		String username;
//		String password;
//		
//		HashMap<String, String> accountInput = console.accountPrompt();
//		username = accountInput.get("username");
//		password = accountInput.get("password");
//		
//		// check if username exists
//		if (db.validateUsername(username, "BusinessOwner"))
//		{
//			if (db.validatePassword(username, password, "BusinessOwner"))
//			{
//				return true;
//			}
//			else {
//				console.alert("Invalid password.");
//			}
//		}
//		else {
//			console.alert("Invalid username.");
//		}
//		return false;
//	}
	
	
	/**
	 * Returns true if a valid name is input.
	 * @author RK
	 */
	
	public static boolean validateName(String input)
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
	
	public static boolean validateEmail(String input)
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
	
	public static boolean validatePhoneNumber(String input)
	{
		if(!input.isEmpty()){
			return true;
		}
		return false;
	}
}
