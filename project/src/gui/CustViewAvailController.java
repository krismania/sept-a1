/*
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
/**
 * Implements a Form for viewing appointment sessions available to customers
 * 
 * @author tn
 */
public class CustViewAvailController implements Initializable {

    @FXML
    private TextField sunday;

    @FXML
    private TextField saturday;

    @FXML
    private TextField wednesday;

    @FXML
    private TextField thursday;

    @FXML
    private TextField friday;

    @FXML
    private TextField Tuesday;

    @FXML
    private TextField monday;

    @FXML
    private Button navMenu;
    
    @FXML
    void outputAvail(ActionEvent event) {

    }
    //Implements back button to navigate back to Customer Menu
    @FXML
    private void navMenuButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) navMenu.getScene().getWindow();
		// load the scene
		Scene boMenu = new Scene(FXMLLoader.load(getClass().getResource("CustMenu.fxml")));
		
		// switch scenes
		stage.setScene(boMenu);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }       
}
