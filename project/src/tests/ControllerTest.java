package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
