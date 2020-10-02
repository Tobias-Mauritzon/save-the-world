package model;

import java.io.IOException;
import java.util.LinkedList;

import controller.CustomParametersController;
import controller.NavigationMenuController;
import controller.ProglemGUIController;
import controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.CustomParametersGUI;
import view.NavigationMenu;
import view.ProblemGUI;
import view.UserProfileGUI;

/***
 * The Main class of the application, creates most classes and communicates
 * between them using delegates.
 * 
 * @author Greppe
 * @author Philip
 * @version 1.0
 * @since 2020-09-17
 */
public class Main extends Application {

	private Scene mainScene;
	private LinkedList<Scene> sceneList;

	// View
	private NavigationMenu navigationMenu;
	private ProblemGUI problemGUI;
	private UserProfileGUI userProfileGUI;
	private CustomParametersGUI customParametersGUI;

	// Model
	private CustomParametersModel customParameters;
	private UserProfile userProfile;
	private SceneHandler sceneHandler;
	private testGenerator gen;
	private NumberGenerator generator;
	private grading grade;
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		// Setup for main scene
		mainScene = createScene("/FXML/NavigationMenu.fxml");
		primaryStage.setScene(mainScene);
		primaryStage.getIcons().add(new Image("/frameIcons/app-icon2.png"));
		primaryStage.setTitle("Mathematics");
		primaryStage.setMinWidth(400);
		primaryStage.setMinHeight(500);
		primaryStage.setWidth(800);
		primaryStage.setHeight(500);
		primaryStage.show();

		// Create a list of scenes that is used in the sceneHandler 
		sceneList = new LinkedList<Scene>();
		sceneList.add(createScene("/FXML/UserProfile.fxml")); // [0]
		sceneList.add(createScene("/FXML/ProblemGUI.fxml")); // [1]
		sceneList.add(createScene("/FXML/CustomParametersGUI.fxml")); // [2]

		// Instantiate all objects for the application
		createViewObjects();
		createModelObjects();
		createControllerObjects();
		setDelegates();

	}

	/**
	 * Creates instances of view classes.
	 */
	private void createViewObjects() {
		navigationMenu = new NavigationMenu((AnchorPane) mainScene.getRoot());
		problemGUI = new ProblemGUI((AnchorPane) sceneList.get(1).getRoot());
		userProfileGUI = new UserProfileGUI((AnchorPane) sceneList.get(0).getRoot());
		customParametersGUI = new CustomParametersGUI((AnchorPane) sceneList.get(2).getRoot());
	}

	/**
	 * Creates instances of model classes.
	 */
	private void createModelObjects() {
		customParameters = new CustomParametersModel();
		sceneHandler = new SceneHandler(sceneList, navigationMenu.getBasePane());
		gen = new testGenerator();
		generator = new NumberGenerator();
		grade = new grading();
		userProfile = new UserProfile("TestProfile");
	}

	/**
	 * Creates instances of controller classes.
	 */
	private void createControllerObjects() {
		new UserController(userProfileGUI, userProfile);
		new NavigationMenuController(navigationMenu, sceneHandler);
		new ProglemGUIController(problemGUI,grade,gen);
		new CustomParametersController(customParametersGUI,customParameters);
	}

	/**
	 * Sets delegates.
	 */
	private void setDelegates() {
		customParameters.setDelegate(new CustomParametersModel.Delegate() {
			@Override
			public void transmitProblemParameters(ProblemParameters p) {
				generator.setSettings(p);

				// TEMPORARY UNTIL SATURDAY
				System.out.println("\nNEW SETTINGS: ");
				for (Operator o : p.getOperators()) {
					System.out.println(o.toString());
				}
				System.out.println(p.getRange()[0]);
				System.out.println(p.getRange()[1]);
				System.out.println(p.getTermAmount());
				System.out.println(p.getTimed());
				// END TEMPORARY

				// Set scene to ProblemGui when Greppe and Philip has made support for the generator in there.
			}
		});
	}

	/**
	 * Creates a new scene with a FXML document as parameter.
	 * 
	 * @param stringFXML
	 * @return returns the created scene
	 */
	private Scene createScene(String stringFXML) {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource(stringFXML));
			Scene scene = new Scene(root, root.getWidth(), root.getHeight());
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
