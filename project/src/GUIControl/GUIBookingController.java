package GUIControl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.stage.Stage;
import main.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class GUIBookingController
{
	Controller c = Controller.getInstance();
	
	@FXML private Button navMenu;
    @FXML private Button submitBooking;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<String> employeePicker;
    @FXML private ChoiceBox<String> bookingOptionsDropdown;
    
    
    @FXML
    public void initialize()
    {
    	// init
    }
    
    @FXML 
    public void handleBack(ActionEvent event) throws IOException
    {
    	Stage stage = (Stage) navMenu.getScene().getWindow();
		// load the scene
		Scene custMenu = new Scene(FXMLLoader.load(getClass().getResource("GUICustMenu.fxml")));
		
		// switch scenes
		stage.setScene(custMenu);
    }
    
    @FXML
    public void handleBook(ActionEvent event)
    {
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
    public void handleDateChange(ActionEvent event)
    {
    	employeePicker.getSelectionModel().clearSelection();
    	bookingOptionsDropdown.getSelectionModel().clearSelection();
    	generateEmployeesByDate();
    }
    
    @FXML
    public void handleEmployeeChange(ActionEvent event)
    {
    	bookingOptionsDropdown.getSelectionModel().clearSelection();
    	generateTimesByEmp();
    }
    
    @FXML
    public void handleTimeChange(ActionEvent event)
    {
    	if (bookingOptionsDropdown.getSelectionModel().getSelectedItem() != null)
    	{
    		submitBooking.setDisable(false);
    	}
    	else
    	{
    		submitBooking.setDisable(true);
    	}
    }
    
    private void generateEmployeesByDate()
    {
    	LocalDate day = datePicker.getValue();
    	employeePicker.getItems().removeAll(employeePicker.getItems());
    	
    	if(day.isBefore(LocalDate.now())) {
    		System.out.println("Date has passed.");
    		employeePicker.getItems().addAll("Please select today or a date in the future");
    	}
    	
    	else 
    	{
	    	ArrayList<String> empIDs = c.getEmpByDay(day);
	    	if(empIDs.isEmpty())
	    	{
	    		employeePicker.getItems().addAll("No employees working on selected date");
	    	}
	    	else {
	    		employeePicker.getItems().addAll(empIDs);
	    	}
    	}
    }
    
    private void generateTimesByEmp()
    {
    	ArrayList<String> times = c.getShiftsByEmp(employeePicker.getValue(), datePicker.getValue());
    	
    	bookingOptionsDropdown.getItems().removeAll(bookingOptionsDropdown.getItems());
    	bookingOptionsDropdown.getItems().addAll(times);
    }
}
