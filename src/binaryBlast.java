/* binary blast creates the animation used in battle when the character
 * attacks. It must be called in multiple threads to create the desired 
 * random blast patter effect.
 */

import java.awt.Font;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.util.RandomGenerator;


public class binaryBlast extends GCompound implements PokemehranConstants, Runnable {

	// Creates private variables that measure change in x, y. Also creates random generator
	private RandomGenerator rgen = new RandomGenerator().getInstance();
	private int digit;
	private double xi;
	private double yi;
	private double xf;
		
	// Creating a binary blast object allows it to be animated using run
	public binaryBlast(double x1,double y1,double x2) {
		// randomly selects the digit 1 or 0 and then sets its starting point equal
		// to the values given
		digit = rgen.nextInt(0,1);
		xi = x1;
		yi = y1;
		xf = x2;
	}
	
	// If run is called multiple times then all the threads together create
	// the animated effect of binary blast
	public void run() {
		// used to animate one label in binary blast
		GLabel binary = new GLabel(""+digit,xi,yi);
		binary.setFont(new Font("Monospaced",Font.BOLD,48));
		add(binary);
		while(binary.getX()<xf) {
			binary.move(rgen.nextInt(2,30), -1*rgen.nextInt(-10,30));
			pause(15);
		}
	}
	
}
