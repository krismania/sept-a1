/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIControl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUILoginPopupController implements Initializable {
    
    @FXML
    private PasswordField password;

    @FXML
    private TextField userName;
    
    @FXML
    private Button signup;
    
    @FXML
    private Button businessOwner;

    @FXML
    private Button login;
    
    @FXML
    private Button exit;

    @FXML
    void registerNewUser(ActionEvent event) {

    }

    @FXML
    void processLogin(ActionEvent event) {

    }
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        if(event.getSource()==login) {
        	//TN - get reference button stage
        	stage=(Stage) login.getScene().getWindow();
        	//TN - load other scene
        	root = FXMLLoader.load(getClass().getResource("GUICustMenu.fxml"));
        }
        else if(event.getSource()==businessOwner)
        {
        	stage=(Stage) businessOwner.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml"));
        }
        else
        {
        	stage=(Stage) signup.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml")); 	
        }
        //TN - call a new scene instance
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
}
