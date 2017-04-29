/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIControl;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Controller;
import main.DBInterface;
import main.Employee;
import main.ShiftTime;
import main.Booking;

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUIBOViewBookingSumController implements Initializable {
	
    private Controller c = Controller.getInstance();
	
    @FXML 
    private AnchorPane root;
	
    @FXML
    private TableView<Booking> booking;

    @FXML
    private TableColumn<Booking, Number> ID;

    @FXML
    private TableColumn<Booking, String> customer;
    
    @FXML
    private TableColumn<Booking, String> date; 

    @FXML
    private TableColumn<Booking, String> time;

    @FXML
    private TableColumn<Booking, Number> employeeID;
	
    @FXML
    private Button navMenu;
	
    @FXML
    private void navMenuButtonAction(ActionEvent event) throws IOException {
    	//Stage stage = (Stage) navMenu.getScene().getWindow();
        // load the scene
        //Scene boMenu = new Scene(FXMLLoader.load(getClass().getResource("GUIBOMenu")));
        // switch scenes
        //stage.setScene(boMenu);
        switchTo("GUIBOMenu");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TN - Populate Appointments
    	//Collects ID from Booking class and returns as a Number
        ID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, Number>, 
    			ObservableValue<Number>>() {

            @Override
            public ObservableValue<Number> call(CellDataFeatures<Booking, Number> param) {
                IntegerProperty prop = new SimpleIntegerProperty();
                prop.setValue(param.getValue().ID);
                return prop;
            }
    	});
    	//Collects customer from Booking class and returns as a String
        customer.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, String>, 
        		ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Booking, String> param)
            {
                StringProperty prop = new SimpleStringProperty();
                prop.setValue(param.getValue().getCustomer());
                return prop;
            }	
        });
        //Collects date from Booking class and returns as a String
        date.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, String>, 
        		ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Booking, String> param)
            {	
                StringProperty prop = new SimpleStringProperty();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String strProp = dateFormatter.format(param.getValue().getDate());
                prop.setValue(strProp);
                return prop;
            }	
        });
        //Collects time from Booking class and returns as a String
        time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, String>, 
        		ObservableValue<String>>() {

             @Override
             public ObservableValue<String> call(CellDataFeatures<Booking, String> param)
             {
                StringProperty prop = new SimpleStringProperty();
                DateTimeFormatter tToStr = DateTimeFormatter.ofPattern("HH:mm:ss");
                String tProp = tToStr.format(param.getValue().getTime());
                prop.setValue(tProp);
                return prop;
            }	
        });
        //Collects employeeID from Booking class and returns as a Number
        employeeID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, Number>, 
        		ObservableValue<Number>>() {

            @Override
            public ObservableValue<Number> call(CellDataFeatures<Booking, Number> param)
            {
                IntegerProperty prop = new SimpleIntegerProperty();
                prop.setValue(param.getValue().getEmployeeID());
                return prop;
            }	
         });
 
        //TN instantiates all Booking class objects
        booking.getItems().setAll(c.getPastBookings());
        System.out.println("Past Bookings display Output of booking.getItems().setAll(c.getPastBookings()) = "
        + booking.getItems().setAll(c.getPastBookings()));
    }    
	//Switches scenes
    private void switchTo(String fxmlName)
    {
        try
        {
            // load the scene
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource(fxmlName + ".fxml")));

            // get current stage
            Stage stage = (Stage) root.getScene().getWindow();
			
            // switch scenes
            stage.setScene(newScene);
        }
        catch (IOException e)
        {
            System.out.println("Could not switch scene.");
            e.printStackTrace();
        }
    }
}

