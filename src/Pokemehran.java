/*
 * This is the main class of the Pokemehran program.
 * Pokemehran handles the main game play loop which checks for actions performed.
 */

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GObject;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.MediaTools;
import acm.util.RandomGenerator;


public class Pokemehran extends GraphicsProgram implements PokemehranConstants {
	
	// These ints and doubles track the location of the character on screen, and 
	// also which music track is playing
	private int charX;
	private int charY;
	private int charXprev;
	private int charYprev;
	private int musicNo;
	private double charXpixel;
	private double charYpixel;
	// These booleans track data about the characters, like whether it is centered
	// vertically or horizontally, whether the mouse is clicked, etc.
	private boolean characterCenteredH=true;
	private boolean characterCenteredV=true;
	private boolean notClicked = true;
	private boolean hasEscaped = false;
	private boolean inMenu = false;
	private boolean takeCommand = false;
	private boolean currentlyHealing = false;
	private boolean paidMafia = false;
	private boolean playerWon;
	private boolean inBattle = false;
	private boolean sameSquare = false;
	private boolean inPortal = false;
	private boolean buyer = false;
	private boolean beatGame = false;
	private boolean lostGame = false;
	// This char tracks what key the player has pressed for character motion
	private char motionDirection;
	// These bool arrays track whether the character or the screen is at an edge, and also
	// whether a direction is walkeable
	private boolean[] charEdgeReached = new boolean[4];
	private boolean[] edgeReached = new boolean[4];
	private boolean[] walkeable = new boolean[4];
	// These GImages store the image for characters
	private GImage character=CHAR_FRONT;
	private GImage mafia;
	private GImage mehranSprite = new GImage("pokemon\\mehran.png");
	private GImage enemySprite;
	// These labels track data about the character and enemies in battle
	private GLabel mHP;
	private GLabel eHP;
	private GLabel mInfo;
	private GLabel eInfo;
	private GLabel battleMessage = new GLabel("");
	// These GRects show HP during the battles
	private GRect mHPbar;
	private GRect eHPbar;
	// These backgrounds store data about each different map
	private Background town; 
	private Background storeI;
	private Background currentBack;
	private Background storeA;
	private Background storeB;
	private Background storeC;
	private Background storeD;
	private Background storeE;
	private Background storeF;
	private Background storeG;
	private Background storeH;
	private Background wild9;
	private Background wild8;
	private Background wild7;
	private Background wild6;
	private Background wild4;
	private Background wild1;
	// These menus store data for either the paus or item store menu
	private Menu pauseMenu;
	private Menu itemStoreMenu;
	// These Strings input from the user
	private String wantsToHeal = "";
	private String battleCommand = "";
	private String characterPressed = "";
	private String menuCharacterSelected ="";
	private String menuCommandSelected ="";
	// These two dimensional string arrays track data about the current map
	private String[][] mapArray;
	private String[][] walkeableArray;
	// These AudioClips store the sound for the game
	private AudioClip mainTheme = MediaTools.loadAudioClip("101-opening.wav");
	private AudioClip preludeTheme = MediaTools.loadAudioClip("IntroScreen\\prelude.wav");
	private AudioClip fighting1 = MediaTools.loadAudioClip("battle\\fighting1.wav");
	private AudioClip fighting2 = MediaTools.loadAudioClip("battle\\fighting2.wav");
	private AudioClip fighting3 = MediaTools.loadAudioClip("battle\\fighting3.wav");
	private AudioClip fighting4 = MediaTools.loadAudioClip("battle\\fighting4.wav");
	private AudioClip fighting5 = MediaTools.loadAudioClip("battle\\fighting5.wav");
	private AudioClip fighting6 = MediaTools.loadAudioClip("battle\\finalbattle.wav");
	private AudioClip victory = MediaTools.loadAudioClip("battle\\victory.wav");
	private AudioClip binaryBlastSound = MediaTools.loadAudioClip("battle\\binaryBlastSound.wav");
	private AudioClip tackle = MediaTools.loadAudioClip("battle\\tackle.wav");
	private AudioClip mehranSound = MediaTools.loadAudioClip("battle\\mehranSound.wav");
	private AudioClip healSound = MediaTools.loadAudioClip("healing\\healSound.wav");
	private AudioClip healSong = MediaTools.loadAudioClip("healing\\healSong.wav");
	// These PokemehranCharacters store data about Mehran and his enemies
	private PokemehranCharacter mehran = new PokemehranCharacter("pokemon\\mehran");
	private PokemehranCharacter enemy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private HashMap<String,Background> nameToBack = new HashMap<String,Background>();
	
	public static void main(String args[]) {
		new Pokemehran().start(args);
	}
	
	public void init() {
		addKeyListeners();
		addMouseListeners();
	}
	
	public void mouseClicked(MouseEvent e) {
		notClicked = false;
	}
	
	// The keypressed key event is used to track input from the user on the keyboard
	public void keyPressed(KeyEvent e){
		characterPressed = ""+e.getKeyChar();
		if (inBattle == false && currentlyHealing == false && inMenu == false && inPortal == false) {
			motionDirection = e.getKeyChar();
		}
		if (inBattle == true && takeCommand == true) {
			battleCommand = ""+e.getKeyChar();
		}
		if (currentlyHealing == true) {
			if ((""+e.getKeyChar()).equals("y")) {
				wantsToHeal = "y";
			}
			else if((""+e.getKeyChar()).equals("n")) {
				wantsToHeal = "n";
			}
		}
		if (inMenu == true) {
			menuCharacterSelected = ""+e.getKeyChar();
		}
	}
	
	// The run method initially sets up the game and then has a main while loop
	// that checks for actions continuously until the game is either won or lost.
	public void run() {
		pregameSetup();
		createInitialBackground();
		while (beatGame==false&&lostGame==false) {
			checkMenu();
			getCharacterLocation();
			checkPortal();
			getCharacterLocation();
			motionOnMap();
			getCharacterLocation();
			checkSameSquare();
			battleCheck();
			healingCheck();
			itemStoreCheck();
		}
		if(lostGame == true) {
			add(new GImage("wonLost\\lost.jpg"));
			pause(500);
			mainTheme.stop();
		}
		if(beatGame == true) {
			add(new GImage("wonLost\\won.jpg"));
			pause(500);
			mainTheme.stop();
		}
	}
	
	// This method checks whether the player is currently standing on a square for an item store
	private void itemStoreCheck() {
		if(charY>0&&(mapArray[charY-1][charX].equals("fi")||mapArray[charY-1][charX].equals("fj"))&&sameSquare==false) {
			// If the character is at a store the item menu is created
			// and the while loop continues until the player selects
			// the exit command
			itemMenuSetup(mapArray[charY-1][charX]);
			while(true) {
				if (menuCharacterSelected.equals("a")||
					menuCharacterSelected.equals("s")||
					menuCharacterSelected.equals("d")) {
						itemStoreMenu.setSelected(menuCharacterSelected);
						menuCharacterSelected = "";
				}
				menuCommandSelected = itemStoreMenu.getSelectedString();
				if(itemStoreMenu.atMainMenu() == true && menuCommandSelected.equals("Exit") && characterPressed.equals(" ")) {
					characterPressed = "";
					break;
				} else if(itemStoreMenu.atMainMenu() == true && menuCommandSelected.equals("Buy") && characterPressed.equals(" ")) {
					characterPressed = "";
					buyer = true;
					itemStoreMenu.switchMenu();
				} else if(itemStoreMenu.atMainMenu() == true && menuCommandSelected.equals("Sell") && characterPressed.equals(" ")) {
					characterPressed = "";
					buyer=false;
					itemStoreMenu.switchMenu();
				}
				checkBuySellMenuOptions();
				
			}
			itemMenuFinish();
		}
	}
	
	// Item Menu Setup takes in a string and determines which store the character
	// is currently at and creates a menu accordingly.
	private void itemMenuSetup(String store) {
		inMenu = true;
		String[] itemMenuSelections;
		String[] mainMenuSelections;
		if(store.equals("fi")) {
			mainMenuSelections = new String[3];
			mainMenuSelections[0]="Exit";
			mainMenuSelections[1]="Buy";
			mainMenuSelections[2]="Sell";
			itemMenuSelections = new String[3];
			itemMenuSelections[0]="Exit";
			itemMenuSelections[1]="Potion";
			itemMenuSelections[2]="X Potion";
		}
		else {
			mainMenuSelections = new String[3];
			mainMenuSelections[0]="Exit";
			mainMenuSelections[1]="Buy";
			mainMenuSelections[2]="Sell";
			itemMenuSelections = new String[3];
			itemMenuSelections[0]="Exit";
			itemMenuSelections[1]="Attack Up";
			itemMenuSelections[2]="Defense Up";
		}
		itemStoreMenu = new Menu(mehran,mainMenuSelections,itemMenuSelections);
		menuCommandSelected = "";
		add(itemStoreMenu);
		remove(currentBack);
		remove(character);
	}
	
	// This method resets all the variables concerning the item store
	// and removes the graphics for the item menu
	private void itemMenuFinish() {
		add(currentBack);
		add(character);
		remove(itemStoreMenu);
		inMenu = false;
		buyer = false;
	}
	
	// This method checks what options the player has in the buy and sell menu
	// and performs any transactions depending upon the players commmands
	private void checkBuySellMenuOptions() {
		// exits buy sell submenu
		if(itemStoreMenu.atItemMenu() == true && menuCommandSelected.equals("Exit") && characterPressed.equals(" ")) {
			characterPressed = "";
			itemStoreMenu.switchMenu();
		} 
		// performs but or sell actions for potion
		else if(itemStoreMenu.atItemMenu() == true && menuCommandSelected.equals("Potion") && characterPressed.equals(" ")) {
			characterPressed = "";
			if(mehran.getItem("Potion")>0 && buyer == false) {
				mehran.removeItem("Potion");
				mehran.moneyUp(P_PRICE/2);
				itemStoreMenu.updateInfoLabels();
				itemStoreMenu.updateItemLabels();
			}
			else if(mehran.getMoney()>P_PRICE && buyer == true) {
				mehran.addItem("Potion");
				mehran.moneyDown(P_PRICE);
				itemStoreMenu.updateInfoLabels();
				itemStoreMenu.updateItemLabels();
			}
		} 
		// performs but or sell actions for X potion
		else if(itemStoreMenu.atItemMenu() == true && menuCommandSelected.equals("X Potion") && characterPressed.equals(" ")) {
			characterPressed = "";
			if(mehran.getItem("X Potion")>0 && buyer == false) {
				mehran.removeItem("X Potion");
				mehran.moneyUp(XP_PRICE/2);
				itemStoreMenu.updateInfoLabels();
				itemStoreMenu.updateItemLabels();
			}
			else if(mehran.getMoney()>XP_PRICE && buyer == true) {
				mehran.addItem("X Potion");
				mehran.moneyDown(XP_PRICE);
				itemStoreMenu.updateInfoLabels();
				itemStoreMenu.updateItemLabels();
			}
		} 
		// performs but or sell actions for defense up
		else if(itemStoreMenu.atItemMenu() == true && menuCommandSelected.equals("Defense Up") && characterPressed.equals(" ")) {
			characterPressed = "";
			if(mehran.getItem("Defense Up")>0 && buyer == false) {
				mehran.removeItem("Defense Up");
				mehran.moneyUp(DU_PRICE/2);
				itemStoreMenu.updateInfoLabels();
				itemStoreMenu.updateItemLabels();
			}
			else if(mehran.getMoney()>DU_PRICE && buyer == true) {
				mehran.addItem("Defense Up");
				mehran.moneyDown(DU_PRICE);
				itemStoreMenu.updateInfoLabels();
				itemStoreMenu.updateItemLabels();
			}
		} 
		// performs but or sell actions for attack up
		else if(itemStoreMenu.atItemMenu() == true && menuCommandSelected.equals("Attack Up") && characterPressed.equals(" ")) {
			characterPressed = "";
			if(mehran.getItem("Attack Up")>0 && buyer == false) {
				mehran.removeItem("Attack Up");
				mehran.moneyUp(AU_PRICE/2);
				itemStoreMenu.updateInfoLabels();
				itemStoreMenu.updateItemLabels();
			}
			else if(mehran.getMoney()>AU_PRICE && buyer == true) {
				mehran.addItem("Attack Up");
				mehran.moneyDown(AU_PRICE);
				itemStoreMenu.updateInfoLabels();
				itemStoreMenu.updateItemLabels();
			}
		}
	}
	
	// this method prints out a save file to continue.txt and overwrites any previous
	// save file allowing the player to continue from where they left off
	private void saveGame() {
		try {
			PrintWriter save = new PrintWriter(new FileWriter("continue.txt"));
			save.println(mehran.getName());
			save.println(mehran.getImageFile());
			save.println(mehran.getAttackName());
			save.println(mehran.getAttack());
			save.println(mehran.getDefense());
			save.println(mehran.getSpeed());
			save.println(mehran.getHPMax());
			save.println(mehran.getHPCur());
			save.println(mehran.getLevel());
			save.println(mehran.getExperience());
			save.println(mehran.getToNextLevel());
			save.println(mehran.getMoney());
			save.println(mehran.getItem("Potion"));
			save.println(mehran.getItem("X Potion"));
			save.println(mehran.getItem("Attack Up"));
			save.println(mehran.getItem("Defense Up"));
			save.println(currentBack.toString());
			save.println(currentBack.getX());
			save.println(currentBack.getY());
			save.println(character.getX());
			save.println(character.getY());
			save.close();
		} catch(IOException ex) {
		}
	}
	
	// This method sets up the pause menu with the appropriate options
	private void menuSetup() {
		inMenu = true;
		String[] mainMenuSelections = new String[3];
		mainMenuSelections[0]="Exit";
		mainMenuSelections[1]="Save";
		mainMenuSelections[2]="Item";
		String[] itemMenuSelections = new String[3];
		itemMenuSelections[0]="Exit";
		itemMenuSelections[1]="Potion";
		itemMenuSelections[2]="X Potion";
		pauseMenu = new Menu(mehran,mainMenuSelections,itemMenuSelections);
		menuCommandSelected = "";
		add(pauseMenu);
		remove(currentBack);
		remove(character);
	}
	//closes out pause menu
	private void menuFinish() {
		add(currentBack);
		add(character);
		remove(pauseMenu);
		inMenu = false;
	}
	//checks if player uses item while in pause menu and updates
	// the inventory and menu accordingly
	private void checkItemMenuOptions() {
		if(pauseMenu.atItemMenu() == true && menuCommandSelected.equals("Exit") && characterPressed.equals(" ")) {
			characterPressed = "";
			pauseMenu.switchMenu();
		} else if(pauseMenu.atItemMenu() == true && menuCommandSelected.equals("Potion") && characterPressed.equals(" ")) {
			characterPressed = "";
			if(mehran.getItem("Potion")>0) {
				mehran.hpUp(50);
				mehran.removeItem("Potion");
				pauseMenu.updateInfoLabels();
				pauseMenu.updateItemLabels();
			}
		} else if(pauseMenu.atItemMenu() == true && menuCommandSelected.equals("X Potion") && characterPressed.equals(" ")) {
			characterPressed = "";
			if(mehran.getItem("X Potion")>0) {
				mehran.heal();
				mehran.removeItem("X Potion");
				pauseMenu.updateInfoLabels();
				pauseMenu.updateItemLabels();
			}
		}
	}
	
	// This method checks if the player has opened the menu and creates the menu if they have
	// it also loops continuously while the player selectes menu options and exits
	// once the player has selected exit
	private void checkMenu() {
		if(characterPressed.equals("p")) {
			menuSetup();
			
			while(true) {
				if (menuCharacterSelected.equals("a")||
					menuCharacterSelected.equals("s")||
					menuCharacterSelected.equals("d")) {
						pauseMenu.setSelected(menuCharacterSelected);
						menuCharacterSelected = "";
				}
				menuCommandSelected = pauseMenu.getSelectedString();
				if(pauseMenu.atMainMenu() == true && menuCommandSelected.equals("Exit") && characterPressed.equals(" ")) {
					characterPressed = "";
					break;
				} else if(pauseMenu.atMainMenu() == true && menuCommandSelected.equals("Save") && characterPressed.equals(" ")) {
					saveGame();
					add(B2_FRAME);
					GLabel message = new GLabel("Game Saved!");
					message.setFont(new Font("helvetica",Font.PLAIN,30));
					message.setLocation(B2_FRAME.getWidth()/2-message.getWidth()/2,B2_FRAME.getHeight()/2+message.getAscent()/3);
					add(message);
					pause(1000);
					remove(message);
					remove(B2_FRAME);
					characterPressed = "";
				} else if(pauseMenu.atMainMenu() == true && menuCommandSelected.equals("Item") && characterPressed.equals(" ")) {
					characterPressed = "";
					pauseMenu.switchMenu();
				}
				checkItemMenuOptions();
			}
			menuFinish();
		}
	}
	
	// This method checks if the player is at a park bench to heal themselves
	private void healingCheck() {
		String check = ""+mapArray[charY][charX].charAt(0);
		if (check.equals("h")==true && sameSquare == false) {
			mainTheme.stop();
			healSong.loop();
			currentlyHealing = true;
			checkWantToRest();
			paidMafia = false;
			currentlyHealing = false;
			healSong.stop();
			mainTheme.loop();
		}
	}
	
	// This method checks if the player selects y to heal themselves at the
	// park bench and responds accordingly
	private void checkWantToRest() {
		add(B2_FRAME);
		GLabel message = new GLabel("Do you want to rest? (y/n)");
		message.setFont(new Font("helvetica",Font.PLAIN,30));
		message.setLocation(B2_FRAME.getWidth()/2-message.getWidth()/2,B2_FRAME.getHeight()/2+message.getAscent()/3);
		add(message);
		wantsToHeal = "";
		while(true){
			if (wantsToHeal.equals("y")) {
				wantsToHeal = "";
				healingAnimation();
				break;
			}
			else if(wantsToHeal.equals("n")) {
				break;
			}
		}
		remove(message);
		remove(B2_FRAME);
	}
	
	// this method provides the sleeping animation that occurs while the player heals
	private void healingAnimation() {
		double x = character.getX();
		double y = character.getY();
		remove(character);
		character = CHAR_SLEEPING;
		add(character,x,y);
		add(SLEEP_BUBBLE,character.getX(),character.getY()-UNIT_H);
		pause(2000);
		fadeToBlack();
	}
	
	// this method cause the fade to black animation when the mafia character enters
	private void fadeToBlack() {
		remove(SLEEP_BUBBLE);
		remove(character);
		GRect n = new GRect(APPLICATION_WIDTH,APPLICATION_HEIGHT);
		GRect s = new GRect(APPLICATION_WIDTH,APPLICATION_HEIGHT);
		GRect e = new GRect(APPLICATION_WIDTH,APPLICATION_HEIGHT);
		GRect w = new GRect(APPLICATION_WIDTH,APPLICATION_HEIGHT);
		n.setLocation(0,-APPLICATION_HEIGHT);
		s.setLocation(0,APPLICATION_HEIGHT);
		e.setLocation(APPLICATION_WIDTH,0);
		w.setLocation(-APPLICATION_WIDTH,0);
		n.setFilled(true);
		s.setFilled(true);
		e.setFilled(true);
		w.setFilled(true);
		add(n);
		add(s);
		add(e);
		add(w);
		add(character);
		add(SLEEP_BUBBLE);
		for(int i=0 ; i<(int)(APPLICATION_WIDTH/2+1) ; i++) {
			n.move(0,1);
			s.move(0,-1);
			e.move(-1,0);
			w.move(1,0);
			pause(5);
		}
		enterMafiaAnimation();
		if (paidMafia == true) {
			remove(character);
			character = CHAR_SLEEPING;
			add(character);
			add(SLEEP_BUBBLE);
			mehran.heal();
			healSong.stop();
			healSound.play();
			pause(3500);
			remove(SLEEP_BUBBLE);
		}
		
		remove(n);
		remove(s);
		remove(e);
		remove(w);
	}
	
	// This method animates the entrance of the mafia member and asks the character
	// for a response on whether they want to heal or not. the animation then responds
	// according to the characters action
	private void enterMafiaAnimation() {
		mafia = MAFIA_BACK;
		add(mafia,character.getX(),APPLICATION_HEIGHT);
		for (int i=0 ; i<(int)character.getY() ; i++) {
			mafia.move(0,-1);
			pause(10);
		}
		pause(1000);
		
		remove(SLEEP_BUBBLE);
		remove(character);
		character = CHAR_FRONT;
		add(character);
		add(EXC_BUBBLE,SLEEP_BUBBLE.getX(),SLEEP_BUBBLE.getY());
		pause(1000);
		
		remove(EXC_BUBBLE);
		add(B_FRAME);
		add(MAFIA_PROFILE,FRAME_WIDTH+TEXT_BUFFER,FRAME_WIDTH+TEXT_BUFFER);
		GLabel message = new GLabel("This is our turf, so if you want to sleep here our");
		message.setFont(new Font("helvetica",Font.PLAIN,20));
		message.setLocation
			(MAFIA_PROFILE.getX()+MAFIA_PROFILE.getWidth()+TEXT_BUFFER,FRAME_WIDTH+TEXT_BUFFER+message.getAscent());
		GLabel message2 = new GLabel("protection fee is $50 per night, wanna rest? (y/n)");
		message2.setFont(new Font("helvetica",Font.PLAIN,20));
		message2.setLocation(message.getX(),message.getY()+message.getAscent()+TEXT_BUFFER);
		add(message);
		add(message2);
		wantsToHeal = "";
		while(true){
			if (wantsToHeal.equals("y")) {
				remove(message);
				remove(message2);
				wantsToHeal = "";
				if(mehran.getMoney()>=50) {
					message.setLabel("You made the right choice. Sleep well...");
					add(message);
					paidMafia=true;
					mehran.moneyDown(50);
				}
				else {
					message.setLabel("You're gonna need more money than that...");
					add(message);
					paidMafia=false;
					break;
				}
				break;
			}
			else if(wantsToHeal.equals("n")) {
				remove(message);
				remove(message2);
				message.setLabel("I better not catch you snoozing -- or else...");
				add(message);
				paidMafia = false;
				break;
			}
		}
		pause(2000);
		
		remove(B_FRAME);
		remove(message);
		remove(MAFIA_PROFILE);
		remove(mafia);
		mafia = MAFIA_FRONT;
		add(mafia,MAFIA_BACK.getX(),MAFIA_BACK.getY());
		for (int i=0 ; i<(int)character.getY() ; i++) {
			mafia.move(0,1);
			pause(10);
		}
	}
	
	// This method is called whenever a battle begins, it adds music animations
	// sets up the battle, and then calls perform battle. Afterwards it cleans up the
	// battle graphics and saves any data from the battle.
	private void battle() {
		addMusic();
		battleIntroAnimation();
		removeAll();
		setupBattle();
		characterEntrance();
		performBattle();
		stopMusic();
		getDataForBattle();
		addWorldMapAfterBattle();
	}
	
	// This method randomly selects a fighting song when a battle begins
	private void addMusic() {
		mainTheme.stop();
		if(currentBack==storeI){
			fighting6.loop();
		}
		else {musicNo = rgen.nextInt(1,5);
		switch(musicNo) {
		case 1: fighting1.loop();
			break;
		case 2: fighting2.loop();
			break;
		case 3: fighting3.loop();
			break;
		case 4: fighting4.loop();
			break;
		case 5: fighting5.loop();
			break;
			}
		}
	}
	
	// this method stops the battle music
	private void stopMusic() {
		switch(musicNo) {
		case 1: fighting1.stop();
			break;
		case 2: fighting2.stop();
			break;
		case 3: fighting3.stop();
			break;
		case 4: fighting4.stop();
			break;
		case 5: fighting5.stop();
			break;
		}
	}
	
	// this method animates the entrance of the enemy character and mehran as the battle
	// begins.
	private void characterEntrance() {
		updateBattleMessage("A wild "+enemy.getName()+" appeared!");
		mehranSprite.setLocation(0-mehranSprite.getWidth(),mehranSprite.getY());
		enemySprite.setLocation(APPLICATION_WIDTH,FRAME_WIDTH+TEXT_BUFFER);
		pause(300);
		while (enemySprite.getX()>3*EBF_WIDTH/2-enemySprite.getWidth()/2) {
			enemySprite.move(-3,0);
			pause(5);
		}
		enemySprite.setLocation(3*EBF_WIDTH/2-enemySprite.getWidth()/2,enemySprite.getY());
		pause(200);
		while (mehranSprite.getX()>FRAME_WIDTH*2) {
			mehranSprite.move(3,0);
			pause(5);
		}
		mehranSprite.setLocation(FRAME_WIDTH*2,mehranSprite.getY());
		mehranSound.play();
	}
	
	// This animation covers the map screen in black squares after the program
	// begins a battle sequence
	private void battleIntroAnimation() {
		add(EXC_BUBBLE,character.getX(),character.getY()-UNIT_H);
		pause(500);
		int H = N_UNITS_H;
		int W = N_UNITS_W;
		int Hi = N_UNITS_H;
		int Wi = N_UNITS_W;
		int x = 0;
		int y = 0;
		while (H>0||W>0) {
			for(int i=0; i<W; i++) {
				x = (i+(Wi-W)/2)*UNIT_W;
				y = UNIT_H*(Hi-H)/2;
				add(new GImage("backgroundImages\\nn.jpg",x,y));
				pause(2);	
			}
			H--;
			for(int i=0 ; i<H ; i++) {
				x = (Wi-(Wi-W)/2-1)*UNIT_W;
				y = (i+1)*UNIT_H+(Hi-H)/2*UNIT_H;
				add(new GImage("backgroundImages\\nn.jpg",x,y));
				pause(2);
			}
			W--;
			for(int i=0; i<W; i++) {
				x = (Wi-(Wi-W)/2-2)*UNIT_W-UNIT_W*i;
				y = UNIT_H*(Hi-(Hi-H)/2-1);
				add(new GImage("backgroundImages\\nn.jpg",x,y));
				pause(2);
			}
			H--;
			for(int i=0 ; i<H ; i++) {
				x = UNIT_W*((Wi-W)/2);
				y = (Hi-(Hi-H)/2-i-1)*UNIT_H;
				add(new GImage("backgroundImages\\nn.jpg",x,y));
				pause(2);
			}
			W--;
		}
	}
	
	// this method sets up all the frames in the battle screen including
	// the players information frame, the enemy's information frame and 
	// the message frame
	private void setupBattle() {
		add(E_FRAME);
		add(P_FRAME,0,APPLICATION_HEIGHT-PBF_HEIGHT);
		add(M_FRAME,APPLICATION_WIDTH-M_FRAME.getWidth(),P_FRAME.getY()-M_FRAME.getHeight());
		
		mInfo = new GLabel("Lv."+mehran.getLevel()+" "+mehran.getName());
		eInfo = new GLabel("Lv."+enemy.getLevel()+" "+enemy.getName());
		mHP = new GLabel("HP: " + mehran.getHPCur() + " / " + mehran.getHPMax());
		eHP = new GLabel("HP: " + enemy.getHPCur() + " / " + enemy.getHPMax());
		mInfo.setFont(new Font("helvetica",Font.BOLD,24));
		eInfo.setFont(new Font("helvetica",Font.BOLD,24));
		mHP.setFont(new Font("helvetica",Font.BOLD,24));
		eHP.setFont(new Font("helvetica",Font.BOLD,24));
		battleMessage.setFont(new Font("helvetica",Font.BOLD,20));
		
		eHPbar = new GRect(enemy.getHPCur()*EHP_BAR_WIDTH/enemy.getHPMax(),eHP.getAscent());
		mHPbar = new GRect(mehran.getHPCur()*PHP_BAR_WIDTH/mehran.getHPMax(),mHP.getAscent());
		eHPbar.setFilled(true);
		mHPbar.setFilled(true);
		
		if (enemy.getHPCur()>enemy.getHPMax()/2) eHPbar.setFillColor(Color.GREEN);
		else if (enemy.getHPCur()>enemy.getHPMax()/5)	eHPbar.setFillColor(Color.ORANGE);
		else eHPbar.setFillColor(Color.RED);
		if (mehran.getHPCur()>mehran.getHPMax()/2) mHPbar.setFillColor(Color.GREEN);
		else if (mehran.getHPCur()>mehran.getHPMax()/5) mHPbar.setFillColor(Color.ORANGE);
		else mHPbar.setFillColor(Color.RED);
		enemySprite = new GImage(enemy.getImageFile());
		
		eInfo.setLocation(FRAME_WIDTH+TEXT_BUFFER,E_FRAME.getY()+FRAME_WIDTH+TEXT_BUFFER+eHP.getAscent());
		eHP.setLocation(eInfo.getX(),eInfo.getY()+eInfo.getAscent()+TEXT_BUFFER);
		eHPbar.setLocation(eHP.getX(),eHP.getY()+TEXT_BUFFER);
		mHPbar.setLocation(FRAME_WIDTH+TEXT_BUFFER,P_FRAME.getY()+FRAME_WIDTH+TEXT_BUFFER);
		mInfo.setLocation(mHPbar.getX(),mHPbar.getY()+mHPbar.getHeight()+mInfo.getAscent()+TEXT_BUFFER);
		mHP.setLocation(mInfo.getX(),mInfo.getY()+mInfo.getAscent()+TEXT_BUFFER);
		enemySprite.setLocation(3*EBF_WIDTH/2-enemySprite.getWidth()/2,FRAME_WIDTH+TEXT_BUFFER);
		mehranSprite.setLocation(FRAME_WIDTH*2,APPLICATION_HEIGHT-PBF_HEIGHT-mehranSprite.getHeight());
		battleMessage.setLocation(M_FRAME.getX()+M_FRAME.getWidth()/2-battleMessage.getWidth()/2,
								  M_FRAME.getY()+M_FRAME.getHeight()/2+battleMessage.getAscent()/2);
		
		add(mHP);
		add(eHP);
		add(mInfo);
		add(eInfo);
		add(mHPbar);
		add(eHPbar);
		add(battleMessage);
		add(enemySprite);
		add(mehranSprite);
		
		
	}
	
	// This updates the battle frames
	private void update() {
		removeAll();
		setupBattle();
	}
	
	// This method updates the message frame with the given message
	private void updateBattleMessage(String message) {
		remove(battleMessage);
		battleMessage.setLabel(message);
		battleMessage.setLocation(M_FRAME.getX()+M_FRAME.getWidth()/2-battleMessage.getWidth()/2,
				  M_FRAME.getY()+M_FRAME.getHeight()/2+battleMessage.getAscent()/2);
		add(battleMessage);
	}
	
	// This method loops until either mehran or the enemy is defeated
	// or the enemy or mehran escapes. It also ends the game if the player feints.
	private void performBattle() {
		while(enemy.getHPCur() > 0 && mehran.getHPCur() > 0 && hasEscaped == false) {
			if (mehran.getSpeed()>=enemy.getSpeed()) {
				playerBattleTurn();
				if(hasEscaped == true) break;
				if(enemy.getHPCur()==0) break;
				enemyBattleTurn();
			}
			else {
				enemyBattleTurn();
				if(mehran.getHPCur()==0) break;
				playerBattleTurn();
				if(hasEscaped == true) break;
			}
			update();
		}
		if (enemy.getHPCur()<=0)  {
			playerWon = true;
			updateBattleMessage("Enemy "+enemy.getName()+" feinted.");
			pause(500);
			animateFeint(enemySprite);
			updateBattleMessage("You Won!");
		}
		else if (hasEscaped == true) {
			
		}
		else {
			updateBattleMessage("Mehran feinted.");
			playerWon = false;
			pause(500);
			animateFeint(mehranSprite);
			updateBattleMessage("You Lost.");
		}
		pause(1500);
		hasEscaped = false;
	}
	
	// This method waits for the player to enter a valid command on their turn
	// and then performs the corresponding actions whether it's changing menus 
	// healing or attacking
	private void playerBattleTurn() {
		updateBattleMessage("Player's Turn");
		battleCommand = "";
		takeCommand=true;
		while(true) {
			if (battleCommand.equals("a")) {
				takeCommand = false;
				updateBattleMessage("Binary Blast!");
				binaryBlastAnimation();
				attack(mehran,enemy);
				battleCommand = "";
				break;
			}
			if (battleCommand.equals("s")) {
				takeCommand = false;
				battleCommand = "";
				item();
				if(battleCommand.equals("q")) {
					battleCommand = "";
					takeCommand = true;
				}
				else {
					battleCommand = "";
					break;
				}
			}
			if (battleCommand.equals("d")) {
				takeCommand = false;
				hasEscaped = escape(mehran,enemy);
				battleCommand = "";
				break;
			}
		}
		update();
		pause(1500);
		
	}
	
	// This method creates the binary blast animation by calling multiple threads that
	// each create either a single 1 or 0 that shoots across the screen
	private void binaryBlastAnimation() {
		for (int i=0 ; i<TEXT_BUFFER*2 ; i++) {
			mehranSprite.move(3,0);
			pause(10);
		}
		for (int i=0 ; i<25 ; i++) {
			binaryBlast bb = new binaryBlast
				(mehranSprite.getX()+mehranSprite.getWidth()/2,3*mehranSprite.getY()/2,APPLICATION_WIDTH);
			add(bb);
			Thread t = new Thread(bb);
			t.start();
			pause(100);
		}
		binaryBlastSound.play();
		animateShake(enemySprite);
	}
	
	// This animation shakes whichever image is passed to it. normally used after
	// either character is hit by an attack.
	private void animateShake(GImage sprite) {
		for(int i=0 ; i<10*4 ; i++) {
			switch (i%4) {
			case 0: sprite.move(-6, 0);
					pause(10);
				break;
			case 1: sprite.move(-6, 0);
					pause(10);
					break;
			case 2: sprite.move(6, 0);
					pause(10);
					break;
			case 3: sprite.move(6, 0);
					pause(10);
					break;
			}
		}
	}
	
	// This method animates the characters feinting. It works for both mehran or
	// his opponent
	private void animateFeint(GImage sprite) {
		if (sprite==mehranSprite) {
			for (int i=0; i<(int)mehranSprite.getHeight()+1 ; i++) {
				GLine t = new GLine(mehranSprite.getX(),mehranSprite.getY()+i,
						mehranSprite.getX()+mehranSprite.getWidth(),mehranSprite.getY()+i);
				t.setColor(Color.WHITE);
				add(t);
				pause(3);
			}
		}
		else {
			for (int i=0; i<(int)enemySprite.getHeight()+1 ; i++) {
				GLine t = new GLine(enemySprite.getX(),enemySprite.getY()+enemySprite.getHeight()-i,
						enemySprite.getX()+enemySprite.getWidth(),enemySprite.getY()+enemySprite.getHeight()-i);
				t.setColor(Color.WHITE);
				add(t);
				pause(3);
			}
		}
	}
	
	//This method animates the tackle attack used by enemy characters
	private void tackleAnimation() {
		for (int i=0 ; i<TEXT_BUFFER*3 ; i++) {
			enemySprite.move(-4,0);
			pause(5);
		}
		pause(250);
		tackle.play();
		animateShake(mehranSprite);
	}
	
	// This method takes in two pokemon characters designated as the attacker or defender
	// and determines what damage should be given to the defending character
	// by the attacker based upon their stats.
	private void attack(PokemehranCharacter attacker,PokemehranCharacter defender) {
		int attack = attacker.getAttack()+attacker.getTempAttack();
		int defense = defender.getDefense()+defender.getTempDefense();
		boolean criticalHit = rgen.nextBoolean(.05);
		int damageDone;
		if (attack-2 > defense && criticalHit == true) {
			damageDone = 18*(attack-defense+rgen.nextInt(-1,1))/10;
			defender.hpDown(damageDone);
			updateBattleMessage("Critical Hit for "+damageDone+" damage!");
		}
		else if(attack-2 > defense) {
			damageDone = attack-defense+rgen.nextInt(-1,1);
			defender.hpDown(damageDone);
			updateBattleMessage("Attack caused "+damageDone+" damage.");
		}
		else {
			damageDone = rgen.nextInt(1,4);
			defender.hpDown(damageDone);
			updateBattleMessage("Attack caused "+damageDone+" damage.");
		}		
	}
	
	// This method is called when the player chooses the item menu in battle
	// it creates a menu within the battle frame and allows the player to use
	// items.
	private void item() {
		add(I_FRAME,P_FRAME.getX(),P_FRAME.getY());
		takeCommand = true;
		while (true) {
			if (battleCommand.equals("q")) {
				takeCommand = false;
				remove(I_FRAME);
				break;
			}
			if (battleCommand.equals("w")&& mehran.getItem("Potion")>0) {
				takeCommand = false;
				
				updateBattleMessage("Player used a Potion.");
				pause(1000);
				updateBattleMessage("Mehran recovered some HP.");
				remove(I_FRAME);
				pause(600);
				mehran.hpUp(50);
				mehran.removeItem("Potion");
				
				battleCommand = "";
				break;
			}
			if (battleCommand.equals("e")&& mehran.getItem("X Potion")>0) {
				takeCommand = false;
				
				updateBattleMessage("Player used an X Potion.");
				pause(1000);
				updateBattleMessage("Mehran recovered all his HP.");
				remove(I_FRAME);
				pause(600);
				mehran.heal();
				mehran.removeItem("X Potion");
				
				battleCommand = "";
				break;
			}
			if (battleCommand.equals("s")&& mehran.getItem("Attack Up")>0) {
				takeCommand = false;
				
				updateBattleMessage("Player used Attack Up.");
				pause(1000);
				updateBattleMessage("Mehran's attack increased.");
				remove(I_FRAME);
				pause(600);
				mehran.tempAttackUp(8);
				mehran.removeItem("Attack Up");
				
				battleCommand = "";
				break;
			}
			if (battleCommand.equals("d")&& mehran.getItem("Defense Up")>0) {
				takeCommand = false;
				
				updateBattleMessage("Player used Defense Up.");
				pause(1000);
				updateBattleMessage("Mehran's defense increased.");
				remove(I_FRAME);
				pause(600);
				mehran.tempDefenseUp(8);
				mehran.removeItem("Defense Up");
				
				battleCommand = "";
				break;
			}
		}
	}
	
	// This method calculates whether a character can escape or not
	private boolean escape(PokemehranCharacter escapee, PokemehranCharacter pursuer) {
		updateBattleMessage("Trying to escape...");
		pause(1000);
		int check = rgen.nextInt(-3,pursuer.getSpeed()-escapee.getSpeed());
		if (check == 1 || check<=-3) {
			updateBattleMessage("Escaped!");
			pause(1000);
			return true;
		}
		updateBattleMessage("Couldn't Escape.");
		return false;
	}
	
	// this method takes care of the enemies turn, and causes it to either run if
	// it's level is to low or fight.
	private void enemyBattleTurn() {
		updateBattleMessage("Enemy "+enemy.getName()+"'s turn.");
		pause(rgen.nextInt(1000,2500));
		if(enemy.getLevel()<mehran.getLevel()/5) {
			run();
		}
		else {
			updateBattleMessage("Enemy "+enemy.getName()+" attacks!");
			pause(750);
			tackleAnimation();
			attack(enemy,mehran);
		}
		pause(1250);
		update();
	}
	
	// This method saves data after the battle like money, experience, etc
	// and adds it to mehrans permanent stats. It also levels him up if necessary
	// and informs the player of his winnings, exp. gained and stats gained
	private void getDataForBattle() {
		mehran.clearTempStats();
		
		if(playerWon == true) {
			if (currentBack==storeI) {
				beatGame=true;
			}
			else {
				victory.play();
			}
			mehran.expUp(enemy.getExperience());
			mehran.moneyUp(enemy.getMoney());
			
			GImage infoBack = new GImage("battle\\blankBattleFrame.png");
			add(infoBack);
			GLabel exp = new GLabel 
					("Mehran gained "+enemy.getExperience()+" exp. points.",eInfo.getX(),eInfo.getY());
			GLabel monies = new GLabel
					("Player received $"+enemy.getMoney()+".",eHP.getX(),eHP.getY());
			GLabel click2cont = new GLabel("Click to Continue",eHPbar.getX(),eHPbar.getY()+eHP.getAscent());
			exp.setFont(new Font("helvetica",Font.BOLD,28));
			monies.setFont(new Font("helvetica",Font.BOLD,28));
			click2cont.setFont(new Font("helvetica",Font.BOLD,28));
			add(exp);
			add(monies);
			add(click2cont);
			waitForClick();
			levelUpData();
			victory.stop();
			
		} else {
			lostGame = true;
		}
	}
	
	// this method calculates level up data for Mehran
	// and places it on the screen for the player to view
	private void levelUpData() {
		if (mehran.getToNextLevel() <= 0) {
			int aPrev = mehran.getAttack();
			int dPrev = mehran.getDefense();
			int sPrev = mehran.getSpeed();
			int hpPrev = mehran.getHPMax();
			mehran.levelUp();
			update();
			remove(enemySprite);
			add(B_FRAME);
			int aUp = mehran.getAttack()-aPrev;
			int dUp = mehran.getDefense()-dPrev;
			int sUp = mehran.getSpeed()-sPrev;
			int hpUp = mehran.getHPMax()-hpPrev;
			GLabel lvlUp = new GLabel("Mehran grew to level "+mehran.getLevel()+"!",eInfo.getX(),eInfo.getY());
			GLabel a = new GLabel("Attack increased by "+aUp+".",eHP.getX(),eHP.getY());
			GLabel d = new GLabel("Defense increased by "+dUp+".",eHPbar.getX(),eHPbar.getY()+eHP.getAscent());
			GLabel s = new GLabel("Speed increased by "+sUp+".",eHP.getX(),eHP.getY());
			GLabel hp = new GLabel("HP increased by "+hpUp+".",eHPbar.getX(),eHPbar.getY()+eHP.getAscent());
			lvlUp.setFont(new Font("helvetica",Font.BOLD,28));
			a.setFont(new Font("helvetica",Font.BOLD,24));
			d.setFont(new Font("helvetica",Font.BOLD,24));
			s.setFont(new Font("helvetica",Font.BOLD,24));
			hp.setFont(new Font("helvetica",Font.BOLD,24));
			add(lvlUp);
			add(a);
			add(d);
			pause(5000);
			remove(a);
			remove(d);
			add(s);
			add(hp);
			pause(2000);
			waitForClick();
		}	
	}	

	// This method randomly generates battles
	// and can randomly select which enemy appears
	private void battleCheck() {
		if (mapArray[charY][charX].equals("g-")||mapArray[charY][charX].equals("g0")||
				mapArray[charY][charX].equals("rz")||mapArray[charY][charX].equals("rx")||
				mapArray[charY][charX].equals("rc")||mapArray[charY][charX].equals("rv")) {
			if (currentBack==storeI && sameSquare==false) {
				inBattle=true;
				enemy=new PokemehranCharacter("pokemon\\skyNet");
				battle();
			}else if (rgen.nextInt(1,BATTLE_PROB)==1 && sameSquare == false) {
				inBattle = true;
				switch (rgen.nextInt(1,3)) {
				case 1: 
				case 2: enemy = new PokemehranCharacter("pokemon\\hardDrive");
					break;
				case 3: enemy = new PokemehranCharacter("pokemon\\iPhone");	
					break;
			}
				battle();
			}
		}
	}
	
	// this method adds back the map after a battle completes itself
	private void addWorldMapAfterBattle() {
		mainTheme.loop();
		add(currentBack);
		add(character);
		inBattle = false;
	}
	
	// this method calls three methods that load screens before the game begins
	private void pregameSetup() {
		loadMaps();
		introduction();
		newOrSavedGame();
	}

	// This method loads all the maps into their backgrounds and
	// stores their names in a hashmap that links to their background
	private void loadMaps() {
		town = new Background("town");
		storeI = new Background("storeI");
		storeA = new Background("storeA");
		storeB = new Background("storeB");
		storeC = new Background("storeC");
		storeD = new Background("storeD");
		storeE = new Background("storeE");
		storeF = new Background("storeF");
		storeG = new Background("storeG");
		storeH = new Background("storeH");
		wild9 = new Background("wild9");
		wild8 = new Background("wild8");
		wild7 = new Background("wild7");
		wild6 = new Background("wild6");
		wild4 = new Background("wild4");
		wild1 = new Background("wild1");
		nameToBack.put(town.toString(),town);
		nameToBack.put(storeI.toString(),storeI);
		nameToBack.put(storeA.toString(),storeA);
		nameToBack.put(storeB.toString(),storeB);
		nameToBack.put(storeC.toString(),storeC);
		nameToBack.put(storeD.toString(),storeD);
		nameToBack.put(storeE.toString(),storeE);
		nameToBack.put(storeF.toString(),storeF);
		nameToBack.put(storeG.toString(),storeG);
		nameToBack.put(storeH.toString(),storeH);
		nameToBack.put(wild9.toString(),wild9);
		nameToBack.put(wild8.toString(),wild8);
		nameToBack.put(wild7.toString(),wild7);
		nameToBack.put(wild6.toString(),wild6);
		nameToBack.put(wild4.toString(),wild4);
		nameToBack.put(wild1.toString(),wild1);
	}
	
	
	// this method creates the introduction for the game by creating the introscreen
	private void introduction() {
		preludeTheme.loop();
		IntroScreen intro = new IntroScreen();
		add(intro);
		while (notClicked == true) {
		intro.animate();
		}
		remove(intro);
	}
	
	// This method calls up the new or saved game screen
	// and allows the player to continue or start a new game
	private void newOrSavedGame() {
		newContinue screen = new newContinue();
		add(screen);
		while(true) {
			if (motionDirection == 'w' || motionDirection == 's') {
				screen.movePointer();
				motionDirection = 'n';
			}
			else if(motionDirection == ' ') {
				if (screen.getNewGameSelected()==true) {
					currentBack = town;
					charXpixel = N_UNITS_W/2*UNIT_W;
					charYpixel = N_UNITS_H/2*UNIT_H;
					character.setLocation(charXpixel,charYpixel);
					characterCenteredH=true;
					characterCenteredV=true;	
					newGameStory();
					break;
				}
				else {
					mehran = new PokemehranCharacter("continue");
					try {
						BufferedReader rd = new BufferedReader(new FileReader("continue.txt"));
						for(int i=0 ; i<16 ; i++) {
							rd.readLine();
						}
						String name = rd.readLine();
						currentBack = nameToBack.get(name);
						currentBack.setLocation(Double.parseDouble(rd.readLine()),Double.parseDouble(rd.readLine()));
						character.setLocation(Double.parseDouble(rd.readLine()),Double.parseDouble(rd.readLine()));
					} catch(IOException ex) {
						
					}
					break;
				}
			}
		}
		remove(screen);
		preludeTheme.stop();
	}
	
	// This method loads the slides for the storyline before a new game
	// and then plays them.
	private void newGameStory() {
		ArrayList<GObject> newGameSlides = new ArrayList<GObject>();
		for(int i=1 ; i<=NUM_NEWGAME_SLIDES ; i++) {
			newGameSlides.add(new GImage("NewGameSlides\\slide"+i+".jpg"));
		}
		SlideShowCutscene newGameCutscene = new SlideShowCutscene(newGameSlides);
		for(int i=1 ; i<=NUM_NEWGAME_SLIDES ; i++) {
			newGameCutscene.nextSlide();
			add(newGameCutscene);
			waitForClick();
		}
		removeAll();
	}
	
	// this method calls various sub methods that allow the character to move on screen
	private void motionOnMap() {
		checkEdge();
		checkCharacterEdge();
		checkCharacterCentered();
		checkWalkeable();
		motion();
	}
	
	// this method checks if the player has moved
	private void checkSameSquare() {
		if(charXprev == charX && charYprev == charY) {
			sameSquare = true;
		}
		else {
			sameSquare = false;
		}
	}
	
	// this method gets the characters location in terms of unit squares
	// and saves the previous location
	private void getCharacterLocation() {
		charXprev = charX;
		charYprev = charY;
		charX = (int)(character.getX()-currentBack.getX())/UNIT_W;
		charY = (int)(character.getY()-currentBack.getY())/UNIT_H;
	}
	
	// this method checks if the character can walk on any of the squares around him
	private void checkWalkeable() {
		
		if(charX-1 > -1) {
			if(walkeableArray[charY][charX-1].equals("n")) walkeable[A]=false;
			else walkeable[A]=true;
		}
		if(charY-1 > -1) {
			if(walkeableArray[charY-1][charX].equals("n")) walkeable[W]=false;
			else walkeable[W]=true;
		}
		if(charX+1 < currentBack.getBackgroundWidth()) {
				if(walkeableArray[charY][charX+1].equals("n")) walkeable[D]=false;
				else walkeable[D]=true;
		}
		if(charY+1 < currentBack.getBackgroundHeight()) {
			if(walkeableArray[charY+1][charX].equals("n")) walkeable[S]=false;
			else walkeable[S]=true;
		}
	}
	
	// this method checks if the character is at the edge of the application screen
	private void checkCharacterEdge() {
		if(character.getX()<=0) charEdgeReached[A]=true;
		if(character.getY()<=0) charEdgeReached[W]=true;
		if(character.getX()+character.getWidth()>=APPLICATION_WIDTH) charEdgeReached[D]=true;
		if(character.getY()+character.getHeight()>=APPLICATION_HEIGHT) charEdgeReached[S]=true;
	}
	
	// this method checks if the character is centered in the application screen
	private void checkCharacterCentered() {
		if (getElementAt(N_UNITS_W/2*UNIT_W+character.getWidth()/2 , character.getY())==character) 
				characterCenteredH=true;
		else characterCenteredH=false;
		
		if (getElementAt(character.getX() , N_UNITS_H/2*UNIT_H+character.getHeight()/2)==character) 
				characterCenteredV=true;
		else characterCenteredV=false;
	}
	
	// this method checks if the background has reached the edge of the application window
	private void checkEdge() {
		if(currentBack.getX()>=0) {edgeReached[A]=true;
		}
		else edgeReached[A]=false;
		if(currentBack.getY()>=0) edgeReached[W]=true;
		else edgeReached[W]=false;
		if(currentBack.getX()+currentBack.getWidth()<=APPLICATION_WIDTH) edgeReached[D]=true;
		else edgeReached[D]=false;
		if(currentBack.getY()+currentBack.getHeight()<=APPLICATION_HEIGHT) edgeReached[S]=true;
		else edgeReached[S]=false;
	}
	
	// this method takes the current key the player is pressing and moves the 
	// character if possible
	private void motion() {
		if (motionDirection=='a') {
			changeCharacterDirection();
			if(walkeable[A]==true) {
				if (edgeReached[A]==false && characterCenteredH==true) {
					moveLeft(currentBack);
					edgeReached[D]=false;
				}
				else if(charEdgeReached[A]==false) {
					moveRight(character);//left
					charEdgeReached[D]=false;
				}
			}
		}
		else if (motionDirection=='s') {
			changeCharacterDirection();
			if(walkeable[S]==true){
				if (edgeReached[S]==false && characterCenteredV==true) {
					moveDown(currentBack);
					edgeReached[W]=false;
				}
				else if(charEdgeReached[S]==false){
					moveUp(character);//down
					charEdgeReached[W]=false;
				}
			}
		}
		else if (motionDirection=='d') {
			changeCharacterDirection();
			if (walkeable[D]==true){
				if (edgeReached[D]==false && characterCenteredH==true) {
					moveRight(currentBack);
					edgeReached[A]=false;
				}
				else if(charEdgeReached[D]==false){
					moveLeft(character);//right
					charEdgeReached[A]=false;
				}
			}
		}
		else if (motionDirection=='w') {
			changeCharacterDirection();
			if (walkeable[W]==true) {
				if (edgeReached[W]==false && characterCenteredV==true) {
					moveUp(currentBack);
					edgeReached[S]=false;
				}
				else if(charEdgeReached[W]==false){
					moveDown(character);//up
					charEdgeReached[S]=false;
				}
			}
		}
		motionDirection=' ';
	}
	
	// This method checks if the player is at a portal to another map or building
	// and then moves the player to a new map if they are on a portal
	private void checkPortal() {
		if (walkeableArray[charY][charX].charAt(0)!='y'&&walkeableArray[charY][charX].charAt(0)!='n') {
			String portalName = walkeableArray[charY][charX];
			HashMap<String,String> portalToName = currentBack.getPortalToName();
			HashMap<String,double[]> nameToLocation = currentBack.getNameToLocation();
			
			String newBackName = portalToName.get(portalName);
			double[] newLocationArray = nameToLocation.get(newBackName);
			Background newBack = nameToBack.get(newBackName);
			
			newBack.setLocation(newLocationArray[0],newLocationArray[1]);
			remove(character);
			character.setLocation(newLocationArray[2],newLocationArray[3]);
			removeAll();
			add(newBack);
			add(character);
			currentBack = nameToBack.get(newBackName);
			
			mapArray = currentBack.getMapArray();
			walkeableArray = currentBack.getWalkeableArray();
			getCharacterLocation();
			checkWalkeable();
			checkEdge();
			checkCharacterCentered();
			checkCharacterEdge();
			getCharacterLocation();
		}
	}
	
	// this method changes what side of the character is shown on the screen depending
	// upon what direction he moves in
	private void changeCharacterDirection() {
		double x = character.getX();
		double y = character.getY();
		remove(character);
		if (motionDirection=='a') character = CHAR_LEFT;
		else if (motionDirection=='s') character = CHAR_FRONT;
		else if (motionDirection=='d') character = CHAR_RIGHT;
		else if (motionDirection=='w') character = CHAR_BACK;
		add(character,x,y);
	}
	
	// this method moves an object one unit left
	private void moveLeft(GObject toMove){
		for (int i=0 ; i<UNIT_W/5 ; i++){
			toMove.move(5,0);
			pause(PAUSE_TIME);
		}
	}
	
	// this method moves an object one unit down
	private void moveDown(GObject toMove){
		for (int i=0 ; i<UNIT_H/5 ; i++){
			toMove.move(0,-5);
			pause(PAUSE_TIME);
		}
	}

	// this method moves an object one unit right
	private void moveRight(GObject toMove){
		for (int i=0 ; i<UNIT_W/5 ; i++){
			toMove.move(-5,0);
			pause(PAUSE_TIME);
		}
	}

	// this method moves an object one unit up
	private void moveUp(GObject toMove){
		for (int i=0 ; i<UNIT_H/5 ; i++){
			toMove.move(0,5);
			pause(PAUSE_TIME);
		}
	}
	
	// this method creates the inital map background when the player begins and adds it
	// to the screen, then begins the music
	private void createInitialBackground() {
		add(currentBack);
		mapArray = currentBack.getMapArray();
		walkeableArray = currentBack.getWalkeableArray();
		add(character);
		getCharacterLocation();
		mainTheme.loop();
	}
}

	