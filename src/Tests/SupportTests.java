package Tests;

import java.awt.Color;
import java.util.ArrayList;

import Control.*;

public final class SupportTests
{
	public static void RunAll()
	{
		T_Battle();
		T_CanTransfer();
	}
	
	public static void T_Battle()
	{
		System.out.println("---Running battle simulation---");
		
		Player player1 = new Player(4, Color.red);
		Player player2 = new Player(8, Color.blue); 
		
		ArrayList<Integer> nb = new ArrayList<Integer>();
		
		nb.clear(); nb.add(8);
		Territory t1 = new Territory(4, "t1", Continents.Africa, nb);
		t1.Owner = player1;
		t1.Units = 20;
		
		nb.clear(); nb.add(4);
		Territory t2 = new Territory(8, "t2", Continents.Africa, nb);
		t2.Owner = player2;
		t2.Units = 20;
		
		int round = 0;
		while(Support.CanAttack(player1, t1, t2, t1.Units - 1))
		{
			System.out.println("Round:" + round++ + "   " + t1.Units + " vs " + t2.Units);
			Support.Attack(player1, t1, t2, t1.Units - 1);
		}
		System.out.println("Round:" + round++ + "   " + t1.Units + " vs " + t2.Units);
		if(t1.Units > t2.Units) System.out.println("Attackers won");
		else System.out.println("Defenders won");
	}
	
	public static void T_CanTransfer()
	{
		System.out.println("---Running cantransfer test---");
		
		Map map = new Map();
		Player player1 = new Player(4, Color.red);
		Player player2 = new Player(88, Color.orange);
		
		map.init();
		for(Territory t : map._territories)
		{
			t.Owner = player1;
		}
		
		map.getTerritory(1).Owner = player2;
		map.getTerritory(15).Owner = player2;
		map.getTerritory(25).Owner = player2;
		
		for(int i = 1; i < Constants.NUMBER_OF_TERRITORIES; i++)
		{
			System.out.print("From 2(" + map.getTerritory(2).Owner.getId() + ")" + " to " + i + "(" + map.getTerritory(i).Owner.getId() + ") ");
			System.out.println(Support.CanTransfer(player1, map, map.getTerritory(2), map.getTerritory(i)));
		}
	}
}
