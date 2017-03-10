package tests;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import main.Menu;

public class MenuTest
{
	public Scanner scanner;
	public String[] options;
	public Menu testMenu;

	@Before
	public void setUp() throws Exception
	{
		// This string is passed as input to the following tests. -kg
		String inputString = "1\n";
		scanner = new Scanner(inputString);
		
		options = new String[] {"first option", "second option"};
		testMenu = new Menu(scanner, options, "Test Menu");
	}

	@Test
	public void testOp1()
	{	
		String selection = testMenu.prompt();
		assertEquals("first option", selection);
	}
	
	// Test isn't working as intended. -kg
//	@Test
//	public void testOp2()
//	{	
//		String selection = testMenu.prompt();
//		assertEquals("second option", selection);
//	}

}
