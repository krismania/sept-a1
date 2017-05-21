package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Controller;

/**
 * Displays registered Businesses details for selection.
 * @author tim
 */
public class ManageBusinessesController implements Initializable
{
	private Controller c = Controller.getInstance();
	@FXML private BorderPane root;
	@FXML private Button select;

	@FXML private TableView<String> businessesTable;
	@FXML private TableColumn<String, String> businessName;

	//Initialise data to populate fields
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{

		// set cell value factories
		businessName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(
					CellDataFeatures<String, String> param)
			{
				SimpleStringProperty prop = new SimpleStringProperty();
				prop.setValue(param.getValue());
				return prop;
			}
		});

		// get the booking objects
		businessesTable.getItems().setAll(c.getAllBusinessOwners());
	}

	/**
	 * Switches to a specified scene
	 * Implements Generic helper method for switching between scenes
	 * @author krismania
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
			e.printStackTrace();
		}
	}

	//Implements back button reverting to Customer Main menu scene
	@FXML
	public void handleBack(ActionEvent event)
	{
		switchTo("AdminMenu");
	}
	
	@FXML
	public void handleSelect(ActionEvent event)
	{
		String selected = businessesTable.getSelectionModel().getSelectedItem();
		c.disconnectDB();
		c.loadDatabase(selected);
		switchTo("EditBusiness");
	}

}
