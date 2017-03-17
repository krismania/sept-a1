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
}
