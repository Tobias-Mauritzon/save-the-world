package view;
/**
 * Class that creates a a prototype for the user profile gui
 * @author Joachim Antfolk, Tobias Mauritzon
 * @since 2020-09-25
 */
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.UserProfile;
import model.SaveManager;


public class UserProfileGui extends Application{
	
	private Label userName;
	private Button profileNew;
    private Button profileSwitch;
    private Button profileDelete;
    private TabPane root;
    private UserProfile profile;
    
    public static void main(String[] args) {
    	Locale.setDefault(Locale.ENGLISH); //Sets button text to english
        launch();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	try {
    		root = (TabPane)FXMLLoader.load(getClass().getResource("/view/UserProfile.fxml"));
    	    
    		Scene scene = new Scene(root, 600, 600);
    		scene.getStylesheets().add(getClass().getResource("/view/UserProfile.css").toExternalForm());
    		stage.setTitle("User profile test");
    		stage.setScene(scene);
    		stage.show();
    		
    		getGUIObjects();
    		setup();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    
    }
    
   /**
    *  Initializes the GUI elements so the users can interact with them.
    */
   private void getGUIObjects() {
	   	userName  = (Label) root.lookup("#profileName");
		profileNew  = (Button) root.lookup("#profileNew");
	    profileSwitch = (Button) root.lookup("#profileSwitch");
	    profileDelete = (Button) root.lookup("#profileDelete");  
    }
   
   /**
    * Creates a user profile, updates user name text and programs buttons 
    */
   private void setup() {
	   profile = new UserProfile("Generic");
	   userName.setText(profile.getName());
	   
	   profileNew.setOnAction((event) -> {
		   try {
			   Optional<String> name = getInput("New Profile");
			   
			   if(name.isPresent() && name.get().length() > 0){
				   SaveManager.saveFile(profile, profile.getName() + "Profile.Save");
				   
				   profile = new UserProfile(name.get());
				   userName.setText(name.get());
			   }
		   }catch(IOException e) {
			   errorMessage("Could not save profile: " + profile.getName() + "!");
		   }
	   });
	   
	   profileSwitch.setOnAction((event) -> {
		   try {
			   Optional<String> name = getInput("Switch Profile");
			   
			   if(name.isPresent() && name.get().length() > 0){
				   SaveManager.saveFile(profile, profile.getName() + "Profile.Save");
				   profile = (UserProfile) SaveManager.loadFile(name.get() + "Profile.Save");
				   
				   userName.setText(profile.getName());
			   }
		   }catch(IOException | ClassNotFoundException e) {
			   errorMessage("Could not switch profile!");
		   }
	   });

	   profileDelete.setOnAction((event) -> {
		   try {
			   Optional<String> name = getInput("Delete Profile");
			   
			   if(name.isPresent() && name.get().length() > 0){
				   SaveManager.deleteFile(name.get() + "Profile.Save");
			   }
		   }catch(IOException e) {
			   errorMessage("Could not delete profile: " + profile.getName() + "!");
		   }
	   });
   }
   
   /**
    * Opens an error dialog with an error message
    * @param error message to display
    */
   private void errorMessage(String error) {
	   Alert alert = new Alert(AlertType.ERROR);
	   alert.getDialogPane().getStylesheets().add(getClass().getResource("/view/dialog.css").toExternalForm());
	   alert.setTitle("Error Detected");
	   alert.setHeaderText("");
	   alert.setContentText(error);
	   alert.showAndWait();
   }
   
   /**
    * Opens a dialog that gets user input
    * @param title title for the dialog box window
    * @return profile name as Optional<String>
    */
   private Optional<String> getInput(String title) {
	   	TextInputDialog dialog = new TextInputDialog("");
		dialog.getDialogPane().getStylesheets().add(getClass().getResource("/view/dialog.css").toExternalForm());
		dialog.setTitle(title);
		dialog.setGraphic(null);
		dialog.setHeaderText("");
		dialog.setContentText("Profile name:");
		
		return  dialog.showAndWait();
   }
}