/*
 *  Pokemehran Character creates an object that represents one character in the game
 *  The public methods in this file are very self explanatory and are thus mostly uncommented
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import acm.util.RandomGenerator;

public class PokemehranCharacter implements PokemehranConstants {
	
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	// Creates string data variables
	private String name;
	private String imageFile;
	private String attackName;
	// creates int data variables
	private int tempAttack=0;
	private int tempDefense=0;
	private int attack;
	private int defense;
	private int speed;
	private int hpMax;
	private int hpCur;
	private int level;
	private int experience;
	private int toNextLevel;
	private int money;
	// creates hashmap to store items
	private HashMap<String,Integer> items = new HashMap<String,Integer>();
	
	// Whenever a new character object is created it reads in the data text file 
	// describing that character
	public PokemehranCharacter(String character) {
		try {
		BufferedReader rd = new BufferedReader(new FileReader(character+".txt"));
		// Reads in all the data stored in text file, format is given in example
		// file in the characters folder
			name = rd.readLine();
			imageFile = rd.readLine();
			attackName = rd.readLine();
			attack = Integer.parseInt(rd.readLine());
			defense = Integer.parseInt(rd.readLine());
			speed = Integer.parseInt(rd.readLine());
			hpMax = Integer.parseInt(rd.readLine());
			hpCur = Integer.parseInt(rd.readLine());
			level = Integer.parseInt(rd.readLine());
			experience = Integer.parseInt(rd.readLine());
			toNextLevel = Integer.parseInt(rd.readLine());	
			money = Integer.parseInt(rd.readLine());
			items.put("Potion", Integer.parseInt(rd.readLine()));
			items.put("X Potion", Integer.parseInt(rd.readLine()));
			items.put("Attack Up", Integer.parseInt(rd.readLine()));
			items.put("Defense Up", Integer.parseInt(rd.readLine()));
		}
		catch(IOException ex) {
		}
	}
	
	// Clears temporary status effects
	public void clearTempStats() {
		tempAttack = 0;
		tempDefense = 0;
	}
	
	// Temporarily increases attack
	public void tempAttackUp(int change) {
		tempAttack += change;
	}
	
	// temporarily increases defense
	public void tempDefenseUp(int change) {
		tempDefense += change;
	}
	
	public int getTempAttack() {
		return tempAttack;
	}
	
	public int getTempDefense() {
		return tempDefense;
	}
	
	public int getItem(String item) {
		int temp = items.get(item);
		return temp;
	}
	
	// Adds item to inventory
	public void addItem(String item) {
		int temp = items.remove(item);
		temp++;
		items.put(item,temp);
	}
	
	// Removes item from inventory
	public void removeItem(String item) {
		if(getItem(item)>0) {	
			int temp = items.remove(item);
			temp--;
			items.put(item,temp);
		}
	}
	
	//heals player completely
	public void heal() {
		hpCur = hpMax;
	}
	
	public void hpDown(int change) {
		hpCur -= change;
		if (hpCur<0) hpCur = 0;
	}
	
	public void hpUp(int change) {
		hpCur += change;
		if (hpCur>hpMax) hpCur = hpMax;
	}
	
	public void moneyDown(int change) {
		money -= change;
		if (money<0) money = 0;
	}
	
	public void moneyUp(int change) {
		money += change;
	}
	
	public void attackDown(int change) {
		attack -= change;
		if (attack<0) attack = 0;
	}
	
	public void attackUp(int change) {
		attack += change;
	}
	
	public void defenseDown(int change) {
		defense -= change;
		if (defense<0) defense = 0;
	}
	
	public void defenseUp(int change) {
		defense += change;
	}
	
	// Performs actions when the character levels up
	public void levelUp() {
		level++;
		// increases defense, speed, attack by set amount plus a random boost
		if (defense<100) defense += 2+rgen.nextInt(0,2);
		if (attack<120) attack += 3+rgen.nextInt(0,2);
		speed += 1+rgen.nextInt(0,2);
		int hpUp = 4+rgen.nextInt(0,4);
		// limits hp, defense and attack
		if (hpMax<120) {
			hpMax += hpUp;
			hpCur+= hpUp;
		}
		if (defense>100) defense=100;
		if (attack>120) attack = 120;
		// sets experience needed to level up
		toNextLevel = 5 + 5*experience/4;
	}
	
	public void expUp(int expAdded) {
		experience += expAdded;
		toNextLevel -= expAdded;
	}
	
	public String getName() {
		return name;
	}
	public String getImageFile() {
		return "pokemon\\"+imageFile;
	}	
	public String getAttackName() {
		return attackName;
	}	
	public int getAttack() {
		return attack;
	}	
	public int getDefense() {
		return defense;
	}
	public int getSpeed() {
		return speed;
	}
	public int getHPMax() {
		return hpMax;
	}
	public int getHPCur() {
		return hpCur;
	}
	public int getLevel() {
		return level;
	}
	public int getExperience() {
		return experience;
	}
	public int getToNextLevel() {
		return toNextLevel;
	}
	public int getMoney() {
		return money;
	}
}
