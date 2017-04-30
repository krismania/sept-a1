package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
//Helper class which constructs a GUI Alert Box allowing for single lines of code
//to deliver popup alerts and responses as feedback
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