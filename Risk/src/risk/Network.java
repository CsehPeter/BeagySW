/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package risk;

import java.awt.Point;


abstract class Network {

	protected Control ctrl;

	Network(Control c) {
		ctrl = c;
	}

	abstract void connect(String ip);

	abstract void disconnect();

	abstract void send(Point p);
}
