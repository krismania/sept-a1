package gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public final class GUIUtil {
    private GUIUtil() {
    };

    /**
     * Implements Generic helper method for switching between scenes
     *
     * @author krismania
     * @param fxmlName
     *            filename of the FXML document, without the extension (e.g.
     *            {@code "Login"} to use {@code Login.fxml}
     * @param root
     *            The current root element
     */
    public static void switchTo(String fxmlName, Node root) {
        try {
            // load the scene
            Scene newScene = new Scene(FXMLLoader.load(GUIUtil.class.getResource(fxmlName + ".fxml")));

            // get current stage
            Stage stage = (Stage) root.getScene().getWindow();

            // switch scenes
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implements Generic helper method for popup alert messages
     *
     * @author tn
     */
    public static void infoBox(String infoMessage, String titleBar) {
        // TN - Info Dialogue
        infoBox(infoMessage, titleBar, null);
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}
