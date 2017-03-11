package main;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//TN - Class to build basic GUI elements for login - such as text fields
// and field position. Includes login button
// To call from another class use the following -
// Application.launch(Login_Popup.class, args) 
public class LoginPopup extends Application {
    @Override
    //TN Call this method to invoke a popup box to accept free
    //text for username and password
    public void start(Stage primaryStage) 
    {
        //TN Creates gridpane object and determines its dimensions
    	//and positioning
    	GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);
        
        //TN Inserts a text field and labels it "User Name"
        pane.add(new Label("User Name:"), 0, 0);
        pane.add(new TextField(), 1, 0);
        //TN Inserts a text field and labels it "Password"
        pane.add(new Label("Password:"), 0, 1);
        pane.add(new TextField(), 1, 1);
        
        //TN Creates a button to enter login information
        Button btLoginButton = new Button("Login");
        pane.add(btLoginButton, 1, 2);
        GridPane.setHalignment(btLoginButton, HPos.RIGHT);
        
        //TN - Call method to process login information - this method is
        //not currently functional
        //btLoginButton.setOnAction(e -> processLogin());

        //TN sets scene for popup box and names tile "login" Instantiates
        //the popup box.
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
