package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import main.Menu;

public class MenuTest
{
	public Scanner scanner;
	public String[] options1;
	public String[] options2;
	public Menu testMenu;

	@Before
	public void setUp() throws Exception
	{
		options1 = new String[] {"first option", "second option"};
		
		options2 = new String[] {"first option", "second option", "third option", "fourth option", "fifth option",
						"sixth option", "seventh option", "eighth option", "ninth option", "tenth option",
						"eleventh option", "twelfth option"};
	}

	@Test
	public void testInBounds()
	{	
		// Create a simulator scanner
		// This string is passed as input to the test. -kg
		String inputString = "1\n2\n";
		scanner = new Scanner(inputString);
		
		// construct the menu
		testMenu = new Menu(scanner, options1, "Test Menu");
		
		// stores the menu's result
		String selection;

		selection = testMenu.prompt();
		assertEquals("first option", selection);
		
		selection = testMenu.prompt();
		assertEquals("second option", selection);
	}
	
	@Test
	public void testManyOptions()
	{	
		// This tests creates menu options with double digit indexes.
		
		// Create a simulator scanner
		String inputString = "1\n5\n10\n11\n3\n";
		scanner = new Scanner(inputString);
		
		// construct the menu
		testMenu = new Menu(scanner, options2, "Test Menu");
		
		// stores the menu's result
		String selection;

		selection = testMenu.prompt();
		assertEquals("first option", selection);
		
		selection = testMenu.prompt();
		assertEquals("fifth option", selection);
		
		selection = testMenu.prompt();
		assertEquals("tenth option", selection);
		
		selection = testMenu.prompt();
		assertEquals("eleventh option", selection);
		
		selection = testMenu.prompt();
		assertEquals("third option", selection);
	}
	
	@Test
	public void testOutOfBounds()
	{	
		// Create a simulator scanner
		String inputString = "0\n1\n3\n2\n";
		scanner = new Scanner(inputString);
		
		// note:
		// Menu loops until a valid option is entered, so for this test, we simulate an out of bounds option
		// followed by an in bounds option to end the menu's loop.
		// We know that the tests worked if we manage to get to the correct input in the right order. -kg
		
		// construct the menu
		testMenu = new Menu(scanner, options1, "Test Menu");
		
		// stores the menu's result
		String selection;

		selection = testMenu.prompt();
		assertEquals("first option", selection);
		
		selection = testMenu.prompt();
		assertEquals("second option", selection);
	}
	
	@Test
	public void testNonNumericInput()
	{	
		// Create a simulator scanner
		String inputString = "a\n1\nsome word\n2\na1ph4num3ric\n1\n";
		scanner = new Scanner(inputString);
		
		// note: same invalid option behaviour to above test. -kg
		
		// construct the menu
		testMenu = new Menu(scanner, options1, "Test Menu");
		
		// stores the menu's result
		String selection;

		selection = testMenu.prompt();
		assertEquals("first option", selection);
		
		selection = testMenu.prompt();
		assertEquals("second option", selection);
		
		selection = testMenu.prompt();
		assertEquals("first option", selection);
	}
}
