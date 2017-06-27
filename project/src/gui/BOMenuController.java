/*
 *
 */
package gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

    @FXML private Button addEmp;
    @FXML private Button viewEmployee;
    @FXML private Button addTime;
    @FXML private Button editHours;
    @FXML private Button editService;
    @FXML private Button viewBooking;
    @FXML private Button makeBooking;
    @FXML private Button exit;
    @FXML private Button logout;


    /**
     * @author krismania
     */
    @FXML
    public void handleAddEmployee()
    {
        GUIUtil.switchTo("AddEmployee", root);
    }

    /**
     * @author krismania
     */
    @FXML
    public void handleViewEmployees()
    {
        GUIUtil.switchTo("ViewEmployee", root);
    }

    /**
     * @author krismania
     */
    @FXML
    public void handleAddShiftTime()
    {
        GUIUtil.switchTo("AddTime", root);
    }

    /**
     * @author krismania
     */
    @FXML
    public void handleEditBusinessHours()
    {
        GUIUtil.switchTo("EditHours", root);
    }

    /**
     * @author krismania
     */
    @FXML
    public void handleEditServices()
    {
        GUIUtil.switchTo("EditService", root);
    }

    /**
     * @author krismania
     */
    @FXML
    public void handleViewBooking()
    {
        GUIUtil.switchTo("BOViewBookingSum", root);
    }

    /**
     * @author krismania
     */
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
        GUIUtil.switchTo("Login", logout);
    }
}
