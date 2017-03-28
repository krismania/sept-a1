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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
    	GridPane gridPane = new GridPane();
    	
        /*Scene sceneLogin = new Scene(gridPane);
        Scene sceneAddEmp = new Scene(gridPane);
        Scene sceneAddTime = new Scene(gridPane);
        Scene sceneAddShift = new Scene(gridPane);*/
                    	
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        gridPane.setHgap(5.5);
    	gridPane.setVgap(5.5);
        
        //TN Inserts a text field and labels it "User Name"
        gridPane.add(new Label("User Name:"), 0, 0);
        gridPane.add(new TextField(), 1, 0);
        //TN Inserts a text field and labels it "Password"
        gridPane.add(new Label("Password:"), 0, 1);
        gridPane.add(new TextField(), 1, 1);
     
        
        //TN Creates a button to enter login information
        Button btLoginButton = new Button("Login");
        gridPane.add(btLoginButton, 1, 2);
        GridPane.setHalignment(btLoginButton, HPos.RIGHT);
        
        //TN- Signup button - navigates to set up for new user name and password        
        Button btSignUp = new Button("Signup");
        gridPane.add(btSignUp, 0, 2);
        GridPane.setHalignment(btSignUp, HPos.RIGHT);
        
        //TN - Call method to process login information - this method is
        //not currently functional - Process login Method should capture a Password and Username 
        //variable then pass it to login
        //btLoginButton.setOnAction(e -> window.setScene(sceneAddTime));
        //btSignUp.setOnAction(e -> window.setScene(sceneAddEmp));
        
        //TN - Call addNewUser() method - yet to be implemented
        //btSignUp.setOnAction(e -> addNewUser());

        //TN sets scene for popup box and names tile "login" Instantiates
        //the popup box.
      	Scene scene = new Scene(gridPane);
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        //System.out.println("Invalid password.");
         
    }
}
