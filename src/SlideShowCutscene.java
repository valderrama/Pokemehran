/*
 *  Creates the slideshow cutscene after the player starts a new game
 */

import java.util.ArrayList;

import acm.graphics.GCompound;
import acm.graphics.GObject;


public class SlideShowCutscene extends GCompound implements PokemehranConstants {

	// Creates the array list containg all the slides in the cutscene
	ArrayList<GObject> slides;
	int atSlideX;
	
	// Takes in the slides for and sets the tracker to the intial slide at 0
	public SlideShowCutscene(ArrayList<GObject> slidesInput) {
		slides = slidesInput;
		atSlideX=0;
	}
	
	// Moves to the next slide
	public void nextSlide() {
		removeAll();
		add(slides.get(atSlideX));
		atSlideX++;
		if(atSlideX>slides.size()) {
			atSlideX=0;
		}
	}
	
	// manually set location of slide tracker
	public void setAtSlideX(int index) {
		atSlideX = index;
	}
	
	// returns number of slides
	public int slideShowLength() {
		return slides.size();
	}
}
