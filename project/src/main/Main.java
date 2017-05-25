package main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{  
    @Override 
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/Login.fxml"));
         
        Scene scene = new Scene(root); 
         
        stage.setResizable(false); //TODO: support resizing
        stage.setScene(scene); 
        stage.setTitle("Appointment Booking System");
        stage.show(); 
    }
    
    public static void main(String[] args)
    { 
        launch(args); 
    }
}
