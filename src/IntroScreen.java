/* The intro screen class creates an intro screen and contains contains the private
 * variables and functions necessary to implement the public method Animate which 
 * will animate the intro screen
 */

import acm.graphics.GCompound;
import acm.graphics.GImage;

public class IntroScreen extends GCompound implements PokemehranConstants {

	// Creates GImage variables for animations
	private GImage background = new GImage("IntroScreen\\introNoBird.jpg");
	private GImage clickToStart = new GImage("IntroScreen\\clickToStart.jpg");
	private GImage sparkle = new GImage("IntroScreen\\sparkle.png");
	private GImage sparkle1 = new GImage("IntroScreen\\sparkle.png");
	private GImage sparkle2 = new GImage("IntroScreen\\sparkle.png");
	private GImage sparkle3 = new GImage("IntroScreen\\sparkle.png");
	private GImage frame = new GImage("IntroScreen\\frame.png");
	// Sets values for locations of various animated parts on the intro screen
	private double birdXi=APPLICATION_WIDTH/2-BIRD.getWidth()/2;
	private double birdYi=2*APPLICATION_HEIGHT/3-BIRD.getHeight()/2;
	private double birdAmplitude = APPLICATION_HEIGHT/8;
	private double dyInit = birdAmplitude/20;
	private double pauseTime = 40;
	private boolean temp = true;
	private double dy = dyInit;
	private double dx = dyInit/1.3;
	private double sp1Xi = birdXi+BIRD.getWidth()-sparkle.getWidth();
	private double sp2Xi = sp1Xi+(APPLICATION_WIDTH-sp1Xi)/3;
	private double sp3Xi = sp1Xi+2*(APPLICATION_WIDTH-sp1Xi)/3;
	private double sp1Yi = birdYi-birdAmplitude+sparkle.getHeight();
	private double sp2Yi = birdYi+sparkle.getHeight();
	private double sp3Yi = birdYi + birdAmplitude+sparkle.getHeight();
	
	// Adds all images to canvas when the intro screen is created
	public IntroScreen() {
		add(background);
		add(clickToStart,APPLICATION_WIDTH/2-clickToStart.getWidth()/2,APPLICATION_HEIGHT - clickToStart.getHeight()*2);
		add(sparkle1,sp1Xi,sp1Yi);
		add(sparkle2,sp2Xi,sp2Yi);
		add(sparkle3,sp3Xi,sp3Yi);
		add(BIRD,birdXi,birdYi);
		add(frame);
	}
	
	// Public method that is called to animate the intro screen after it is created
	public void animate() {
		animateBird();
		animateSparkle();
		pause(pauseTime);
			
	}
	
	// This method animates the three sparkles coming out of the tail of the bird on the intro screen
	private void animateSparkle() {
		// Moves sparkles to the right
		sparkle1.move(dx,0);
		sparkle2.move(dx,0);
		sparkle3.move(dx,0);
		// Resets sparkles after they move off screen to create continuous effect
		if (sparkle1.getX()>APPLICATION_WIDTH) {
			sparkle1.setLocation(sp1Xi,BIRD.getY()+sparkle.getHeight());
		}
		if (sparkle2.getX()>APPLICATION_WIDTH) {
			sparkle2.setLocation(sp1Xi,BIRD.getY()+sparkle.getHeight());
		}
		if (sparkle3.getX()>APPLICATION_WIDTH) {
			sparkle3.setLocation(sp1Xi,BIRD.getY()+sparkle.getHeight());
		}
	}
	
	// Animates the bird on the intro screen
	private void animateBird() {
		// Moves bird then checks to see if bird is within the set amplitude
		// and changes the motion of the bird accordingly
		BIRD.move(0,dy);
		if (temp==true)
			dy = dy-dy/4*((BIRD.getY()-birdYi)/birdYi);
		else 
			dy = dy+dy/4*((BIRD.getY()-birdYi)/birdYi);
		if (dy>0&&BIRD.getY()>birdYi+birdAmplitude) {
			dy=-dy;
			temp = false;
		}
		if (dy<0&&BIRD.getY()<birdYi-birdAmplitude) {
			dy=-dy;
			temp=true;
		}
		if (BIRD.getY()<birdYi&&BIRD.getY()+dy>birdYi) {
			dy = Math.abs(dy)/dy*dyInit;
		}
	}
	
}
