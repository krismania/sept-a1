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
import javafx.scene.control.ChoiceBox;

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUIAddTimeController implements Initializable {


    @FXML
    private Button exit;

    @FXML
    private Button menu;

    @FXML
    private Button btRecordAvail;
    
    @FXML
    private ChoiceBox<String> shiftDropdown;
    
    @FXML
    private ChoiceBox<String> durationDropdown;

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {

    }
	
	//TN old code
	   
  /*  @FXML
    private void handleButtonAction(ActionEvent event) throws IOException{
        Stage stage;
        Parent root;
        if(event.getSource()==btRecordAvail) {
            //TN - Need to implement capture data and send to method
        	
            //TN - Directs to confirmation scene
            stage=(Stage) btRecordAvail.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIAddShift.fxml"));
            
        }
        else
        {
            stage=(Stage) menu.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIBOMenu.fxml"));
        } 
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } */
	
    @FXML
    //TN - Button to close app.
    private void closeButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TN - initialise shift time slot dropdown menus
    	
    	shiftDropdown.getItems().removeAll(shiftDropdown.getItems());
        shiftDropdown.getItems().addAll("9am", "9:30am", "10am", "10:30am", 
        		"11am", "12pm", "12:30pm", "1pm", "1:30pm", "2pm", "2:30pm",
        		"3pm", "3:30pm", "4pm", "4:30pm", "5pm");
        shiftDropdown.getSelectionModel().select("9am");
        //TN - initialise shift duration dropdown menus
        durationDropdown.getItems().removeAll(durationDropdown.getItems());
        durationDropdown.getItems().addAll("30 minutes", "1 hour", 
        		"1 hour 30 minutes", "2 hours");
        durationDropdown.getSelectionModel().select("30 minutes");
    }
}
