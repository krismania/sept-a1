package display;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.Account;
import main.BusinessOwner;
import main.Controller;
import main.Customer;

public class LoginController implements Initializable
{
	Controller c = Controller.getInstance();
	
	@FXML private HBox root;
	@FXML private TextField tfUsername;
    @FXML private PasswordField tfPassword;
    @FXML private Label lblError;
    
    
	@FXML
	public void handleLogin(ActionEvent event) throws IOException
	{
		// attempt login
		Account account = c.login(tfUsername.getText(), tfPassword.getText());
		
		if (account instanceof Customer)
		{
			lblError.setVisible(false);
			
			// load the scene
			Scene customerLogin = new Scene(FXMLLoader.load(getClass().getResource("CustMenu.fxml")));
			
			// get current stage
			Stage stage = (Stage) root.getScene().getWindow();
			
			// switch scenes
			stage.setScene(customerLogin);
		}
		else if (account instanceof BusinessOwner)
		{
			lblError.setVisible(false);
			
			// load the scene
			Scene boLogin = new Scene(FXMLLoader.load(getClass().getResource("BOMenu.fxml")));
			
			// get current stage
			Stage stage = (Stage) root.getScene().getWindow();
			
			// switch scenes
			stage.setScene(boLogin);
		}
		else
		{
			// if account isn't an instance of either account type, login failed.
			lblError.setVisible(true);
			tfPassword.clear();
		}
	}
	
	@FXML
	public void handleSignup(ActionEvent event) throws IOException
	{
		// load the scene
		Scene signup = new Scene(FXMLLoader.load(getClass().getResource("Signup.fxml")));
		
		// get current stage
		Stage stage = (Stage) root.getScene().getWindow();
		
		// switch scenes
		stage.setScene(signup);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// init
	}
}
