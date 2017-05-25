/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXSlider;
import database.model.Employee;
import database.model.TimeSpan;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    @FXML private Label startLabel;
    @FXML private Label endLabel;
    @FXML private JFXSlider startDropdown;
    @FXML private JFXSlider endDropdown;
    @FXML private ChoiceBox<Employee> employeeDropdown;
    @FXML private ChoiceBox<String> dayDropdown;
    
    
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
		
		// add listener for sliders
		startDropdown.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable,
							Number oldValue, Number newValue)
			{
				startLabel.setText("Start Time: " + timeFromDouble((double) newValue).toString());
			}
		});
		
		endDropdown.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable,
							Number oldValue, Number newValue)
			{
				endLabel.setText("End Time: " + timeFromDouble((double) newValue).toString());
			}
		});
		
		// populate employee list
		employeeDropdown.getItems().addAll(c.getAllEmployees());
		
		// populate day list
		dayDropdown.getItems().addAll("Monday", "Tuesday", "Wednesday", 
						"Thursday", "Friday", "Saturday", "Sunday");

	}
	
	private boolean validate()
	{
		// check employee selected
		if (employeeDropdown.getSelectionModel().getSelectedItem() == null)
		{
			return false;
		}
		
		// check day selected
		if (dayDropdown.getSelectionModel().getSelectedItem() == null)
		{ 
			return false; 
		}
		
		// check that start is before end
		if (startDropdown.getValue() >= endDropdown.getValue())
		{
			return false;
		}
				
		// check that hours are within business hours
		TimeSpan hours = c.getHours(DayOfWeek.valueOf(dayDropdown.getValue().toUpperCase()));
		if (hours != null)
		{
			if (hours.start.isBefore(timeFromDouble(startDropdown.getValue())) || 
							hours.end.isAfter(timeFromDouble(endDropdown.getValue())))
			{
				// selected hours are within opening hours
				return true;
			}
			else
			{
				GUIAlert.infoBox("Please select a start and end time within the opening hours", 
								"Invalid shift times");
			}
		}
		else
		{
			GUIAlert.infoBox("The store is not open on this day. Update the opening hours to add a shift today.", 
							"Invalid shift times");
		}
		return false;
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
    		//double startTime = startDropdown.getValue();
    		//double endTime = endDropdown.getValue();
    		LocalTime startTime = timeFromDouble(startDropdown.getValue());
    		LocalTime endTime = timeFromDouble(endDropdown.getValue());
    		boolean added = c.addShift(employeeDropdown.getValue().ID, 
    						dayDropdown.getValue(), startTime,//Double.toString(startTime), 
    						endTime);//Double.toString(endTime));
			if (added)
			{
				GUIAlert.infoBox("Shift has been successfully added!", "Confirmation");
			}
			else
			{
				GUIAlert.infoBox("Unable to add shift!", "Confirmation");
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
	
	/**
	 * Converts a double (0-24) to a LocalTime
	 * @author krismania
	 */
	private LocalTime timeFromDouble(double input)
	{
	    int hour = (int) input;
	    int minute = (int) ((input % 1) * 60);
	    
	    return LocalTime.of(hour, minute);
	}
    
}  
