package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import console.Menu;

@Ignore
@SuppressWarnings("deprecation")
public class MenuTest
{
	public Menu testMenu;
	
	public String[] options1 = new String[] {"first option", "second option"};
	public String[] options2 = new String[] {"first option", "second option", "third option", "fourth option", "fifth option",
					"sixth option", "seventh option", "eighth option", "ninth option", "tenth option",
					"eleventh option", "twelfth option"};

	public Scanner mockScanner(String input)
	{
		// creates a mock scanner with the given input in it.
		return new Scanner(input);
	}
	
	@Before
	public void setUp() throws Exception
	{
		// redirect output (prevents test menus printing to console)
		System.setOut(new PrintStream(new OutputStream() {
			@Override public void write(int b) throws IOException {}
		}));
	}

	@Test
	public void testInBounds()
	{	
		// create scanner
		Scanner sc = mockScanner("1\n2\n");
		
		// construct the menu
		testMenu = new Menu(sc, options1, "Test Menu");
		
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
		Scanner sc = mockScanner("1\n5\n10\n11\n3\n");
		
		// construct the menu
		testMenu = new Menu(sc, options2, "Test Menu");
		
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
		Scanner sc = mockScanner("0\n1\n3\n2\n");
		
		// note:
		// Menu loops until a valid option is entered, so for this test, we simulate an out of bounds option
		// followed by an in bounds option to end the menu's loop.
		// We know that the tests worked if we manage to get to the correct input in the right order. -kg
		
		// construct the menu
		testMenu = new Menu(sc, options1, "Test Menu");
		
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
		Scanner sc = mockScanner("a\n1\nsome word\n2\na1ph4num3ric\n1\n");
		
		// note: same invalid option behaviour to above test. -kg
		
		// construct the menu
		testMenu = new Menu(sc, options1, "Test Menu");
		
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
