package userinterface;

import java.awt.Color;
import java.io.Serializable;

import ecs100.UI;

public class Oval implements Shapes, Serializable {
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	private double widthLine;
	Color colLine;
	Color colFill;
	
	public Oval() {}
	public Oval(double x1, double y1, double x2, double y2, double widthLine, Color colLine, Color colFill) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.widthLine = widthLine;
		this.colLine = colLine;
		this.colFill = colFill;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		double xMin = this.x1;
		double yMin = this.y1;
		if (this.x2 < this.x1) {xMin = this.x2;}
		if (this.y2 < this.y1) {yMin = this.y2;}
		UI.setColor(this.colLine);
		UI.setLineWidth(this.widthLine);		
		UI.drawOval(xMin, yMin, Math.abs(this.x1 - this.x2), Math.abs(this.y1 - this.y2));		
	}

	@Override
	public void fill() {
		// TODO Auto-generated method stub
		double xMin = this.x1 + this.widthLine;
		double yMin = this.y1 + this.widthLine;
		if (this.x2 < this.x1) {xMin = this.x2 + this.widthLine;}
		if (this.y2 < this.y1) {yMin = this.y2 + this.widthLine;}
		UI.setColor(this.colFill);	
		UI.fillOval(xMin, yMin, Math.abs(this.x1 - this.x2) - this.widthLine*2, Math.abs(this.y1 - this.y2) - this.widthLine*2);		
	}

	@Override
	public void erase() {
		// TODO Auto-generated method stub
		double xMin = this.x1;
		double yMin = this.y1;
		if (this.x2 < this.x1) {xMin = this.x2;}
		if (this.y2 < this.y1) {yMin = this.y2;}
		UI.eraseOval(xMin, yMin, Math.abs(this.x1 - this.x2), Math.abs(this.y1 - this.y2));
	}

	@Override
	public void invert() {
		// TODO Auto-generated method stub
		double xMin = this.x1 + this.widthLine;
		double yMin = this.y1 + this.widthLine;
		if (this.x2 < this.x1) {xMin = this.x2 + this.widthLine;}
		if (this.y2 < this.y1) {yMin = this.y2 + this.widthLine;}
		UI.invertOval(xMin, yMin, Math.abs(this.x1 - this.x2) - this.widthLine*2, Math.abs(this.y1 - this.y2) - this.widthLine*2);
	}
	public String toString() {
		return "Oval：(" + this.x1 + "," + this.y1 + "),(" + this.x2 + "," + this.y2 + ").";
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
