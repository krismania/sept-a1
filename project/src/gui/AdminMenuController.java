package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import main.Controller;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Implements Admin Main Menu options selections
 *
 * @author tn 
 * @author RK
 */
public class AdminMenuController implements Initializable 
{

	
	private Controller c = Controller.getInstance();
	
	@FXML
    private Button createBusiness;

    @FXML
    private Button manageBusiness;
	
	// Implements button redirecting to create business scene or manage business scene
	void handleCreateBusinessAction(ActionEvent event) throws IOException
	{
        Stage stage;
        Parent rootAddBus, rootManBus;
		if(event.getSource()==manageBusiness)
        {
            stage=(Stage) manageBusiness.getScene().getWindow();
            rootManBus = FXMLLoader.load(getClass().getResource("ManageBusinesses.fxml"));
            Scene scene = new Scene(rootManBus);
            stage.setScene(scene);
            stage.show();
        }
        else 
        {
        	stage=(Stage) createBusiness.getScene().getWindow();
        	rootAddBus = FXMLLoader.load(getClass().getResource("CreateBusiness.fxml"));
            Scene scene = new Scene(rootAddBus);
            stage.setScene(scene);
            stage.show();
        }
	
	}
	
	@FXML
    	private Button logout;
	
	//Implements logout button redirecting to login scene
    	@FXML
    	private void logoutButtonAction(ActionEvent event) throws IOException
	{
    	   c.logout();
    	   Stage stage = (Stage) logout.getScene().getWindow();
    	
    	   Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
    	   
           Scene scene = new Scene(root);
           stage.setScene(scene);
           stage.show();
    	}
	
	 @Override
	 public void initialize(URL url, ResourceBundle rb) 
	 {
	        // INIT
	 }   
	
}
