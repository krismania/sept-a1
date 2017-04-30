package display;

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
import model.Customer;

/**
 * Displays the logged in customer's details.
 * @author krismania
 */
public class CustDetailsSummary implements Initializable
{
    private Controller c = Controller.getInstance();
	
    @FXML 
    private Node root;
	
    @FXML 
    private Label username;
	
    @FXML 
    private TextField firstName;
	
    @FXML 
    private TextField lastName;
	
    @FXML 
    private TextField email;
	
    @FXML 
    private TextField phone;
	
	
    /**
     * Switches to a specified scene
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
            System.out.println("Could not switch scene.");
            e.printStackTrace();
        }
    }
	
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // get user's account objects
        Customer customer = (Customer) c.getLoggedUser();
		
        // populate the fields
        username.setText(customer.username);
        firstName.setText(customer.getFirstName());
        lastName.setText(customer.getLastName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhoneNumber());
    }
	
    @FXML
    public void handleBack(ActionEvent event)
    {
        switchTo("CustMenu");
    }
}
