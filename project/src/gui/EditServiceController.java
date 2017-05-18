package gui;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.model.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Controller;

/**
 * Screen for viewing, editing and deleting services as a business owner.
 * @author krismania
 */
public class EditServiceController implements Initializable
{
	private Controller c = Controller.getInstance();
	
	@FXML Node root;
	@FXML Label lblError;
	
	@FXML ChoiceBox<Service> cbServices;
	@FXML TextField tfName;
	@FXML TextField tfDuration;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// restrict duration to only integers
		tfDuration.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*"))
			{
				tfDuration.setText(oldValue);
			}
		});
		
		loadServices();
	}
	
	@FXML
	public void handleServiceSelect()
	{
		loadServiceDeatil();
		// enable editing
		tfName.setDisable(false);
		tfDuration.setDisable(false);
	}
	
	@FXML
	public void handleSave()
	{
		// store index of current item for later
		int selectedIndex = cbServices.getSelectionModel().getSelectedIndex();
		
		// update the local object
		Service s = cbServices.getValue();
		
		try {
			s.setName(tfName.getText());
			s.setDuration(Duration.ofMinutes(Integer.parseInt(tfDuration.getText())));
		}
		catch (NumberFormatException e)
		{
			lblError.setText("Please enter a whole number duration.");
			lblError.setVisible(true);
			return;
			
		}
		
		// send the update to the controller
		if (c.updateService(s))
		{
			lblError.setVisible(false);
			
			// refresh the list
			loadServices();
			
			// select the updated service
			cbServices.getSelectionModel().select(selectedIndex);
		}
		else
		{
			// there was an error
		}
	}
	
	@FXML
	public void handleNew()
	{	
		c.addNewService();
		
		// refresh the service dropdown
		loadServices();
		
		// select the new service (should be last in the list)
		cbServices.getSelectionModel().selectLast();
		
		// highlight the name field
		tfName.requestFocus();
	}
	
	@FXML
	public void handleDelete()
	{
		Service s = cbServices.getValue();
		
		if (s != null)
		{
			c.deleteService(s);
			loadServices();
		}
	}
	
	@FXML
	public void handleBack()
	{
		switchTo("BOMenu");
	}
	
	/**
	 * Gets list of services from the DB, and updates the combo box with them.
	 * @author krismania
	 */
	private void loadServices()
	{
		// clear the list first
		cbServices.getItems().clear();
		
		ArrayList<Service> services = c.getServices();
		cbServices.getItems().addAll(services);
	}
	
	/**
	 * Loads the selected service's details into the other form fields.
	 * @author krismania
	 */
	private void loadServiceDeatil()
	{
		Service s = cbServices.getValue();
		
		if (s != null)
		{
			tfName.setText(s.getName());
			tfDuration.setText(Long.toString(s.getDuration().toMinutes()));
		}
		else
		{
			// if nothing selected, empty fields & disable input.
			tfName.setDisable(true);
			tfName.clear();
			
			tfDuration.setDisable(true);
			tfDuration.clear();
		}
	}
	
	// TODO: refactor this
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
}
