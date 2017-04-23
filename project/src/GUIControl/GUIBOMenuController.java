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
public class GUIBOMenuController implements Initializable {

    @FXML
    private Button addEmp;

    @FXML
    private Button addTime;

    @FXML
    private Button viewBooking;
    
    @FXML
    private Button exit;
    

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent rootAddEmp, rootAddTime, rootViewBooking;
        if(event.getSource()==addEmp) {
        	//TN - get reference button stage
        	stage=(Stage) addEmp.getScene().getWindow();
        	//TN - load other scene
        	rootAddEmp = FXMLLoader.load(getClass().getResource("GUIAddEmployee.fxml"));
            Scene scene = new Scene(rootAddEmp);
            stage.setScene(scene);
            stage.show();
        }
        else if(event.getSource()==addTime) {
        	stage=(Stage) addTime.getScene().getWindow();
        	rootAddTime = FXMLLoader.load(getClass().getResource("GUIAddTime.fxml"));
            Scene scene = new Scene(rootAddTime);
            stage.setScene(scene);
            stage.show();
        }
        else //if(event.getSource()==viewBooking)
        {
        	stage=(Stage) viewBooking.getScene().getWindow();
        	rootViewBooking = FXMLLoader.load(getClass().getResource("GUIBOViewBookingSum.fxml"));
            Scene scene = new Scene(rootViewBooking);
            stage.setScene(scene);
            stage.show();
        }
   
        //TN - call a new scene instance

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
