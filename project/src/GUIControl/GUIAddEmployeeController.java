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
    void handleButtonAction(ActionEvent event) throws IOException {
    	
    	submitNewEmpData.setOnAction(e -> employee());

    }
	private void employee(){
		
		String firstName = tfEmpFName.getText();
		String lastName = tfEmpLName.getText();
		String email = tfEmpEmailAdd.getText();
		String employeeID;
		
		// HashMap for adding employee 
		
				/*<key, value>*/
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("First Name",firstName);
		map.put("Last Name", lastName);
		map.put("Email", email);
		map.put("Employee ID", "E008");
		
		// Just testing hashMap for employees. Will refine after....
		
		firstName = map.get("First Name");
		System.out.println("First name is " + firstName);
		lastName = map.get("Last Name");
		System.out.println("Last name is " + lastName);
		email = map.get("Email");
		System.out.println("Email is " + email);
		employeeID = map.get("Employee ID");
		System.out.println("Employee ID is " + employeeID);
		
		
		map.remove("First Name");
		
		if(map.containsKey("First Name")){
			System.out.println("First Name is still a variable");
		}else{
			System.out.println("First name is no longer a variable");
		}
	} 	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
