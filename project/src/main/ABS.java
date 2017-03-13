package main;
import java.util.Scanner;

public class ABS
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		mainMenu(sc);
	}
	
	
	/*
	 * Main menu loop. -kg
	 */
	
	private static void mainMenu(Scanner sc) {
		String[] options = {"Business Owner", "Customer", "Register", "Exit"};
		Menu menu = new Menu(sc, options, "Appointment Booking System");
		
		// main loop
		boolean exit = false;
		while (!exit)
		{
			switch (menu.prompt())
			{
			case "Business Owner":
				businessOwnerMenu(sc);
				break;
			case "Customer":
				customerMenu(sc);
				break;
			case "Register":
				customerRegister(sc);
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
	
	private static void businessOwnerMenu(Scanner sc)
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
	
	
	/*
	 * Customer submenu. -kg
	 */
	
	private static void customerMenu(Scanner sc)
	{
		String[] options = {"View available days/times", "Log out"};
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
	
	
	/*
	 * Register a new customer, adapts code from Richard's menu. -kg
	 */
	
	private static void customerRegister(Scanner sc)
	{
		String username;
		String password;
		String firstName;
		String lastName;
		String email;
		String phoneNumber;
		
		// get username/password -kg
		System.out.print("username: "); username = sc.nextLine();
		System.out.print("password: "); password = sc.nextLine();
		
		// test password -kg
		if (Account.passwordAccepted(password))
		{
			System.out.println("Password OK!");
			System.out.println();
		}
		else
		{
			// if password is unacceptable, end account creation here. -kg
			System.out.println("Invalid password.");
			System.out.println();
			return;
		}
		
		// collect customer info -kg
		System.out.print("Enter your first name: "); firstName = sc.nextLine();
		System.out.print("Enter your last name: "); lastName = sc.nextLine();
		System.out.print("Enter an email address: "); email = sc.nextLine();
		System.out.print("Enter a contact number: "); phoneNumber = sc.nextLine();
		
		// create the Customer instance -kg
		Customer customer = new Customer(username, firstName, lastName, email, phoneNumber);
		
		// store customer in db -kg
		// db.create(customer); // TODO: awaiting db implementation
		// db.setPassword(username, password);
		
		System.out.println("\nNot implemented\n");
	}
	
	
	// I'm depreciating the old menu. -kg
	
//	private static void menu()
//	{
//		Scanner sc = new Scanner(System.in);
//		
//		//declare variables
//		String userInput;
//		String selection = "0";
//		
//		do
//		{
//			// main menu for booking system
//			System.out.println("Business Booking System");
//			System.out.println("-----------------------");
//			System.out.println("1. Business Owner Login");
//			System.out.println("2. Customer Login");
//			System.out.println("3. Customer Registration");
//			System.out.println("4. Exit");
//			
//			 // prompt the user for input
//            System.out.print("Enter your selection: ");
//           
//            userInput = sc.nextLine();
//            selection = userInput;
//            //TN - checks input length to ensure correct menue selection
//            if (userInput.length() != 1) 
//            {
//                System.out.println("Error - invalid selection!");
//            } 
//            else
//            // process the user's selection
//            switch (selection)
//            {
//               case "1":
//            	   // need to check business owner against db
//            	   System.out.println("Business owner successfully logged in\n");
//            	   // Business Owner Menu
//       			   System.out.println("Business Owner Menu");
//       			   System.out.println("-------------------");
//       			   System.out.println("1. Add a new employee");
//       			   System.out.println("2. Add working times/dates for the next month");
//       			   System.out.println("3. View summary of bookings");
//       			   System.out.println("4. View employee availability for next 7 days ");
//       			   System.out.println("5. Log out ");
//       			   System.out.println("-------------------");
//       			   System.out.print("Enter in an option >> ");
//       			   String businessChoice = sc.nextLine();
//                  break;
//                  
//               case "2":
//            	   // need to check customer against db
//            	   System.out.println("Customer successfully logged in");
//            	   System.out.println("Customer Menu");
//            	   System.out.println("-------------");
//            	   System.out.println("1. View available days/time");
//            	   System.out.println("2. Log out ");
//            	   System.out.println("-------------");
//            	   System.out.print("Enter in an option >> ");
//       			   String customerChoice = sc.nextLine();
//                  break;
//                  
//               case "3":
//            	   // new customer details
//            	   System.out.println("Enter your first name: ");
//           		   String firstName = sc.nextLine();
//           		   System.out.println("Enter your last name: ");
//           	 	   String lastName = sc.nextLine();
//           		   System.out.println("Enter an email address: ");
//           		   String emailAddress = sc.nextLine();
//           		   System.out.println("Enter a contact number: ");
//           		   int contactNumber = sc.nextInt();
//           		   
//           		   //consume trailing line
//           		   sc.nextLine();
//           		   
//           		   //instance of class CustomerReg
//           		   CustomerReg customer = new CustomerReg(firstName, lastName, emailAddress, contactNumber);
//           		   System.out.printf("%s %s has been added to the System", firstName, lastName);
//            	   break;
//            	   
//               case "4":
//                  System.out.println("Program Terminated!");
//                  return;
//
//               default:
//                  System.out.println("Error - invalid selection!");
//                  break;
//            }
//            System.out.println();
//            
//	   }while(!selection.equals(0));
//	   //TN Close any instances of open scanner to avoid buffer overflow.
//           sc.close();
//	}
}
