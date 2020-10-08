package controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import model.ProfileHandler;
import model.SaveManager;
import model.UserProfile;
import model.UserProfile;
import view.Operator;
import view.UserProfileGUI;

/***
 * A controller for the userProfiler so the userGUI can interact with the
 * UserprofileModell.
 * 
 * @author Greppe
 * @author Philip
 * @version 1.0
 * @since 2020-10-01
 */
public class UserController implements ControllerInterface {

	private UserProfileGUI userProfileGUI;
	private ProfileHandler profileHandler;

	/***
	 * Constructor for the UserController.
	 * 
	 * @param userProfileGUI the UserProfileGUI instance
	 * @param userProfile    the UserProfile model instance.
	 */
	public UserController(UserProfileGUI userProfileGUI, ProfileHandler profileHandler) {
		this.userProfileGUI = userProfileGUI;
		this.profileHandler = profileHandler;
		// Updates the UserProfileGUi with the name of the testprofile, this will most likely be replaced with the start window / loggin windows profile
		userProfileGUI.setUserNameLabel(profileHandler.getCurrentProfile().getName());
		// Updates the history tabell with the pregenerated sample history for the testprofile.
		userProfileGUI.getHistory().setItems(profileHandler.getCurrentProfile().getHistory());
		setActions();
	}

	@Override
	/***
	 * Sets the actions for the userController GUI.
	 */
	public void setActions() {
		//System.out.println("test2: " + userProfileGUI.getPSwitchButton());
		
		// Sets the actions for the profile switch button
		userProfileGUI.getPSwitchButton().setOnAction((event) -> {
			try {
					Optional<String> name = userProfileGUI.getInput("Switch Profile", "Profile name:");
					if(profileHandler.switchProfile(name)) {
						userProfileGUI.setUserNameLabel(profileHandler.getCurrentProfile().getName());
						userProfileGUI.getHistory().setItems(FXCollections.observableArrayList(profileHandler.getCurrentProfile().getHistory()));
					}
					else {
						
						throw new IOException("");
					}

			}
			catch (IOException | ClassNotFoundException e) {
				userProfileGUI.errorMessage("Could not switch profile!");
			}
		});
		
		// Sets the actions for the profile Delete Button.
		userProfileGUI.getPDeleteButton().setOnAction((event) -> {
			String deleteProfile = "";
			try {
				
				Optional<String> name = userProfileGUI.getInput("Delete Profile", "Profile name:");
				deleteProfile = name.get();
				if(!profileHandler.deleteProfile(name)) 
				{
					throw new IOException("");
				}
			} 
			
			catch (IOException e) {
				
				userProfileGUI.errorMessage("Could not delete profile: " + deleteProfile + "!");
			}
		});
		
		// Sets the action for the new profile button.
		userProfileGUI.getPNewButton().setOnAction((event) -> {
			String newProfile = "";
			try {
					Optional<String> name = userProfileGUI.getInput("New Profile", "Profile name:");
					newProfile = name.get();
					
					if(profileHandler.addProfile(name)) {
						
						userProfileGUI.setUserNameLabel(profileHandler.getCurrentProfile().getName());
						userProfileGUI.getHistory().setItems(FXCollections.observableArrayList(profileHandler.getCurrentProfile().getHistory()));
					}
					else {
						
						throw new IOException("");
					}


			}
			catch (IOException e) {
				userProfileGUI.errorMessage("Could not save profile: " + newProfile + "!");
			}
		});

	}

}