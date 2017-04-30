/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import main.Controller;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * FXML Controller class
 * Initialises customer main menu   
 * @author tn
 */
public class CustMenuDisplay implements Initializable {

    private Controller c = Controller.getInstance();
	
    @FXML
    private Button viewAvailCust;
	
    @FXML
    private Button makeBooking;
    
    @FXML
    private Button logout;

    //TN - Initializes button menu actions
    // Redirects to Upcomming appointments form.
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
       	//TN - get reference button stage
        Stage stage = (Stage) viewAvailCust.getScene().getWindow();
       	//TN - load other scene
        Parent root = FXMLLoader.load(getClass().getResource("UpcomingAppointments.fxml"));
   
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // Redirects to Login - is a logout button
    @FXML
    private void logoutButtonAction(ActionEvent event) throws IOException {
        c.logout();
    	
        Stage stage = (Stage) logout.getScene().getWindow();
    	
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
    	   
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // Redirects to the make a booking form
    @FXML
    void handleBookingAction(ActionEvent event) throws IOException {
       	//TN - get reference button stage
        Stage stage = (Stage) makeBooking.getScene().getWindow();
       	//TN - load other scene
        Parent root = FXMLLoader.load(getClass().getResource("Booking.fxml"));
   
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Opens the ViewCustomerDetails screen
     * @author krismania
     */
    @FXML
    public void handleViewDetails(ActionEvent event) throws IOException
    {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("CustDetails.fxml"));
    	
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}