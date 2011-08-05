/*
 * The menu class creates a menu which can be used at either the item stores
 * or during the pause screen.
 */

import java.awt.Color;
import java.awt.Font;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GRect;


public class Menu extends GCompound implements PokemehranConstants {
	
	
	private PokemehranCharacter mehran;
	// Creates the buffers and spacing for the menu
	private int verticalBuffer = 50;
	private int horizontalBuffer = 40;
	private int infoColBuffer = 10;
	private int menuOptionsBuffer = 15;
	private int columnWidth = (APPLICATION_WIDTH-2*horizontalBuffer)/3;
	private int columnWidth2 = (APPLICATION_WIDTH-2*horizontalBuffer)/2;
	private int rowWidth = 60;
	private double colA = horizontalBuffer+infoColBuffer;
	private double colB = colA+columnWidth2;
	private double col1 = horizontalBuffer+infoColBuffer;
	private double col2 = col1+columnWidth;
	private double col3 = col2+columnWidth;
	private double row0 = verticalBuffer+menuOptionsBuffer;
	private double row1 = verticalBuffer+3*rowWidth/2;
	private double row2 = row1+rowWidth;
	private double row3 = row2+rowWidth;
	private double row4 = row3+rowWidth;
	private double row5 = verticalBuffer+6*rowWidth;
	private double row6 = row5+rowWidth;
	// Sets booleans to determine what menu or submenus are currently being accessed
	private boolean mainMenu = true;
	private boolean itemMenu = false;
	// Creates GLabels for menu
	private GLabel op1;
	private GLabel op2;
	private GLabel op3;
	private GLabel selected;
	private GLabel name;
	private GLabel attack;
	private GLabel defense;
	private GLabel speed;
	private GLabel lvl;
	private GLabel exp;
	private GLabel nxtLvl;
	private GLabel money;
	private GLabel hp;
	private GLabel potion;
	private GLabel xPotion;
	private GLabel attackUp;
	private GLabel defenseUp;
	// Sets fonts
	private Font infoFont = new Font("Helvetica",Font.PLAIN,22);
	private Font optionsFont = new Font("Helvetica",Font.PLAIN,32);
	private Font selectedFont = new Font("Helvetica",Font.BOLD,32);
	private GRect divider1;
	private GRect divider2;
	// Creates arrays of strings that contain the menu selections
	private String[] mainMenuSelections;
	private String[] itemMenuSelections;
	
	// Creating a menu requires a character, and two arrays of strings to be handed in
	public Menu(PokemehranCharacter character,String[] menuSelections1,String[] menuSelections2) {
		// Because there is only one character available currently it is set to mehran
		mehran = character;
		mainMenuSelections=menuSelections1;
		itemMenuSelections=menuSelections2;
		// These calls add graphics to the canvas
		add(MENU_BACK,0,0);
		add(MEHRAN_PROFILE,col1,row1);
		// Calls menus selections to add the menu options
		newMenuSelections(mainMenuSelections[0],mainMenuSelections[1],mainMenuSelections[2]);
		// Adds all labels and items data to the menu
		newInfoLabels();
		newItemLabels();
		setInfoLocation();
		setItemLocation();
		setInfoFont();
		setItemFont();
		setInfoColor();
		setItemColor();
		addInfo();
		addItems();
		newDividers();
		
	}
	
	// Sets the values of the menu variables tracking what menu is currently
	// being shown and updates the menu options accordingly
	public void switchMenu() {
		if(mainMenu == true) {
			mainMenu = false;
			itemMenu = true;
		} else if (itemMenu==true) {
			itemMenu = false;
			mainMenu = true;
		}
		updateMenuOptions();
	}
	// Returns true if at main menu
	public boolean atMainMenu() {
		if (mainMenu == true) return true;
		else return false;
	}
	
	// Returns true if at item menu
	public boolean atItemMenu() {
		if (itemMenu == true) return true;
		else return false;
	}
	
	// Returns the selected string
	public String getSelectedString() {
		String temp = selected.getLabel();
		return temp;
	}
	
	// Sets selected string depending upon the users choice
	public void setSelected(String s) {
		if(s.equals("a")) selected = op1;
		else if(s.equals("s")) selected = op2;
		else if(s.equals("d")) selected = op3;
		updateMenuOptions();

	}
	
	// creates item labels
	private void newItemLabels() {
		potion = new GLabel(""+mehran.getItem("Potion")+" : Potion");
		xPotion = new GLabel(""+mehran.getItem("X Potion")+" : X Potion");
		attackUp = new GLabel(""+mehran.getItem("Attack Up")+" : Attack Up");
		defenseUp = new GLabel(""+mehran.getItem("Defense Up")+" : Defense Up");
	}
	
	// Sets item labels locations
	private void setItemLocation() {
		potion.setLocation(colA,row5);
		xPotion.setLocation(colB,row5);
		attackUp.setLocation(colA,row6);
		defenseUp.setLocation(colB,row6);
	}
	
	// Sets items fonts
	private void setItemFont() {
		potion.setFont(infoFont);
		xPotion.setFont(infoFont);
		attackUp.setFont(infoFont);
		defenseUp.setFont(infoFont);
	}
	
	// Sets items colors
	private void setItemColor() {
		potion.setColor(Color.WHITE);
		xPotion.setColor(Color.WHITE);
		attackUp.setColor(Color.WHITE);
		defenseUp.setColor(Color.WHITE);
	}
	
	// Sets item labels text
	private void setItemLabels() {
		potion.setLabel(""+mehran.getItem("Potion")+" : Potion");
		xPotion.setLabel(""+mehran.getItem("X Potion")+" : X Potion");
		attackUp.setLabel(""+mehran.getItem("Attack Up")+" : Attack Up");
		defenseUp.setLabel(""+mehran.getItem("Defense Up")+" : Defense Up");
	}
	
	// Adds items to canvas
	private void addItems() {
		add(potion);
		add(xPotion);
		add(attackUp);
		add(defenseUp);
	}
	
	// Removes items from canvas
	private void removeItems() {
		remove(potion);
		remove(xPotion);
		remove(attackUp);
		remove(defenseUp);
	}
	
	// Creates new menu selections depending upon the three strings given
	private void newMenuSelections(String s1, String s2, String s3) {
		op1 = new GLabel(s1);
		op2 = new GLabel(s2);
		op3 = new GLabel(s3);
		op1.setLocation(col1,row0);
		op2.setLocation(col2,row0);
		op3.setLocation(col3,row0);
		op1.setFont(optionsFont);
		op2.setFont(optionsFont);
		op3.setFont(optionsFont);
		op1.setColor(Color.GRAY);
		op2.setColor(Color.GRAY);
		op3.setColor(Color.GRAY);
		selected = op1;
		selected.setFont(selectedFont);
		selected.setColor(Color.WHITE);
		add(op1);
		add(op2);
		add(op3);
	}
	
	// Changes existing menu selections depending upon the strings given
	private void setMenuSelections(String s1, String s2, String s3) {
		op1.setLabel(s1);
		op2.setLabel(s2);
		op3.setLabel(s3);
		op1.setLocation(col1,row0);
		op2.setLocation(col2,row0);
		op3.setLocation(col3,row0);
		op1.setFont(optionsFont);
		op2.setFont(optionsFont);
		op3.setFont(optionsFont);
		op1.setColor(Color.GRAY);
		op2.setColor(Color.GRAY);
		op3.setColor(Color.GRAY);
		if(selected == op1) {
			op1.setFont(selectedFont);
			op1.setColor(Color.WHITE);
		} else if(selected == op2) {
			op2.setFont(selectedFont);
			op2.setColor(Color.WHITE);
		} else if(selected == op3) {
			op3.setFont(selectedFont);
			op3.setColor(Color.WHITE);
		}
		add(op1);
		add(op2);
		add(op3);
	}
	
	// Creates spacing dividers in the menu
	private void newDividers() {
		divider1 = new GRect(APPLICATION_WIDTH-2*horizontalBuffer,2);
		divider2 = new GRect(APPLICATION_WIDTH-2*horizontalBuffer,2);
		divider1.setLocation(horizontalBuffer,verticalBuffer+2*rowWidth/3);
		divider2.setLocation(horizontalBuffer,row4+1*rowWidth/2);
		divider1.setFilled(true);
		divider2.setFilled(true);
		divider1.setColor(Color.GRAY);
		divider2.setColor(Color.GRAY);
		add(divider1);
		add(divider2);
	}
	
	// Creates information labels for character
	private void newInfoLabels() {
		name = new GLabel("Name: "+mehran.getName());
		attack = new GLabel("Attack: "+mehran.getAttack());
		defense = new GLabel("Defense: "+mehran.getDefense());
		speed = new GLabel("Speed: "+mehran.getSpeed());
		lvl = new GLabel("Lvl: "+mehran.getLevel());
		exp = new GLabel("Exp: "+mehran.getExperience());
		nxtLvl = new GLabel("Lvl. Up: "+mehran.getToNextLevel());
		money = new GLabel("Money: $"+mehran.getMoney());
		hp = new GLabel("HP: "+mehran.getHPCur()+" / "+mehran.getHPMax());
	}
	
	// Sets the information labels text
	private void setInfoLabels() {
		name.setLabel("Name: "+mehran.getName());
		attack.setLabel("Attack: "+mehran.getAttack());
		defense.setLabel("Defense: "+mehran.getDefense());
		speed.setLabel("Speed: "+mehran.getSpeed());
		lvl.setLabel("Lvl: "+mehran.getLevel());
		exp.setLabel("Exp: "+mehran.getExperience());
		nxtLvl.setLabel("Lvl. Up: "+mehran.getToNextLevel());
		money.setLabel("Money: $"+mehran.getMoney());
		hp.setLabel("HP: "+mehran.getHPCur()+" / "+mehran.getHPMax());
	}
	
	// Sets information labels font
	private void setInfoFont() {
		name.setFont(infoFont);
		attack.setFont(infoFont);
		defense.setFont(infoFont);
		speed.setFont(infoFont);
		lvl.setFont(infoFont);
		exp.setFont(infoFont);
		nxtLvl.setFont(infoFont);
		money.setFont(infoFont);
		hp.setFont(infoFont);
	}
	
	// Sets information labels colors
	private void setInfoColor() {
		name.setColor(Color.WHITE);
		attack.setColor(Color.WHITE);
		defense.setColor(Color.WHITE);
		speed.setColor(Color.WHITE);
		lvl.setColor(Color.WHITE);
		exp.setColor(Color.WHITE);
		nxtLvl.setColor(Color.WHITE);
		money.setColor(Color.WHITE);
		hp.setColor(Color.WHITE);
	}
	
	// Sets information labels locations
	private void setInfoLocation() {
		name.setLocation(col2,row1);
		attack.setLocation(col2,row2);
		defense.setLocation(col2,row3);
		speed.setLocation(col2,row4);
		lvl.setLocation(col3,row1);
		exp.setLocation(col3,row2);
		nxtLvl.setLocation(col3,row3);
		money.setLocation(col3,row4);
		hp.setLocation(col1,row4);
	}
	
	// Adds information labels to canvas
	private void addInfo() {
		add(name);
		add(attack);
		add(defense);
		add(speed);
		add(lvl);
		add(exp);
		add(nxtLvl);
		add(money);
		add(hp);
	}
	
	// removes information labels from cannvas
	private void removeInfo() {
		remove(name);
		remove(attack);
		remove(defense);
		remove(speed);
		remove(lvl);
		remove(exp);
		remove(nxtLvl);
		remove(money);
		remove(hp);
	}
	
	// Updates the menu depending upon what user selection is made
	public void update() {
		removeAll();
		add(MENU_BACK,0,0);
		add(MEHRAN_PROFILE,col1,row1);
		newDividers();
		
		setInfoLabels();		
		setInfoLocation();
		setItemLabels();
		setItemLocation();
		addInfo();
		addItems();
		
		if (mainMenu == true) {
			setMenuSelections("Exit","Save","Item");
			
		} else if (itemMenu == true) {
			setMenuSelections("Exit","Potion","X Potion");
		}
	}

	// Updates menus options
	public void updateMenuOptions() {
		remove(op1);
		remove(op2);
		remove(op3);
		if (mainMenu == true) {
			setMenuSelections(mainMenuSelections[0],mainMenuSelections[1],mainMenuSelections[2]);
			
		} else if (itemMenu == true) {
			setMenuSelections(itemMenuSelections[0],itemMenuSelections[1],itemMenuSelections[2]);
		}
	}
	
	// Updates item labels
	public void updateItemLabels() {
		removeItems();
		setItemLabels();
		setItemLocation();
		addItems();
	}
	
	// Updates information labels
	public void updateInfoLabels() {
		removeInfo();
		setInfoLabels();		
		setInfoLocation();
		addInfo();
	}
	
}
