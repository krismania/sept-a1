package gui;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ResourceBundle;

import database.model.TimeSpan;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import main.Controller;

/**
 * View for business owners to edit the businesses hours by day of week
 *
 * @author krismania
 */
public class EditHoursController implements Initializable {
    private Controller c = Controller.getInstance();

    @FXML
    private Node root;

    @FXML
    private ChoiceBox<String> openH;
    @FXML
    private ChoiceBox<String> openM;
    @FXML
    private ChoiceBox<String> closeH;
    @FXML
    private ChoiceBox<String> closeM;

    @FXML
    private ChoiceBox<String> day;
    @FXML
    private ChoiceBox<String> opened;

    /**
     * Initializes scene
     *
     * @author krismania
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // fill days
        String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
        day.getItems().addAll(days);

        // populate open/closed choicebox
        opened.getItems().add("Open");
        opened.getItems().add("Closed");

        // populate time choiceboxes
        for (int hour = 0; hour < 24; hour++) {
            String hourString = String.format("%02d", hour);
            openH.getItems().add(hourString);
            closeH.getItems().add(hourString);
        }

        for (int minute = 0; minute < 60; minute++) {
            String minuteString = String.format("%02d", minute);
            openM.getItems().add(minuteString);
            closeM.getItems().add(minuteString);
        }

        // watch for changes to day choicebox
        day.valueProperty().addListener((observable, oldValue, newValue) -> {
            loadHours(newValue);
        });

        // watch for changes to opened choicebox
        opened.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Open")) {
                openH.setDisable(false);
                openM.setDisable(false);
                closeH.setDisable(false);
                closeM.setDisable(false);
                openH.getSelectionModel().select("09");
                openM.getSelectionModel().select("00");
                closeH.getSelectionModel().select("17");
                closeM.getSelectionModel().select("00");
            } else {
                openH.setDisable(true);
                openM.setDisable(true);
                closeH.setDisable(true);
                closeM.setDisable(true);
                openH.getSelectionModel().clearSelection();
                openM.getSelectionModel().clearSelection();
                closeH.getSelectionModel().clearSelection();
                closeM.getSelectionModel().clearSelection();
            }
        });

        // set default day
        day.getSelectionModel().selectFirst();
    }

    /**
     * Switches scene
     *
     * @author krismania
     */
    @FXML
    public void handleBack() {
        GUIUtil.switchTo("BOMenu", root);
    }

    @FXML
    public void handleSave() {
        TimeSpan hours = getHours();
        if (c.setHours(DayOfWeek.valueOf(day.getValue().toUpperCase()), hours)) {
            GUIUtil.infoBox("Save Successful", "Save");
        } else {
            GUIUtil.infoBox("Something went wrong!", "Save");
        }
    }

    /**
     * Manages save button
     *
     * @author krismania
     */
    @FXML
    public void handleSaveAll() {
        TimeSpan hours = getHours();
        if (c.setAllHours(hours)) {
            GUIUtil.infoBox("Save Successful", "Save");
        } else {
            GUIUtil.infoBox("Something went wrong!", "Save");
        }
    }

    /**
     * Loads chosen hours
     *
     * @author krismania
     */
    private void loadHours(String day) {
        TimeSpan hours = c.getHours(DayOfWeek.valueOf(day.toUpperCase()));

        if (hours == null) {
            // if hours is null, the store is closed on that day
            opened.getSelectionModel().select("Closed");
        } else {
            // else, set times appropriately
            opened.getSelectionModel().select("Open");

            openH.getSelectionModel().select(hours.start.getHour());
            openM.getSelectionModel().select(hours.start.getMinute());
            closeH.getSelectionModel().select(hours.end.getHour());
            closeM.getSelectionModel().select(hours.end.getMinute());
        }
    }

    /**
     * Returns a {@link TimeSpan} containing the currently input hours
     *
     * @author krismania
     */
    private TimeSpan getHours() {
        TimeSpan hours = null;

        if (opened.getValue().equals("Open")) {
            LocalTime open = LocalTime.of(Integer.parseInt(openH.getValue()), Integer.parseInt(openM.getValue()));
            LocalTime close = LocalTime.of(Integer.parseInt(closeH.getValue()), Integer.parseInt(closeM.getValue()));

            hours = new TimeSpan(open, close);
        }

        return hours;
    }
}
