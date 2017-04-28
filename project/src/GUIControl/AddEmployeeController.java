package GUIControl;

import java.io.IOException;
import javafx.stage.Stage;
import main.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author tn, krismania
 */
public class AddEmployeeController
{  
    Controller c = Controller.getInstance();
	
    @FXML private BorderPane root;
    
    @FXML private Label lblError;
    @FXML private TextField tfEmpFName;
    @FXML private TextField tfEmpLName;
    @FXML private TextField tfEmpEmailAdd;
    @FXML private TextField tfEmpPhNum;
    @FXML private Button submitNewEmpData;
    @FXML private Button navMenu;
    @FXML private Button exit;
    
    /**
     * Switches to a specified scene
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
			System.out.println("Could not switch scene.");
			e.printStackTrace();
		}
	}

    @FXML
    public void handleAddEmployee(ActionEvent event)
    {
    	if (validateEmployee())
    	{
    		// try to add the employee
    		if (c.addEmployee(tfEmpFName.getText(), tfEmpLName.getText(), tfEmpEmailAdd.getText(), tfEmpPhNum.getText()))
    		{
    			// employee added successfully
    			//TN -  If validation is successful a confirmation popup is presented.
                lblError.setVisible(false);
                GUIAlert.infoBox("New Employee Successfully Added", "Add Employee Confirmation");
                
                //TN - Fields are cleared following correct input.
                tfEmpFName.clear();
                tfEmpLName.clear(); 
                tfEmpEmailAdd.clear();
                tfEmpPhNum.clear();
                
                // set focus to the first field. -kg
                tfEmpFName.requestFocus();
    		}
    		else
    		{
    			setError("Couldn't add an employee.");
    		}
    	}
    }
    
    @FXML
    public void handleBack(ActionEvent event)
    {
    	switchTo("GUIBOMenu");
    }
    
    /**
     * @author krismania
     */
    private void setError(String message)
    {
    	lblError.setText(message);
    	lblError.setVisible(true);
    }
    
    /**
     * Returns true if all fields are valid, otherwise returns false and displays
     * a message in the GUI.
     * TODO: this looks kind of gross, fix it
     * @author krismania
     */
    private boolean validateEmployee()
    {
    	if (c.validateName(tfEmpFName.getText()))
    	{
    		if (c.validateName(tfEmpLName.getText()))
    		{
    			if (c.validateEmail(tfEmpEmailAdd.getText()))
    			{
    				if (c.validatePhoneNumber(tfEmpPhNum.getText()))
    				{
    					lblError.setVisible(false);
    					return true;
    				}
    				else
    				{
    					setError("Please enter a valid phone number");
    					// TODO: this message is too long
    				}
    			}
    			else
    			{
    				setError("Please enter a valid email");
    			}
    		}
    		else
    		{
    			setError("Please enter a last name");
    		}
    	}
    	else
    	{
    		setError("Please enter a first name");
    	}
    	return false;
    }
}
