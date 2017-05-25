/*
 *
 *
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import database.model.Booking;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import main.Controller;

/**
 * Implements Business owner form to view Past and Future Bookings
 *
 * @author tn
 */
public class BOViewBookingSumController implements Initializable {
    // Initialise field Variables.
    private Controller c = Controller.getInstance();

    @FXML
    private Node root;

    @FXML
    private Tab pastBookings;

    @FXML
    private Tab futureBookings;

    @FXML
    private TableView<Booking> bookingP;

    @FXML
    private TableView<Booking> bookingF;

    @FXML
    private TableColumn<Booking, Number> IDP;

    @FXML
    private TableColumn<Booking, String> customerP;

    @FXML
    private TableColumn<Booking, String> dateP;

    @FXML
    private TableColumn<Booking, String> timeP;

    @FXML
    private TableColumn<Booking, Number> employeeIDP;

    @FXML
    private TableColumn<Booking, Number> IDF;

    @FXML
    private TableColumn<Booking, String> customerF;

    @FXML
    private TableColumn<Booking, String> dateF;

    @FXML
    private TableColumn<Booking, String> timeF;

    @FXML
    private TableColumn<Booking, Number> employeeIDF;

    @FXML
    private Button navMenu;

    @FXML
    private void navMenuButtonAction(ActionEvent event) throws IOException {
        GUIUtil.switchTo("BOMenu", root);
    }

    /**
     * Implements Factory patter to construct cells for appointment information
     * display
     *
     * @author tn
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**
         * TN - Uses ***Observer & Factory Patterns***
         *
         * Populate Cells for Past Appointments Collects Appointment ID from
         * Booking class and returns as a Number
         *
         * @author tn
         */
        IDP.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, Number>, ObservableValue<Number>>() {

            @Override
            public ObservableValue<Number> call(CellDataFeatures<Booking, Number> param) {
                IntegerProperty prop = new SimpleIntegerProperty();
                prop.setValue(param.getValue().ID);
                return prop;
            }
        });
        // Collects customer from Booking class and returns as a String
        customerP.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Booking, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Booking, String> param) {
                        StringProperty prop = new SimpleStringProperty();
                        prop.setValue(param.getValue().getCustomer());
                        return prop;
                    }
                });
        // Collects date from Booking class and returns as a String
        dateP.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Booking, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Booking, String> param) {
                        StringProperty prop = new SimpleStringProperty();
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String strProp = dateFormatter.format(param.getValue().getDate());
                        prop.setValue(strProp);
                        return prop;
                    }
                });
        // Collects time from Booking class and returns as a String
        timeP.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Booking, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Booking, String> param) {
                        StringProperty prop = new SimpleStringProperty();
                        DateTimeFormatter tToStr = DateTimeFormatter.ofPattern("HH:mm:ss");
                        String tProp = tToStr.format(param.getValue().getStart());
                        prop.setValue(tProp);
                        return prop;
                    }
                });
        // Collects employeeID from Booking class and returns as a Number
        employeeIDP.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Booking, Number>, ObservableValue<Number>>() {

                    @Override
                    public ObservableValue<Number> call(CellDataFeatures<Booking, Number> param) {
                        IntegerProperty prop = new SimpleIntegerProperty();
                        prop.setValue(param.getValue().getEmployeeID());
                        return prop;
                    }
                });

        /**
         * TN - Uses ***Observer & Factory Patterns*** Populate Cells for Future
         * Appointments Collects Appointment ID from Booking class and returns
         * as a Number
         *
         * @author tn
         */
        IDF.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, Number>, ObservableValue<Number>>() {

            @Override
            public ObservableValue<Number> call(CellDataFeatures<Booking, Number> param) {
                IntegerProperty prop = new SimpleIntegerProperty();
                prop.setValue(param.getValue().ID);
                return prop;
            }
        });
        // Collects customer from Booking class and returns as a String
        customerF.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Booking, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Booking, String> param) {
                        StringProperty prop = new SimpleStringProperty();
                        prop.setValue(param.getValue().getCustomer());
                        return prop;
                    }
                });
        // Collects date from Booking class and returns as a String
        dateF.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Booking, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Booking, String> param) {
                        StringProperty prop = new SimpleStringProperty();
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String strProp = dateFormatter.format(param.getValue().getDate());
                        prop.setValue(strProp);
                        return prop;
                    }
                });
        // Collects time from Booking class and returns as a String
        timeF.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Booking, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Booking, String> param) {
                        StringProperty prop = new SimpleStringProperty();
                        DateTimeFormatter tToStr = DateTimeFormatter.ofPattern("HH:mm:ss");
                        String tProp = tToStr.format(param.getValue().getStart());
                        prop.setValue(tProp);
                        return prop;
                    }
                });
        // Collects employeeID from Booking class and returns as a Number
        employeeIDF.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Booking, Number>, ObservableValue<Number>>() {

                    @Override
                    public ObservableValue<Number> call(CellDataFeatures<Booking, Number> param) {
                        IntegerProperty prop = new SimpleIntegerProperty();
                        prop.setValue(param.getValue().getEmployeeID());
                        return prop;
                    }
                });

        /**
         * TN instantiates all Booking class objects to populate cells -
         *
         * includes past and future bookings
         * 
         * @author tn
         */
        bookingP.getItems().setAll(c.getPastBookings());
        bookingF.getItems().setAll(c.getFutureBookings());
    }

}
