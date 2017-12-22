package com.neet.MapViewer.Main;

import javafx.scene.image.Image;

/**
 * This class contains the cursor used by the map.
 *
 * @author  Amirul Umar Bin Pandai, Joan Kabura, Hoi Fei Long
 * @since   2017-12-23
*/
public class MyCursor {
	/**
	 * The variable loads the cursor images.
	 */
	public Image[] imageOption;


	/**
	 * The variable decides which image to use. Gray, red or green cursor.
	 * Currently the cursor is gray.
	 */
	public int current = 2;

	public int cursorCols;
	public int cursorRows;

	public MyCursor() {
		imageOption = new Image[3];
		imageOption[0] = new Image(MyCursor.class.getResourceAsStream("/Sprites/cursor_green.gif"));
		imageOption[1] = new Image(MyCursor.class.getResourceAsStream("/Sprites/cursor_red.gif"));
		imageOption[2] = new Image(MyCursor.class.getResourceAsStream("/Sprites/cursor_normal.gif"));
		cursorCols = 17;
		cursorRows = 17;

	}

}
