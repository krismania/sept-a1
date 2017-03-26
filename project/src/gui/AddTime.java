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

//TN - Class to build basic GUI elements for login - such as text fields
// and field position. Includes login button
// To call from another class use the following -
// Application.launch(AddEmployee.class, args); 
public class AddTime extends Application {
	    private TextField tfEmpNum = new TextField();
	    private TextField tfTime = new TextField();
	    private TextField tfDay = new TextField();
    	private TextField tfMonth = new TextField();
    	private TextField tfYear = new TextField();
    	private TextField tfDate = new TextField();
    	private Button btGenerateDate = new Button("Set Available Date");
    public void start(Stage primaryStage) 
    {
    	
    	GridPane gridPane = new GridPane();
    	gridPane.setHgap(5);
    	gridPane.setVgap(5);
    	gridPane.add(new Label("Enter the Employee Number"), 0, 0);
    	gridPane.add(tfEmpNum, 1, 0);
    	gridPane.add(new Label("Enter Availability as 2hr blocks"), 0, 1);
    	gridPane.add(tfTime, 1, 1);
    	gridPane.add(new Label("Enter day of the Month"), 0, 2);
    	gridPane.add(tfDay, 1, 2);
    	gridPane.add(new Label("Enter available Month"), 0, 3);
    	gridPane.add(tfMonth, 1, 3);
    	gridPane.add(new Label("Add Availability"), 0, 5);
    	gridPane.add(btGenerateDate, 1, 5);
    	gridPane.add(new Label("Availability Confirmed"), 0, 6);
    	gridPane.add(tfDate, 1, 6);
    	
    	gridPane.setAlignment(Pos.CENTER);
    	tfEmpNum.setAlignment(Pos.BOTTOM_RIGHT);
    	tfTime.setAlignment(Pos.BOTTOM_RIGHT);
    	tfDay.setAlignment(Pos.BOTTOM_RIGHT);
    	tfMonth.setAlignment(Pos.BOTTOM_RIGHT);
    	tfDate.setAlignment(Pos.BOTTOM_RIGHT);
    	tfDate.setEditable(false);
    	GridPane.setHalignment(btGenerateDate, HPos.RIGHT);
    	btGenerateDate.setOnAction(e -> displayAvailability());

    	Scene scene = new Scene(gridPane, 400, 250);
    	primaryStage.setTitle("Add Booking Availability Time");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	}

    	private void displayAvailability() {

    	    
    	    tfDate.setText(String.format("2PM-4PM: 31/03/2017"));
    	}
}