package gui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import main.Controller;
import main.Validate;

/**
 * Implements Signup form for people without existing login details
 *
 * @author tn
 *
 */
public class CreateBusinessController {
    private Controller c = Controller.getInstance();

    @FXML
    private BorderPane root;
    @FXML
    private TextField busName;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordConf;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;

    @FXML
    private Label lblError1;
    @FXML
    private Label lblError2;

    /**
     * Helper method for triggering error output
     *
     * @param label
     * @param text
     * @author tn
     */
    private void setError(Label label, String text) {
        label.setText(text);
        label.setVisible(true);
    }

    /**
     * Implements validation for new account data such as Password length and
     * complexity
     *
     * @author tn
     * @return
     */
    private boolean validateAccount() {
        // check the username and password
        if (Validate.username(username.getText())) {
            if (Validate.password((password.getText()))) {
                // check that confirm password matches
                if (password.getText().equals(passwordConf.getText())) {
                    lblError1.setVisible(false);
                    // username and password are accepted
                    return true;
                } else {
                    setError(lblError1, "Passwords don't match.");

                    passwordConf.clear();
                    passwordConf.requestFocus();
                }
            } else {
                // invalid password
                // TODO: more detailed error
                setError(lblError1, "Invalid Password.");

                password.clear();
                passwordConf.clear();
                password.requestFocus();
            }
        } else {
            // invalid username
            setError(lblError1, "Invalid Username.");

            username.requestFocus();
        }
        return false;
    }

    /**
     * Implements validation for new account data such as email address fields
     * etc
     *
     * @author tn
     */
    private boolean validateDetails() {
        if (!busName.getText().isEmpty()) {
            if (Validate.name(firstName.getText())) {
                if (Validate.name(lastName.getText())) {
                    if (Validate.phone(phone.getText())) {
                        lblError2.setVisible(false);
                        return true;
                    } else {
                        // invalid last name
                        // TODO: more detailed error
                        setError(lblError2, "Invalid phone number.");

                        phone.requestFocus();
                    }
                }

                else {
                    // invalid last name
                    setError(lblError2, "Please enter a last name.");

                    lastName.requestFocus();
                }
            } else {
                // invalid first name
                setError(lblError2, "Please enter a first name.");

                firstName.requestFocus();
            }
        } else {
            // invalid business name
            setError(lblError2, "Please enter a business name.");

            busName.requestFocus();
        }
        return false;
    }

    // Implements back button reverting to Login scene
    @FXML
    public void handleCancel(ActionEvent event) throws IOException {
        GUIUtil.switchTo("AdminMenu", root);
    }

    // Accepts data input from fields and inserts them into db via controller
    // class
    @FXML
    public void handleSignUp(ActionEvent event) throws IOException {
        // TODO: verify details
        if (validateAccount() & validateDetails()) {
            // try to sign up
            boolean addBooking = c.addBusiness(username.getText(), password.getText(), firstName.getText(),
                    lastName.getText(), address.getText(), phone.getText(), busName.getText());

            if (addBooking) {

                // alert the user
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Account Created!");
                alert.setHeaderText(null);
                alert.setContentText("Account was successfully created.");
                alert.showAndWait();

                GUIUtil.switchTo("AdminMenu", root);
            } else {

                // TODO: more specific errors here
                setError(lblError2, "Account already exists.");
            }
        }

    }
}
