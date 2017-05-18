package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Service;

public class EditServiceController implements Initializable
{
	@FXML Node root;
	@FXML ChoiceBox<Service> cbServices;
	@FXML TextField tfName;
	@FXML ChoiceBox<String> cbDuration;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	public void handleSave()
	{
		
	}
	
	@FXML
	public void handleNew()
	{
		
	}
	
	@FXML
	public void handleDelete()
	{
		
	}
	
	@FXML
	public void handleBack()
	{
		switchTo("BOMenu");
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
