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
// and field position. Includes employee text fields 
// To call from another class use the following -
// Application.launch(AddEmployee.class, args); 
public class AddEmployee extends Application {
      private TextField tfEmpFName = new TextField();
    	private TextField tfEmpLName = new TextField();
    	private TextField tfEmpEmailAdd = new TextField();
    	private TextField tfEmpPhNum = new TextField();
    	private TextField tfEmpNum = new TextField();
    	private Button btGenerateEmp = new Button("Add New Employee");
   //TN - loads GUI 
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
    	//TN - Button calls a Lambda expression which captures inserted values into string variables.
    	btGenerateEmp.setOnAction(e -> {
    		String EmpFName = tfEmpFName.getText();
    		String EmpLName = tfEmpLName.getText();
    		String EmpEmail = tfEmpEmailAdd.getText();
    		String EmpPhNum = tfEmpPhNum.getText();
    		String EmpNum = tfEmpNum.getText();
    	});

    	Scene scene = new Scene(gridPane, 400, 250);
    	primaryStage.setTitle("Add Employee");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	}
}
