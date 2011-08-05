/*
 *  Creates the new continue screen after the introduction
 */

import acm.graphics.GCompound;
import acm.graphics.GImage;

public class newContinue extends GCompound implements PokemehranConstants {
	
	// Sets images for the frame, background and pointer
	private GImage frame = new GImage("newContinue\\frame.png");
	private GImage display = new GImage("newContinue\\newContinueBackground2.png");
	private GImage pointer = new GImage("newContinue\\pointer.png");
	// Sets pointer locations and whether the player is starting a new game
	private double pointerXi = 160;
	private double pointerYi = 85;
	private double pointerY1 = 185;
	private boolean newGameSelected = true;
	
	// Creates the new continue object
	public newContinue() {
		add(display);
		add(frame);
		add(pointer,pointerXi,pointerYi);
	}
	
	// returns true if the new game option is selected
	public boolean getNewGameSelected() {
		return newGameSelected;
	}
	
	// moves pointer when called. Moves up if on lower option, moves down
	// if on the upper option
	public void movePointer() {
		if (newGameSelected == true) {
			newGameSelected = false;
			pointer.setLocation(pointerXi,pointerY1);
		}
		else {
			newGameSelected = true;
			pointer.setLocation(pointerXi,pointerYi);
		}
	}
	
	
}
