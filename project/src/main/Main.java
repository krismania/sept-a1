package main;
import java.util.Scanner;
import console.Console;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{  
	     
    @Override 
    public void start(Stage stage) throws Exception { 
        Parent root = FXMLLoader.load(getClass().getResource("/GUIControl/GUILoginPopup.fxml")); 
         
        Scene scene = new Scene(root); 
         
        stage.setScene(scene); 
        stage.show(); 
    } 
 
    /** 
     * @param args the command line arguments 
     */ 
    public static void main(String[] args) { 
        launch(args); 
    } 
	
    
    // console launch. -kg
    
//	public static void main(String[] args)
//	{
//		if (args.length == 1 && args[0].equals("-debugDB"))
//		{
//			Controller.debugDB = true;
//		}
//		Scanner sc = new Scanner(System.in);
//		Console console = new Console(sc);
//		
//		console.start();
//	}
}
