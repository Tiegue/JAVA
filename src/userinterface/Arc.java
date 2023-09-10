package userinterface;

import java.awt.Color;
import java.io.Serializable;

import ecs100.UI;

public class Arc implements Shapes, Serializable {
	private double x1;
	private double y1;
	private double x2;
	private double y2;	
	private double startAngle;
	private double arcAngle;
	private double widthLine;
	Color colLine;
	Color colFill;
	
	public Arc() {}
	public Arc(double x1, double y1, double x2, double y2, double startAngle, double arcAngle, double widthLine,
			Color colLine, Color colFill) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.startAngle = startAngle;
		this.arcAngle = arcAngle;
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
		UI.drawArc(xMin, yMin, Math.abs(this.x1 - this.x2), Math.abs(this.y1 - this.y2),this.startAngle, this.arcAngle);		
	}

	@Override
	public void fill() {
		// TODO Auto-generated method stub
		double xMin = this.x1;
		double yMin = this.y1;
		if (this.x2 < this.x1) {xMin = this.x2;}
		if (this.y2 < this.y1) {yMin = this.y2;}
		UI.setColor(this.colFill);
		UI.fillArc(xMin, yMin, Math.abs(this.x1 - this.x2), Math.abs(this.y1 - this.y2),this.startAngle, this.arcAngle);		
	}

	@Override
	public void erase() {
		// TODO Auto-generated method stub
		double xMin = this.x1;
		double yMin = this.y1;
		if (this.x2 < this.x1) {xMin = this.x2;}
		if (this.y2 < this.y1) {yMin = this.y2;}
		UI.eraseArc(xMin, yMin, Math.abs(this.x1 - this.x2), Math.abs(this.y1 - this.y2),this.startAngle, this.arcAngle);		
	}

	@Override
	public void invert() {
		// TODO Auto-generated method stub
		double xMin = this.x1;
		double yMin = this.y1;
		if (this.x2 < this.x1) {xMin = this.x2;}
		if (this.y2 < this.y1) {yMin = this.y2;}
		UI.invertArc(xMin, yMin, Math.abs(this.x1 - this.x2), Math.abs(this.y1 - this.y2),this.startAngle, this.arcAngle);		
	}
	public String toString() {
		return "Arc：(" + this.x1 + "," + this.y1 + "),(" + this.x2 + "," + this.y2 + 
				"),angle("+ this.startAngle + "," + this.arcAngle + ").";
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
	public double getStartAngle() {
		return startAngle;
	}
	public void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
	}
	public double getArcAngle() {
		return arcAngle;
	}
	public void setArcAngle(double arcAngle) {
		this.arcAngle = arcAngle;
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
