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
import javafx.scene.layout.HBox;
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
public class AddEmployeeController implements Initializable {  
    Controller c = Controller.getInstance();
	
    @FXML 
    private Label lblError;
    
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
    	//Controller.getInstance().getAllEmployees();
    	String firstName = tfEmpFName.getText();
    	String lastName = tfEmpLName.getText();
    	String email = tfEmpEmailAdd.getText();
    	String phoneNumber = tfEmpPhNum.getText();
    	employee(firstName, lastName, email, phoneNumber);
    }

    @FXML
    private void closeButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void navMenuButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) navMenu.getScene().getWindow();
        // load the scene
        Scene boMenu = new Scene(FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml")));
        // switch scenes
        stage.setScene(boMenu);
    }
    
    //TN - Gathers Employee variables and returns a boolean for validation of field processing
    private boolean employee(String firstName, String lastName, String email, String phoneNumber){
    	//TN - Add text field input to Controller addEmployee method
    	boolean addEmp = c.addEmployee(firstName, lastName, email, phoneNumber);
    	if(addEmp == false)
        {
            //TN - Presents red error message if input is false
            lblError.setVisible(true);
            return false;
        }
        else 
    	{
            //TN -  If validation is successful a confirmation popup is presented.
            GUIAlert info  = new GUIAlert();
            lblError.setVisible(false);
            info.infoBox("New Employee Successfully Added", "Add Employee Confirmation");
            //TN - Fields are cleared following correct input.
            tfEmpFName.clear();
            tfEmpLName.clear(); 
            tfEmpEmailAdd.clear();
            tfEmpPhNum.clear();
            return true;
    	}
    } 	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
