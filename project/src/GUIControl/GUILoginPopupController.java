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
    private PasswordField password;

    @FXML
    private TextField userName;
    
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
        Parent root;
        //login();
        if((event.getSource()==login)&&(login() != false)) {

        		stage=(Stage) login.getScene().getWindow();
        		root = FXMLLoader.load(getClass().getResource("GUICustMenu.fxml"));
        	
        	//TN - get reference button stage
            //stage=(Stage) login.getScene().getWindow();
            //TN - load other scene
        }
        else if(event.getSource()==businessOwner)
        {

        	String pw = password.getText();
     
        	//pw.passwordAccepted.Account(pw);
        	
        	stage=(Stage) businessOwner.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml"));
        }
        else
        {
        	stage=(Stage) signup.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUISignup.fxml")); 	
        }
        //TN - call a new scene instance
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    private boolean login()
    {
    	
		String pwCheck = password.getText();
		String unCheck = userName.getText();

		// HashMap for adding employee 
		
				/*<key, value>*/

		
		// Just testing hashMap for employees. Will refine after....
		boolean value = Controller.getInstance().addEmployee(map);
		return value;
    	// get the input
    	// put it in a hashmap
    	HashMap<String, String> loginInfo = new HashMap<String, String>();
    	
    	// call the controller login function and receives a class object
    	// the class object expresses the type of account we have
    	Class<?> accountType = Controller.getInstance().login(loginInfo);
    	
    	if (accountType.equals(Customer.class))
    	{
    		// user logged in as a customer
    		return true;
    	}
    	else if (accountType.equals(BusinessOwner.class))
    	{
    		// user logged in as a b.o.
    		return true;
    	}
    	else {
    		// account is invalid
    		return false;
    	}
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
}
