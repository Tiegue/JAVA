package userinterface;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import ecs100.UI;

public class Poly implements Shapes, Serializable {
	private double[] x;
	private double[] y;
	//ArrayList <Double> x = new ArrayList<Double>();
	private double widthLine;
	Color colLine;
	Color colFill;
	
	public Poly() {}
	public Poly(double[] x, double[] y, int points, double widthLine, Color colLine, Color colFill) {
		//this.x = Arrays.copyOf(x, x.length);
		//this.y = Arrays.copyOf(y, y.length);
		this.x = x;
		this.y = y;
		this.widthLine= widthLine;
		this.colLine = colLine;
		this.colFill = colFill;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		UI.setColor(this.colLine);
		UI.setLineWidth(this.widthLine);
		UI.drawPolygon(this.x, this.y, this.x.length);
	}
	@Override
	public void fill() {
		// TODO Auto-generated method stub
		UI.setColor(this.colFill);
		UI.fillPolygon(this.x, this.y, this.x.length);
	}

	@Override
	public void erase() {
		// TODO Auto-generated method stub
		UI.erasePolygon(this.x, this.y, this.x.length);
	}

	@Override
	public void invert() {
		// TODO Auto-generated method stub
		UI.invertPolygon(this.x, this.y, this.x.length);
	}
	public String toString() {
		if (this.x.length == 4) {
			return "Tri： (" + 
		           this.x[0] + "," + this.y[0] + "),(" + 
		           this.x[1] + "," + this.y[1] + "),(" + 
				   this.x[2] + "," + this.y[2] + ").";
		}else {
		    return "Polygon： Start(" + this.x[0] + "," + this.y[0] + "), Points Num: " + this.x.length + ".";
		}
	}
	public double[] getX() {
		return x;
	}
	public void setX(double[] x) {
		this.x = x;
	}
	public double[] getY() {
		return y;
	}
	public void setY(double[] y) {
		this.y = y;
	}
	public double getWidthLine() {
		return widthLine;
	}
	public void setWidthLine(double widthLine) {
		this.widthLine = widthLine;
	}
	public Color getColLine() {
		return colLine;
	}
	public void setColLine(Color colLine) {
		this.colLine = colLine;
	}
	public Color getColFill() {
		return colFill;
	}
	public void setColFill(Color colFill) {
		this.colFill = colFill;
	}

}
