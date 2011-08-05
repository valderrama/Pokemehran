import acm.graphics.GImage;


public interface PokemehranConstants {

	// Set the standard width and height in pixels for one square on the map
	public static final int UNIT_W = 25;
	public static final int UNIT_H = 25;
	// Set the number of squares that make up the width and height of the screen
	public static final int N_UNITS_W = 25;
	public static final int N_UNITS_H = 25;
	// Calculates the application height and width
	public static final int APPLICATION_WIDTH = N_UNITS_W*UNIT_W;
	public static final int APPLICATION_HEIGHT = N_UNITS_H*UNIT_H+25;
	// Standard pause time
	public static final int PAUSE_TIME = 30;
	// Enumerates values to be used with keystrokes a, s, d and w
	public static final int A = 0;
	public static final int S = 1;
	public static final int D = 2;
	public static final int W = 3;
	// Set GImage values for the the characters
	public static final GImage CHAR_FRONT = new GImage("characters\\characterFront.png");
	public static final GImage CHAR_BACK = new GImage("characters\\characterBack.png");
	public static final GImage CHAR_LEFT = new GImage("characters\\characterLeft.png");
	public static final GImage CHAR_RIGHT = new GImage("characters\\characterRight.png");
	public static final GImage CHAR_SLEEPING = new GImage("characters\\characterSleeping.png");
	public static final GImage BIRD = new GImage("IntroScreen\\birdMehran.png");
	// Set GImage values for frames used in menus and battle scenes
	public static final GImage P_FRAME = new GImage("battle\\playerBattleFrame.png");
	public static final GImage E_FRAME = new GImage("battle\\enemyBattleFrame.png");
	public static final GImage M_FRAME = new GImage("battle\\messageBattleFrame.png");
	public static final GImage I_FRAME = new GImage("battle\\playerItemFrame.png");
	public static final GImage B_FRAME = new GImage("battle\\blankBattleFrame.png");
	public static final GImage B2_FRAME = new GImage("battle\\blankBattleFrame2.png");
	// Set GImage values for other characters and menu
	public static final GImage MEHRAN_SPRITE = new GImage("pokemon\\mehran.png");
	public static final GImage SLEEP_BUBBLE = new GImage("characters\\sleepBubble.png");
	public static final GImage EXC_BUBBLE = new GImage("characters\\speechBubble.png");
	public static final GImage MAFIA_FRONT = new GImage("characters\\mafiaFront.png");
	public static final GImage MAFIA_BACK = new GImage("characters\\mafiaBack.png");
	public static final GImage MAFIA_PROFILE = new GImage("characters\\mafiaProfile.png");
	public static final GImage MEHRAN_PROFILE = new GImage("menu\\mehranProfile.png");
	public static final GImage MENU_BACK = new GImage("menu\\menuBack.png");
	// Set values for constant ints like battle probability, frame height and width and prices
	public static final int BATTLE_PROB = 30;
	public static final int PBF_HEIGHT = 200;
	public static final int EBF_HEIGHT = 125;
	public static final int EBF_WIDTH = 313;
	public static final int FRAME_WIDTH = 13;
	public static final int TEXT_BUFFER = 5;
	public static final int PHP_BAR_WIDTH = APPLICATION_WIDTH-FRAME_WIDTH*2-TEXT_BUFFER*2;
	public static final int EHP_BAR_WIDTH = EBF_WIDTH-FRAME_WIDTH*2-TEXT_BUFFER*2;
	public static final int NUM_NEWGAME_SLIDES = 5;
	public static final int XP_PRICE = 100;
	public static final int P_PRICE = 25;
	public static final int AU_PRICE = 250;
	public static final int DU_PRICE = 250;
	
		
}
