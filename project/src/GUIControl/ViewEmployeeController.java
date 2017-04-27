package GUIControl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Controller;
import main.Employee;

public class ViewEmployeeController implements Initializable
{
	private Controller c = Controller.getInstance();
	
	@FXML private BorderPane root;
	
	@FXML private TableView<Employee> employeeTable;
	@FXML private TableColumn<Employee, Number> employeeID;
	@FXML private TableColumn<Employee, String> employeeName;

	@FXML private TextField selEmpID;
	@FXML private TextField selEmpFirstName;
	@FXML private TextField selEmpLastName;
	@FXML private TextField selEmpEmail;
	@FXML private TextField selEmpPhone;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// observe changes to selection
		
		// populate employee list
		employeeID.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(
							CellDataFeatures<Employee, Number> param)
			{
				IntegerProperty prop = new SimpleIntegerProperty();
				prop.setValue(param.getValue().ID);
				
				return prop;
			}
			
		});
		
		// employeeID.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("ID"));
		employeeName.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
		
		employeeTable.getItems().setAll(c.getAllEmployees());
	}
	
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
	
	@FXML
	public void handleChangeEmployee(MouseEvent event)
	{
		Employee selEmp = employeeTable.getSelectionModel().getSelectedItem();
		
		// show employee details in details pane
		selEmpID.setText(Integer.toString(selEmp.ID));
		selEmpFirstName.setText(selEmp.getFirstName());
		selEmpLastName.setText(selEmp.getLastName());
		selEmpEmail.setText(selEmp.getEmail());
		selEmpPhone.setText(selEmp.getPhoneNumber());
	}
	
	@FXML
	public void handleBack(ActionEvent event)
	{
		switchTo("GUIBOMenu");
	}
}
