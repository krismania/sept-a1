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
public class AddTimeAndShift extends Application {
    
	//TN - addTime variables & button
	private TextField tfEmpNum = new TextField();
    private TextField tfDay = new TextField();
    private Button btAddDay = new Button("Add Staff Shift Day");
    
    //TN addShift variables & button
	private TextField tfShift = new TextField();
	private TextField tfShiftConfirmed = new TextField();
    private Button btReserveShift = new Button("Reserve Shift");
    
    //TN - Common variables
    Scene sceneLogin, sceneAddEmp, sceneAddTime, sceneAddShift;
    Stage window;
    
    public void start(Stage primaryStage) 
    {
    	window = primaryStage;	
    	
    	//Layout 1 - TN
    	GridPane gridPane1 = new GridPane();
    	
        gridPane1.setHgap(5);
    	gridPane1.setVgap(5);
    	gridPane1.add(new Label("Enter Employee Number"), 0, 0);
    	gridPane1.add(tfEmpNum, 1, 0);
    	gridPane1.add(new Label("Enter Shift Day: Monday ... Saturday"), 0, 1);
    	gridPane1.add(tfDay, 1, 1);
    	gridPane1.add(new Label("Add Shift Day"), 0, 2);
    	gridPane1.add(btAddDay, 1, 2);
    	
    	gridPane1.setAlignment(Pos.CENTER);
    	tfEmpNum.setAlignment(Pos.BOTTOM_RIGHT);
    	tfDay.setAlignment(Pos.BOTTOM_RIGHT);
    	GridPane.setHalignment(btAddDay, HPos.RIGHT);
      
      //TN - Lambda expression that transitions to second menu - 
      // this is a temporary solution to the problem of transitioning from one JavaFX stage to another. 
      // It's messy but effective enough for demonstration purposes.	
    	btAddDay.setOnAction(e -> {
           	String empNum = tfEmpNum.getText();
            String day = tfDay.getText();
        	String staff = "Enter Shift for Staff #1: " + empNum;
    	    String shift = "Select Shift Morning/Afternoon/Evening";
    	    GridPane gridPane2 = new GridPane();
    	    gridPane2.setHgap(5);
    	    gridPane2.setVgap(5);
    	    gridPane2.add(new Label(staff), 0, 0);
    	    gridPane2.add(new Label(shift), 0, 1);
    	    gridPane2.add(tfShift, 1, 1);
    	    gridPane2.add(new Label("Confirm Shift"), 0, 2);
    	    gridPane2.add(btReserveShift, 1, 2);
    	    gridPane2.add(new Label("Availability Confirmed"), 0, 3);
    	    gridPane2.add(tfShiftConfirmed, 1, 3);
    	
    	    gridPane2.setAlignment(Pos.CENTER);
    	    tfEmpNum.setAlignment(Pos.BOTTOM_RIGHT);
    	    tfShift.setAlignment(Pos.BOTTOM_RIGHT);
    	    tfDay.setAlignment(Pos.BOTTOM_RIGHT);
    	    tfShiftConfirmed.setAlignment(Pos.BOTTOM_RIGHT);
    	    tfShiftConfirmed.setEditable(false);
    	    tfShiftConfirmed.setPrefWidth(250);
    	    GridPane.setHalignment(btReserveShift, HPos.RIGHT);
    	    
          //TN - nested Lambda expression to reacton to second transiiton button press.
          btReserveShift.setOnAction(e1 -> {
    	      String shiftBracket = tfShift.getText();
    	      tfShiftConfirmed.setText(String.format("Staff - #" + empNum + ": Shift - " 
    	    	+ day + ", " + shiftBracket));
	  });

    	    Scene sceneAddShift = new Scene(gridPane2, 600, 250);
        	window.setTitle("Confirm Shift");
        	window.setScene(sceneAddShift);
        	window.show();
    	});
    	
        //TN - Calls initial scene
    	sceneAddTime = new Scene(gridPane1, 500, 250);
    	window.setTitle("Add Staff Availability");
    	window.setScene(sceneAddTime);
    	window.show(); 
    }   
    
}
