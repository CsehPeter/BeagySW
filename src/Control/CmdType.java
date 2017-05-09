package Control;

import java.io.Serializable;

public enum CmdType implements Serializable
{
	ClientConnected,
	Territory,
	Ok,
	Next,
	Exit
}