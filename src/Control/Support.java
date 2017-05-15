package Control;

import java.util.ArrayList;
import java.util.Random;

import Gui.Map;
import Gui.Territory;

//Support functions to help the game flow using the rules of Risk
public final class Support
{
	/**
	 * @param playerId ID of the player whose units should be calculated
	 * @param map The world map
	 * @return Number of units that can be deployed by the player based on the player's owned territories
	 * @throws NullPointerException
	 * @see Gui.Map
	 **/
	public static int DeployUnits(int playerId, Map map) throws NullPointerException
	{
		if(map == null) throw new NullPointerException("Map is null");
		
		int units = 0;
		for(Territory territory : map.Territories)
		{
			if(territory.Owner.getId() == playerId) units++;
		}
		
		return units / Constants.TERRITORY_PER_UNIT + Constants.DEFAULT_DEPLOY_UNITS;
	}
	
	/**
	 * @param a One of the two territories where territory 'b' should be a neighbour
	 * @param b One of the two territories where territory 'a' should be a neighbour
	 * @return True if the two territories are neighbours, otherwise false
	 * @throws NullPointerException
	 * @see Gui.Territory
	 */
	public static Boolean AreNeighbours(Territory a, Territory b) throws NullPointerException
	{
		if(a == null) throw new NullPointerException("Territory 'a' is null");
		if(b == null) throw new NullPointerException("Territory 'b' is null");
		
		for(int territoryAId : a.getNeighbours())
		{
			if(territoryAId == b.getId())
			{
				return true;
			}
		}
		return false;
	}
			
	/**
	 * @param playerId ID of the attacking player
	 * @param a Base territory of the attacking player
	 * @param b Aim territory of the defending player
	 * @param attackUnits Number of units which are attacking
	 * @return True if the attack can be executed according to the rules of Risk, otherwise false
	 * @throws NullPointerException
	 * @see Gui.Territory
	 */
	public static Boolean CanAttack(int playerId, Territory a, Territory b, int attackUnits) throws NullPointerException
	{
		if(a == null) throw new NullPointerException("Territory 'a' is null");
		if(b == null) throw new NullPointerException("Territory 'b' is null");
		
		//Check if the base territory is owned by the attacker, and the units on the territory is greater than 1, and there is at least 1 defender unit
		if(a.Owner.getId() == playerId && a.Units > 1 && b.Units > 0)
		{
			//Check if the attacking units is greater than 1 and there are enough units on the territory
			if(attackUnits > 0 && attackUnits < a.Units)
			{
				//Check if the target territory is owned by some other player
				if(b.Owner.getId() != playerId)
				{
					//Check if the two territory is near each other
					if(AreNeighbours(a, b))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
		
	/**
	 * @param playerId ID of the attacking player
	 * @param a Base territory of the attacking player, this method is modifying this territory
	 * @param b Aim territory of the defending player, this method is modifying this territory
	 * @param attackUnits Number of units which are attacking
	 * @return True if the attacker conquered territory 'b'
	 * @throws NullPointerException
	 * @see Gui.Territory
	 */
	public static Boolean Attack(int playerId, Territory a, Territory b, int attackUnits)
	{
		CanAttack(playerId, a, b, attackUnits);
		
		int aUnits = attackUnits;
		int bUnits = b.Units;
		
		do
		{
			//Calculate the number of friendly and enemy dice
			int friendlyDice = 0;
			int enemyDice = 0;
			for(int i = 0; i < Constants.DICE_LIMITS.length - 1; i++)
			{
				if(attackUnits > Constants.DICE_LIMITS[i]) friendlyDice++;
				if(b.Units > Constants.DICE_LIMITS[i]) enemyDice++;
			}
			
			//Battle
			if(Roll(friendlyDice) > Roll(enemyDice))
			{
				bUnits--;
				b.IsChanged = true;
			}
			else
			{
				aUnits--;
				a.IsChanged = true;
			}
		} while (bUnits > 0 && aUnits > 0);
		
		//Attackers won
		if(bUnits == 0)
		{
			b.Owner = a.Owner;
			b.Units = aUnits;	//remaining units
			a.Units = a.Units - attackUnits;	//remove lost/transferred units
			
			return true;
		}
		else //Defenders won
		{
			b.Units = bUnits;
			a.Units = a.Units - attackUnits;
			
			return false;
		}
	}

	/**
	 * @param dice Number of dice in a single roll
	 * @return The biggest number from a dice roll(random generated values)
	 * @throws IllegalArgumentException
	 */
	private static int Roll(int dice) throws IllegalArgumentException
	{
		if(dice < Constants.DICE_MIN_NUMBER && dice > Constants.DICE_MAX_NUMBER) 
			throw new IllegalArgumentException("Number of dice must be beetween" + Constants.DICE_MIN_NUMBER + " and " + Constants.DICE_MAX_NUMBER);
		
		Random rnd = new Random();
		
		int tmp = 0;
		int ret = 0;
		for(int i = 0; i < dice; i++)
		{
			//Random number between 1 and 6
			tmp = rnd.nextInt(Constants.DICE_MAX_VALUE) + Constants.DICE_MIN_VALUE;
			if(tmp > ret) ret = tmp;
		}
		
		return ret;
	}

	/**
	 * @param playerId ID of the player
	 * @param t Territory to check if it's owned by the player with the given ID
	 * @return true if the given territory is owned by the player, otherwise false
	 * @see Gui.Territory
	 */
	public static Boolean IsFriendlyTerritory(int playerId, Territory t)
	{
		if(t == null) throw new NullPointerException("Territory is null");
		if(t.Owner == null) throw new NullPointerException("Territory's owner is null");
		
		if(t.Owner.getId() == playerId) return true;
		return false;
	}
	
	/**
	 * @param playerId Id of the player whose units should be transferred
	 * @param map World map
	 * @param current Base territory which should be the start point of the transfer
	 * @param goal Goal territory of the transfer
	 * @return True if the units on the current territory can reach the goal territory in a friendly line
	 * @throws NullPointerException
	 * @see Gui.Map
	 * @see Gui.Territory
	 * Uses recursive backtrack algorithm
	 */
	public static Boolean CanTransfer(int playerId, Map map, Territory current, Territory goal) throws NullPointerException
	{
		if(map == null) throw new NullPointerException("Map is null");
		
		//Current or goal territory is not owned by the player
		if(!IsFriendlyTerritory(playerId, current) || !IsFriendlyTerritory(playerId, goal)) return false;
		
		ArrayList<Integer> used = new ArrayList<Integer>();
		used.add(current.getId());
		return CanTransfer(playerId, map, current, goal, used);
	}
	
	/**
	 * @see The public Control.Support.CanTransfer method
	 * @param used An ArrayList with the used territory IDs for the backtrack algorithm
	 */
	private static Boolean CanTransfer(int playerId, Map map, Territory current, Territory goal, ArrayList<Integer> used)
	{
		//reached our goal
		if(current.getId() == goal.getId()) return true;
		
		for(int aId : current.getNeighbours())
		{
			if(!used.contains(aId) && map.getTerritory(aId).Owner.getId() == playerId)
			{
				used.add(aId);
					//System.out.print(current.getId() + " ");
				if(CanTransfer(playerId, map, map.getTerritory(aId), goal, used)) return true;
			}
		}
		used.remove(used.size() - 1);
		return false;
	}

	/**
	 * @param playerId ID of the player whose units should be transferred
	 * @param map World map
	 * @param current Base territory which should be the start point of the transfer
	 * @param goal Goal territory of the transfer
	 * @return True if the transfer is completed, otherwise false
	 * @see Gui.Map
	 * @see Gui.Territory
	 * @see Control.Support.CanTransfer
	 */
	public static Boolean Transfer(int playerId, Map map, Territory current, Territory goal, int units)
	{
		if(CanTransfer(playerId, map, current, goal) == false) return false;
		
		if(current.Units <= units) return false;
		
		current.Units = current.Units - units;
		current.IsChanged = true;
		
		goal.Units = goal.Units + units;
		goal.IsChanged = true;
		
		return true;
	}
}
