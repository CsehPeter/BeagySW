import Control.*;
import Gui.*;

public class Main
{
	public static void main(String[] args)
	{
		Control game = new Control();
		GUI gui = new GUI(game);
		game.Play();
	}
}
