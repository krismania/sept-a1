package gui;

import java.net.URL;
import java.util.ResourceBundle;

import database.model.Employee;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import main.Controller;

/**
 * Implements scene for viewing Employee records
 *
 * @author tn
 *
 */
public class ViewEmployeeController implements Initializable {
    private Controller c = Controller.getInstance();

    @FXML
    private BorderPane root;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, Number> employeeID;
    @FXML
    private TableColumn<Employee, String> employeeName;

    @FXML
    private TextField selEmpID;
    @FXML
    private TextField selEmpFirstName;
    @FXML
    private TextField selEmpLastName;
    @FXML
    private TextField selEmpEmail;
    @FXML
    private TextField selEmpPhone;

    /**
     * Initialise scene implementing observer pattern and using Factory pattern
     * to construct Cells
     *
     * @author tn
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // observe changes to selection

        // populate employee list
        employeeID.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Employee, Number>, ObservableValue<Number>>() {

                    @Override
                    public ObservableValue<Number> call(CellDataFeatures<Employee, Number> param) {
                        IntegerProperty prop = new SimpleIntegerProperty();
                        prop.setValue(param.getValue().ID);

                        return prop;
                    }

                });

        employeeName.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Employee, String> param) {
                        StringProperty prop = new SimpleStringProperty();
                        prop.setValue(param.getValue().getFirstName() + " " + param.getValue().getLastName());

                        return prop;
                    }

                });

        employeeTable.getItems().setAll(c.getAllEmployees());
    }

    /**
     * Implements context sensitive details on mouse select action
     * 
     * @param event
     * @author tn
     */
    @FXML
    public void handleChangeEmployee(MouseEvent event) {
        Employee selEmp = employeeTable.getSelectionModel().getSelectedItem();

        // show employee details in details pane
        if (selEmp != null) {
            selEmpID.setText(Integer.toString(selEmp.ID));
            selEmpFirstName.setText(selEmp.getFirstName());
            selEmpLastName.setText(selEmp.getLastName());
            selEmpEmail.setText(selEmp.getEmail());
            selEmpPhone.setText(selEmp.getPhoneNumber());
        }
    }

    /**
     * Implements back button reverting to Business Owner Main menu scene
     * 
     * @param event
     * @author tn
     */
    @FXML
    public void handleBack(ActionEvent event) {
        GUIUtil.switchTo("BOMenu", root);
    }
}
