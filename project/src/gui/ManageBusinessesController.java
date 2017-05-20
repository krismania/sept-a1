package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Controller;
import database.model.BusinessOwner;

/**
 * Displays registered Businesses details for selection.
 * @author tim
 */
public class ManageBusinessesController implements Initializable
{
	/**
     * Switches to a specified scene
     * Implements Generic helper method for switching between scenes
     * @author krismania
     */
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
    private Controller c = Controller.getInstance();
		
    @FXML private Node root;	
    @FXML private Label busID;
    @FXML private TextField busName;
    @FXML private TextField BOName;
		
    //Initialise data to populate fields
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // get user's account objects
       /* BusinessOwner businessOwner = (BusinessOwner) c.getLoggedUser();
			
        // populate the fields
        busID.setText(businessOwner.username);
        busName.setText(businessOwner.getName());
        BOName.setText(businessOwner.getBusinessName());*/
    }
    //Implements back button reverting to Customer Main menu scene
    @FXML
    public void handleBack(ActionEvent event)
    {
        switchTo("AdminMenu");
    }
	
}
