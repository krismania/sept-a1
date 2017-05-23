package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.model.BusinessOwner;
import database.model.Customer;
import database.model.Employee;
import database.model.Service;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.Availability;
import main.Controller;
import main.TimeSpan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
//Implements form for creating bookings
public class BookingController implements Initializable
{
	Controller c = Controller.getInstance();
	private String customerUsername = null;
	
	@FXML private Label lblError;
    @FXML private Label customerLabel;
    @FXML private TextField customerUser;
	@FXML private Button navMenu;
    @FXML private Button submitBooking;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<Employee> employeePicker;
    @FXML private ChoiceBox<String> bookingOptionsDropdown;
    @FXML private ChoiceBox<Service> serviceDropdown;
    
    @FXML private VBox customerDetailsContainer;
    @FXML private Label TitleOfDetails;
    @FXML private Label Email;
    @FXML private Label Phone;
    @FXML private Label Name;
    @FXML private TextField customerEmail;
    @FXML private TextField customerPhone;
    @FXML private TextField customerName;
    
    @FXML private Pane availabilityPane;
    
    /**
     * Add converters for dropdowns.
     * @author krismania
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
	{
		// set employee dropdown
    	employeePicker.setConverter(new StringConverter<Employee>()
		{
			@Override
			public String toString(Employee e)
			{
				return e.getFirstName() + " " + e.getLastName();
			}
			@Override public Employee fromString(String string) { return null; }	
		});
    	
    	// populate services dropdown
    	serviceDropdown.getItems().addAll(c.getServices());
    	serviceDropdown.getSelectionModel().selectFirst();
    	
    	// set date to today
    	datePicker.setValue(LocalDate.now());
    	handleDateChange();
    	
    	// show customer picker if business owner
    	if(c.getLoggedUser() instanceof BusinessOwner)
    	{
    		customerDetailsContainer.setMinHeight(Region.USE_COMPUTED_SIZE);
    		customerDetailsContainer.setVisible(true);
    	}
	}

	//Implements on select button action for returning to main menu - 
    //selection of menu based on account type - Business Owner or Customer
    @FXML 
    public void handleBack(ActionEvent event) throws IOException
    {
    	Stage stage = (Stage) navMenu.getScene().getWindow();
		// load the scene
    	Scene accountMenu;
    	if(c.getLoggedUser() instanceof Customer)
    	{
    		accountMenu = new Scene(FXMLLoader.load(getClass().getResource("CustMenu.fxml")));
    	}
    	else
    	{
    		accountMenu = new Scene(FXMLLoader.load(getClass().getResource("BOMenu.fxml")));
    	}
		
		// switch scenes
		stage.setScene(accountMenu);
    }
    
    //Implement Calendar Selection for appointment date selection
    @FXML
    private void handleBook(ActionEvent event) throws IOException{
    	if(c.getLoggedUser() instanceof BusinessOwner && customerUser.getText().isEmpty())
    	{
    		GUIAlert.infoBox("Cannot process a booking without customer name", "Booking Error");
    	}
    	else
    	{
    		boolean booked = c.addBooking(datePicker.getValue(), bookingOptionsDropdown.getValue(), 
    				serviceDropdown.getValue(),	employeePicker.getValue().ID, customerUser.getText());
    		
    		if(booked)
	    	{
    			GUIAlert.infoBox("Booking Successful", "Booking Confirmation");
	    	}
	    	else 
	    	{
	    		GUIAlert.infoBox("Booking was not successful. Please ensure you have not already booked this date.", "Booking Error");
	    	}
    	}
    }
    
    //Implement context sensitive staff selector
    @FXML
    public void handleDateChange()
    {
    	employeePicker.getSelectionModel().clearSelection();
    	bookingOptionsDropdown.getSelectionModel().clearSelection();
    	generateEmployeesByDate();
    }
    
    //Implements context sensitive booking time selector
    @FXML
    public void handleEmployeeChange(ActionEvent event)
    {
    	bookingOptionsDropdown.getSelectionModel().clearSelection();
    	generateTimesByEmp();
    	populateAvailabilityPane();
    }
    
    //Changes time selection based on staff availability
    @FXML
    public void handleTimeChange(ActionEvent event)
    {
    	if(c.loggedUser instanceof Customer)
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
    }
    
    //Shows context data and or error message responses
    @FXML
    private void generateCustomerList()
    {
    	Customer customer = c.getCustomer(customerUser.getText());
    	if (customer != null)
    	{
    		submitBooking.setDisable(false);
    		lblError.setVisible(false);
    		customerName.setVisible(true);
    		customerName.setText(customer.getFirstName() + " " + customer.getLastName());
    		customerName.setDisable(false);
    		
    		customerEmail.setVisible(true);
    		customerEmail.setText(customer.getEmail());
    		customerEmail.setDisable(false);
    		
    		customerPhone.setVisible(true);
    		customerPhone.setText(customer.getPhoneNumber());
    		customerPhone.setDisable(false);
    	}
    	else
    	{
    		submitBooking.setDisable(true);
    		lblError.setVisible(true);
    		
    		customerName.setDisable(true);
    		customerName.clear();
    		
    		customerEmail.setDisable(true);
    		customerEmail.clear();
    		
    		customerPhone.setDisable(true);
    		customerPhone.clear();
    	}
    }
    
    //Generates employee availability based on date selection and availability
    private void generateEmployeesByDate()
    {
    	LocalDate day = datePicker.getValue();
    	employeePicker.getItems().removeAll(employeePicker.getItems());
    	
    	if(day.isBefore(LocalDate.now())) {
    		// TODO: create an error
    		// employeePicker.getItems().addAll("Please select today or a date in the future");
    	}
    	else 
    	{
	    	ArrayList<Employee> employees = c.getEmployeesWorkingOn(day);
	    	
	    	System.out.println("There are " + employees.size() + " employees working");
	    	
	    	if(employees.isEmpty())
	    	{
	    		// TODO: create an error
	    		// employeePicker.getItems().addAll("No employees working on selected date");
	    	}
	    	else {
	    		employeePicker.getItems().addAll(employees);
	    	}
    	}
    }
    
    //Generates available times based on selected staff
    private void generateTimesByEmp()
    {
    	bookingOptionsDropdown.getItems().removeAll(bookingOptionsDropdown.getItems());
    	
    	ArrayList<String> times = c.getEmployeeAvailability(datePicker.getValue(), employeePicker.getValue().ID);
    	bookingOptionsDropdown.getItems().addAll(times);
    }
    
    /**
     * TODO: document
     * @author krismania
     */
    private void populateAvailabilityPane()
    {
    	// first, clear the old values
    	availabilityPane.getChildren().clear();
    	
    	Availability avail = c.getEmployeeAvailability2(datePicker.getValue(), employeePicker.getValue().ID);
    	
    	for (TimeSpan t : avail.getAvailability())
    	{
    		Rectangle rect = new Rectangle();
    		rect.setFill(Color.DODGERBLUE);
    		rect.heightProperty().bind(availabilityPane.heightProperty());
    		
    		rect.layoutXProperty().bind(availabilityPane.widthProperty().multiply(t.start.toSecondOfDay()/(24.0 * 60.0 * 60.0)));
        	rect.widthProperty().bind(availabilityPane.widthProperty().multiply(t.getDuration().getSeconds()/(24.0 * 60.0 * 60.0)));
        	
        	availabilityPane.getChildren().add(rect);
    	}
    	
    	// draw hour lines
    	for (int hour = 1; hour < 24; hour++)
    	{
    		Line line = new Line();
    		line.setStroke(Color.GRAY);
    		line.endYProperty().bind(availabilityPane.heightProperty());
    		line.layoutXProperty().bind(availabilityPane.widthProperty().multiply(hour/24.0));
    		
    		availabilityPane.getChildren().add(line);
    	}
    }
}
