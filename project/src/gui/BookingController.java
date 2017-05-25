package gui;

import java.io.IOException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.model.Availability;
import database.model.BusinessOwner;
import database.model.Customer;
import database.model.Employee;
import database.model.Service;
import database.model.TimeSpan;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import main.Controller;

//Implements form for creating bookings
public class BookingController implements Initializable {
    Controller c = Controller.getInstance();

    @FXML
    private Label lblError;
    @FXML
    private Label customerLabel;
    @FXML
    private TextField customerUser;
    @FXML
    private Button navMenu;
    @FXML
    private Button submitBooking;

    @FXML
    private ChoiceBox<Service> serviceDropdown;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ChoiceBox<Employee> employeePicker;
    @FXML
    private TextField timeH;
    @FXML
    private TextField timeM;
    @FXML
    private ChoiceBox<String> timeMeridiem;

    @FXML
    private VBox customerDetailsContainer;
    @FXML
    private TextField customerEmail;
    @FXML
    private TextField customerPhone;
    @FXML
    private TextField customerName;

    @FXML
    private Pane availabilityPane;

    /**
     * Add converters for dropdowns.
     *
     * @author krismania
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // restrict time to only integers
        timeH.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || Integer.parseInt(newValue) > 12) {
                timeH.setText(oldValue);
            } else {
                handleTimeChange();
            }
        });
        timeM.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || Integer.parseInt(newValue) > 60) {
                timeM.setText(oldValue);
            } else {
                handleTimeChange();
            }
        });
        timeMeridiem.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleTimeChange();
        });

        // set employee dropdown
        employeePicker.setConverter(new StringConverter<Employee>() {
            @Override
            public String toString(Employee e) {
                return e.getFirstName() + " " + e.getLastName();
            }

            @Override
            public Employee fromString(String string) {
                return null;
            }
        });

        // populate services dropdown
        serviceDropdown.getItems().addAll(c.getServices());
        serviceDropdown.getSelectionModel().selectFirst();

        // set date to today
        datePicker.setValue(LocalDate.now());
        handleDateChange();

        // show customer picker if business owner
        if (c.getLoggedUser() instanceof BusinessOwner) {
            customerDetailsContainer.setMinHeight(Region.USE_COMPUTED_SIZE);
            customerDetailsContainer.setVisible(true);
        }

        // populate meridiem choicebox
        timeMeridiem.getItems().add("AM");
        timeMeridiem.getItems().add("PM");
        timeMeridiem.getSelectionModel().selectFirst();
    }

    // Implements on select button action for returning to main menu -
    // selection of menu based on account type - Business Owner or Customer
    @FXML
    public void handleBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) navMenu.getScene().getWindow();
        // load the scene
        Scene accountMenu;
        if (c.getLoggedUser() instanceof Customer) {
            accountMenu = new Scene(FXMLLoader.load(getClass().getResource("CustMenu.fxml")));
        } else {
            accountMenu = new Scene(FXMLLoader.load(getClass().getResource("BOMenu.fxml")));
        }

        // switch scenes
        stage.setScene(accountMenu);
    }

    /**
     * Implement Calendar Selection for appointment date selection
     *
     * @param event
     * @throws IOException
     * @author tn
     */
    @FXML
    private void handleBook(ActionEvent event) throws IOException {
        if (c.getLoggedUser() instanceof BusinessOwner && customerUser.getText().isEmpty()) {
            GUIUtil.infoBox("Cannot process a booking without customer name", "Booking Error");
        } else {
            boolean booked = c.addBooking(datePicker.getValue(), getTime(), serviceDropdown.getValue(),
                    employeePicker.getValue().ID, customerUser.getText());

            if (booked) {
                GUIUtil.infoBox("Booking Successful", "Booking Confirmation");
            } else {
                GUIUtil.infoBox("Booking was not successful. Please ensure you have not already booked this date.",
                        "Booking Error");
            }
        }
    }

    /**
     * Implement context sensitive staff selector
     *
     * @author tn
     */
    @FXML
    public void handleDateChange() {
        employeePicker.getSelectionModel().clearSelection();
        timeH.clear();
        timeM.clear();
        generateEmployeesByDate();
    }

    /**
     * Implements context sensitive booking time selector
     *
     * @param event
     * @author tn
     */
    @FXML
    public void handleEmployeeChange(ActionEvent event) {
        populateAvailabilityPane();
    }

    /**
     * Changes time selection based on staff availability
     *
     * @author tn
     */
    @FXML
    public void handleTimeChange() {
        if (c.loggedUser instanceof Customer) {
            if (getTime() != null) {
                submitBooking.setDisable(false);
            } else {
                submitBooking.setDisable(true);
            }
        }
    }

    /**
     * Selects the entire text field on click.
     *
     * @author krismania
     */
    @FXML
    public void handleTimeClick(MouseEvent event) {
        TextField source = (TextField) event.getSource();
        source.selectAll();
    }

    /**
     * Shows context data and or error message responses
     *
     * @author tn
     */
    @FXML
    private void generateCustomerList() {
        Customer customer = c.getCustomer(customerUser.getText());
        if (customer != null) {
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
        } else {
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

    /**
     * Generates employee availability based on date selection and availability
     *
     * @author tn
     */
    private void generateEmployeesByDate() {
        LocalDate day = datePicker.getValue();
        employeePicker.getItems().removeAll(employeePicker.getItems());

        if (day.isBefore(LocalDate.now())) {
            // TODO create an error
            // GUIUtil.infoBox("Please select today or a date in the future",
            // "Error");
        } else {
            ArrayList<Employee> employees = c.getEmployeesWorkingOn(day);

            System.out.println("There are " + employees.size() + " employees working");

            if (employees.isEmpty()) {
                // TODO create an error
                // GUIUtil.infoBox("No employees working on selected date",
                // "Error");
            } else {
                employeePicker.getItems().addAll(employees);
            }
        }
    }

    /**
     * Populates
     *
     * @author krismania
     */
    private void populateAvailabilityPane() {
        // first, clear the old values
        availabilityPane.getChildren().clear();

        Availability avail = c.getEmployeeAvailability(datePicker.getValue(), employeePicker.getValue().ID);

        for (TimeSpan t : avail.getAvailability()) {
            Rectangle rect = new Rectangle();
            rect.setFill(Color.DODGERBLUE);
            rect.heightProperty().bind(availabilityPane.heightProperty());

            rect.layoutXProperty()
                    .bind(availabilityPane.widthProperty().multiply(t.start.toSecondOfDay() / (24.0 * 60.0 * 60.0)));
            rect.widthProperty().bind(
                    availabilityPane.widthProperty().multiply(t.getDuration().getSeconds() / (24.0 * 60.0 * 60.0)));

            availabilityPane.getChildren().add(rect);
        }

        /**
         * draw hour lines
         *
         * @author tn
         */
        for (int hour = 1; hour < 24; hour++) {
            Line line = new Line();
            line.endYProperty().bind(availabilityPane.heightProperty());
            line.layoutXProperty().bind(availabilityPane.widthProperty().multiply(hour / 24.0));

            // display 12 hr times
            String labelText;
            if (hour <= 12)
                labelText = Integer.toString(hour);
            else
                labelText = Integer.toString(hour - 12);

            Label label = new Label(labelText);
            label.setFont(Font.font(9));
            label.layoutXProperty().bind(availabilityPane.widthProperty().multiply(hour / 24.0).add(2));

            availabilityPane.getChildren().add(line);
            availabilityPane.getChildren().add(label);
        }
    }

    /**
     * Reads a local time from the 2 time input textFields
     *
     * @author krismania
     */
    private LocalTime getTime() {
        try {
            int hour, minute;

            if (timeMeridiem.getValue().equals("AM"))
                hour = Integer.parseInt(timeH.getText());
            else
                hour = Integer.parseInt(timeH.getText()) + 12;

            minute = Integer.parseInt(timeM.getText());

            System.out.println(LocalTime.of(hour, minute));
            return LocalTime.of(hour, minute);
        } catch (NumberFormatException | DateTimeException e) {
            return null;
        }
    }
}
