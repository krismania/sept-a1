package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import database.model.BusinessOwner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Controller;

/**
 * Screen for viewing, editing and deleting services as a business owner.
 * @author krismania
 */
public class EditBusinessController implements Initializable
{
    private Controller c = Controller.getInstance();

    @FXML Node root;
    @FXML Label lblError;

    @FXML TextField tfAddress;
    @FXML TextField tfOwnerUsername;
    @FXML TextField tfOwnerNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        BusinessOwner owner = c.getBusinessOwner();
        tfOwnerUsername.setText(owner.username);
        tfAddress.setText(owner.getAddress());
        tfOwnerNumber.setText(owner.getPhoneNumber());
    }

    // TODO: refactor this
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
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack()
    {
        c.disconnectDB();
        c.loadDatabase("master");
        switchTo("ManageBusinesses");
    }

    @FXML
    public void handleDelete()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("WARNING");
        alert.setHeaderText("Delete Business");
        alert.setContentText("Warning! You are about to delete a business. This action cannot be undone. All business data will"
                + "be destroyed.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            c.removeBusiness();
            File db = new File(c.getCurrentBusiness() + ".db");
            if(db.exists())
            {
                db.delete();
                switchTo("ManageBusinesses");
            }
        }
    }
}
