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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUIAddTimeController implements Initializable {

    @FXML
    private Button btRecordAvail;

    @FXML
    private Button exit;
 
    @FXML
    private Button menu;
    
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
        if(event.getSource()==btRecordAvail) {
            //TN - Need to impliment capture data and send to method
        	
        	//TN - Directs to confirmation scene
        	stage=(Stage) btRecordAvail.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIAddShift.fxml"));
            
        }
        else
        {
            stage=(Stage) menu.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml"));
        }
    }
	
    @FXML
    //TN - Button to close app.
    private void closeButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
