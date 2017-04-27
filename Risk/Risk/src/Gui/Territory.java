package Gui;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import Control.*;

public class Territory {
	private int 	id;
	static  AtomicInteger nextId = new AtomicInteger();
	private String 	name;
	private String 	continent;
	private Shape	shape;
	private ArrayList<Integer>	neighbours;
	//private Player 	owner;
	private int 	units;
	private Color 	fillColor;
	
	
	Territory(String nam, String cont, Shape sh) {
		id = nextId.incrementAndGet();
		name = nam;
		continent = cont;
		
		setUnits(0);
		shape = sh;
		fillColor = new Color(0x222222);
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public String getContinent() {
		return continent;
	}

	public Shape getShape() {
		return shape;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

}
