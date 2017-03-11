package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileInputStream;

//TN Two capture methods (commented out), first one using Lambda Expression
//method 1 - 
//menuItem3.setOnAction(event -> {
//  System.out.println("Option 3 selected via Lambda");
//});
//method 2 -
//MenuItem menuItem3 = new MenuItem("Option 3");
//menuItem3.setOnAction(new EventHandler<ActionEvent>() {
//  @Override
//  public void handle(ActionEvent event) {
//      System.out.println("Option 3 selected");
//  }
//});

public class MenuDisplay extends Application  {

    //TN Builds GUI stage for Menu
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Menu Display");


        MenuItem menuItem1 = new MenuItem("Customer");
        MenuItem menuItem2 = new MenuItem("Business");
        MenuItem menuItem3 = new MenuItem("Registration");

        // TN example for adding image to button
        FileInputStream input = new FileInputStream("images/image1.png");
        Image image = new Image(input);
        ImageView imageView = new ImageView(image);

        //TN Creates interactive menu buttons
        MenuButton menuButton = new MenuButton("Options", imageView, menuItem1, menuItem2, menuItem3);

        HBox hbox = new HBox(menuButton);

        Scene scene = new Scene(hbox, 200, 160);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
