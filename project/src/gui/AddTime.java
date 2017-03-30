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
    private TextField tfDay = new TextField();
    private Button btAddDay = new Button("Add Staff Shift Day");
    
    public void start(Stage primaryStage) 
    {
    	GridPane gridPane = new GridPane();
        
        /*Scene sceneLogin = new Scene(gridPane);
        Scene sceneAddEmp = new Scene(gridPane);
        Scene sceneAddTime = new Scene(gridPane);
        Scene sceneAddShift = new Scene(gridPane);
        Stage window = primaryStage;*/
    	
        
        gridPane.setHgap(5);
    	gridPane.setVgap(5);
    	gridPane.add(new Label("Enter Employee Number"), 0, 0);
    	gridPane.add(tfEmpNum, 1, 0);
    	gridPane.add(new Label("Enter Shift Day: Monday ... Saturday"), 0, 1);
    	gridPane.add(tfDay, 1, 1);
    	gridPane.add(new Label("Add Shift Day"), 0, 2);
    	gridPane.add(btAddDay, 1, 2);
    	
    	gridPane.setAlignment(Pos.CENTER);
    	tfEmpNum.setAlignment(Pos.BOTTOM_RIGHT);
    	tfDay.setAlignment(Pos.BOTTOM_RIGHT);
    	GridPane.setHalignment(btAddDay, HPos.RIGHT);
    	btAddDay.setOnAction(e -> addTime());
    	
    	Scene scene = new Scene(gridPane, 500, 250);
    	primaryStage.setTitle("Add Staff Availability");
    	primaryStage.setScene(scene);
    	primaryStage.show();   	
    }
    private void addTime() {  
    		
      	String empNum = tfEmpNum.getText();
    	String Day = tfDay.getText();
    }
}
