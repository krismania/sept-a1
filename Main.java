import java.util.Scanner;
//s3563242 Richard Kuoch Contribution
public class Main {
	
	public static void main(String[] args){
		// Scanner for user input
		Scanner sc = new Scanner(System.in);
		
		//declare variables
		String userInput;
		String selection = "0";
		
		do{
			// main menu for booking system
			System.out.println("Business Booking System");
			System.out.println("-----------------------");
			System.out.println("1. Business Owner Login");
			System.out.println("2. Customer Login");
			System.out.println("3. Customer Registration");
			System.out.println("4. Exit");
			
			 // prompt the user for input
            System.out.print("Enter your selection: ");
           
            userInput = sc.nextLine();
            selection = userInput;
            
            // process the user's selection
            switch (selection)
            {
               case "1":
            	   // need to check business owner against db
            	   System.out.println("Business owner successfully logged in");
                  break;
                  
               case "2":
            	// need to check customer against db
            	   System.out.println("Customer successfully logged in");
                  break;
                  
               case "3":
            	   // new customer details
            	   System.out.println("Enter your first name: ");
           		   String firstName = sc.nextLine();
           	 	   System.out.println("Enter your last name: ");
           		   String lastName = sc.nextLine();
           		   System.out.println("Enter an email address: ");
           		   String emailAddress = sc.nextLine();
           		   System.out.println("Enter a contact number: ");
           		   int contactNumber = sc.nextInt();
           		   
           		   //consume trailing line
           		   sc.nextLine();
           		   
           		   //instance of class CustomerReg
           		   CustomerReg customer = new CustomerReg(firstName, lastName, emailAddress, contactNumber);
           		   System.out.printf("%s %s has been added to the System", firstName, lastName);
            	   break;
            	   
               case "4":
                  System.out.println("Program Terminated!");
                  return;

               default:
                  System.out.println("Error - invalid selection!");
                  break;
            }
            System.out.println();
            
	   }while(!selection.equals(0));

	}
}
