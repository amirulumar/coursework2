package com.neet.MapViewer.Main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * This class contains the map and canvas and cursor to illustrate the proper image
 * to the user interface.
 * Essentially the tile is drew into the mainCanvas and convert the canvas into originalMapImage using snapshot,
 * then add the cursor into the mainCanvas, and convert the canvas into mapImage.
 *
 * Every time the cursor is moved, previous position is updated by taking originalMapImage
 * and mainCanvas will draw the cursor and items that already put.
 *
 * @author  Amirul Umar Bin Pandai, Joan Kabura, Hoi Feo Long
 * @since   2017-12-23
*/

public class TileMapViewer {
	private int boatRow = -1;
	private int boatCol = -1;
	private int axeRow = -1;
	private int axeCol = -1;


	public int getBoatRow() {
		return boatRow;
	}
	public int getBoatCol() {
		return boatCol;
	}
	public int getAxeRow() {
		return axeRow;
	}
	public int getAxeCol() {
		return axeCol;
	}

	private final int BOAT = 0;
	private final int AXE = 1;

	private int tileSize = 16;
	public int numCols;
	public int numRows;

	public MyCursor cursor;
	public boolean cursorColor = false;

	private int[][] mapMatrix;
	private int[][] tileType;

	private Image tileset;
	private int numTilesAcross;

	/**
	 * Variable mainCanvas is to update the whole map
	 */
	public Canvas mainCanvas;

	/**
	 * Variable originalMapImage is to store original image of the whole map without the cursor, so that when
	 * the cursor is moved, the current map image is updated properly
	 */
	private Image originalMapImage;

	public Image items;
	public boolean axePut = false;
	public boolean boatPut = false;

	/**
	 * The method reads numbers from map file, and then write the data to mapMatrix.
	 * The value of numCols and numRows is also known from the map file.
	 *
	 * @param mapFile The file that contains information of each grid in the map
	 */
	public void loadMapFile(String mapFile) {
		try {
			InputStream in = getClass().getResourceAsStream(mapFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());

			mapMatrix = new int[numRows][numCols];

			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					mapMatrix[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method inputs image from resource.
	 *
	 * @param tilesetImage The file that contains images of each tile
	 * @param itemsImage The file that contains images of axe and boat
	 */
	public void loadImagesFiles(String tilesetImage, String itemsImage) {
		try {
			tileset = new Image(TileMapViewer.class.getResourceAsStream(tilesetImage));
			items = new Image(TileMapViewer.class.getResourceAsStream(itemsImage));
			numTilesAcross = (int)tileset.getWidth() / tileSize;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The method initializes canvas and takes the snapshot.
	 */
	public void initialiseCanvas() {
		mainCanvas = new Canvas(640,640);
		tileType = new int[numRows][numCols];
		cursor = new MyCursor();

		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				if(mapMatrix[row][col] == 0) continue;

				int rc = mapMatrix[row][col];

				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;

				if (r == 0) {
					mainCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, 0, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					tileType[row][col] = 0;
				}
				else {
					mainCanvas.getGraphicsContext2D().drawImage(
							tileset, c * tileSize, tileSize, tileSize, tileSize,
							col * tileSize, row * tileSize, tileSize, tileSize);
					tileType[row][col] = 1;
				}

			}
		}
		originalMapImage = mainCanvas.snapshot(null, null);
		drawCursorToMainCanvas();
		mainCanvas.snapshot(null, null);
	}

	/**
	 * The method is used to delete the cursor image from main canvas. To achieve it, redraw the tile
	 * from the original image.
	 */
	private void replaceTileInMainCanvasToOriginal(int col, int row) {
		mainCanvas.getGraphicsContext2D().drawImage(
				originalMapImage,
				col * tileSize,
				row * tileSize,
				tileSize, tileSize,
				col * tileSize,
				row * tileSize,
				tileSize, tileSize);
	}

	/**
	 * The method is used to draw the cursor image to main canvas.
	 */
	private void drawCursorToMainCanvas() {
		mainCanvas.getGraphicsContext2D().drawImage(
				cursor.imageOption[cursor.current], 0, 0,
				tileSize, tileSize,
				cursor.cursorCols * tileSize,
				cursor.cursorRows * tileSize,
				tileSize, tileSize);
	}

	/**
	 * After press `U` or `I`, the user is very easy to observe the cursor color changed to red/green
	 * so that they know whether this position is available or not. This method is used to change the
	 * cursor color after each move during the button pressed process.
	 *
	 * @see MyCursor
	 * @see #turningOnCursorColor()
	 */
	private void changeCursorColor() {
		if (cursorColor == true) {
			cursor.current = tileType[cursor.cursorRows][cursor.cursorCols];
		}
		else {
			cursor.current = 2;
		}
	}

	/**
	 * The method is used to change the cursor color after button pressed for puting axe or boat.
	 */
	public void turningOnCursorColor() {
		cursorColor = true;

		changeCursorColor();

		replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

		updateItemsDraw();
		drawCursorToMainCanvas();
		mainCanvas.snapshot(null, null);
	}

	/**
	 * The method is used to move cursor up.
	 */
	public void cursorUp() {
		if (cursor.cursorRows > 0) {
			replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

			cursor.cursorRows --;
			changeCursorColor();

			updateItemsDraw();
			drawCursorToMainCanvas();

			mainCanvas.snapshot(null, null);
		}
	}
	/**
	 * The method is used to move cursor down.
	 */
	public void cursorDown() {
		if (cursor.cursorRows < numRows - 1 ) {
			replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

			cursor.cursorRows ++;
			changeCursorColor();

			updateItemsDraw();
			drawCursorToMainCanvas();

			mainCanvas.snapshot(null, null);
		}
	}
	/**
	 * The method is used to move cursor left.
	 */
	public void cursorLeft() {
		if (cursor.cursorCols > 0) {
			replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

			cursor.cursorCols --;
			changeCursorColor();

			updateItemsDraw();
			drawCursorToMainCanvas();

			mainCanvas.snapshot(null, null);
		}
	}
	/**
	 * The method is used to move cursor right.
	 */
	public void cursorRight() {
		if (cursor.cursorCols < numCols - 1 ) {
			replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

			cursor.cursorCols ++;
			changeCursorColor();

			updateItemsDraw();
			drawCursorToMainCanvas();

			mainCanvas.snapshot(null, null);
		}
	}

	/**
	 * The method is used to draw the already-put items each time the map is changed.
	 */
	private void updateItemsDraw() {
		if (axePut) {
			mainCanvas.getGraphicsContext2D().drawImage(
					items,
					AXE  * tileSize, tileSize, tileSize, tileSize,
					axeCol * tileSize,
					axeRow * tileSize,
					tileSize, tileSize);
		}
		if (boatPut) {
			mainCanvas.getGraphicsContext2D().drawImage(
					items,
					BOAT  * tileSize, tileSize, tileSize, tileSize,
					boatCol * tileSize,
					boatRow * tileSize,
					tileSize, tileSize);
		}
	}

	/**
	 * The method is used to handle the event that `U` is released, i.e. the user
	 * chooses the position to put the AXE. The cursor color will back to gray. It
	 * will return relative message to update the information shown in the information bar
	 * under the map.
	 *
	 * @return handleType Return the type of setting result.
	 */
	public int handleSetAxeRequest() {
		int handleType;
		cursorColor = false;
		changeCursorColor();

		replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

		// return type: Position invalid
		if (tileType[cursor.cursorRows][cursor.cursorCols] == 1) {
			handleType = 1;
		}
		// return type: Axe put successful
		else {
			if (axePut) {
				replaceTileInMainCanvasToOriginal(axeCol, axeRow);

				tileType[axeRow][axeCol] = 0;
				tileType[cursor.cursorRows][cursor.cursorCols] = 1;

				handleType = 2;
			}
			else {
				handleType = 0;
			}

    		axePut = true;
	    	tileType[cursor.cursorRows][cursor.cursorCols] = 1;

	    	axeRow = cursor.cursorRows;
	    	axeCol = cursor.cursorCols;
		}

		updateItemsDraw();
    	drawCursorToMainCanvas();

    	mainCanvas.snapshot(null, null);

    	return handleType;
	}

	/**
	 * The method is used to handle the event that `I` is released, i.e. the user
	 * chooses the position to put the BOAT. The cursor color will back to gray. It
	 * will return relative message to update the information shown in the information bar
	 * under the map.
	 *
	 * @return handleType Return the type of setting result.
	 */
	public int handleSetBoatRequest() {
		int handleType;
		cursorColor = false;
		changeCursorColor();

		replaceTileInMainCanvasToOriginal(cursor.cursorCols, cursor.cursorRows);

		// return type: Position invalid
		if (tileType[cursor.cursorRows][cursor.cursorCols] == 1) {
			handleType = 1;
		}
		// return type: Boat put successful
		else {
			if (boatPut) {
				replaceTileInMainCanvasToOriginal(boatCol, boatRow);

				tileType[boatRow][boatCol] = 0;
				tileType[cursor.cursorRows][cursor.cursorCols] = 1;

				handleType = 2;
			}
			else {
				handleType = 0;
			}

    		boatPut = true;
	    	tileType[cursor.cursorRows][cursor.cursorCols] = 1;

	    	boatRow = cursor.cursorRows;
	    	boatCol = cursor.cursorCols;

		}
		updateItemsDraw();
		drawCursorToMainCanvas();
    	mainCanvas.snapshot(null, null);
    	return handleType;
	}
}
