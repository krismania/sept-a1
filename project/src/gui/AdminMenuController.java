package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Controller;

/**
 * Implements Admin Main Menu options selections
 *
 * @author tn
 * @author RK
 */
public class AdminMenuController implements Initializable {

    private Controller c = Controller.getInstance();

    @FXML
    private Button createBusiness;

    @FXML
    private Button manageBusiness;

    /**
     * Implements button redirecting to create business scene or manage business
     * 
     * @param event
     * @throws IOException
     * @author tn
     */
    // scene
    @FXML
    public void handleCreateBusinessAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent rootAddBus, rootManBus;
        if (event.getSource() == manageBusiness) {
            stage = (Stage) manageBusiness.getScene().getWindow();
            rootManBus = FXMLLoader.load(getClass().getResource("ManageBusinesses.fxml"));
            Scene scene = new Scene(rootManBus);
            stage.setScene(scene);
            stage.show();
        } else {
            stage = (Stage) createBusiness.getScene().getWindow();
            rootAddBus = FXMLLoader.load(getClass().getResource("CreateBusiness.fxml"));
            Scene scene = new Scene(rootAddBus);
            stage.setScene(scene);
            stage.show();
        }

    }

    /**
     * Implements logout button redirecting to login scene
     *
     * @author tn
     */
    @FXML
    private Button logout;

    @FXML
    private void logoutButtonAction(ActionEvent event) throws IOException {
        c.logout();
        Stage stage = (Stage) logout.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // INIT
    }

}
