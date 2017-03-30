package console;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles the applications interaction with the console. Any input
 * collected from the user, as well as any output printed to them, should go
 * through this class.
 * @author krismania
 */

public class Console
{
	private Scanner sc;
	
	
	public Console(Scanner sc)
	{
		this.sc = sc;
	}
	
	
	/**
	 * Prints an alert message to the console, with a trailing line for spacing.
	 * @param message The message to print
	 */
	
	public void alert(String message)
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
	
	public HashMap<String, String> accountPrompt()
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
	
	public HashMap<String, String> accountInfoPrompt()
	{
		LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("firstName", "First Name");
		fields.put("lastName", "Last Name");
		fields.put("email", "Email Address");
		fields.put("phoneNumber", "Contact Number");
		
		return prompt(fields);
	}
	
	public HashMap<String, String> addShiftPrompt()
	{
		LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("employeeID", "Employee ID");
		fields.put("shiftDay", "Shift Day (mon/tue/wed/thu/fri/sat/sun)");
		fields.put("shiftTime", "Shift Time (morning/afternoon/evening)");
		
		return prompt(fields);
	}
}
