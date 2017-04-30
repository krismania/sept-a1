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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUIAddTimeController implements Initializable {
    Controller c = Controller.getInstance();   
    
    @FXML 
    private Label lblError;

    @FXML
    private Button navMenu;

    @FXML
    private Button btRecordAvail;
    
    @FXML
	private ChoiceBox<String> dayDropdown;
    
    @FXML
    private ChoiceBox<String> timeDropdown;
    
    @FXML
    private ChoiceBox<String> durationDropdown;

    @FXML
    private ChoiceBox<Number> empIDDropdown;
    
    @FXML
    private void navMenuButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) navMenu.getScene().getWindow();
		// load the scene
		Scene boMenu = new Scene(FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml")));
		
		// switch scenes
		stage.setScene(boMenu);
    }
    
    //TN - Collects data from DatePicker and Dropdowns and stores into variables
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{    	
     
      boolean added = c.addShift((int) empIDDropdown.getValue(), 
    		  dayDropdown.getValue(), timeDropdown.getValue(), durationDropdown.getValue());
      if(added)
      {
    	  System.out.println("Shift Added!");
      }
      else
      {
    	  System.out.println("Unable to add shift!");
      }
    }
    
    //TN Send data to Controller - not yet implemented
 /*   private boolean testShiftDate(String dayString, String timeString, int empID){
    	//TN - Add text field input to Controller addEmployee method
    	boolean addDate = c.shiftExists(dayString, timeString, empID);
    	if(addDate == false)
        {
            //TN - Presents red error message if input is false
            lblError.setVisible(true);
            return false;
        }
        else 
    	{
            //TN -  If validation is successful a confirmation popup is presented.
            lblError.setVisible(false);

            return true;
    	}
 */     
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    	empIDDropdown.getItems().removeAll(empIDDropdown.getItems());
    	empIDDropdown.getItems().addAll(1, 2, 3, 4);
    	empIDDropdown.getSelectionModel().select(0);
    	
    	//TN - initialise shift time slot dropdown menus
    	timeDropdown.getItems().removeAll(timeDropdown.getItems());
    	timeDropdown.getItems().addAll("9:00 am", "9:30 am", "10:00 am", "10:30 am", 
        		"11:00 am", "11:30 am", "12:00 pm", "12:30 pm", "1:00 pm", "1:30 pm", 
        		"2:00 pm", "2:30 pm", "3:00 pm", "3:30 pm", "4:00 pm", "4:30 pm", "5:00 pm");
    	timeDropdown.getSelectionModel().select("9:00 am");
        
        //TN - initialise shift duration dropdown menus
        durationDropdown.getItems().removeAll(durationDropdown.getItems());
        durationDropdown.getItems().addAll("30 minutes", "1 hour", 
        		"1 hour 30 minutes", "2 hours");
        durationDropdown.getSelectionModel().select("30 minutes");
        
        //TN - initialise shift duration dropdown menus
        dayDropdown.getItems().removeAll(dayDropdown.getItems());
        dayDropdown.getItems().addAll("Monday", "Tuesday", 
        		"Wednesday", "Thursday", "Friday", "Saturday");
        dayDropdown.getSelectionModel().select("Monday"); 
    }
    
}   
