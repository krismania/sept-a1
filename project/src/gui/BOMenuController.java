/*
 * 
 */
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
 * Implements Business Owner Main Menu options selections
 *
 * @author tn
 */
public class BOMenuController implements Initializable {

	private Controller c = Controller.getInstance();
	
    @FXML
    private Button addEmp;

    @FXML
    private Button viewEmployee;
    
    @FXML
    private Button addTime;
    
    @FXML
    private Button editHours;
    
    @FXML
    private Button editService;

    @FXML
    private Button viewBooking;
    
    @FXML
    private Button makeBooking;
    
    @FXML
    private Button exit;
    
    @FXML
    private Button logout;
    
    // Implements on button action and directs to appropriate form based on selection.
    // TODO: this should be broken into individual methods, and should also use the helper
    // method to switch scenes. -kg
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent rootAddEmp, rootViewEmployee, rootAddTime, rootEditService, rootViewBooking, rootMakeBooking;
        if(event.getSource()==addEmp) {
        	//TN - get reference button stage
        	stage=(Stage) addEmp.getScene().getWindow();
        	//TN - load other scene
        	rootAddEmp = FXMLLoader.load(getClass().getResource("AddEmployee.fxml"));
            Scene scene = new Scene(rootAddEmp);
            stage.setScene(scene);
            stage.show();
        }
        else if (event.getSource()==viewEmployee)
        {
        	// Temp button to get to employees view. -kg
        	stage = (Stage) viewEmployee.getScene().getWindow();
        	rootViewEmployee = FXMLLoader.load(getClass().getResource("ViewEmployee.fxml"));
        	Scene scene = new Scene(rootViewEmployee);
        	stage.setScene(scene);
        	stage.show();
        }
        else if(event.getSource()==addTime) {
        	stage=(Stage) addTime.getScene().getWindow();
        	rootAddTime = FXMLLoader.load(getClass().getResource("AddTime.fxml"));
            Scene scene = new Scene(rootAddTime);
            stage.setScene(scene);
            stage.show(); 
        }
        else if(event.getSource()==editHours)
        {
        	stage=(Stage)editService.getScene().getWindow();
        	rootEditService = FXMLLoader.load(getClass().getResource("EditHours.fxml"));
        	Scene scene = new Scene(rootEditService);
        	stage.setScene(scene);
        	stage.show();
        }
        else if(event.getSource()==editService)
        {
        	stage=(Stage)editService.getScene().getWindow();
        	rootEditService = FXMLLoader.load(getClass().getResource("EditService.fxml"));
        	Scene scene = new Scene(rootEditService);
        	stage.setScene(scene);
        	stage.show();
        }
        else if(event.getSource()==viewBooking)
        {
        	stage=(Stage) viewBooking.getScene().getWindow();
        	rootViewBooking = FXMLLoader.load(getClass().getResource("BOViewBookingSum.fxml"));
            Scene scene = new Scene(rootViewBooking);
            stage.setScene(scene);
            stage.show();
        }
        else //if(event.getSource()==makeBookingForCustomer)
        {
        	stage=(Stage) makeBooking.getScene().getWindow();
        	rootMakeBooking = FXMLLoader.load(getClass().getResource("Booking.fxml"));
            Scene scene = new Scene(rootMakeBooking);
            stage.setScene(scene);
            stage.show();
        }
        

    }    
    //Implements logout button redirecting to login scene
    @FXML
    private void logoutButtonAction(ActionEvent event) throws IOException {
    	c.logout();
    	//reconnect to master DB
    	c.loadDatabase("master");
    			
    	Stage stage = (Stage) logout.getScene().getWindow();
    	
    	Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
    	   
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // INIT
    }    
    
}
