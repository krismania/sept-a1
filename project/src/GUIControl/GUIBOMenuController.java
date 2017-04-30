/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIControl;

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
 * FXML Controller class
 *
 * @author tn
 */
public class GUIBOMenuController implements Initializable {

	private Controller c = Controller.getInstance();
	
    @FXML
    private Button addEmp;

    @FXML
    private Button viewEmployee;
    
    @FXML
    private Button addTime;

    @FXML
    private Button viewBooking;
    
    @FXML
    private Button logout;
    

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent rootAddEmp, rootViewEmployee, rootAddTime, rootViewBooking;
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
        	rootAddTime = FXMLLoader.load(getClass().getResource("GUIAddTime.fxml"));
            Scene scene = new Scene(rootAddTime);
            stage.setScene(scene);
            stage.show(); 
        }
        else //if(event.getSource()==viewBooking)
        {
        	stage=(Stage) viewBooking.getScene().getWindow();
        	rootViewBooking = FXMLLoader.load(getClass().getResource("GUIBOViewBookingSum.fxml"));
            Scene scene = new Scene(rootViewBooking);
            stage.setScene(scene);
            stage.show();
        }

    }    
    
    @FXML
    private void logoutButtonAction(ActionEvent event) throws IOException {
    	c.logout();
    	Stage stage = (Stage) logout.getScene().getWindow();
    	
    	Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
    	   
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
