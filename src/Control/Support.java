package Control;

import java.util.ArrayList;
import java.util.Random;

public final class Support
{
	///\brief Calculates the number of deploy units
	//TODO: extra units for the owned continents
	public static int DeployUnits(int playerId, Map map) throws NullPointerException
	{
		if(map == null) throw new NullPointerException("Map is null");
		
		int units = 0;
		for(Territory territory : map._territories)
		{
			if(territory.Owner.getId() == playerId) units++;
		}
		
		return units / Constants.TERRITORY_PER_UNIT + Constants.DEFAULT_DEPLOY_UNITS;
	}
	
	///\brief Returns true if the two territories are near each other
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
			
	///\brief Returns true if the attack can be executed
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
		
	///\brief Returns true if the attacker wins the battle, otherwise false
	public static Boolean Attack(int playerId, Territory a, Territory b, int attackUnits)
	{
		CanAttack(playerId, a, b, attackUnits);
		
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
			b.Units--;
			b.IsChanged = true;
			return true;
		}
		else
		{
			a.Units--;
			a.IsChanged = true;
			return false;
		}
	}
		
	///\brief Returns the biggest number from a dice roll
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
		
	///\brief Returns true if the given territory is owned by the player, otherwise false
	public static Boolean IsFriendlyTerritory(int playerId, Territory t)
	{
		if(t == null) throw new NullPointerException("Territory is null");
		if(t.Owner == null) throw new NullPointerException("Territory's owner is null");
		
		if(t.Owner.getId() == playerId) return true;
		return false;
	}
	
	///\brief 	Return true if there is a territory connection line between the two friendly territories
	///\details	Backtrack algorithm to search a line
	///TODO Check for the remaining 1 unit on the base territory
	public static Boolean CanTransfer(int playerId, Map map, Territory current, Territory goal) throws NullPointerException
	{
		if(map == null) throw new NullPointerException("Map is null");
		
		//Current or goal territory is not owned by the player
		if(!IsFriendlyTerritory(playerId, current) || !IsFriendlyTerritory(playerId, goal)) return false;
		
		ArrayList<Integer> used = new ArrayList<Integer>();
		used.add(current.getId());
		return CanTransfer(playerId, map, current, goal, used);
	}
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
	
	//TODO Function to convert map's changed territories to an action msg
}
