package GUIControl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import main.Controller;
import main.Employee;

public class ViewEmployeeController implements Initializable
{
	private Controller c = Controller.getInstance();
	
	@FXML private BorderPane root;
	
	@FXML private TableView<Employee> employeeTable;
	@FXML private TableColumn<Employee, String> employeeID;
	@FXML private TableColumn<Employee, String> employeeName;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		employeeID.setCellValueFactory(new PropertyValueFactory<Employee, String>("id"));
		employeeName.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
		
		employeeTable.getItems().setAll(c.getAllEmployees());
	}
	
	
}
