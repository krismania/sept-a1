package gui;

import java.net.URL;
import java.util.ResourceBundle;

import database.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Controller;

/**
 * Displays the logged in customer's details.
 * 
 * @author krismania
 */
public class ViewCustomerDetailsController implements Initializable {
    private Controller c = Controller.getInstance();

    @FXML
    private Node root;

    @FXML
    private Label username;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField phone;

    /**
     * Initialise data to populate fields
     * 
     * @author tn
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // get user's account objects
        Customer customer = (Customer) c.getLoggedUser();

        // populate the fields
        username.setText(customer.username);
        firstName.setText(customer.getFirstName());
        lastName.setText(customer.getLastName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhoneNumber());
    }

    /**
     * Implements back button reverting to Customer Main menu scene
     * 
     * @param event
     * @author tn
     */
    @FXML
    public void handleBack(ActionEvent event) {
        GUIUtil.switchTo("CustMenu", root);
    }

}
