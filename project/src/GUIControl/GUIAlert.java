package GUIControl;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class GUIAlert
{

    public static void infoBox(String infoMessage, String titleBar)
    {
        //TN - Info Dialogue 
        infoBox(infoMessage, titleBar, null);
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}