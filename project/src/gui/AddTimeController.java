/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.Controller;
import model.Employee;
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
 * Implements form to add employee availability times
 * @author tn
 * @author krismania
 */
public class AddTimeController implements Initializable {
    Controller c = Controller.getInstance();   
    
    @FXML private Label lblError;
    @FXML private Button navMenu;
    @FXML private Button btRecordAvail;

    @FXML private ChoiceBox<Employee> employeeDropdown;
    @FXML private ChoiceBox<String> dayDropdown;
    @FXML private ChoiceBox<String> startDropdown;
    @FXML private ChoiceBox<String> endDropdown;
    
    
    /**
     * Populate dropdowns on load
     * @author krismania
     * @author TN
     */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// set employee converter
		employeeDropdown.setConverter(new StringConverter<Employee>()
		{
			@Override
			public String toString(Employee e)
			{
				return "" + e.ID + ": " + e.getFirstName() + " " + e.getLastName();
			}
			@Override public Employee fromString(String string) { return null; }	
		});
		
		// populate employee list
		employeeDropdown.getItems().addAll(c.getAllEmployees());
		
		// populate day list
		dayDropdown.getItems().addAll("Monday", "Tuesday", "Wednesday", 
						"Thursday", "Friday", "Saturday", "Sunday");
		
		// populate times
		String[] times = {"9:00 am", "9:30 am", "10:00 am", "10:30 am", "11:00 am", 
						"11:30 am", "12:00 pm", "12:30 pm", "1:00 pm", "1:30 pm", 
						"2:00 pm", "2:30 pm", "3:00 pm", "3:30 pm", "4:00 pm", 
						"4:30 pm", "5:00 pm"};
		startDropdown.getItems().setAll(times);
		endDropdown.getItems().setAll(times);
	}
	
	private boolean validate()
	{
		// check employee selected
		if (employeeDropdown.getSelectionModel().getSelectedItem() == null) return false;
		
		// check day selected
		if (dayDropdown.getSelectionModel().getSelectedItem() == null) return false;
		
		// check start/end selected
		if (startDropdown.getSelectionModel().getSelectedItem() == null ||
			endDropdown.getSelectionModel().getSelectedItem() == null)
		{
			return false;
		}
		
		// check that start is before end
		if (startDropdown.getSelectionModel().getSelectedIndex() >= endDropdown.getSelectionModel().getSelectedIndex())
		{
			return false;
		}
		
		return true;
	}
	

	/**
	 * Attempt to add the shift
	 * @author TN
	 * @author krismania
	 */
    @FXML	
    public void handleRecord(ActionEvent event) throws IOException
    {
    	if (validate())
    	{
    		lblError.setVisible(false);
    		    		
    		boolean added = c.addShift(employeeDropdown.getValue().ID, 
    						dayDropdown.getValue(), startDropdown.getValue(), 
    						endDropdown.getValue());
    		
			if (added)
			{
				GUIAlert.infoBox("Shift has been successfully added!", "Confirmation");
				System.out.println("Shift Added!");
			}
			else
			{
				System.out.println("Unable to add shift!");
			}
    	}
    	else
    	{
    		lblError.setVisible(true);
    	}
    }


	//Implements back button navigation - redirects to Business Owner Main menu
	@FXML
	public void handleBack(ActionEvent event) throws IOException
	{
		Stage stage = (Stage) navMenu.getScene().getWindow();
		// load the scene
		Scene boMenu = new Scene(FXMLLoader.load(getClass().getResource("BOMenu.fxml")));
		
		// switch scenes
		stage.setScene(boMenu);
	}
    
}   
