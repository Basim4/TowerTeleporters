/* Basim Khokhar
 * February 2023 to May 2023
 * This BackGround.java file creates the background of the game. The
 * game field is created here as well; and the obstacles created are
 * decided and placed with Java's Random package. These obstacles are
 * all placed in arrayLists that are primarily used in PlayerIcon.java
 * for easy collision checking.
 */

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.*;
import java.util.ArrayList;
import java.util.Random;

public class BackGround extends BorderPane {
	private Rectangle backGroundColorLeft;
	private Rectangle backGroundColorRight;
	private Rectangle backGroundBrickL;
	private Rectangle backGroundBrickR;
	private Rectangle randTelep;
	private Circle sun;
	private Circle moonGold;
	private Circle moonPurple;
	private Circle star;
	private int starX, starY;
	private int brickCount;
	private int colorDecider;
	private int[] brickRowTypes;
	private int[] randomGapPlacement;
	private Line playerDivider;
	private BorderPane leftSide;
	private BorderPane rightSide;
	private HBox sideContainers;
	private ArrayList<Rectangle> backGroundBrickContainerL; 
	private ArrayList<Rectangle> backGroundBrickContainerR;
	private ArrayList<Rectangle> pinkObstacles;
	private Random randomBrickRowType;
	private Random randomGap;
	private String colorSent;

	public BackGround(int WIDTH, int HEIGHT) {

		leftSide = new BorderPane(); // create equal sized halves for the game scene
		rightSide = new BorderPane();
		sideContainers = new HBox();

		backGroundBrickContainerL = new ArrayList<Rectangle>();
		backGroundBrickContainerR = new ArrayList<Rectangle>();
		pinkObstacles = new ArrayList<Rectangle>();

		backGroundColorLeft = new Rectangle();  // fill left side with color and add to scene
		backGroundColorLeft.setWidth(WIDTH / 2);
		backGroundColorLeft.setHeight(HEIGHT);
		backGroundColorLeft.setFill(Color.web("48076a"));
		this.getChildren().add(backGroundColorLeft);

		backGroundColorRight = new Rectangle(); // fill right side with color and add to scene
		backGroundColorRight.setX(WIDTH / 2);
		backGroundColorRight.setWidth(WIDTH);
		backGroundColorRight.setHeight(HEIGHT);
		backGroundColorRight.setFill(Color.SKYBLUE);
		this.getChildren().add(backGroundColorRight);

		sun = new Circle(740, 60, 50, Color.YELLOW); // create the sun
		sun.setStrokeWidth(10);

		// create and randomly place the stars for the left (night) side.
		randomGap = new Random();
		for (int i = 0; i <= 40; i++) {
			starX = 100;
			starY = 100;
			while ((starX > 50) && (starX < 350) && (starY > 64) && (starY < HEIGHT)) { // constrain to left side
				starX = randomGap.nextInt(398) + 2;
				starY = randomGap.nextInt(HEIGHT - 2) + 2;
			}
			star = new Circle(starX, starY, 2, Color.WHITE); // create new star with random X and Y values.
			this.getChildren().add(star);
		}

		moonPurple = new Circle(60, 60, 50, Color.GRAY); // create the moon
		moonGold = new Circle(80, 60, 50, Color.web("48076a"));

		playerDivider = new Line((WIDTH / 2), 0, (WIDTH / 2), HEIGHT); // create the line that splits the scene's halves
		playerDivider.setStroke(Color.BLACK);
		playerDivider.setStrokeWidth(10);

		randomGapPlacement = new int[7];
		createBackGround(0, HEIGHT, 6, 23, "444444", randomGapPlacement);
		Rectangle bridgeUp = new Rectangle(372.5, 66, 55, 30); // create 1 brick of the top of the bridge
		bridgeUp.setFill(Color.web("444444"));
		bridgeUp.setStrokeWidth(2);
		bridgeUp.setStroke(Color.BLACK);

		Rectangle bridgeMid = new Rectangle(372.5, 98, 55, 30); // create 1 brick of the middle of the bridge
		bridgeMid.setFill(Color.web("444444"));
		bridgeMid.setStrokeWidth(2);
		bridgeMid.setStroke(Color.BLACK);

		Rectangle bridgeDown = new Rectangle(372.5, 130, 55, 30); // create 1 brick of the bottom of the bridge
		bridgeDown.setFill(Color.web("444444"));
		bridgeDown.setStrokeWidth(2);
		bridgeDown.setStroke(Color.BLACK);

		brickRowTypes = new int[10];
		brickRowTypes[0] = 0;
		randomBrickRowType = new Random(); // used to randomly determine what color a row of bricks will be

		// used to determine an integer value that corresponds to the type of brick a line will have
		for (int i = 0; i < brickRowTypes.length; i++) {
			if (i <= 10) {
				brickRowTypes[i] = randomBrickRowType.nextInt(3) + 1;
			} else {
				brickRowTypes[i] = 4;
			}
		}
		backGroundBrickContainerL.clear();
		backGroundBrickContainerR.clear();

		for (int p = 0; p < brickRowTypes.length; p++) {
			colorDecider = randomBrickRowType.nextInt(10);
			if (colorDecider <= 6) { // Essentially 60% chance bricks will be the normal grey.
				colorSent = "818181";
			} else if (colorDecider <= 8) { // Essentially 20% chance bricks will be green.
				colorSent = "17e857";
			} else {
				colorSent = "972114"; // Essentially 20% chance bricks will be red.
			}

			addForeGroundBrick(0, HEIGHT, randomGap, p, colorSent); // add the row of bricks
		}

		// two RNG x and y changing (other) teleporter icons
		randTelep = new Rectangle((WIDTH / 2) - 7, 105, 15, 15);
		randTelep.setFill(Color.GOLD);
		randTelep.setStroke(Color.BLACK);
		randTelep.setStrokeWidth(2);

		int numYTeleporters = randomBrickRowType.nextInt(7) + 2;
		int levelPlaced;
		int randomXPlacedLeft;
		int randomXPlacedRight;
		Rectangle miniTeleportLeft;
		Rectangle miniTeleportRight;

		// Repeatedly create pink telporters and add them to the scene.
		for (int i = 0; i <= numYTeleporters; i++) { 
			levelPlaced = randomBrickRowType.nextInt(23) + 3;
			while (levelPlaced % 2 == 1) { // reroll levelPlaced if the value is on a level of walls
				levelPlaced = randomBrickRowType.nextInt(23) + 3;
			}
			
			// each tower will have the same number of pink teleporters on each side and level.
			randomXPlacedLeft = randomBrickRowType.nextInt(315) + 50;
			randomXPlacedRight = randomBrickRowType.nextInt(315) + 430;
			miniTeleportLeft = new Rectangle(randomXPlacedLeft, HEIGHT - (32 * levelPlaced) + 45, 10, 10);
			miniTeleportLeft.setFill(Color.PINK);
			miniTeleportRight = new Rectangle(randomXPlacedRight, HEIGHT - (32 * levelPlaced) + 45, 10, 10);
			miniTeleportRight.setFill(Color.PINK);

			// adding this set of teleporters to the scene
			pinkObstacles.add(miniTeleportLeft);
			pinkObstacles.add(miniTeleportRight);
		}

		// add everything created to the scene
		sideContainers.getChildren().addAll(leftSide, rightSide);
		this.getChildren().addAll(playerDivider, sun, moonPurple, moonGold, bridgeUp, bridgeMid, bridgeDown);
		this.getChildren().add(sideContainers);
	}

	// This createBackGround method creates the lines of walls that the player will have to
	// traverse through. The gaps in these walls are also taken into account here. The special
	// colors of a wall are also implemented. The left and right towers will have the same set
	// of walls. 
	private void createBackGround(int initialBackgroundX, int HEIGHT, int brickCountLimit, int totalRowsLimit,
			String colorChoice, int[] gapPlaces) {
		int totalRows = 0;
		int changedX = 22; // changedX/Y are used to offset each brick
		int changedY = 30;
		int gapTracker = 0;
		// fill the entire tower with bricks and no more and no less
		while ((brickCount <= brickCountLimit) && (totalRows < totalRowsLimit)) { 
			if (gapPlaces[gapTracker] == 0) { // if there is not supposed to be a gap, place a brick.
				// create all the darker grey bricks that will create the towers (background)
				// creating the bricks of the left tower
				backGroundBrickL = new Rectangle((initialBackgroundX + changedX), (HEIGHT - changedY), 50, 30);
				backGroundBrickL.setFill(Color.web(colorChoice));
				backGroundBrickL.setStrokeWidth(2);
				backGroundBrickL.setStroke(Color.BLACK);
				backGroundBrickContainerL.add(backGroundBrickL);
				leftSide.getChildren().add(backGroundBrickL);

				// creating the bricks of the right tower
				backGroundBrickR = new Rectangle((initialBackgroundX + 406 + changedX), (HEIGHT - changedY), 50, 30);
				backGroundBrickR.setFill(Color.web(colorChoice));
				backGroundBrickR.setStrokeWidth(2);
				backGroundBrickR.setStroke(Color.BLACK);
				backGroundBrickContainerR.add(backGroundBrickR);
				rightSide.getChildren().add(backGroundBrickR);
			}
			brickCount++;
			
			if (brickCount <= brickCountLimit) { // if more bricks can be added to the same row, add to changedX
				changedX = changedX + 50;
				gapTracker++;
			} else { // no more bricks can be added to the same row, reset changedX & add 32 to changedY and start new row.
				changedX = 22;
				changedY = changedY + 32;
				brickCount = 0;
				totalRows++;
				gapTracker = 0;
			}
		}
	}

	// This addForeGroundBrick method determines where the gaps are in each line. Then, the createBackGround
	// method is called to create and add the row of bricks to the scene with the gap information from here.
	public void addForeGroundBrick(int xValue, int HEIGHT, Random insertGap, int level, String color) {
		int oneGap = insertGap.nextInt(5);
		int twoGap = oneGap + 1;
		
		// the left and right most bricks will always be filled with a wall.
		if (twoGap >= 6) { // if the right-most brick is said to be empty, have the 2nd gap be on brick 4.
			twoGap = 4;
		} else if (oneGap == 0) { // if the left-most brick is said to be empty, set the two gaps at space 2 and 3
			oneGap = 2;
			twoGap = 3;
		}

		// set these gap placement values in the randomGapPlacement array for the createBackGround method
		randomGapPlacement[oneGap] = 1;
		randomGapPlacement[twoGap] = 1;
		
		// create and set the level of walls
		createBackGround(xValue, HEIGHT - (32 * (2 * level) + 32), 6, 1, color, randomGapPlacement);
		// set every index in randomGapPlacement for the next level of walls
		for (int j = 0; j < randomGapPlacement.length; j++) { 
			randomGapPlacement[j] = 0;
		}
	}

	// a simple getter method that returns the arrayList of backGroundBrickContainerL
	public ArrayList<Rectangle> getBackGroundBrickContainerL() {
		return backGroundBrickContainerL;
	}

	// a simple getter method that returns the arrayList of backGroundBrickContainerR
	public ArrayList<Rectangle> getBackGroundBrickContainerR() {
		return backGroundBrickContainerR;
	}

	// a simple getter method that returns the arrayList of pinkObstacles
	public ArrayList<Rectangle> getPinkObstacles() {
		return pinkObstacles;
	}

	// a simple getter method that returns the randTelep rectangle
	public Rectangle getGoldObstacle() {
		return randTelep;
	}

}