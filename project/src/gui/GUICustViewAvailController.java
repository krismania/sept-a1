/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * FXML Controller class
 *
 * @author tn
 */
public class GUICustViewAvailController implements Initializable {

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

    @FXML
    private void navMenuButtonAction(ActionEvent event) throws IOException {
    	Stage stage = (Stage) navMenu.getScene().getWindow();
		// load the scene
		Scene boMenu = new Scene(FXMLLoader.load(getClass().getResource("GUICustMenu.fxml")));
		
		// switch scenes
		stage.setScene(boMenu);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }       
}
