package gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class GUIUtil
{
	private GUIUtil() {};
		
	/**
	 * Implements Generic helper method for switching between scenes
	 * @author krismania
	 */
	public static void switchTo(String fxmlName, Node root)
	{
		try
		{
			// load the scene
			Scene newScene = new Scene(FXMLLoader.load(GUIUtil.class.getResource(fxmlName + ".fxml")));
			
			// get current stage
			Stage stage = (Stage) root.getScene().getWindow();
			
			// switch scenes
			stage.setScene(newScene);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
