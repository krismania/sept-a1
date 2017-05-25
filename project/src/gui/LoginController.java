package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import database.model.Account;
import database.model.Admin;
import database.model.BusinessOwner;
import database.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Controller;

/**
 * Implements login scene and is the initial point of entry into the booking
 * system app
 *
 * @author tn
 *
 */
public class LoginController implements Initializable {
    Controller c = Controller.getInstance();

    @FXML
    private HBox root;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Label lblError;
    @FXML
    private ChoiceBox<String> businessPicker;

    @FXML
    private ImageView imageView;

    /**
     * implements button action and decision logic for determining the users
     * access level based on login information Forwards data to controller for
     * validation then progresses to the appropriate menu based on return from
     * controller
     *
     * @author tn
     */
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        // Disconnect from master DB
        c.disconnectMaster();
        // Load selected DB. JM
        if (businessPicker.getValue() == "Administration") {
            c.loadDatabase("master");
        } else {
            c.loadDatabase(businessPicker.getValue());
        }

        // attempt login
        Account account = c.login(tfUsername.getText(), tfPassword.getText(), businessPicker.getValue());

        if (account instanceof Customer) {
            lblError.setVisible(false);

            // load the scene
            Scene customerLogin = new Scene(FXMLLoader.load(getClass().getResource("CustMenu.fxml")));

            // get current stage
            Stage stage = (Stage) root.getScene().getWindow();

            // switch scenes
            stage.setScene(customerLogin);
        } else if (account instanceof BusinessOwner) {
            lblError.setVisible(false);

            // load the scene
            Scene boLogin = new Scene(FXMLLoader.load(getClass().getResource("BOMenu.fxml")));

            // get current stage
            Stage stage = (Stage) root.getScene().getWindow();

            // switch scenes
            stage.setScene(boLogin);
        } else if (account instanceof Admin) {
            lblError.setVisible(false);

            // load the scene
            Scene adminLogin = new Scene(FXMLLoader.load(getClass().getResource("AdminMenu.fxml")));

            // get current stage
            Stage stage = (Stage) root.getScene().getWindow();

            // switch scenes
            stage.setScene(adminLogin);
        } else {
            // if account isn't an instance of either account type, login
            // failed.
            lblError.setVisible(true);
            // Remove attempted DB connect from memory in Controller.JM
            c.disconnectDB();
            // reconnect to master DB
            c.loadDatabase("master");
            tfPassword.clear();
        }
    }

    /**
     * Implements button to direct new customers to the signup form
     *
     * @param event
     * @throws IOException
     * @author tn
     */
    @FXML
    public void handleSignup(ActionEvent event) throws IOException {
        // load the scene
        Scene signup = new Scene(FXMLLoader.load(getClass().getResource("Signup.fxml")));

        // get current stage
        Stage stage = (Stage) root.getScene().getWindow();

        // switch scenes
        stage.setScene(signup);
    }

    /**
     * switches the splash image when the business changes
     *
     * @author krismania
     */
    @FXML
    public void handleBusinessChange() {
        String imagePath = c.getImageForBusiness(businessPicker.getValue());

        if (imagePath == null) {
            // if no header image, use the default one
            imagePath = getClass().getResource("resources/images/default.jpg").toString();
        }

        imageView.setImage(new Image(imagePath));
    }

    /**
     * Initialises business data for use in table fields
     *
     * @author tn
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        businessPicker.getItems().removeAll(businessPicker.getItems());
        businessPicker.getItems().addAll(c.getAllBusinessNames());
        businessPicker.getItems().add("Administration");

        // default to the first business
        businessPicker.getSelectionModel().selectFirst();
    }
}
