package Control;

import java.awt.Color;

public final class Constants
{
	//Territory
	public static final int NUMBER_OF_TERRITORIES = 42;
	
	//Dice rolls
	public static final int DICE_MIN_VALUE = 1;		//Minimum value on a die
	public static final int DICE_MAX_VALUE = 6;		//Maximum value on a dice
	public static final int DICE_MIN_NUMBER = 1;	//Minimum number of dice in a roll
	public static final int DICE_MAX_NUMBER = 3;	//Maximum number of dice in a roll
	public static final int[] DICE_LIMITS = {3, 7};	//Number of units required to increase the number of dice in a roll
	
	//Deploy
	public static final int TERRITORY_PER_UNIT = 3;	//This amount of territories required to get one extra unit in the deploy phase
	public static final int DEFAULT_DEPLOY_UNITS = 5;
	
	//Colors
	public static final Color ACTIVE_COLOR = Color.cyan;
}
