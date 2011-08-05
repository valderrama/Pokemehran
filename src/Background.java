/*
 * The backround object creates a background from a set of text files by
 * creating two dimensional array of strings that takes the characters from
 * the corresponding locations in each text file and concatenating them.
 * For example:
 * File 1     File 2       File3       Array
 * a a        a a          1 1         aa1 aa1
 * a b        b b          2 2         ab2 ab2
 * Each of the string codes in the array contains the information necessary
 * to add a GImage square in the correct location and to assemble the map.
 * In the previous array example the code aa could correspond to grass and the
 * code ab could correspond to concrete. Additional data can be added to the string
 * so that more information about each square can be stored. For example some squares 
 * are considered to be portals that move between map screens. This information is
 * in the array. Information about what squares can be walked on by the character
 * is also stored in the array. 
 * 
 * Personally, I think this is the best part of the entire project because it takes
 * text files and creates an entire graphical world from them. Awesome.
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import acm.graphics.GCompound;
import acm.graphics.GImage;

public class Background extends GCompound {
	
	// The mapArray stores data about what the different map squares will show
	private String[][] mapArray;
	// The walkeable array stores data about where the character can walk, and what
	// squares are portals
	private String[][] walkeableArray;
	// Location stores the x and y for a specific location on the map
	private int[] location = new int[2];
	private int height;
	private int width;
	private String name;
	// This map keys the portal name and links it to the new location
	private HashMap<String,int[]> portalLocation = new HashMap<String,int[]>();
	// This map keys the portal name to the name of the new background
	private HashMap<String,String> portalToName = new HashMap<String,String>();
	// This map keys the background name to it's location and the characters location
	private HashMap<String,double[]> nameToLocation = new HashMap<String,double[]>();
	
	// Creating a background object requires the filename for the background
	// the background file is then read in, portals are located and the background
	// is created
	public Background(String fileName) {
		name = fileName;
		readInFile();
		readInPortals();
		makeBackground();
	}
	// The following functions all return data concerning the background
	public String toString() {
		return name;
	}
	
	public HashMap<String,String> getPortalToName() {
		return portalToName;
	}
	
	public HashMap<String,double[]> getNameToLocation() {
		return nameToLocation;
	}
	
	public String[][] getMapArray(){
		return mapArray;
	}
	
	public String[][] getWalkeableArray(){
		return walkeableArray;
	}
	
	public int getBackgroundWidth(){
		return width;
	}
	
	public int getBackgroundHeight(){
		return height;
	}
	
	// returns the portal location given a key name
	public int[] getPortalLocation(String key){
		location = portalLocation.get(key);
		return location;
	}
	
	// Read in file creates four buffered readers that read the three separate text files
	// required for each background. 
	private void readInFile() {
		try {
			BufferedReader dimensions = new BufferedReader(new FileReader(name + "1.txt"));
			BufferedReader mapping = new BufferedReader(new FileReader(name + "1.txt"));
			BufferedReader edges = new BufferedReader(new FileReader(name + "2.txt"));
			BufferedReader walkeable = new BufferedReader(new FileReader(name + "3.txt"));
			
			getDimensions(dimensions);
			setupArrays();
			addArrayData(mapping, edges);
			addWalkeableData(walkeable);
			
		}
		catch(FileNotFoundException ex) {
		}
	}
	
	// Read in portals finds the location of all portals on a map and
	private void readInPortals() {
		try {
			BufferedReader portals = new BufferedReader(new FileReader(name + "4.txt"));
			setPortalHashmaps(portals);
		} catch(FileNotFoundException ex) {
		}
	}
	
	// Set portal hashmaps takes the buffered reader and finds the protals on the map
	// it then saves the location and names of the portals, along with all data about
	// where they port to and where the character ports to and saves it
	// in the correct hashmaps
	private void setPortalHashmaps(BufferedReader portals) {
		while(true) {
			String portalName;
			String backName;
			double[] backAndCharLocation = new double[4];
			try {
				portalName = portals.readLine();
				backName = portals.readLine();
				if (portalName==null) break;
				portalToName.put(portalName, backName);
				for (int i=0 ; i<4 ; i++) {
					backAndCharLocation[i] = Double.parseDouble(portals.readLine());
				}
				nameToLocation.put(backName, backAndCharLocation);
			} 
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// This buffered reader calculates the dimensions of the map
	private void getDimensions(BufferedReader dimensions) {
		height = 0;
		width = 0;
		while (true) {
			String s;
			try {
				s = dimensions.readLine();
				if (s == null) break;
				height++;
				width = s.length();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// This method sets the string arrays with empty strings
	private void setupArrays() {
		mapArray = new String[height][width];
		walkeableArray = new String[height][width];
		for (int i=0; i<height; i++){
			for (int j=0; j<width; j++){
				mapArray[i][j]="";
				walkeableArray[i][j]="";
			}
		}
		
	}
	
	// This method takes two readers and uses them to calculate the required image
	// for each square on the map and also reads where the edges of the map are
	private void addArrayData(BufferedReader mapping, BufferedReader edges) {
		try {
			for (int i=0; i<height; i++){
				String s = mapping.readLine();
				String t = edges.readLine();
				for (int j=0; j<width; j++){
					mapArray[i][j]=mapArray[i][j]+s.charAt(j)+t.charAt(j); 
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This method uses a buffered reader to check whether areas are walkeable or not
    // and adds that data to the walkeable array
	private void addWalkeableData(BufferedReader walkeable) {
		try {
			for (int i=0; i<height; i++){
				String s = walkeable.readLine();
				for (int j=0; j<width; j++){
					walkeableArray[i][j]=walkeableArray[i][j]+s.charAt(j);
					if (walkeableArray[i][j].equals("y")||walkeableArray[i][j].equals("n")) {
					}
					else {
						location[0]=i;
						location[1]=j;
						portalLocation.put(walkeableArray[i][j],location);
					}
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// This method creates the GCanvas background by adding each individual 
	// squares given gimage. Sweet!
	private void makeBackground() {
		for (int i=0; i<height; i++){
			for (int j=0; j<width; j++){
				
				GImage x=new GImage("backgroundImages\\"+mapArray[i][j]+".jpg");
				add(x,j*x.getWidth(),i*x.getHeight());
				
			}
		}
	}
}
