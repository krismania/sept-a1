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

/**
 * FXML Controller class
 *
 * @author tn
 */
public class GUIBOMenuController implements Initializable {

    @FXML
    private Button addEmp;

    @FXML
    private Button addShift;

    @FXML
    private Button viewBooking;

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        if(event.getSource()==addEmp) {
        	//TN - get reference button stage
        	stage=(Stage) addEmp.getScene().getWindow();
        	//TN - load other scene
        	root = FXMLLoader.load(getClass().getResource("GUIAddEmployee.fxml"));
        }
        else if(event.getSource()==addShift) {
        	stage=(Stage) addShift.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIAddShift.fxml"));
        }
        else
        {
        	stage=(Stage) viewBooking.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("GUIBOViewBooking.fxml"));
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
