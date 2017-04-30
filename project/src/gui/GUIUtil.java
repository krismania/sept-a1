package gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Utility class for common GUI functionality
 * @author krismania
 */
public final class GUIUtil
{
	private GUIUtil() {} // prevents instantiation
	
	/**
	 * Application root node, should be set on launch. the switchTo method uses
	 * this node to switch scenes.
	 */
	public static Node root;
	
	/**
     * Generic Helper Method for Switching to specified scenes
     * @author krismania
     */
    public void switchTo(String fxmlName)
	{
		try
		{
			// load the scene
			Scene newScene = new Scene(FXMLLoader.load(getClass().getResource(fxmlName + ".fxml")));
			
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
