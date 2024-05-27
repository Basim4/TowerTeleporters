/* Basim Khokhar
 * February 2023 to May 2023 
 * This PlayerIcon.java file houses the majority of the game's logic - most particularly
 * the game's collision and movement systems. One player moves with the WASD keys, while 
 * the other moves with the arrow keys. JavaFX's AnimationTimer class allows for continuous
 * updates on these changed key values and allows for constant collision checks.
 */


import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;

public class PlayerIcon extends StackPane {
	private Pane rectangleHouse;
	private Pane pinkObsHouse;
	private Rectangle firstP;
	private Rectangle secondP;
	private Rectangle offScreenRed;
	private Rectangle offScreenGreen;
	private Rectangle offScreenGray;
	private Rectangle offScreenPink;
	private Rectangle offScreenGold;
	private Rectangle leftWin;
	private Rectangle rightWin;
	private Rectangle youWin;
	private Rectangle goldObs;
	private boolean p1W, p1A, p1S, p1D, p2Up, p2Left, p2Down, p2Right;
	private AnimationTimer playerTimer;
	private ArrayList<Rectangle> allBricks;
	private int HEIGHT;

	public PlayerIcon(int height, int width, ArrayList<Rectangle> leftBricks, ArrayList<Rectangle> rightBricks,
			ArrayList<Rectangle> pinkObs, Rectangle randTelep) {
		HEIGHT = height;

		// creating the two players
		firstP = new Rectangle(193, height - 15, 15, 15);
		firstP.setFill(Color.RED);
		secondP = new Rectangle(597, height - 15, 15, 15);
		secondP.setFill(Color.GREEN);

		// creating the panes that will hold and apply the players and obstacles
		rectangleHouse = new Pane();
		pinkObsHouse = new Pane();
		playerTimer = new AnimationTimerCharacters(); // creating the Animation timer for movement and collisions

		// combine the given leftBricks and rightBricks into one arrayList to make future checks easier.
		allBricks = new ArrayList<Rectangle>();
		for (Rectangle brick : leftBricks) {
			allBricks.add(brick);
		}
		for (Rectangle brick : rightBricks) {
			allBricks.add(brick);
		}

		// These off-screen rectangles are used for color checking in collisions so as
		// not to mess up any collidable objects on the game board
		offScreenRed = new Rectangle(-100, -100, 50, 50);
		offScreenRed.setFill(Color.web("972114"));

		offScreenGreen = new Rectangle(-200, -200, 50, 50);
		offScreenGreen.setFill(Color.web("17e857"));

		offScreenGray = new Rectangle(-300, -300, 50, 50);
		offScreenGray.setFill(Color.web("818181"));

		offScreenPink = new Rectangle(-400, -400, 50, 50);
		offScreenPink.setFill(Color.PINK);

		offScreenGold = new Rectangle(-500, -500, 50, 50);
		offScreenGold.setFill(Color.GOLD);

		// creating the two "wining" end boxes
		leftWin = new Rectangle(35, 775, 15, 15);
		leftWin.setFill(Color.DARKGREEN);
		leftWin.setStrokeWidth(2);
		leftWin.setStroke(Color.DARKGOLDENROD);
		allBricks.add(leftWin);

		rightWin = new Rectangle(758, 775, 15, 15);
		rightWin.setFill(Color.DARKRED);
		rightWin.setStrokeWidth(2);
		rightWin.setStroke(Color.DARKGOLDENROD);
		allBricks.add(rightWin);

		// creating the box that goes behind the "<player> wins text"
		youWin = new Rectangle(200, 10, 400, 50);
		youWin.setFill(Color.rgb(0, 0, 0, 0));
		allBricks.add(youWin);

		// initial initializations of the player movement boolean trackers 
		p1W = false;
		p1A = false;
		p1S = false;
		p1D = false;
		p2Up = false;
		p2Left = false;
		p2Down = false;
		p2Right = false;

		// placing each pink obstacle created into the action scene and in the allBricks arrayList.
		for (Rectangle obs : pinkObs) {
			allBricks.add(obs);
			pinkObsHouse.getChildren().add(obs);
		}

		// add everything to the rectangleHouse pane
		goldObs = randTelep;
		allBricks.add(goldObs);
		rectangleHouse.getChildren().add(goldObs);
		rectangleHouse.getChildren().add(youWin);
		rectangleHouse.getChildren().add(firstP);
		rectangleHouse.getChildren().add(secondP);
		rectangleHouse.getChildren().add(leftWin);
		rectangleHouse.getChildren().add(rightWin);
		
		// add pinkObsHouse and rectangleHouse to the actual scene
		this.getChildren().add(pinkObsHouse);
		this.getChildren().add(rectangleHouse);
		this.setFocusTraversable(true);
		
		// start the Animation Timer for movement and collision checks.
		playerTimer.start();
	}

	// This movingPlayers method constantly updates the keys being pressed by the two players
	public void movingPlayers(StackPane header) {
		header.setOnKeyPressed(new CharacterMoverPressed());
		header.setOnKeyReleased(new CharacterMoverReleased());
	}

	// Symmetric to CharacterMoverReleased - allows the program to know which key is currently pressed.
    // WASD corresponds to the left player's movement, while UP/DOWN/RIGHT/LEFT corresponds to
    // the right player's movement.
	private class CharacterMoverPressed implements EventHandler<KeyEvent> {
		public void handle(KeyEvent e) {
			// check which key is pressed
			switch (e.getCode()) {

			case W:
				p1W = true;
				break;

			case D:
				p1D = true;
				break;

			case S:
				p1S = true;
				break;

			case A:
				p1A = true;
				break;
				
			// below UP/DOWN/RIGHT/LEFT corresponds to their respective arrow keys.
			case UP:
				p2Up = true;
				break;

			case DOWN:
				p2Down = true;
				break;

			case RIGHT:
				p2Right = true;
				break;

			case LEFT:
				p2Left = true;
				break;
				
			default:
				break;
			}
		}
	}

	// Symmetric to CharacterMoverPressed - allows the program to know which key is not pressed.
	// WASD corresponds to the left player's movement, while UP/DOWN/RIGHT/LEFT corresponds to
	// the right player's movement.
	private class CharacterMoverReleased implements EventHandler<KeyEvent> {
		public void handle(KeyEvent e) {
			switch (e.getCode()) { // gets the code of the key that is currently being pressed on the keyboard

			case W:
				p1W = false;
				break;

			case S:
				p1S = false;
				break;

			case D:
				p1D = false;
				break;

			case A:
				p1A = false;
				break;

			// below UP/DOWN/RIGHT/LEFT corresponds to their respective arrow keys.
			case UP: 
				p2Up = false;
				break;

			case DOWN: 
				p2Down = false;
				break;

			case RIGHT:
				p2Right = false;
				break;

			case LEFT:
				p2Left = false;
				break;
				
			default:
				break;
			}
		}
	}
	
	// This AnimationTimerCharacters method sends each player to the checkPlayer method. This 
	// repeated calling of the checkPlayer method allows for smooth player movement and immediate
	// collision checking. 
	private class AnimationTimerCharacters extends AnimationTimer {
		public void handle(long currentTime) {
			checkPlayer(firstP);
			checkPlayer(secondP);	
		}
	}
	
	// This checkPlayer method checks if a player has collidided with any type of wall, collided with
	// any teleporting, obstacle, is in the map's bounds, and if the player touches their "winning" 
	// end block. If a player has done any of these situations, this method will update the game's state
	// to reflect these events.
	private void checkPlayer(Rectangle player) {
		boolean doneLeft = false;
		boolean doneRight = false;
		boolean doneUp = false;
		boolean doneDown = false;
		boolean pW = false; // generalized movement variables 
		boolean pA = false;
		boolean pS = false;
		boolean pD = false;
		Rectangle otherPlayer;
		
	    // fill the generalized movement variables with the player's specific values and
		// determine who the other player is. 
		if (player == firstP) {
			pW = p1W;
			pA = p1A;
			pS = p1S;
			pD = p1D;
			otherPlayer = secondP; 
		}
		else {
			pW = p2Up;
			pA = p2Left;
			pS = p2Down;
			pD = p2Right;
			otherPlayer = firstP;
		}
		
		// creating a rectangle that will act as the player's "hitbox"
		Rectangle checker = new Rectangle(player.getX(), player.getY(), 15, 15);
		String collidedColor = ""; // will store the color of the object that the player hit

		// check through each block to see if the player is intersecting any of them.
		for (Rectangle wallChecked : allBricks) {
			Shape boundaryChecker = Shape.intersect(player, wallChecked); 
			if (boundaryChecker.getBoundsInLocal().getWidth() != -1) {
				if (wallChecked.getFill().equals(offScreenRed.getFill())) { // Colliding through a red block
					collidedColor = "RED";
					break;

				} else if (wallChecked.getFill().equals(offScreenGreen.getFill())) { // Colliding through a green block
					collidedColor = "GREEN";
					break;

				} else if (wallChecked.getFill().equals(offScreenGray.getFill())) { // Colliding through a grey block
					collidedColor = "GRAY";
					break;

				} else if (wallChecked.getFill().equals(offScreenPink.getFill())) { // Colliding through a pink block
					collidedColor = "PINK";
					break;
				} else if (wallChecked.getFill().equals(offScreenGold.getFill())) { // Colliding through the gold block
					collidedColor = "GOLD";
					break;
				} else if (wallChecked.getFill().equals(leftWin.getFill())) { // Colliding through green's winning block
					collidedColor = "DARKGREEN";
					break;
				} else if (wallChecked.getFill().equals(rightWin.getFill())) { // Colliding through red's winning block
					collidedColor = "DARKRED";
					break;
				}
			}
		}

		// checks if the player moved UP. Symmetric with DOWN, LEFT, and RIGHT.
		if (pW) {
			boolean didItCol = false;
			checker.setY(checker.getY() - 2); // move the "hitbox" slightly above the player.

			if (collidedColor.equals("RED")) { // slowing down the player if they intersect with a red brick
				player.setY(player.getY() - 0.25);

			} else if (collidedColor.equals("GREEN")) { // teleport the player upwards if they intersect a green brick
				player.setY(player.getY() - 113);

			} else if (collidedColor.equals("GRAY")) { // player intersected with a grey block. 
				didItCol = true;
				doneUp = true;
				if (didItCol) {
					if (doneRight || doneDown || doneLeft) { // move the players based on if another key is pressed.
						player.setY(player.getY() - 4);
					} else {
						player.setY(player.getY() + 4);
					}
					doneRight = false;
					doneDown = false;
					doneLeft = false;
				}

			} else if (collidedColor.equals("PINK")) { // player touched a pink block, randomly generate a new Y coordinate.
				teleportY(player, HEIGHT, allBricks);

			} else if (collidedColor.equals("GOLD")) { // player touched a gold block, randomly generate the other's XY position.
				teleportXY(otherPlayer, HEIGHT);

			} else if (collidedColor.equals("DARKRED") && (player == firstP)) { // first player touched their winning block.
				gameEnd(player);

			} else if (collidedColor.equals("DARKGREEN") && (player == secondP)) { // second player touched their winning block
				gameEnd(player);
			}  
			
			else { // there is no special case, simply move the player upwards.
				player.setY(player.getY() - 2);
			}

		// checks if the player moved DOWN. Symmetric with UP, LEFT, and RIGHT.
		} else if (pS) {
			boolean didItCol = false;
			checker.setY(checker.getY() + 2); // move the "hitbox" slightly below the player.

			if (collidedColor.equals("RED")) { // slowing down the player if they intersect with a red brick
				player.setY(player.getY() + 0.25);

			} else if (collidedColor.equals("GREEN")) { // teleport the player upwards if they intersect a green brick
				player.setY(player.getY() + 113);

			} else if (collidedColor.equals("GRAY")) { // player intersected with a grey block.			
				didItCol = true;
				doneDown = true;
				if (didItCol) {
					if (doneRight || doneUp || doneLeft) { // move the players based on if another key is pressed.
						player.setY(player.getY() + 4);
					} else {
						player.setY(player.getY() - 4);
					}
					doneRight = false;
					doneUp = false;
					doneLeft = false;
				} 

			} else if (collidedColor.equals("PINK")) { // player touched a pink block, randomly generate a new Y coordinate.
				teleportY(player, HEIGHT, allBricks);

			} else if (collidedColor.equals("GOLD")) { // player touched a gold block, randomly generate the other's XY position
				teleportXY(otherPlayer, HEIGHT);

			} else if (collidedColor.equals("DARKRED") && (player == firstP)) { // first player touched their winning block.
				gameEnd(player);
				
			} else if (collidedColor.equals("DARKGREEN") && (player == secondP)) { // second player touched their winning block
				gameEnd(player);
				
			} else { // there is no special case, simply move the player downwards.
				player.setY(player.getY() + 2);
			}
		}

		// checks if the player moved LEFT. Symmetric with DOWN, UP, and RIGHT.
		if (pA) {
			boolean didItCol = false;
			checker.setX(checker.getX() - 2); // move the "hitbox" slightly to the left of the player.

			if (collidedColor.equals("RED")) { // slowing down the player if they intersect with a red brick
				player.setX(player.getX() - 0.25);

			} else if (collidedColor.equals("GREEN")) { // teleport the player upwards if they intersect a green brick
				player.setX(player.getX() + 2);

			} else if (collidedColor.equals("GRAY")) { // player intersected with a grey block.
				didItCol = true;
				doneLeft = true;
				if (didItCol) {
					if (doneRight || doneUp || doneDown) { // move the players based on if another key is pressed.
						player.setX(player.getX() - 4);
					} else {
						player.setX(player.getX() + 4);
					}
					doneRight = false;
					doneUp = false;
					doneDown = false;
				}
			} else if (collidedColor.equals("PINK")) { // player touched a pink block, randomly generate a new Y coordinate.
				teleportY(player, HEIGHT, allBricks);

			} else if (collidedColor.equals("GOLD")) { // player touched a gold block, randomly generate the other's XY position
				teleportXY(otherPlayer, HEIGHT);

			} else if (collidedColor.equals("DARKRED") && (player == firstP)) { // first player touched their winning block.
				gameEnd(player);

			} else if (collidedColor.equals("DARKGREEN") && (player == secondP)) { // second player touched their winning block
				gameEnd(player);
			
			}  else { // there is no special case, simply move the player to the left.
				player.setX(player.getX() - 2);
			}

		// checks if the player moved RIGHT. Symmetric with DOWN, LEFT, and UP.
		} else if (pD) {
			boolean didItCol = false;
			checker.setX(checker.getX() + 2); // move the "hitbox" slightly to the right of the player.

			if (collidedColor.equals("RED")) { // slowing down the player if they intersect with a red brick
				player.setX(player.getX() + 0.25);

			} else if (collidedColor.equals("GREEN")) { // teleport the player upwards if they intersect a green brick
				player.setX(player.getX() - 2);

			} else if (collidedColor.equals("GRAY")) { // player intersected with a grey block. Check from which direction.
				didItCol = true;
				doneRight = true;
				if (didItCol) {
					if (doneLeft || doneUp || doneDown) { // move the players based on if another key is pressed.
						player.setX(player.getX() + 4);
					} else {
						player.setX(player.getX() - 4);
					}
					doneLeft = false;
					doneUp = false;
					doneDown = false;
				} 
				
			} else if (collidedColor.equals("PINK")) { // player touched a pink block, randomly generate a new Y coordinate.
				teleportY(player, HEIGHT, allBricks);

			} else if (collidedColor.equals("GOLD")) { // player touched a gold block, randomly generate the other's XY position
				teleportXY(otherPlayer, HEIGHT);

			} else if (collidedColor.equals("DARKRED") && (player == firstP)) { // first player touched their winning block.
				gameEnd(player);

			} else if (collidedColor.equals("DARKGREEN") && (player == secondP)) { // second player touched their winning block
				gameEnd(player);
			
			} else { // there is no special case, simply move the player to the left.
				player.setX(player.getX() + 2);
			}
		} 
		
		// Check if the player is contained in the map's left and right (X) bounds
		if (player.getX() < 22) {
			player.setX(23);
		} else if ((player.getX() > 358) && !(player.getY() > 64) && !(player.getY() < 160)) {
			player.setX(357);
		} else if (player.getX() > 763) {
			player.setX(762);
		}

		// Check if the player is contained in the map's top and bottom (Y) bounds
		if (player.getY() < 64) { 
			player.setY(65);
		} else if (player.getY() > 785) {
			player.setY(784);
		}

		// Check if the player is contained outside the map's empty middle area
		if ((player.getX() > 355) && (player.getX() < 427) && (player.getY() > 148)) {
			if (player.getX() > 424) {
				player.setX(player.getX() + 4);
			} else if (player.getX() < 358) {
				player.setX(player.getX() - 4);
			}
			else if (player.getY() < 152) {
				player.setY(player.getY() - 4);
			}
		}
	}

	// This gameEnd method occurs when one player touches their respective goal blocks on the other
	// tower. The game will halt the players' movements and display a text box with a label stating 
	// who won the game.
	private void gameEnd(Rectangle player) {
		String winner = "";
		if (player == firstP) {  // determine who won for the label.
			winner = "Red";
		} else {
			winner = "Green";
		}

		youWin.setFill(Color.WHITE);
		Label whoWon = new Label(winner + " won!"); // display who won the game at the top.
		whoWon.setLayoutX(370);
		whoWon.setLayoutY(25);
		rectangleHouse.getChildren().add(whoWon);
	}

	// This teleportY method triggers when a player touches a pink square. The player who touched
	// this square will have a new Y value randomly generated and assigned to their location.
	public void teleportY(Rectangle victim, int height, ArrayList<Rectangle> pinkObs) {
		Random changedY = new Random();
		int newYLevel = changedY.nextInt(23) + 3; // determine the player's new Y value
		
		// ensuring that the level the victim is sent to is not a wall level
		while (newYLevel % 2 == 1) {
			newYLevel = changedY.nextInt(23) + 3;
		}
		
		// changing the victim's y value
		victim.setY(height - (32 * newYLevel) + 45);
	}

	// This teleportXY method triggers when the Golden square in the middle of the "bridge" is touched
	// by either player. The other player will have their X and Y coordinates changed with randomly 
	// generated values through java's Random package.
	public void teleportXY(Rectangle victim, int height) {
		Random changedValue = new Random(); 
		int newYLevel = changedValue.nextInt(23) + 3; // determine the new X and Y values
		int newXVal = changedValue.nextInt(740) + 40;
		
		// ensuring that the level the victim is sent to is not a wall level or in middle empty box
		while ((newYLevel % 2 == 1) || ((victim.getX() > 355) && (victim.getX() < 427) && (victim.getY() > 148))) {
			newYLevel = changedValue.nextInt(23) + 3; 
		}
		
		// changing the victim's y value
		victim.setX(newXVal);
		victim.setY(height - (32 * newYLevel) + 40);

		allBricks.get(allBricks.size() - 1).setFill(Color.web("444444")); // remove the gold block (one time use)
		allBricks.get(allBricks.size() - 1).setStroke(Color.web("444444"));
		allBricks.remove(allBricks.size() - 1);
	}
}