/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIControl;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import main.*;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Controller;

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUILoginPopupController implements Initializable {
    
    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUsername;
    
    @FXML
    private Button signup;
    
    @FXML
    private Button businessOwner;

    @FXML
    private Button login;
    
    @FXML
    private Button exit;
    private boolean result;

    @FXML
    void registerNewUser(ActionEvent event) {

    }

    @FXML
    void processLogin(ActionEvent event) {

    }
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
    	Stage stage;
        Parent rootLogin, rootSignup, rootBO;
    	if(event.getSource()==login) {
    	    boolean valueBus = loginCust();
            //TN - present incorrect login error message
            if (loginCust() == false) {
                GUIAlert.infoBox("Your login is incorrect, please try again", "");	
                Stage alert = (Stage) exit.getScene().getWindow();
            	alert.close();
            }
            //TN - Call Customer Menu
            else {
        	stage = (Stage) login.getScene().getWindow();
        	rootLogin = FXMLLoader.load(getClass().getResource("GUICustMenu.fxml"));
                //TN - call a new scene instance
                Scene scene = new Scene(rootLogin);
                stage.setScene(scene);
                stage.show();
            }
        //TN - get reference button stage
        //stage=(Stage) login.getScene().getWindow();
        //TN - load other scene
    	}
    	//TN - if business owner and staff login button selected go to BO menu
    	else if(event.getSource()==businessOwner) {
            boolean valueBus = loginBus();
    	    //TN - present incorrect login error message
            if (valueBus == false) {
                GUIAlert.infoBox("Your login is incorrect, please try again", "");	
                Stage alert = (Stage) exit.getScene().getWindow();
            	alert.close();
            }
            //TN - Call Customer Menu
            else {
                stage=(Stage) businessOwner.getScene().getWindow();
                rootBO = FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml"));
                //TN - call a new scene instance
                Scene scene = new Scene(rootBO);
                stage.setScene(scene);
                stage.show();
            } 
    	}
    	//TN - if Signup option selected go to signup menu
        else {
            stage=(Stage) signup.getScene().getWindow();
            rootSignup = FXMLLoader.load(getClass().getResource("GUISignup.fxml")); 	
            //TN - call a new scene instance
            Scene scene = new Scene(rootSignup);
            stage.setScene(scene);
            stage.show();              
        }
        //TN test code please remove before merging.
     
    }

    private boolean loginCust() {
    	
        String username = tfUsername.getText();
	String password = tfPassword.getText();
    	
	    
	// call the controller login function and receives a class object
    	// the class object expresses the type of account we have
    	Account accountType = Controller.getInstance().login(username, password);
    	
    	if (accountType.equals(Customer.class))
    	{
    	    // user logged in as a customer
    	    return true;
    	}
    	else 
    	    // account is invalid
    	    return false;	
    		
    	//TN test return value
    	//return true;
    }
    
    private boolean loginBus() {
    	
        String username = tfUsername.getText();
	String password = tfPassword.getText();
    	
	    
	// call the controller login function and receives a class object
    	// the class object expresses the type of account we have
    	Account accountType = Controller.getInstance().login(username, password);
        if (accountType.equals(BusinessOwner.class))
    	{
    	    // user logged in as a b.o.
    	    return true;
    	}
    	else 
            // account is invalid
    	    return false;	
    		
    	//TN test return value
    	//return true;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
}
