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
import java.util.HashMap;

import console.Console;
import console.Menu;

//TN - Class to build basic GUI elements for login - such as text fields
// and field position. Includes login button
// To call from another class use the following -
// Application.launch(AddEmployee.class, args); 
public class AddEmployee extends Application {
      	private TextField tfEmpFName = new TextField();
    	private TextField tfEmpLName = new TextField();
    	private TextField tfEmpEmailAdd = new TextField();
    	private TextField tfEmpPhNum = new TextField();
    	private TextField tfEmpNum = new TextField();
    	private Button btGenerateEmp = new Button("Add New Employee");
    public void start(Stage primaryStage) 
    {
    	
    	GridPane gridPane = new GridPane();
    	gridPane.setHgap(5);
    	gridPane.setVgap(5);
    	gridPane.add(new Label("Enter Employees First Name"), 0, 0);
    	gridPane.add(tfEmpFName, 1, 0);
    	gridPane.add(new Label("Enter Employees Last Name"), 0, 1);
    	gridPane.add(tfEmpLName, 1, 1);
    	gridPane.add(new Label("Enter Employees email Address"), 0, 2);
    	gridPane.add(tfEmpEmailAdd, 1, 2);
    	gridPane.add(new Label("Enter Employees Phone Number"), 0, 3);
    	gridPane.add(tfEmpPhNum, 1, 3);
    	gridPane.add(new Label("Employee Number"), 0, 5);
    	gridPane.add(tfEmpNum, 1, 5);
    	gridPane.add(btGenerateEmp, 1, 6);

    	gridPane.setAlignment(Pos.CENTER);
    	tfEmpFName.setAlignment(Pos.BOTTOM_RIGHT);
    	tfEmpLName.setAlignment(Pos.BOTTOM_RIGHT);
    	tfEmpEmailAdd.setAlignment(Pos.BOTTOM_RIGHT);
    	tfEmpPhNum.setAlignment(Pos.BOTTOM_RIGHT);
    	tfEmpNum.setAlignment(Pos.BOTTOM_RIGHT);
    	tfEmpNum.setEditable(false);
    	GridPane.setHalignment(btGenerateEmp, HPos.RIGHT);
    	btGenerateEmp.setOnAction(e -> employee());

    	Scene scene = new Scene(gridPane, 400, 250);
    	primaryStage.setTitle("Add Employee");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	}

    	private void btGenerateEmp() {
    	    final int PlaceKeeperEmpNum = 0001;
    	        
    	    int empNum = PlaceKeeperEmpNum;
    	    tfEmpNum.setText(String.format("%d",empNum));
    	    
    	    
    	    
    	    
    	}
    	
    	/* @author RK */
    	
    	private void employee(){
    		
    		String firstName;
    		String lastName;
    		String email;
    		String employeeID;
    		
    		// HashMap for adding employee 
    		
					/*<key, value>*/
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("First Name","Richard");
    		map.put("Last Name", "Kuoch");
    		map.put("Email", "abc123@gmail.com");
    		map.put("Employee ID", "E008");
    		
    		// Just testing hashMap for employees. Will refine after....
    		
    		firstName = map.get("First Name");
    		System.out.println("First name is " + firstName);
    		lastName = map.get("Last Name");
    		System.out.println("Last name is " + lastName);
    		email = map.get("Email");
    		System.out.println("Email is " + email);
    		employeeID = map.get("Employee ID");
    		System.out.println("Employee ID is " + employeeID);
    		
    		
    		map.remove("First Name");
    		
    		if(map.containsKey("First Name")){
    			System.out.println("First Name is still a variable");
    		}else{
    			System.out.println("First name is no longer a variable");
    		}
    	} 	
}
