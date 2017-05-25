package gui;

import java.io.IOException;
import javafx.stage.Stage;
import main.Controller;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Implements Business Owner Main Menu options selections
 * @author tn
 * @author krismania
 */
public class BOMenuController
{
	private Controller c = Controller.getInstance();
	
    @FXML Node root;
    
    @FXML 
    public void handleAddEmployee()
    {
    	switchTo("AddEmployee");
    }
    
    @FXML
    public void handleViewEmployees()
    {
    	switchTo("ViewEmployee");
    }
    
    @FXML
    public void handleAddShift()
    {
    	switchTo("AddTime");
    }
    
    @FXML
    public void handleEditBusinessHours()
    {
    	switchTo("EditHours");
    }
    
    @FXML
    public void handleEditServices()
    {
    	switchTo("EditService");
    }
    
    @FXML
    public void handleViewBooking()
    {
    	switchTo("BOViewBookingSum");
    }
    
    @FXML
    public void handleMakeBooking()
    {
    	switchTo("Booking");
    }
    
    @FXML
    public void handleLogout()
    {
    	c.logout();
    	//reconnect to master DB
    	c.loadDatabase("master");
    	
    	switchTo("Login");
    }
	
	// TODO: refactor this
	private void switchTo(String fxmlName)
	{
		try
		{
			// load the scene
			Scene newScene = new Scene(FXMLLoader.load(getClass().getResource(fxmlName + ".fxml")));
			
			// get current stage
			Stage stage = (Stage) root.getScene().getWindow();
			
			// switch scenes
			stage.setScene(newScene);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
