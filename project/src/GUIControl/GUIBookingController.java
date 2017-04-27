package GUIControl;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
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
    private Button exit;
    
    @FXML
    private void closeButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    
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
    	//Do stuff
    	System.out.println("Button doesnt work just yet :D");
    }
    
    @FXML
    private void generateEmployeesByDate(ActionEvent event) throws IOException{
    	LocalDate day = datePicker.getValue();
    	
    	employeePicker.getItems().removeAll(employeePicker.getItems());
    	employeePicker.getItems().addAll(c.getEmpByDay(day));
    }
}
