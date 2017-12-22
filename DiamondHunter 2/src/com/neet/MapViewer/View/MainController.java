package com.neet.MapViewer.View;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.neet.DiamondHunter.Main.Game;
import com.neet.MapViewer.Main.MapMain;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
/**
* 	The main class that encompasses the GUI elements of MapViewer,
* 	with MapOverview and RootLayout FXML files working as markup.
* 	This is where the methods for various functions take place, e.g.
* 	cursor movement, item placement, saving item positions, etc.
*
* 	@author Amirul Umar Bin Pandai, Joan Kabura, Hoi Fei Long
* 	@since 	2017-12-23
*/
public class MainController {

	@FXML
	private Label cursorPosition;

	@FXML
	private Label information;

	@FXML
	private Label axePosition;

	@FXML
	private Label boatPosition;

	private int axeX = -1, boatX = -1;
	private int axeY, boatY;

	private PrintWriter axe;

	private PrintWriter boat;

	@FXML
	private void handleKeyAction(KeyEvent event) {
		if (MapMain.tileMapViewer.cursorColor == false) {
			information.setText("U: Axe, I: Boat");
		}

	    if (event.getCode() == KeyCode.W) {
	    	MapMain.tileMapViewer.cursorUp();
	    	updateCursorPosition();
	    }
	    else if (event.getCode() == KeyCode.S) {
	    	MapMain.tileMapViewer.cursorDown();
	    	updateCursorPosition();
	    }
	    else if (event.getCode() == KeyCode.A) {
	    	MapMain.tileMapViewer.cursorLeft();
	    	updateCursorPosition();
	    }
	    else if (event.getCode() == KeyCode.D) {
	    	MapMain.tileMapViewer.cursorRight();
	    	updateCursorPosition();
	    }
	    else if (event.getCode() == KeyCode.U) {
	    	MapMain.tileMapViewer.turningOnCursorColor();
	    	information.setText("Putting Axe...");
	    }
	    else if (event.getCode() == KeyCode.I) {
	    	MapMain.tileMapViewer.turningOnCursorColor();
	    	information.setText("Putting Boat...");
	    }
	    else if (event.getCode() == KeyCode.ESCAPE) {
			MapMain.primaryStage.hide();
			Game.main(null);
	    }
	}

	@FXML
	private void handleSetPosition(KeyEvent event) {
		int temp;
		if (event.getCode() == KeyCode.U) {
			temp = MapMain.tileMapViewer.handleSetAxeRequest();
			if (temp == 1) {
				information.setText("Position invalid!");
			}
			else if (temp == 2) {
				information.setText("Axe pos updated!");
				axePosition.setText(
    					"Axe: (" + MapMain.tileMapViewer.cursor.cursorRows + ", " + MapMain.tileMapViewer.cursor.cursorCols + ")");
				axeX = MapMain.tileMapViewer.cursor.cursorRows;
				axeY = MapMain.tileMapViewer.cursor.cursorCols;
			}
			else if (temp == 0) {
				information.setText("Axe done!");
    			axePosition.setText(
    					"Axe: (" + MapMain.tileMapViewer.cursor.cursorRows + ", " + MapMain.tileMapViewer.cursor.cursorCols + ")");
    			axeX = MapMain.tileMapViewer.cursor.cursorRows;
				axeY = MapMain.tileMapViewer.cursor.cursorCols;
			}
		}

		else if (event.getCode() == KeyCode.I) {
			temp = MapMain.tileMapViewer.handleSetBoatRequest();
			if (temp == 1) {
				information.setText("Position invalid!");
			}
			else if (temp == 2) {
				information.setText("Boat pos updated!");
				boatPosition.setText(
    					"Boat: (" + MapMain.tileMapViewer.cursor.cursorRows + ", " + MapMain.tileMapViewer.cursor.cursorCols + ")");
				boatX = MapMain.tileMapViewer.cursor.cursorRows;
				boatY = MapMain.tileMapViewer.cursor.cursorCols;
			}
			else if (temp == 0) {
				information.setText("Boat done!");
    			boatPosition.setText(
    					"Boat: (" + MapMain.tileMapViewer.cursor.cursorRows + ", " + MapMain.tileMapViewer.cursor.cursorCols + ")");
    			boatX = MapMain.tileMapViewer.cursor.cursorRows;
				boatY = MapMain.tileMapViewer.cursor.cursorCols;
			}
		}
	}

	@FXML
	private void exit() {
		Platform.setImplicitExit(true);
		MapMain.primaryStage.hide();
	}
	/**
	* 	Displays instructions for interacting with the map viewer,
	* 	complete with keybindings.
	*
	*/
	@FXML private void helpInfo() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Instruction");
		alert.setHeaderText("Instructions of Map Viewer");
		alert.setContentText("W/A/S/D to move.\n"
        		+ "Hold U/I to detect if axe/boat positions are valid, release it to confirm. "
        		+ "(Green is valid, Red is invalid)\n\n"
        		+ "Press Escape to the main game.\n\n"
        		+ "NOTICE: When you hold U or I, you will find "
        		+ "the cursor color automatically change to red/green "
        		+ "so that you know whether the position is available to you."
        		+ "During your press, you can move the cursor to find a position you would like to set the item up. "
        		+ "Once you have decided, release the key.\n"
        		+ "You can update the axe and boat position many times as you like.\n");
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.showAndWait();
		alert.setOnCloseRequest(event -> {alert.close();});
	}
	/**
	 * 	Locations for both items are saved to their respective files,
	 * 	with x-axes first then y-axes.
	 *
	 *  @throws IOException
	 */
	@FXML private void save() throws IOException {
		// If both locations for both items are not set before saving. X-axes for
		// both items are used instead of the entire coordinate for simplicity.
		if (axeX == -1 || boatX == -1) {
			information.setText("Please add locations!");
		} else {
			axe = new PrintWriter(new FileWriter("Resources/Coordinates/axeLocation.txt"));
			boat = new PrintWriter(new FileWriter("Resources/Coordinates/boatLocation.txt"));
			axe.println(axeX);
			axe.println(axeY);
			boat.println(boatX);
			boat.println(boatY);
			axe.close();
			boat.close();
			information.setText("Locations saved!");
		}
	}
	/**
	 * 	Reverts back to initial item locations.
	 *
	 * 	@throws IOException
	 */
	@FXML private void saveDefault() throws IOException {
		axe = new PrintWriter(new FileWriter("Resources/Coordinates/axeLocation.txt"));
		boat = new PrintWriter(new FileWriter("Resources/Coordinates/boatLocation.txt"));
		axe.println(26);
		axe.println(37);
		boat.println(12);
		boat.println(4);
		axe.close();
		boat.close();
		information.setText("Set to default!");
	}

	private void updateCursorPosition() {
		cursorPosition.setText("(" + MapMain.tileMapViewer.cursor.cursorRows + ", " + MapMain.tileMapViewer.cursor.cursorCols + ")");
	}
}
