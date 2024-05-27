/* Basim Khokhar
 * February 2023 to May 2023
 * This StartUp.java file creates the game by creating the background,
 * the players, the obstacles, and adding these components to the stage.
 */

import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class StartUp extends Application {
	public static final int WIDTH = 800, HEIGHT = 800; // size of the application

	// launches the entire program by calling the start() method
	public static void main(String[] args) {
		launch(args);
	}

	// this start method creates and adds all the game's visual components and logic to the scene and stage. 
	public void start(Stage stage) {
		StackPane header = new StackPane();
		BackGround scenery = new BackGround(WIDTH, HEIGHT); // creates the background for the program
		ArrayList<Rectangle> leftBricks = scenery.getBackGroundBrickContainerL(); // collect all obstacles and special blocks
		ArrayList<Rectangle> rightBricks = scenery.getBackGroundBrickContainerR();
		ArrayList<Rectangle> pinkObstacles = scenery.getPinkObstacles();
		Rectangle randTelep = scenery.getGoldObstacle();
		header.getChildren().add(scenery); // adds the "meat" of the program to the root.

		// create players and add it to the entire scene
		PlayerIcon creatingPlayers = new PlayerIcon(HEIGHT, WIDTH, leftBricks, rightBricks, pinkObstacles, randTelep);
		header.getChildren().add(creatingPlayers);
		StackPane.setAlignment(creatingPlayers, Pos.TOP_LEFT);
		creatingPlayers.movingPlayers(header);

		Scene scene = new Scene(header, WIDTH, HEIGHT); // creates the window with the StackPane.
		stage.setTitle("TOWER TELEPORTERS"); 
		stage.setScene(scene); // puts the main scene (everything) on the Stage

		stage.show(); // display the stage to the screen
		header.requestFocus();
	}
}
