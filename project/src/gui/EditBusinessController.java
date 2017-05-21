package gui;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.model.BusinessOwner;
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
public class EditBusinessController implements Initializable
{
	private Controller c = Controller.getInstance();
	
	@FXML Node root;
	@FXML Label lblError;
	
	@FXML TextField tfAddress;
	@FXML TextField tfOwnerUsername;
	@FXML TextField tfOwnerNumber;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		BusinessOwner owner = c.getBusinessOwner();
		tfOwnerUsername.setText(owner.username);
		tfAddress.setText(owner.getAddress());
		tfOwnerNumber.setText(owner.getPhoneNumber());
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
	
	@FXML
	public void handleBack()
	{
		c.disconnectDB();
		c.loadDatabase("master");
		switchTo("ManageBusinesses");
	}
}
