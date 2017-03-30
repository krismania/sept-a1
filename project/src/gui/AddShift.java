package gui;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Scanner;

import console.Console;
import console.Menu;

//TN - Class to build basic GUI elements for reserving Shifts - such as text fields
// and field position. Includes confirmation
// To call from another class use the following -
// Application.launch(AddShift.class, args); 
public class AddShift extends Application {
	private TextField tfEmpNum = new TextField();
	private TextField tfShift = new TextField();
	private TextField tfDay = new TextField();
    	private TextField tfShiftConfirmed = new TextField();
    	private Button btReserveShift = new Button("Reserve Shift");
    public void start(Stage primaryStage) 
    {
    	
    	String staff = "Enter Shift for Staff #1: Tim Novice";
    	String shift = "Select Shift Morning/Afternoon/Evening";
    	
    	GridPane gridPane = new GridPane();
    	gridPane.setHgap(5);
    	gridPane.setVgap(5);
    	gridPane.add(new Label(staff), 0, 0);
    	gridPane.add(new Label(shift), 0, 1);
    	gridPane.add(tfShift, 1, 1);
    	gridPane.add(new Label("Confirm Shift"), 0, 2);
    	gridPane.add(btReserveShift, 1, 2);
    	gridPane.add(new Label("Availability Confirmed"), 0, 3);
    	gridPane.add(tfShiftConfirmed, 1, 3);
    	
    	gridPane.setAlignment(Pos.CENTER);
    	tfEmpNum.setAlignment(Pos.BOTTOM_RIGHT);
    	tfShift.setAlignment(Pos.BOTTOM_RIGHT);
    	tfDay.setAlignment(Pos.BOTTOM_RIGHT);
    	tfShiftConfirmed.setAlignment(Pos.BOTTOM_RIGHT);
    	tfShiftConfirmed.setEditable(false);
    	tfShiftConfirmed.setPrefWidth(250);
    	GridPane.setHalignment(btReserveShift, HPos.RIGHT);
    	btReserveShift.setOnAction(e -> displayAvailability());

    	Scene scene = new Scene(gridPane, 600, 250);
    	primaryStage.setTitle("Confirm Shift");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	}

    	private void displayAvailability() {
    	    
            String empNum = tfEmpNum.getText();
            String shiftBracket = tfShift.getText();
            String day = tfDay.getText();
	    
            fShiftConfirmed.setText(String.format(empNum + ": " + shiftBracket + ". " + day));
    	  
    	}
}
