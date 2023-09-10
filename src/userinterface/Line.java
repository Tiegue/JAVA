package userinterface;

import java.awt.Color;
import java.io.Serializable;

import ecs100.UI;
/**
 * Line class, for dealing with line, methods include draw, fill, erase, invert, and toString,
 * implements Serializable just for ObjectInputStream etc.
 */
public class Line implements Shapes, Serializable {
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	private double width;
	Color col;
	public Line() {}
	public Line(double x1, double y1, double x2, double y2, double width, Color col) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.width = width;
		this.col = col;
	}


	@Override
	public void draw() {
		// TODO Auto-generated method stub
		UI.setColor(this.col);
		UI.setLineWidth(this.width);
		UI.drawLine(this.x1, this.y1, this.x2, this.y2);
	}

	@Override
	public void fill() {
		// TODO Auto-generated method stub
		UI.setColor(this.col);
		UI.setLineWidth(this.width);
		UI.drawLine(this.x1, this.y1, this.x2, this.y2);
	}
	@Override
	public void erase() {
		UI.eraseLine(this.x1, this.y1, this.x2, this.y2);
	}	
	@Override
	public void invert() {}
	
	public String toString() {
		return "Line： (" + this.x1 + ", " + this.y1 + "), (" + this.x1 + ", " + this.y2 + "), " + this.width +".";
	}
	
	public double getX1() {
		return x1;
	}
	public void setX1(double x1) {
		this.x1 = x1;
	}
	public double getY1() {
		return y1;
	}
	public void setY1(double y1) {
		this.y1 = y1;
	}
	public double getX2() {
		return x2;
	}
	public void setX2(double x2) {
		this.x2 = x2;
	}
	public double getY2() {
		return y2;
	}
	public void setY2(double y2) {
		this.y2 = y2;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public Color getCol() {
		return col;
	}
	public void setCol(Color col) {
		this.col = col;
	}

}
