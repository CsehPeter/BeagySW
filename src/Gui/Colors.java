package Gui;

import java.awt.Color;
import java.util.ArrayList;

//Final class to generate soem colors
public final class Colors
{
	private static ArrayList<Color> _colors;
	private static int num = 0;
	
	//Initialize the ArrayList with some default colors
	private static void Init()
	{
		_colors.add(Color.blue);
		_colors.add(Color.red);
		_colors.add(Color.green);
		_colors.add(Color.black);
		_colors.add(Color.cyan);
		_colors.add(Color.pink);
		_colors.add(Color.gray);
		_colors.add(Color.magenta);
		_colors.add(Color.orange);
		_colors.add(Color.yellow);
		_colors.add(Color.white);
	}
	
	///\brief Gets a color from the list
	public static Color NextColor()
	{
		if(_colors == null)
		{
			_colors = new ArrayList<Color>();
			Init();
		}
		if(num >= _colors.size()) num = 0;
		return _colors.get(num++);
	}
}
