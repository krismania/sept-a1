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
import main.BusinessOwner;
import main.Controller;
import main.Customer;
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
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class GUIBookingController {
	Controller c = Controller.getInstance();
	private String customerUsername = null;
	
	@FXML
    private Button navMenu;

    @FXML
    private Button submitBooking;
    
    @FXML 
    private TextField customerName;
    
    @FXML
    private Label customerLabel;
    
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
    	Scene accountMenu;
    	if(c.getLoggedUser() instanceof Customer)
    	{
    		accountMenu = new Scene(FXMLLoader.load(getClass().getResource("GUICustMenu.fxml")));
    	}
    	else
    	{
    		accountMenu = new Scene(FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml")));
    	}
		
		// switch scenes
		stage.setScene(accountMenu);
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{
    	if(c.getLoggedUser() instanceof BusinessOwner && customerName.getText().isEmpty())
    	{
    		System.out.println("Cannot process a booking without customer name");
    	}
    	else
    	{
    		boolean booked = c.addBooking(datePicker.getValue(), LocalTime.parse(bookingOptionsDropdown.getValue()), 
    				Integer.parseInt(employeePicker.getValue()), customerName.getText());
    	
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
    	
    }
    
    @FXML
    private void generateEmployeesByDate(ActionEvent event) throws IOException{
    	setCustomerVisbility(event);
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
    
    @FXML
    private void generateTimesByEmp(ActionEvent event) throws IOException{
    	ArrayList<String> times = c.getShiftsByEmp(employeePicker.getValue(), datePicker.getValue());
    	
    	bookingOptionsDropdown.getItems().removeAll(bookingOptionsDropdown.getItems());
    	bookingOptionsDropdown.getItems().addAll(times);
    }
    
    /*@FXML
    private void selectCustomer(ActionEvent event) throws IOException{
    	
    	Scene accountMenu = new Scene(FXMLLoader.load(getClass().getResource("GUICustMenu.fxml")));
    	
    }*/
   
    public void setCustomerVisbility(ActionEvent event) throws IOException {
    	if(c.getLoggedUser() instanceof BusinessOwner)
    	{
    		System.out.println("Displaying Customer Name!");
    		customerName.setVisible(true);
    		customerLabel.setVisible(true);
    	}
    }
    
    public void initialize(URL url, ResourceBundle rb)
	{
    	
	}
}
