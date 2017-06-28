/*
 *
 */
package gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import main.Controller;

/**
 * Implements Business Owner Main Menu options selections
 *
 * @author tn
 */
public class BOMenuController
{

    private Controller c = Controller.getInstance();

    @FXML private Node root;

    @FXML
    public void handleViewEmployees()
    {
        GUIUtil.switchTo("ViewEmployee", root);
    }

    @FXML
    public void handleAddShiftTime()
    {
        GUIUtil.switchTo("AddTime", root);
    }

    @FXML
    public void handleEditBusinessHours()
    {
        GUIUtil.switchTo("EditHours", root);
    }

    @FXML
    public void handleEditServices()
    {
        GUIUtil.switchTo("EditService", root);
    }

    @FXML
    public void handleViewBooking()
    {
        GUIUtil.switchTo("BOViewBookingSum", root);
    }

    @FXML
    public void handleMakeBooking()
    {
        GUIUtil.switchTo("Booking", root);
    }

    /**
     * Implements logout button redirecting to login scene
     *
     * @author tn
     * @author krismania
     */
    @FXML
    public void handleLogout()
    {
        c.logout();
        // reconnect to master DB
        c.loadDatabase("master");
        GUIUtil.switchTo("Login", root);
    }
}
