/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIControl;
import java.time.DayOfWeek;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Controller;
import main.ShiftTime;

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUIBOViewBookingSumController implements Initializable {
    
	private Controller c;
	
    @FXML
    private TableView<main.Booking> booking;
    
	@FXML
    private TableColumn<main.Booking, Date> date;

    @FXML
    private TableColumn<main.Booking, String> employeeID;

    @FXML
    private TableColumn<main.Booking, String> ID;

    @FXML
    private TableColumn<main.Booking, ShiftTime> time;

    @FXML
    private TableColumn<main.Booking, String> customer;

	
	@FXML
    private Button navMenu;
    
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
		Scene boMenu = new Scene(FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml")));
		
		// switch scenes
		stage.setScene(boMenu);
    }
    /*private List<Controller> getPastBookings() {
    	
    }*/
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	booking.getItems().setAll(c.getPastBookings());
        // TODO
    }    
    
}
