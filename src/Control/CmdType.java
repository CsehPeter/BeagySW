package Control;

import java.io.Serializable;

//These types of interaction handles from the player
public enum CmdType implements Serializable
{
	ClientConnected,	//Client button
	Territory,			//Click on a territory
	Ok,					//Ok button
	Next,				//Next button
	Exit				//Exit button
}