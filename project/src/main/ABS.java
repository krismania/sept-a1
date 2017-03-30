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
				db.getCustomerDataEntries();
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
		String[] options = {"Add a new employee", "Add working times/dates for next month", 
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
		String employeeID;
		
		// prompt user for input
		
		do{
			System.out.print("Enter Employees First Name: "); firstName = sc.nextLine();
			
			// output error if no first name is entered
			if(firstName.isEmpty()){
				System.out.println("Error: A first name must be entered");
			}
			
		}while(firstName.isEmpty());
		
		do{
			System.out.println("Enter Employees Last Name: "); lastName = sc.nextLine();
			
			// output error if no last name is entered
			if(lastName.isEmpty()){
				System.out.println("Error: A first name must be entered");
			}
			
		}while(lastName.isEmpty());
		
		/*
		do{
			System.out.println("Enter Employees Email Address: "); email = sc.nextLine();
			
			if(!email.contains("@") || !email.contains(".")){
				System.out.println("Error: Invalid email address format. Must contain @ and .");
			}
			
		}while(!email.contains("@") || !email.contains("."));
		*/
			
		System.out.println("Enter Employees Employee Number: ");
		employeeID = sc.nextLine();
		
		System.out.println("Employee successfully added!");	
	}
	
	/*
	 *  Add new shifts
	 *  @author RK
	 */
	
	private void addShifts(){
		// declare variables
		String employeeID;
		String shiftDay;
		String shiftTime;
		boolean employeeDB = false;
		
		
		// prompt user for input
		
		do{
			System.out.println("Enter employee ID: ");
			employeeID = sc.nextLine();
			
			if(employeeDB = true){
				System.out.println("Employee ID cannot be found in database");
			}
		}while(false);
		
		System.out.println("Enter shift day: ");
		shiftDay = sc.nextLine();
		
		System.out.println("Shift times: Morning  Afternoon Evening");
		System.out.println("Select a shift time:");
		shiftTime = sc.nextLine();
		
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
		HashMap<String, String> accountInfoInput = console.accountInfoPrompt();
		firstName = accountInfoInput.get("firstName");
		lastName = accountInfoInput.get("lastName");
		email = accountInfoInput.get("email");
		phoneNumber = accountInfoInput.get("phoneNumber");
		
		// collect customer info @author -RK
		
		// reprompt customer until valid first name
		do{
			System.out.print("Enter your first name: "); firstName = sc.nextLine();
			
			// output error if no first name is entered
			if(!Customer.validateName(firstName)){
				System.out.println("Error: A first name must be entered");
			}
			
		}while(!Customer.validateName(firstName));
		
		
		// reprompt customer until valid last name
		do{
			System.out.print("Enter your last name: "); lastName = sc.nextLine();
			
			// output error if no last name is entered
			if(!Customer.validateName(lastName)){
				System.out.println("Error: A last name must be entered");
			}
			
		}while(!Customer.validateName(lastName));
		
		do{
			System.out.print("Enter an email address: "); email = sc.nextLine();
			
			if(!Customer.validateEmail(email)){
				System.out.println("Error: Invalid email address format. Must contain @ and .");
			}
			
		}while(!Customer.validateEmail(email));
			
		do{
			System.out.print("Enter a contact number: "); phoneNumber = sc.nextLine();
			
			if(!Customer.validatePhoneNumber(phoneNumber)){
				System.out.println("Error: A contact number must be entered");
			}
			
		}while(!Customer.validatePhoneNumber(phoneNumber));

		
		// create the Customer instance -kg
		// Customer customer = new Customer(username, firstName, lastName, email, phoneNumber);
		
		// store customer in db -kg
		created = db.CreateDataEntry("Customers", firstName, lastName, email, phoneNumber, username, password);
		
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
		if (db.checkUsername(username, "Customers"))
		{
			// test the password
			if (db.checkPassword(username, password, "Customers"))
			{
				// TODO: go to the correct submenu depending on account type
				customerMenu();
			}
			else {
				console.alert("Invalid password.");
			}
		}
		else {
			console.alert("Invalid username.");
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
//		if (db.checkUsername(username, "Customers"))
//		{
//			if (db.checkPassword(username, password, "Customers"))
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
//		if (db.checkUsername(username, "BusinessOwner"))
//		{
//			if (db.checkPassword(username, password, "BusinessOwner"))
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
}
