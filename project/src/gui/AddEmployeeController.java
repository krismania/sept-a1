package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import main.Controller;
import main.Validate;

/**
 * Form for adding new employees
 *
 * @author tn, krismania
 */
public class AddEmployeeController {
    Controller c = Controller.getInstance();

    @FXML
    private BorderPane root;
    @FXML
    private Label lblError;
    @FXML
    private TextField tfEmpFName;
    @FXML
    private TextField tfEmpLName;
    @FXML
    private TextField tfEmpEmailAdd;
    @FXML
    private TextField tfEmpPhNum;
    @FXML
    private Button submitNewEmpData;
    @FXML
    private Button navMenu;
    @FXML
    private Button exit;

    /**
     * Implements button to gather input data from fields and forward to
     * controller class
     *
     * @author tn
     */
    @FXML
    public void handleAddEmployee(ActionEvent event) {
        if (validateEmployee()) {
            // try to add the employee
            if (c.addEmployee(tfEmpFName.getText(), tfEmpLName.getText(), tfEmpEmailAdd.getText(),
                    tfEmpPhNum.getText())) {
                // employee added successfully
                // TN - If validation is successful a confirmation popup is
                // presented.
                lblError.setVisible(false);
                GUIUtil.infoBox("New Employee Successfully Added", "Add Employee Confirmation");

                // TN - Fields are cleared following correct input.
                tfEmpFName.clear();
                tfEmpLName.clear();
                tfEmpEmailAdd.clear();
                tfEmpPhNum.clear();

                // set focus to the first field. -kg
                tfEmpFName.requestFocus();
            } else {
                setError("Couldn't add an employee.");
            }
        }
    }

    /**
     * Implements Back button reverting to Business Owner Menu
     *
     * @author tn
     */
    @FXML
    public void handleBack(ActionEvent event) {
        GUIUtil.switchTo("BOMenu", root);
    }

    /**
     * @author krismania
     */
    private void setError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
    }

    /**
     * Returns true if all fields are valid, otherwise returns false and
     * displays a message in the . TODO: this looks kind of gross, fix it
     *
     * @author krismania
     */
    private boolean validateEmployee() {
        if (Validate.name(tfEmpFName.getText())) {
            if (Validate.name(tfEmpLName.getText())) {
                if (Validate.email(tfEmpEmailAdd.getText())) {
                    if (Validate.phone(tfEmpPhNum.getText())) {
                        lblError.setVisible(false);
                        return true;
                    } else {
                        setError("Please enter a valid phone number");
                        // TODO: this message is too long
                    }
                } else {
                    setError("Please enter a valid email");
                }
            } else {
                setError("Please enter a last name");
            }
        } else {
            setError("Please enter a first name");
        }
        return false;
    }
}
