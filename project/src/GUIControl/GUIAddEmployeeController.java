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
import main.Controller;
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

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUIAddEmployeeController implements Initializable {  
    @FXML
    private TextField tfEmpFName;

    @FXML
    private TextField tfEmpLName;

    @FXML
    private TextField tfEmpEmailAdd;

    @FXML
    private TextField tfEmpPhNum;

    @FXML
    private Button submitNewEmpData;
    
    @FXML
    private Button navMenu;
    
    @FXML
    private Button exit;

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
    	
    submitNewEmpData.setOnAction(e -> 
    {
    	if (employee() != true)
    	{
    		GUIAlert.infoBox("You have entered incorrect data please try again", "");
    	}
    	else
    	    GUIAlert.infoBox("New employee is successfully added", "");
    	});
    }
    public boolean textFieldCheck(String firstName, String lastName, String email, String employeeID)
    {
    	if(firstName.equals("tfEmfName"))
        {
            System.out.print(true);
            return true;
        }
        else
        {
            System.out.print(false);
            return false;
        }

    }
    @FXML
    private void closeButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void navMenueButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) navMenu.getScene().getWindow();
		// load the scene
		Scene boMenu = new Scene(FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml")));
		
		// switch scenes
		stage.setScene(boMenu);
    }
    
    //TN - Gathers Employee variables and returns a boolean for validation of field processing
  
    private boolean employee(){
  
	    return true;
  /*	String firstName = tfEmpFName.getText();
  * 	String lastName = tfEmpLName.getText();
  * 	String email = tfEmpEmailAdd.getText();
  *     String phone = tfEmpPhNum.getText();
  *		
  *	    // HashMap for adding employee 
  *	
  *		//<key, value>
  *	    HashMap<String, String> map = new HashMap<String, String>();
  *     map.put("firstName",firstName);
  *	    map.put("lastName", lastName);
  *     map.put("email", email);
  *	    map.put("phoneNumber", phone);
  *	
  *     // Just testing hashMap for employees. Will refine after....
  *     boolean value = Controller.getInstance().addEmployee(map);
  *     return value;
  */ 
    } 	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
