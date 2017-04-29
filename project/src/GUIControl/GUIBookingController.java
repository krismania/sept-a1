package GUIControl;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import main.Controller;
import javafx.scene.Parent;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class GUIBookingController {
	Controller c = Controller.getInstance();
	
	@FXML
    private Button navMenu;

    @FXML
    private Button submitBooking;
    
    @FXML
	private DatePicker datePicker;
    
    @FXML
    private ChoiceBox<String> employeePicker;
    
    @FXML
    private ChoiceBox<String> bookingOptionsDropdown;
    
    @FXML
    private void navMenuButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) navMenu.getScene().getWindow();
		// load the scene
		Scene custMenu = new Scene(FXMLLoader.load(getClass().getResource("GUICustMenu.fxml")));
		
		// switch scenes
		stage.setScene(custMenu);
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{
    	boolean booked = c.addBooking(datePicker.getValue(), LocalTime.parse(bookingOptionsDropdown.getValue()), 
    			Integer.parseInt(employeePicker.getValue()));
    	
    	if(booked)
    	{
    		System.out.println("Booked in!");
    		c.getPastBookings();
    		c.getFutureBookings();
    	}
    	else 
    	{
    		System.out.println("Booking has gone wrong!");
    	}
    	
    }
    
    @FXML
    private void generateEmployeesByDate(ActionEvent event) throws IOException{
    	LocalDate day = datePicker.getValue();
    	employeePicker.getItems().removeAll(employeePicker.getItems());
    	
    	if(day.isBefore(LocalDate.now())) {
    		System.out.println("Date has passed.");
    		employeePicker.getItems().addAll("Please select today or a date in the future");
		submitBooking.setDisable(true);
    	}
    	
    	else 
    	{
	    	ArrayList<String> empIDs = c.getEmpByDay(day);
	    	if(empIDs.isEmpty())
	    	{
	    		employeePicker.getItems().addAll("No employees working on selected date");
			submitBooking.setDisable(true);
	    	}
	    	else {
	    		employeePicker.getItems().addAll(empIDs);
			submitBooking.setDisable(false);
			
	    	}
    	}
    }
    
    @FXML
    private void generateTimesByEmp(ActionEvent event) throws IOException{
    	ArrayList<String> times = c.getShiftsByEmp(employeePicker.getValue(), datePicker.getValue());
    	
    	bookingOptionsDropdown.getItems().removeAll(bookingOptionsDropdown.getItems());
    	bookingOptionsDropdown.getItems().addAll(times);
    }
}
