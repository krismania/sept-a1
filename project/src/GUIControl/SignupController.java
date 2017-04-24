package GUIControl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Account;
import main.BusinessOwner;
import main.Controller;
import main.Customer;

public class SignupController
{
	private Controller c = Controller.getInstance();
	private String storedUsername;
	private String storedPassword;
	
	@FXML private VBox root;
	@FXML private TextField username;
	@FXML private PasswordField password;
	@FXML private PasswordField passwordConf;
	
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField email;
	@FXML private TextField phone;
	
	@FXML private Label lblError;
	
	@FXML
	public void handleNext(ActionEvent event) throws IOException
	{
		// check the username and password
		if (c.validateUserName(username.getText()))
		{
			if (Account.passwordAccepted(password.getText()))
			{
				// check that confirm password matches
				if (password.getText().equals(passwordConf.getText()))
				{
					// store customer username/password
					storedUsername = username.getText();
					storedPassword = password.getText();
					
					lblError.setVisible(false);
					// username and password are accepted, transition to details
					Scene details = new Scene(FXMLLoader.load(getClass().getResource("Signup-2.fxml")));
					Stage stage = (Stage) root.getScene().getWindow();
					stage.setScene(details);
				}
				else
				{
					lblError.setText("Passwords don't match.");
					lblError.setVisible(true);

					passwordConf.clear();
					passwordConf.requestFocus();
				}
			}
			else
			{
				// invalid password
				// TODO: more detailed error
				lblError.setText("Invalid Password.");
				lblError.setVisible(true);
				
				password.clear();
				password.requestFocus();
			}
		}
		else
		{
			// invalid username
			lblError.setText("Invalid Username.");
			lblError.setVisible(true);
			
			username.requestFocus();
		}
	}
	
	@FXML
	public void handleCancel(ActionEvent event) throws IOException
	{
		// load the scene
		Scene login = new Scene(FXMLLoader.load(getClass().getResource("Login.fxml")));
		
		// get current stage
		Stage stage = (Stage) root.getScene().getWindow();
		
		// switch scenes
		stage.setScene(login);
	}
	
	@FXML
	public void handleBack(ActionEvent event) throws IOException
	{
		Scene signup = new Scene(FXMLLoader.load(getClass().getResource("Signup.fxml")));
		Stage stage = (Stage) root.getScene().getWindow();
		stage.setScene(signup);
	}
	
	@FXML
	public void handleSignUp(ActionEvent event) throws IOException
	{
		// TODO: verify details
		
		// try to sign up
		if (c.addCustomer(storedUsername, storedPassword,
						firstName.getText(), lastName.getText(),
						email.getText(), phone.getText()))
		{
			// sign up success
			System.out.println("Sign up success");
		}
		else
		{
			// sign up failure
			System.out.println("Sign up failed");
		}
	}
}
