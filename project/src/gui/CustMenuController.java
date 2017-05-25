/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Controller;

/**
 * Implements main menu for Customer access level options
 *
 * @author tn
 */
public class CustMenuController implements Initializable {

    private Controller c = Controller.getInstance();

    @FXML
    private Node root;

    @FXML
    private Button viewAvailCust;

    @FXML
    private Button makeBooking;

    @FXML
    private Button logout;

    /**
     * Initializes the controller class.
     */
    /**
     * Implements on button action to proceed to Upcomming Appointments form
     *
     * @param event
     * @throws IOException
     * @author tn
     */
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        // TN - get reference button stage
        Stage stage = (Stage) viewAvailCust.getScene().getWindow();
        // TN - load other scene
        Parent root = FXMLLoader.load(getClass().getResource("UpcomingAppointments.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Implements logout button to exit to login
     *
     * @param event
     * @throws IOException
     * @author tn
     */
    @FXML
    private void logoutButtonAction(ActionEvent event) throws IOException {
        c.logout();
        // reconnect to master DB
        c.loadDatabase("master");
        GUIUtil.switchTo("Login", root);

    }

    // Implements On select button action navigating to bookings form
    @FXML
    void handleBookingAction(ActionEvent event) throws IOException {
        // TN - get reference button stage
        Stage stage = (Stage) makeBooking.getScene().getWindow();
        // TN - load other scene
        Parent root = FXMLLoader.load(getClass().getResource("Booking.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens the ViewCustomerDetails screen
     *
     * @author krismania
     */
    @FXML
    public void handleViewDetails(ActionEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("ViewCustomerDetails.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // INIT
    }
}