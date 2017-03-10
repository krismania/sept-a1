package main;

/*
 * Generic menu class, used to encapsulate menus and make
 * the rest of our solution easier. -kg
 */

public class Menu
{
	private String header;
	private String[] options;
	
	
	public Menu(String header, String[] options)
	{
		this.header = header;
		this.options = options;
	}
	
	
	/*
	 * Displays the menu, and returns the user's choice as a
	 * string
	 */
	
	public String prompt()
	{
		System.out.println(header);
		System.out.println(line(header.length()));
		
		return "";
	}
	
	
	/*
	 * Draws a line on the command line
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
