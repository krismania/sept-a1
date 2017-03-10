package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.Menu;

public class MenuTest
{
	public String[] options = {"option 1", "option 2"};
	public Menu testMenu = new Menu("", options);

	@Before
	public void setUp() throws Exception
	{
		
	}

	@Test
	public void test()
	{
		fail("Not yet implemented");
	}

}
