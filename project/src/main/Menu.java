package main;

import java.util.Scanner;

/*
 * Generic menu class, used to encapsulate menus and make
 * the rest of our solution easier. -kg
 */

public class Menu
{
	private String header;
	private String[] options;
	
	private Scanner sc = new Scanner(System.in);
	
	
	public Menu(String header, String[] options)
	{
		this.header = header;
		this.options = options;
	}
	
	
	/*
	 * Displays the menu, and returns the user's choice as a
	 * string. -kg
	 */
	
	public String prompt()
	{
		int userSelection; // user input
		
		System.out.println(header);
		System.out.println(line(header.length()));
		System.out.println();
		for (int i = 0; i <= options.length; i++)
		{
			System.out.printf("  {0}. {1}", i+1, options[i]);
		}
		System.out.println();
		
		while (true)
		{
			System.out.print("Enter your selection: ");
			userSelection = Integer.parseInt(sc.nextLine());
			if (userSelection > 0 && userSelection < options.length)
			{
				return options[userSelection];
			}
		}
	}
	
	
	/*
	 * Draws a line on the command line. -kg
	 */
	
	private String line(int len)
	{
		String output = "";
		for (int i = 0; i < len; i++)
		{
			output += "-";
		}
		
		return output;
	}
}
