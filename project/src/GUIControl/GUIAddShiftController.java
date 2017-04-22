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



public class GUIAddShiftController implements Initializable {
	
	@FXML
	private Button exit;

	@FXML
	private Button menu;
    
	@FXML
	void handleButtonAction(ActionEvent event) throws IOException{
    Stage stage;
    Parent root;		
    stage=(Stage) menu.getScene().getWindow();
    root = FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml"));
    
	}
	@FXML
    private void closeButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
