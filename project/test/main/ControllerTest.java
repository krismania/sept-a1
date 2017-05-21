package main;

import org.junit.Before;
import main.Controller;

public class ControllerTest
{
	public Controller controller;
	
	@Before
	public void setUp()
	{
		controller = Controller.getInstance();
	}
	
	// validation test moved to ValidateTest. -kg
	
}
