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
	
	public ABS()
	{
		db.CreateDatabase();
	}
	
	
	/*
	 * Main menu loop. -kg
	 */
	
	public void mainMenu() {
		String[] options = {"Business Owner", "Customer", "Register", "[debug] print customer db", "Exit"};
		Menu menu = new Menu(sc, options, "Appointment Booking System");
		
		// main loop
		boolean exit = false;
		while (!exit)
		{
			switch (menu.prompt())
			{
			case "Business Owner":
				businessOwnerMenu();
				break;
			case "Customer":
				customerMenu();
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
	
	
	/*
	 * Business owner submenu. -kg
	 */
	
	private void businessOwnerMenu()
	{
		// Log in
		if (businessOwnerLogin())
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
				case "Log out":
					exit = true;
					break;
				}
			}
		}
	}
	
	
	/*
	 * Customer submenu. -kg
	 */
	
	private void customerMenu()
	{
		// Log in
		if (customerLogin())
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
	}
	
	
	/*
	 * Register a new customer, adapts code from Richard's menu. -kg
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
	
	private boolean customerLogin()
	{
		String username;
		String password;
		
		HashMap<String, String> accountInput = console.accountPrompt();
		username = accountInput.get("username");
		password = accountInput.get("password");
		
		// check if username exists
		if (db.checkUsername(username, "Customers"))
		{
			if (db.checkPassword(username, password, "Customers"))
			{
				return true;
			}
			else {
				console.alert("Invalid password.");
			}
		}
		else {
			console.alert("Invalid username.");
		}
		return false;
	}
	
	private boolean businessOwnerLogin()
	{
		String username;
		String password;
		
		HashMap<String, String> accountInput = console.accountPrompt();
		username = accountInput.get("username");
		password = accountInput.get("password");
		
		// check if username exists
		if (db.checkUsername(username, "BusinessOwner"))
		{
			if (db.checkPassword(username, password, "BusinessOwner"))
			{
				return true;
			}
			else {
				console.alert("Invalid password.");
			}
		}
		else {
			console.alert("Invalid username.");
		}
		return false;
	}
}
