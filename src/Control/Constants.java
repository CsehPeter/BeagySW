package Control;

import java.awt.Color;

//Class for constant values which can modify the way that the game is played/shown
public final class Constants
{
	//Territory
	public static final int NUMBER_OF_TERRITORIES = 42;
	
	//Dice rolls
	public static final int DICE_MIN_VALUE = 1;		//Minimum value on a die
	public static final int DICE_MAX_VALUE = 6;		//Maximum value on a die
	public static final int DICE_MIN_NUMBER = 1;	//Minimum number of dice in a roll
	public static final int DICE_MAX_NUMBER = 3;	//Maximum number of dice in a roll
	public static final int[] DICE_LIMITS = {3, 7};	//Number of units required to increase the number of dice in a roll
	
	//Deploy
	public static final int TERRITORY_PER_UNIT = 3;		//This amount of territories required to get one extra unit in the deploy phase
	public static final int DEFAULT_DEPLOY_UNITS = 5;	//The lowest number of deploy units
	
	//Colours
	public static final Color ACTIVE_COLOR[] = {Color.cyan, Color.orange}; //Indicates the selected territories colour on the GUI
	
	//Init
	public static final int STARTING_UNITS = 10;				//Default number of units on a freshly initialised map
	
	//Players
	public static final int SERVER_PLAYER_ID = 0;				//ID of the first player in the game, which is the server
	public static final Color SERVER_PLAYER_COLOR = Color.blue;
	
	public static final int CLIENT_PLAYER_ID = 1;				//ID of the second player in the game, which is the client
	public static final Color CLIENT_PLAYER_COLOR = Color.red;
}
