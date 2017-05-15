package Control;

import java.io.Serializable;

//The three phases of a player's turn in Risk
public enum Phases implements Serializable
{
	Deploy,
	Attack,
	Transfer
}
