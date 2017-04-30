package display;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Controller;
import model.Booking;
import model.Employee;

/**
 * Customer screen to view their upcoming bookings
 * @author krismania
 */
public class UpcomingAppointmentsSummary implements Initializable
{
    private Controller c = Controller.getInstance();
	
    @FXML 
    private Node root;
	
    @FXML 
    private TableView<Booking> appointmentsTable;
  
    @FXML 
    private TableColumn<Booking, String> date;
	
    @FXML 
    private TableColumn<Booking, String> time;
	
    @FXML 
    private TableColumn<Booking, String> employee;
	
    //TN - Implements Factory Pattern to build cell content
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // set cell value factories
        date.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, String>, 
        		ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Booking, String> param)
            {
                SimpleStringProperty prop = new SimpleStringProperty();
                prop.setValue(param.getValue().getDate().format(DateTimeFormatter.
                		ofPattern("EEEE dd/MM/yyyy")));
                return prop;
            }
        });
		
        time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, String>, 
        		ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Booking, String> param)
            {
                SimpleStringProperty prop = new SimpleStringProperty();
                prop.setValue(param.getValue().getTime().format(DateTimeFormatter.
						ofPattern("hh:mm a")));
                return prop;
            }
        });
		
        employee.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Booking, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Booking, String> param)
            {
                // get the employee object by their ID in booking
                Employee employee = c.getEmployee(param.getValue().getEmployeeID());
				
                SimpleStringProperty prop = new SimpleStringProperty();
                prop.setValue(employee.getFirstName() + " " + employee.getLastName());
                return prop;
            }
        });
        // get the booking objects
        appointmentsTable.getItems().setAll(c.getFutureBookings(c.getLoggedUser().username));
    }
	//TN - Switches scene to Customer main menu
    @FXML
    public void handleBack(ActionEvent event)
    {
        switchTo("CustMenu");
    }
	
	/**
	 * @author krismania
	 * Helper method for scene switching
	 */
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
