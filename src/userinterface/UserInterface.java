package userinterface;

import java.awt.Color;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import ecs100.UI;
/**
 * The diagram project's UserInterface,
 * Product: "BTC" - A Simple Freehand Drawing Software especially for Kids, Designed for Mouse Use.
 * @author zhangtiec (tiegue)
 * @version 1.01
 */
public class UserInterface implements Serializable {
	private static final long serialVersionUID = 1L;
	List<Shapes> shapes = new ArrayList <Shapes>();
	ArrayList<Line> lines = new ArrayList<Line>();
	ArrayList<Rect> rects = new ArrayList<Rect>();
	ArrayList<Arc> arcs = new ArrayList<Arc>();
	ArrayList<Oval> ovals = new ArrayList<Oval>();
	ArrayList<Poly> polygons = new ArrayList<Poly>();
	
	//define my canvas parameters
	private double xCan = 0;//x of canvas
	private double yCan = 0;//y of canvas
	private double widthCan = 650;//width of canvas
	private double HeightCan = 600;//height of canvas
	private double widthLine = 2;//width of line, can be changed
	private int spaceScale = 50;//space of each scale
	private int scale = 5;// length of scale line
	Color colLine = Color.darkGray;//used for all draw method
	Color colFill = Color.yellow;//used for all fill method
	public boolean isChanged = false;//monitor the change of shapes
	private String type = "";
	//  first point XY-positon
	private double x1 = 0;
	private double y1 = 0;
	private double arcStartAngle = 90;
	private double arcAngle = 90;
	// mark the index and position info of current shape selected by Mouse pressed
	private int indexCrtShp = 99999;
	private double x1Temp = 0;
	private double y1Temp = 0;
	private double x2Temp = 0;
	private double y2Temp = 0;
	private double[] xTempPoly;
	private double[] yTempPoly;
	private double[] xFreePoly;
	private double[] yFreePoly;
	
	// initialize parameters if other method needs
	public void initialGlobalParameters() {
		this.colLine = Color.darkGray;//used for all draw method
		this.colFill = Color.yellow;//used for all fill method
		this.type = "";
		//  first point XY-positon
		this.x1 = 0;
		this.y1 = 0;
		this.arcStartAngle = 90;
		this.arcAngle = 90;
		// mark the index and position info of current shape selected by Mouse pressed
		this.indexCrtShp = 99999;
		this.x1Temp = 0;
		this.y1Temp = 0;
		this.x2Temp = 0;
		this.y2Temp = 0;
		this.xTempPoly = new double[0];
		this.yTempPoly = new double[0];
		this.xFreePoly = new double[0];
		this.yFreePoly = new double[0];
	}
	//the panel are mainly controlled by doMouse
	public void doMouse(String action, double x, double y) {
    	//"pressed" occurs when you click the mouse down
    	//"released" occurs when release
    	//"clicked" occurs only when both of those happen at the same space
    	//("pressed", "released", or "clicked", "doubleclicked")"moved", or "dragged")
		if (action.equals("pressed") ) {
			
			//only if Mouse is in canvas area, do drawing work
			if (x > 0 && y> 0 && x < this.xCan + this.widthCan && y < this.yCan + this.HeightCan) {
				//mark the first point
				this.x1 = x;
				this.y1 = y;
				//get current shape index, if it ondrawing return 99999
				indexCrtShp = this.getCurrentShapeIndex(x, y);
			}//end if in canvas			
		}//end if pressed
		
		if (action.equals("released") ) {
			
			//only if Mouse is in canvas area, do drawing work
			if (x > 0 && y> 0 && x < this.xCan + this.widthCan && y < this.yCan + this.HeightCan) {
				//draw work
				switch (type.toLowerCase()) {
				case "line":
					Line ln = new Line(this.x1, this.y1, x, y, 
							  this.widthLine, this.colLine);
					shapes.add(ln);
					lines.add(ln);
					ln.draw();
					UI.println(ln);
					break;
				case "triangle":
		    		double[] triX = new double[]{this.x1, this.x1 + Math.abs(x - this.x1)/2, this.x1 - Math.abs(x - this.x1)/2, this.x1};
			        double[] triY = new double[]{this.y1, y, y, this.y1};
			        Poly tri = new Poly(triX, triY, triX.length, 
							  this.widthLine, this.colLine, this.colFill);
			        shapes.add(tri);					
					polygons.add(tri);
					tri.draw();
					tri.fill();
					UI.println(tri);
					break;
				case "rectangle":
					Rect rt = new Rect(this.x1, this.y1, x, y, 
							  this.widthLine, this.colLine, this.colFill);
					shapes.add(rt);					
					rects.add(rt);
					rt.draw();
					rt.fill();
					UI.println(rt);
					break;
				case "arc":
					Arc ac = new Arc(this.x1, this.y1, x, y, 
							  this.arcStartAngle, this.arcAngle, this.widthLine, this.colLine, this.colFill);
					shapes.add(ac);					
					arcs.add(ac);
					ac.draw();
					ac.fill();
					UI.println(ac);
					break;
				case "oval":
					Oval ol = new Oval(this.x1, this.y1, x, y, 
							  this.widthLine, this.colLine, this.colFill);
					shapes.add(ol);					
					ovals.add(ol);
					ol.draw();
					ol.fill();
					break;
								
				}//end witch
				//set the shapes changed
				if (!type.equals("")) {
					this.isChanged = true;
				}
			}//end if canvas			
		}//end if released
		
		if (action.equals("clicked") ) {
			//colFill get color
			this.chooseColor("fill", x, y);	
			
			//only if Mouse is in canvas area, 
			//and type == polygon
			if (x > 0 && y> 0 && 
				x < this.xCan + this.widthCan && 
				y < this.yCan + this.HeightCan&&
				this.type.equals("polygon")) {
				
				if (this.xFreePoly.length == 0) {
					this.xFreePoly = new double[] {x,x};
					this.yFreePoly = new double[] {y,y};
					UI.println("Polygon ondrawing ...");
					
				}else{
					//array length +1, and set xy in last 2nd index, set x0y0 in last index
					double x0 = this.xFreePoly[0];
					double y0 = this.yFreePoly[0];
					this.xFreePoly = Arrays.copyOf(this.xFreePoly, this.xFreePoly.length + 1);
					this.yFreePoly = Arrays.copyOf(this.yFreePoly, this.yFreePoly.length + 1);
					int len = this.xFreePoly.length;					
					this.xFreePoly[len -1] = x;
					this.yFreePoly[len -1] = y;
					//if points >=3 draw polygon
					if (len > 2) {
						UI.setColor(this.colLine);
						UI.drawPolygon(this.xFreePoly, this.yFreePoly, len);
						UI.setColor(this.colFill);
						UI.fillPolygon(this.xFreePoly, this.yFreePoly, len);
							    
					}//end if (len >2)
					
				}//end if else
				
			}//end if (x > 0 && y> 0 && ....
			
		}//end if mouse clicked
		
		if (action.equals("doubleclicked") ) {			
			
		}
		
		if (action.equals("moved") ) {
			// show MouseXY real time
			this.showMouseXY(x, y);			
		}//end if moved
		
		if (action.equals("dragged") ) {
			// show MouseXY real time
			this.showMouseXY(x, y);
			//colLine get color
			this.chooseColor("line", x, y);//colLine get color
			
      /*=====do move or delete current selected shape by Mouse pressed just now =========*/
			if (indexCrtShp != 99999) {
				if (x > 0 && y> 0 && x < this.xCan + this.widthCan && y < this.yCan + this.HeightCan) {
					//only if Mouse is in canvas area, do erase and then redraw work
					shapes.get(indexCrtShp).erase();
					
					//compute new positon of the current selected shape by xy of Mouse dragged now
					//the first x1y1 reset to xy of Mouse, do not need to compute
					//other points need to compute
					double x2New = this.x2Temp + x - this.x1Temp;
					double y2New = this.y2Temp + y - this.y1Temp;
					Shapes spCurrent = shapes.get(indexCrtShp);
					if(spCurrent instanceof Line){
						((Line) spCurrent).setX1(x);
						((Line) spCurrent).setY1(y);
						((Line) spCurrent).setX2(x2New);
						((Line) spCurrent).setY2(y2New);
					}
					if(spCurrent instanceof Rect){
						((Rect) spCurrent).setX1(x);
						((Rect) spCurrent).setY1(y);
						((Rect) spCurrent).setX2(x2New);
						((Rect) spCurrent).setY2(y2New);
					}
					if(spCurrent instanceof Oval){
						((Oval) spCurrent).setX1(x);
						((Oval) spCurrent).setY1(y);
						((Oval) spCurrent).setX2(x2New);
						((Oval) spCurrent).setY2(y2New);
					}
					if (spCurrent instanceof Arc) {
						((Arc) spCurrent).setX1(x);
						((Arc) spCurrent).setY1(y);
						((Arc) spCurrent).setX2(x2New);
						((Arc) spCurrent).setY2(y2New);
					}
					if(spCurrent instanceof Poly){
						double xMove = x - this.xTempPoly[0];
						double yMove = y - this.yTempPoly[0];
						for (int i = 0; i <this.yTempPoly.length; i++) {
							this.xTempPoly[i] = this.xTempPoly[i] + xMove;
							this.yTempPoly[i] = this.yTempPoly[i] + yMove;
						}
						//assign to array xy of poly
						((Poly) spCurrent).setX(this.xTempPoly);
						((Poly) spCurrent).setY(this.yTempPoly);
					}//end if (spCurrent instanceof Poly)
					UI.sleep(2);
					this.reDrawAll();
				}else {//delete the current selected shape from shapes for ever, and redraw all shapes
					UI.sleep(2);
					shapes.remove(indexCrtShp);
					this.indexCrtShp = 99999;
					this.reDrawAll();
				}//end if else: if (x > 0 && y> 0 && x < this.xCan + this.widthCan && y < this.yCan + this.HeightCan)
				//hint the shapes changed
				if (!type.equals("")) {
					this.isChanged = true;
				}
			}//end if (indexCrtShp != 99999)
			
		    /*=====do resize all shapes on canvas include canvas itself by drag the right-bottom point area(40x40) of canvas =========*/
			if (x > 420 && x < 670 && y > 440 && y < 640&&// set the canvas max 650X600 min 400*400
				Math.abs(x-this.xCan-this.widthCan)<40 &&
				Math.abs(y-this.yCan-this.HeightCan)<40) {
				//max widthCan = 650;
				//max HeightCan = 600;
				//compute xyRate, 
				double xRate = (x - 20)/(this.xCan + this.widthCan);
				double yRate = (y - 20)/(this.yCan + this.HeightCan);
				//resize the width and height of canvas according to the current xyRate
				this.widthCan = this.widthCan*xRate;
				this.HeightCan = this.HeightCan*yRate;
				
				//redraw all based on current xyRate
				this.resizeShapes(xRate, yRate);
				//UI.sleep(10);
			}//end if do resize
			
		    /*=====do rotate all shapes on canvas by dragging in area of the rotate area near the rotate arrow =========*/
			if (x > 660 && x < 720 && y > 150 && y < 450){// set the rotate area in arrow area
				//resize to initial status
				if (this.widthCan != 650 || this.HeightCan != 600) {
					double xRate = 650/this.widthCan;
					double yRate = 600/this.HeightCan;
				    //resize the width and height of canvas according to the current xyRate
				    this.widthCan = 650;
				    this.HeightCan = 600;
				    //resize all shapes according to the current xyRate
				    this.resizeShapes(xRate, yRate);
				    UI.sleep(10);
				}
				
				//define cos and sin
				double cx = this.xCan + this.widthCan/2;
				double cy = this.yCan + this.HeightCan/2;
				double cos = (x - cx)/Math.sqrt((x - cx)*(x - cx) + (y - cy)*(y - cy));
				double sin = (y - cy)/Math.sqrt((x - cx)*(x - cx) + (y - cy)*(y - cy));
								
				//redraw all, based on cos and sin
				this.rotateShapes(cos, sin);
			}//end if rotate
		}//end if dragged
    }//end doMouse method
	public void testRotate(){
		//control rotation speed
		for(int i = 0; i<360;i++) {
			this.rotateShapes(Math.cos(i), Math.sin(i));
			UI.sleep(4);
		}
		UI.println("this is test");
	}
	/*=================Below for addButtons ==========*/ 
	public void addLine(){
		//click twice stop draw
		if (this.type.equalsIgnoreCase("polygon")) {//put current polygon in ArrayList shapes
			Poly pl = new Poly(this.xFreePoly, this.yFreePoly, this.xFreePoly.length,
					  this.widthLine, this.colLine, this.colFill); 
			shapes.add(pl);				
			polygons.add(pl);
			pl.draw();
			pl.fill();	
			UI.println("Current Polygon finished ...");
		}		
		if (!this.type.equalsIgnoreCase("line")) {
			this.type = "line";
		}else {this.type ="";}
	}
	public void addTriangle(){
		//click twice stop draw
		if (this.type.equalsIgnoreCase("polygon")) {//put current polygon in ArrayList shapes
			Poly pl = new Poly(this.xFreePoly, this.yFreePoly, this.xFreePoly.length,
					  this.widthLine, this.colLine, this.colFill); 
			shapes.add(pl);					
			polygons.add(pl);
			pl.draw();
			pl.fill();	
			UI.println("Current Polygon finished ...");
		}
		if (!this.type.equalsIgnoreCase("triangle")) {
			this.type = "triangle";
		}else {this.type ="";}
	}
	public void addRectangle(){
		//click twice stop draw
		if (this.type.equalsIgnoreCase("polygon")) {//put current polygon in ArrayList shapes
			Poly pl = new Poly(this.xFreePoly, this.yFreePoly, this.xFreePoly.length,
					  this.widthLine, this.colLine, this.colFill); 
			shapes.add(pl);					
			polygons.add(pl);
			pl.draw();
			pl.fill();
			UI.println("Current Polygon finished ...");
		}
		if (!this.type.equalsIgnoreCase("rectangle")) {
			this.type = "rectangle";
		}else {this.type ="";}
	}
	public void addOval(){
		//click twice stop draw
		if (this.type.equalsIgnoreCase("polygon")) {//put current polygon in ArrayList shapes
			Poly pl = new Poly(this.xFreePoly, this.yFreePoly, this.xFreePoly.length,
					  this.widthLine, this.colLine, this.colFill); 
			shapes.add(pl);					
			polygons.add(pl);
			pl.draw();
			pl.fill();
			UI.println("Current Polygon finished ...");
		}
		if (!this.type.equalsIgnoreCase("oval")) {
			this.type = "oval";
		}else {this.type ="";}
	}
	public void addArc(){
		//click twice stop draw
		if (this.type.equalsIgnoreCase("polygon")) {//put current polygon in ArrayList shapes
			Poly pl = new Poly(this.xFreePoly, this.yFreePoly, this.xFreePoly.length,
					  this.widthLine, this.colLine, this.colFill); 
			shapes.add(pl);					
			polygons.add(pl);
			pl.draw();
			pl.fill();	
			UI.println("Current Polygon finished ...");
		}
		if (!this.type.equalsIgnoreCase("arc")) {
			this.type = "arc";
		}else {this.type ="";}
	}
	public void addPolygon(){
		//click twice stop draw
		if (!this.type.equalsIgnoreCase("polygon")) {
			this.type = "polygon";
			this.xFreePoly = new double[0];
			this.yFreePoly = new double[0];
		}else {
			this.type ="";
			// put the final status of polygon to shapes
			Poly pl = new Poly(this.xFreePoly, this.yFreePoly, this.xFreePoly.length,
					  this.widthLine, this.colLine, this.colFill); 
			shapes.add(pl);					
			polygons.add(pl);
			pl.draw();
			pl.fill();
			UI.println("Current Polygon finished ...");
		}
	}
	public void invertAll(){
		UI.clearGraphics();
		// draw canvas and colorchooser
		UI.setLineWidth(1);		
		UI.setColor(Color.black);
		this.drawCanvas();//draw canvas with scales
		this.drawColorChooserPanel();//draw color chooser
		//draw from shapes
		for (Shapes sp: shapes) {
			if (shapes.indexOf(sp) == 0) {//index0 instore the canvas
				sp.draw();
			}else {
				sp.draw();
				sp.fill();
				sp.invert();
			}		
		}	
	}
	public void clearText(){
		UI.clearText();
	}
	/**
	 * clear canvas, clear arraylist shapes,graphics,and drawcanvas,draw color chooser panel,
	 * finally, save changes to file shapes.BTC.
	*/
	public void clearCanvas(){
		String yn = UI.askString("Delete all, Really? Y/N").toUpperCase().trim();
		if (yn.contains("Y")) {
			//delete all shapes and clear Graphics
			shapes.clear();
			UI.clearGraphics();
			// draw canvas and colorchooser
			this.widthLine = 1;
			UI.setLineWidth(1);		
			UI.setColor(Color.black);
			this.drawCanvas();//draw canvas with scales
			this.drawColorChooserPanel();//draw color chooser
			this.isChanged = true;
			this.saveToFile();
			UI.sleep(10);
		}
	}
	public void quitSaveAll(){
		this.saveToFile();
		UI.sleep(10);
		UI.quit();
	}

	public void widthSlider(double widthLine){
		this.widthLine = widthLine;
	}
	public void arcStartAngleSlider(double arcStartAngle){
		this.arcStartAngle = arcStartAngle;
	}
	public void arcAngleSlider(double arcAngle){
		this.arcAngle = arcAngle;
	}
	/*=================Below methods for rotate/ resize/move/delete current selected shape ==========*/ 
	
	/**
	*Rotate all shapes according to angle of Mouse point to center.
	*If rotate around (0,0), the fomula is: 
    *x' = x * cos - y * sin, 
    *y' = x * sin + y * cos.
    *in this Cartesian coordinate system, center intersection point of two crosslines is (x0,y0) instead of (0,0), 
	*so compute point(x',y') according to point(x0,y0), the formula above need to change a little bit: 
	*x'-x0 = (x-x0) * cos - (y-y0) * sin, 
    *y'-y0 = (x-x0) * sin + (y-y0) * cos. 
	* and then draw all.
	* @param cos means cos of rotate angle
	* @param sin means sin of rotate angle
	*/
	public void rotateShapes(double cos, double sin) {
		
		// set the center of canvas as the rotate center 
		double cx = this.xCan + this.widthCan/2;
		double cy = this.yCan + this.HeightCan/2;
		for (int i = 1; i < shapes.size(); i++) {
			double x1p;//x1'
			double y1p;//y1'
			double x2p;//x2'
			double y2p;//y2'
			Shapes spCurrent = shapes.get(i);
			if(spCurrent instanceof Line){
				//get value of 2 point of this Line
				this.x1Temp = ((Line) spCurrent).getX1();
				this.y1Temp = ((Line) spCurrent).getY1();
				this.x2Temp = ((Line) spCurrent).getX2();
				this.y2Temp = ((Line) spCurrent).getY2();
				//compute new x y
				x1p = (this.x1Temp - cx)*cos - (this.y1Temp - cy)*sin + cx;
				y1p = (this.x1Temp - cx)*sin + (this.y1Temp - cy)*cos + cy;
				x2p = (this.x2Temp - cx)*cos - (this.y2Temp - cy)*sin + cx;
				y2p = (this.x2Temp - cx)*sin + (this.y2Temp - cy)*cos + cy;
				//reset the value in this shape
				((Line) spCurrent).setX1(x1p);
				((Line) spCurrent).setY1(y1p);
				((Line) spCurrent).setX2(x2p);
				((Line) spCurrent).setY2(y2p);
			}
			if(spCurrent instanceof Rect){
				//get value of 2 point of this Rect
				this.x1Temp = ((Rect) spCurrent).getX1();
				this.y1Temp = ((Rect) spCurrent).getY1();
				this.x2Temp = ((Rect) spCurrent).getX2();
				this.y2Temp = ((Rect) spCurrent).getY2();
				//compute new x y
				x1p = (this.x1Temp - cx)*cos - (this.y1Temp - cy)*sin + cx;
				y1p = (this.x1Temp - cx)*sin + (this.y1Temp - cy)*cos + cy;
				x2p = (this.x2Temp - cx)*cos - (this.y2Temp - cy)*sin + cx;
				y2p = (this.x2Temp - cx)*sin + (this.y2Temp - cy)*cos + cy;
				//reset the value in this shape
				((Rect) spCurrent).setX1(x1p);
				((Rect) spCurrent).setY1(y1p);
				((Rect) spCurrent).setX2(x2p);
				((Rect) spCurrent).setY2(y2p);
			}	
			if(spCurrent instanceof Oval){
				//get value of 2 point of this Oval
				this.x1Temp = ((Oval) spCurrent).getX1();
				this.y1Temp = ((Oval) spCurrent).getY1();
				this.x2Temp = ((Oval) spCurrent).getX2();
				this.y2Temp = ((Oval) spCurrent).getY2();
				//compute new x y
				x1p = (this.x1Temp - cx)*cos - (this.y1Temp - cy)*sin + cx;
				y1p = (this.x1Temp - cx)*sin + (this.y1Temp - cy)*cos + cy;
				x2p = (this.x2Temp - cx)*cos - (this.y2Temp - cy)*sin + cx;
				y2p = (this.x2Temp - cx)*sin + (this.y2Temp - cy)*cos + cy;
				//reset the value in this shape
				((Oval) spCurrent).setX1(x1p);
				((Oval) spCurrent).setY1(y1p);
				((Oval) spCurrent).setX2(x2p);
				((Oval) spCurrent).setY2(y2p);
			}
			if (spCurrent instanceof Arc) {
				//get value of 2 point of this Arc
				this.x1Temp = ((Arc) spCurrent).getX1();
				this.y1Temp = ((Arc) spCurrent).getY1();
				this.x2Temp = ((Arc) spCurrent).getX2();
				this.y2Temp = ((Arc) spCurrent).getY2();
				//compute new x y
				x1p = (this.x1Temp - cx)*cos - (this.y1Temp - cy)*sin + cx;
				y1p = (this.x1Temp - cx)*sin + (this.y1Temp - cy)*cos + cy;
				x2p = (this.x2Temp - cx)*cos - (this.y2Temp - cy)*sin + cx;
				y2p = (this.x2Temp - cx)*sin + (this.y2Temp - cy)*cos + cy;
				//reset the value in this shape
				((Arc) spCurrent).setX1(x1p);
				((Arc) spCurrent).setY1(y1p);
				((Arc) spCurrent).setX2(x2p);
				((Arc) spCurrent).setY2(y2p);
			}
		if(spCurrent instanceof Poly){
				//get value of all points of this Poly
				this.xTempPoly = ((Poly)shapes.get(i)).getX();
				this.yTempPoly = ((Poly)shapes.get(i)).getY();
				
				//renew all the value, and then assign to Poly xy[]
				for (int j = 0; j <this.yTempPoly.length; j++) {
					//compute new x y
					this.xTempPoly[j] = (this.xTempPoly[j] - cx)*cos - (this.yTempPoly[j] - cy)*sin + cx;
					this.yTempPoly[j] = (this.yTempPoly[j] - cx)*sin + (this.yTempPoly[j] - cy)*cos + cy;
				}				
				//assign the renewed xyTempPoly to array xy of poly
				((Poly) spCurrent).setX(this.xTempPoly);
				((Poly) spCurrent).setY(this.yTempPoly);
			}//end if (spCurrent instanceof Poly)
		}//end for each loop
		UI.sleep(2);
		this.reDrawAll();
	}
	/**
	 * resize shapes according to canvas size changing
	 * @param xRate means the rate of new width/current width of canvas
	 * @param yRate means the rate of new height/current height of canvas
	 
	Set the every xy positon to xy*Rate
	and then draw
	other points need to compute
	double x2New = this.x2Temp + x - this.x1Temp;
	double y2New = this.y2Temp + y - this.y1Temp;
	*/
	public void resizeShapes(double xRate, double yRate) {
		
		for (int i = 0; i < shapes.size(); i++) {
			Shapes spCurrent = shapes.get(i);
			if(spCurrent instanceof Line){
				//get value of 2 point of this Line
				this.x1Temp = ((Line) spCurrent).getX1();
				this.y1Temp = ((Line) spCurrent).getY1();
				this.x2Temp = ((Line) spCurrent).getX2();
				this.y2Temp = ((Line) spCurrent).getY2();
				//reset the value xy*Rate				
				((Line) spCurrent).setX1(this.x1Temp*xRate);
				((Line) spCurrent).setY1(this.y1Temp*yRate);
				((Line) spCurrent).setX2(this.x2Temp*xRate);
				((Line) spCurrent).setY2(this.y2Temp*yRate);
			}
			if(spCurrent instanceof Rect){
				//get value of 2 point of this Rect
				this.x1Temp = ((Rect) spCurrent).getX1();
				this.y1Temp = ((Rect) spCurrent).getY1();
				this.x2Temp = ((Rect) spCurrent).getX2();
				this.y2Temp = ((Rect) spCurrent).getY2();
				//reset the value xy*Rate				
				((Rect) spCurrent).setX1(this.x1Temp*xRate);
				((Rect) spCurrent).setY1(this.y1Temp*yRate);
				((Rect) spCurrent).setX2(this.x2Temp*xRate);
				((Rect) spCurrent).setY2(this.y2Temp*yRate);				
			}
			if(spCurrent instanceof Oval){
				//get value of 2 point of this Oval
				this.x1Temp = ((Oval) spCurrent).getX1();
				this.y1Temp = ((Oval) spCurrent).getY1();
				this.x2Temp = ((Oval) spCurrent).getX2();
				this.y2Temp = ((Oval) spCurrent).getY2();
				//reset the value xy*Rate				
				((Oval) spCurrent).setX1(this.x1Temp*xRate);
				((Oval) spCurrent).setY1(this.y1Temp*yRate);
				((Oval) spCurrent).setX2(this.x2Temp*xRate);
				((Oval) spCurrent).setY2(this.y2Temp*yRate);
			}
			if (spCurrent instanceof Arc) {
				//get value of 2 point of this Arc
				this.x1Temp = ((Arc) spCurrent).getX1();
				this.y1Temp = ((Arc) spCurrent).getY1();
				this.x2Temp = ((Arc) spCurrent).getX2();
				this.y2Temp = ((Arc) spCurrent).getY2();
				//reset the value xy*Rate				
				((Arc) spCurrent).setX1(this.x1Temp*xRate);
				((Arc) spCurrent).setY1(this.y1Temp*yRate);
				((Arc) spCurrent).setX2(this.x2Temp*xRate);
				((Arc) spCurrent).setY2(this.y2Temp*yRate);
			}
			if(spCurrent instanceof Poly){
				//get value of all points of this Poly
				this.xTempPoly = ((Poly)shapes.get(i)).getX();
				this.yTempPoly = ((Poly)shapes.get(i)).getY();
				//renew all the value to xy*Rate, and then assign to Poly xy[]
				for (int j = 0; j <this.yTempPoly.length; j++) {
					this.xTempPoly[j] = this.xTempPoly[j]*xRate;
					this.yTempPoly[j] = this.yTempPoly[j]*yRate;
				}				
				//assign the renewed xyTempPoly to array xy of poly
				((Poly) spCurrent).setX(this.xTempPoly);
				((Poly) spCurrent).setY(this.yTempPoly);
			}//end if (spCurrent instanceof Poly)
			
		}//end for loop if (int i = 0; i < shapes.size(); i++)
		UI.sleep(2);
		this.reDrawAll();
	}
	/**
	 * draw all shapes including canvas
	 */
	public void reDrawAll() {
		UI.clearGraphics();
		// draw canvas and colorchooser
		UI.setLineWidth(1);	
		UI.setColor(Color.black);
		this.drawCanvas();//draw canvas with scales
		this.drawColorChooserPanel();//draw color chooser
		//draw from shapes
		for (Shapes sp: shapes) {
			if (shapes.indexOf(sp) == 0) {//index0 instore the canvas
				sp.draw();
			}else {
				sp.draw();
				sp.fill();
			}		
		}
		UI.setColor(this.colLine);
	}
	/**
	 * get the index of the shape on which the mouse dragging now 
	 * @param x x of mouse
	 * @param y y of mouse
	 * @return the index of dragging shape in arraylist shapes
	 * if return 99999 means no shape dragging 
	 */
	public int getCurrentShapeIndex(double x, double y) { //return 99999 means nothing to do
		// if canvas null or on drawing a shape, do nothing
		if (shapes.size() == 0 || !type.equals("")) {
			//UI.println("HINT: please click the STOP button first");
			return 99999;}
		//find the current shape which Mouse on now
		for (int i = shapes.size()-1; i > 0 ; i--) {
			if(shapes.get(i) instanceof Line){
				this.x1Temp = ((Line)shapes.get(i)).getX1();
				this.y1Temp = ((Line)shapes.get(i)).getY1();
				this.x2Temp = ((Line)shapes.get(i)).getX2();
				this.y2Temp = ((Line)shapes.get(i)).getY2();
				if (x >= Math.min(this.x1Temp, this.x2Temp) && (x <= Math.max(this.x1Temp, this.x2Temp))&&
					y >= Math.min(this.y1Temp, this.y2Temp) && (y <= Math.max(this.y1Temp, this.y2Temp))  ){//XY of Mouse are both between min and max of the shape
					return i;//mark current shape's index in the shapes
				}
			}else if(shapes.get(i) instanceof Rect){
				this.x1Temp = ((Rect)shapes.get(i)).getX1();
				this.y1Temp = ((Rect)shapes.get(i)).getY1();
				this.x2Temp = ((Rect)shapes.get(i)).getX2();
				this.y2Temp = ((Rect)shapes.get(i)).getY2();
				if (x >= Math.min(this.x1Temp, this.x2Temp) && (x <= Math.max(this.x1Temp, this.x2Temp))&&
					y >= Math.min(this.y1Temp, this.y2Temp) && (y <= Math.max(this.y1Temp, this.y2Temp))  ){
					return i;//mark current shape's index in the shapes
				}
			}else if(shapes.get(i) instanceof Oval){
				this.x1Temp = ((Oval)shapes.get(i)).getX1();
				this.y1Temp = ((Oval)shapes.get(i)).getY1();
				this.x2Temp = ((Oval)shapes.get(i)).getX2();
				this.y2Temp = ((Oval)shapes.get(i)).getY2();
				if (x >= Math.min(this.x1Temp, this.x2Temp) && (x <= Math.max(this.x1Temp, this.x2Temp))&&
					y >= Math.min(this.y1Temp, this.y2Temp) && (y <= Math.max(this.y1Temp, this.y2Temp))  ){
					return i;//mark current shape's index in the shapes
				}
			}else if (shapes.get(i) instanceof Arc) {
				this.x1Temp = ((Arc)shapes.get(i)).getX1();
				this.y1Temp = ((Arc)shapes.get(i)).getY1();
				this.x2Temp = ((Arc)shapes.get(i)).getX2();
				this.y2Temp = ((Arc)shapes.get(i)).getY2();
				if (x >= Math.min(this.x1Temp, this.x2Temp) && (x <= Math.max(this.x1Temp, this.x2Temp))&&
					y >= Math.min(this.y1Temp, this.y2Temp) && (y <= Math.max(this.y1Temp, this.y2Temp))  ){
					return i;//mark current shape's index in the shapes
				}
			}else if(shapes.get(i) instanceof Poly){
				int len =((Poly) shapes.get(i)).getX().length;//get points of poly shape	
				this.xTempPoly = Arrays.copyOf(((Poly)shapes.get(i)).getX(),len);
				this.yTempPoly = Arrays.copyOf(((Poly)shapes.get(i)).getY(),len);
				//select min and max of xyTemp
				double xMin = 99999;
				double yMin = 99999;
				double xMax = 0;
				double yMax = 0;
				for (int j = 0; j < this.xTempPoly.length; j++) {
					if (xMin >= this.xTempPoly[j]) {xMin = this.xTempPoly[j];}
					if (yMin >= this.yTempPoly[j]) {yMin = this.yTempPoly[j];}
					if (xMax <= this.xTempPoly[j]) {xMax = this.xTempPoly[j];}
					if (yMax <= this.yTempPoly[j]) {yMax = this.yTempPoly[j];}
				}
				if (x >= xMin && x <= xMax && y >= yMin && y <= yMax ){
					return i;//mark current shape's index in the shapes
				}
			}//end all if
		}//end for loop--
		return 99999;
	}	
	
	
	 // =================below for Frame, such as canvas, rotation icon and color chooser==========
	/**
	 * show Mouse xy in graphics
	 */  
	public void showMouseXY(double x, double y) {
		UI.setColor(Color.white);
		UI.fillRect(25, 20, 100, 60);
		UI.setColor(Color.red);
		UI.drawString("MouseX: " + x, 30, 33);
		UI.drawString("MouseY: " + y, 30, 45);
	}
	/**
	 * draw canvas, 
	 * important: canvas is stored in  index0 of arraylist shapes 
	 */
	public void drawCanvas() {
		//draw canvas frame;
		if (shapes.size() == 0) {
			Rect rt = new Rect(this.xCan, this.yCan, this.xCan + this.widthCan, this.yCan + this.HeightCan, this.widthLine, Color.black, Color.black);
			shapes.add(rt);
			rt.draw();
			UI.println(rt);
		}else {//when load from file or reDrawAll
			//judge the parameters of saved canvas before from the shapes[0] which contains the widthCan and HeightCan of canvas
			if(shapes.get(0) instanceof Rect){
				this.xCan = ((Rect)shapes.get(0)).getX1();
				this.yCan = ((Rect)shapes.get(0)).getY1();
				this.widthCan = ((Rect)shapes.get(0)).getX2() - this.xCan;
				this.HeightCan = ((Rect)shapes.get(0)).getY2() - this.yCan;
				this.widthLine = ((Rect)shapes.get(0)).getWidthLine();
				this.colLine = ((Rect)shapes.get(0)).getColLine();
				this.colFill = ((Rect)shapes.get(0)).getColFill();
				
			}
			shapes.get(0).draw();
		}
		//draw X&Y-asis and scale
		this.drawScale();
		this.drawCrossCenter();
		//draw tiegue's logo width=80
		UI.drawImage("logo.JPG", (this.xCan + this.widthCan/2 - 34), (this.yCan + this.HeightCan/2 - 40), 80, 80);
	}
	/**
	 * draw X&Y scale of canvas
	 */
	public void drawScale() {
		//draw scale of X-axis
		for (int i = 0; i < Math.round(this.widthCan/this.spaceScale) + 1; i++){
			UI.drawLine(this.xCan + this.spaceScale*i, this.yCan, this.xCan + this.spaceScale*i, this.yCan + this.scale);
			UI.drawString(Integer.toString(i*this.spaceScale), this.xCan + i*this.spaceScale, this.yCan + 20);
		}
		//draw scale of Y-axis
		for (int i = 0; i < Math.round(this.HeightCan/this.spaceScale) + 1; i++){
			UI.drawLine(this.xCan, this.yCan + this.spaceScale*i, this.xCan + this.scale, this.yCan + this.spaceScale*i);
			UI.drawString(Integer.toString(i*this.spaceScale),  this.xCan + 10, this.yCan + i*this.spaceScale);
		}
	}
	/**
	 * draw crossline at the center of canvas
	 */
	public void drawCrossCenter() {
		//draw vertical
		UI.drawLine(this.xCan + this.widthCan/2, this.yCan, this.xCan + this.widthCan/2, this.yCan + this.HeightCan);
		//draw horizan
		UI.drawLine(this.xCan, this.yCan + this.HeightCan/2, this.xCan + this.widthCan, this.yCan + this.HeightCan/2);
	}
	/**
	 * draw color choose area on the left side of window
	 * the last color block is for free choosing
	 * 
	 */
	public void drawColorChooserPanel() {
		int px = 730;// position x-axis
		int wd = 70;
		int ht = 40;
		UI.setColor(Color.lightGray);
		UI.fillRect(660, 0, wd+72, 656);
		
		UI.setColor(Color.cyan);
		UI.fillRect(px, 20, wd, ht);
		UI.setColor(Color.darkGray);
		UI.fillRect(px, 60, wd, ht);
		UI.setColor(Color.gray);
		UI.fillRect(px, 100, wd, ht);
		UI.setColor(Color.green);
		UI.fillRect(px, 140, wd, ht);
		UI.setColor(Color.lightGray);
		UI.fillRect(px, 180, wd, ht);
		UI.setColor(Color.magenta);
		UI.fillRect(px, 220, wd, ht);
		UI.setColor(Color.orange);
		UI.fillRect(px, 260, wd, ht);
		UI.setColor(Color.pink);
		UI.fillRect(px, 300, wd, ht);
		UI.setColor(Color.red);
		UI.fillRect(px, 340, wd, ht);
		UI.setColor(Color.white);
		UI.fillRect(px, 380, wd, ht);
		UI.setColor(Color.yellow);
		UI.fillRect(px, 420, wd, ht);
		UI.setColor(Color.black);
		UI.fillRect(px, 460, wd, ht);
		UI.setColor(Color.blue);
		UI.fillRect(px, 500, wd, ht);
		// set a free color chooser area
		UI.setColor(new Color((int)(Math.random()*(1<<24))));
		UI.fillRect(px, 540, wd, ht);
		UI.setColor(new Color((int)(Math.random()*(1<<24))));
		UI.setFontSize(30);
		UI.drawString(" Free", px, 570);
		UI.setFontSize(12);
		/*try {
			InputStream inputStream = UserInterface.class.getResourceAsStream("rotate.JPG");
			Image image = ImageIO.read(inputStream);
			//UI.drawImage(image,660, 150, 60, 300);
		}catch(IOException e){e.printStackTrace();}
		*/
		
		// add a rotate icon
		UI.drawImage("rotate.JPG", 660, 150, 60, 300);
		//draw a icon shows current color for line
		UI.setColor(this.colLine);
		UI.setLineWidth(5);
		//draw a icon showing current color for line
		UI.drawLine(668, 40, 705, 75);
		UI.setLineWidth(1);
		//draw a icon shows current color for fill
		UI.setColor(this.colFill);
		UI.fillOval(668, 85, 50, 40);
		
	}
	/**
	 * When Mouse in the chooser color area 800=px+wd>=X>=px=720, each block represents a color
	 */
	public void chooseColor(String lineOrFill,double x, double y) {//lineOrFill: line/Fill, for draw/fill method
		//if draw, the color assign to colLine
		if (x > 720 && x < 800 && lineOrFill.equalsIgnoreCase("line")) {//if draw
			UI.printMessage("Click to get draw color; pressed to get fill color");
			switch ((int)Math.floor((y-20)/40)) {//compute the Mouse in which color rectangle
			    case 0://UI.setColor(Color.cyan)
			    	this.colLine = Color.cyan;
			    	break;
			    case 1://UI.setColor(Color.darkGray);			    	
			    	this.colLine = Color.darkGray;
			    	break;
			    case 2://UI.setColor(Color.gray);			    	
			    	this.colLine = Color.gray;
			    	break;
			    case 3://UI.setColor(Color.green);			    	
			    	this.colLine = Color.green;
			    	break;
			    case 4://UI.setColor(Color.lightGray);			    	
			    	this.colLine = Color.lightGray;
			    	break;
			    case 5://UI.setColor(Color.magenta);			    	
			    	this.colLine = Color.magenta;
			    	break;
			    case 6://UI.setColor(Color.orange);			    	
			    	this.colLine = Color.orange;
			    	break;
			    case 7://UI.setColor(Color.pink);			    	
			    	this.colLine = Color.pink;
			    	break;
			    case 8://UI.setColor(Color.red);			    	
			    	this.colLine = Color.red;
			    	break;
			    case 9://UI.setColor(Color.white);			    	
			    	this.colLine = Color.white;
			    	break;
			    case 10://UI.setColor(Color.yellow);			    	
			    	this.colLine = Color.yellow;
			    	break;
			    case 11://UI.setColor(Color.black);			    	
			    	this.colLine = Color.black;
			    	break;
			    case 12://UI.setColor(Color.blue);			    	
			    	this.colLine = Color.blue;
			    	break;
			    case 13://free chooser			    	
			    	this.colLine = JColorChooser.showDialog(null, "Draw Chooser", Color.darkGray);
			    	break;
			}//end of switch
			//draw a icon shows current color for line
			UI.setColor(this.colLine);
			UI.setLineWidth(5);
			UI.drawLine(668, 40, 705, 75);
			UI.setLineWidth(1);
		}//end if draw
		//if fill, the color assign to colFill
		if (x > 720 && x < 800 && lineOrFill.equalsIgnoreCase("Fill")) {//if fill
			UI.printMessage("Click to get draw color; Pressed to get fill color");
			switch ((int)Math.floor((y-20)/40)) {//compute the Mouse in which color rectangle
			    case 0://UI.setColor(Color.cyan)
			    	this.colFill = Color.cyan;
			    	break;
			    case 1://UI.setColor(Color.darkGray);			    	
			    	this.colFill = Color.darkGray;
			    	break;
			    case 2://UI.setColor(Color.gray);			    	
			    	this.colFill = Color.gray;
			    	break;
			    case 3://UI.setColor(Color.green);			    	
			    	this.colFill = Color.green;
			    	break;
			    case 4://UI.setColor(Color.lightGray);			    	
			    	this.colFill = Color.lightGray;
			    	break;
			    case 5://UI.setColor(Color.magenta);			    	
			    	this.colFill = Color.magenta;
			    	break;
			    case 6://UI.setColor(Color.orange);			    	
			    	this.colFill = Color.orange;
			    	break;
			    case 7://UI.setColor(Color.pink);			    	
			    	this.colFill = Color.pink;
			    	break;
			    case 8://UI.setColor(Color.red);			    	
			    	this.colFill = Color.red;
			    	break;
			    case 9://UI.setColor(Color.white);			    	
			    	this.colFill = Color.white;
			    	break;
			    case 10://UI.setColor(Color.yellow);			    	
			    	this.colFill = Color.yellow;
			    	break;
			    case 11://UI.setColor(Color.black);			    	
			    	this.colFill = Color.black;
			    	break;
			    case 12://UI.setColor(Color.blue);			    	
			    	this.colFill = Color.blue;
			    	break;
			    case 13://free chooser			    	
			    	this.colFill = JColorChooser.showDialog(null, "Fill Chooser", Color.yellow);
			    	break;
			}//end of switch
			//draw a icon shows current color for fill
			UI.setColor(this.colFill);
			UI.fillOval(668, 85, 50, 40);
		}//end if fill
	}//end chooseColor method
	
	/*=================below for load from and save shapes to files 'shapes.BTC'==========*/
	/**
	 * Use the FileInputStream and ObjectInputStream to load the shapes from shapes.BTC.
	 * and then call the reDrawAll method to draw all the shapes on screen.
	 */
	//I self-studied the FileInputStream and ObjectInputStream knowledge and used/praticed in Team project last week 
	public void loadFromFile() {//"shapes.BTC"
		try {
			FileInputStream shapeFile = new FileInputStream("shapes.BTC");
		    ObjectInputStream shapeLoad = new ObjectInputStream(shapeFile);
		    this.shapes = (List<Shapes>) shapeLoad.readObject();
		    shapeLoad.close();
		    UI.println("---shapes.BTC loaded success---");
		    // draw all the shapes loaded just now
		    this.reDrawAll();
		} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
	}
	/**
	 * Use the FileOutputStream and ObjectOutputStream to save the shapes to shapes.BTC.
	 * only when arraylist shapes be changed.
	 */ 
    public void saveToFile() {// 
    	if (this.isChanged == false) {return;}// if no changed, return
    	try {
		    // Write to shapes.BTC
		    FileOutputStream shapeFile = new FileOutputStream("shapes.BTC");
		    ObjectOutputStream shapeSave = new ObjectOutputStream(shapeFile);
		    shapeSave.writeObject(this.shapes);
		    shapeSave.flush();
		    shapeSave.close();
		    
		    LocalDate today = LocalDate.now();
		    LocalTime time = LocalTime.now();
		    UI.println("---All shapes(" + shapes.size()+ ") saved to shapes.BTC----");
		    UI.println("---at " + today + " " + time + "---");
		    //mark all the shapes saved, so no changed things, 
		    //until some changes happened when other method will mark it true
		    this.isChanged = false;
		} catch (IOException e) {e.printStackTrace();}    	
	}
    /**
     * every 5 minutes call the saveToFile method.
     * the method is learned from gpt, but I rebuild it
     */
    public void autoSave() { //the method is learned from gpt, but I rebuild it
    	
    	// Create a Timer
    	Timer saveTime = new Timer();
    	// Set the initial delay before the task starts
    	long delay = 0;
    	// Set the interval period for subsequent executions (5 minutes in milliseconds)
    	long period = 5 * 60 * 1000;
    	// Define the task to be executed
    	TimerTask task = new TimerTask() {
    	    @Override
    	    public void run() {
    	        saveToFile();
    	    }
    	};
    	// Schedule the task to run with the specified delay and period
    	saveTime.schedule(task, delay, period);    	
    }
    
	public UserInterface() {
		UI.initialise();
		//Initialize painting area
		UI.setWindowSize(1030, 700);
		UI.setLineWidth(1);	
		UI.setColor(Color.black);
		this.drawCanvas();//draw canvas with scales
		this.drawColorChooserPanel();//draw color chooser
		
		//add buttons
		UI.addButton("Line/ Stop", this::addLine);
		UI.addButton("Triangle/ Stop", this::addTriangle);
		UI.addButton("Rectange/ Stop", this::addRectangle);
		UI.addButton("Oval/ Stop", this::addOval);
		UI.addButton("Arc/ Stop", this::addArc);
		UI.addButton("Polygon/ Stop", this::addPolygon);		
		UI.addButton("Invert All", this::invertAll);
		UI.addButton("Clear Text", this::clearText);
		UI.addButton("Clear Canvas", this::clearCanvas);
		UI.addButton("Quit & SaveAll", this::quitSaveAll);
		UI.addButton("test", this:: testRotate);
		
		UI.addSlider("Line Width", 1, 20, this.widthLine,this::widthSlider);
		UI.addSlider("Arc Start Angle", 0, 180, this.arcStartAngle,this::arcStartAngleSlider);
		UI.addSlider("Arc Angle", 0, 360, this.arcAngle,this::arcAngleSlider);
		
		UI.setMouseMotionListener(this::doMouse);				
		//set autoSave every 5 minutes if shapes have changed
		this.autoSave();				
		//auto load work from existed file shapes.BTC that was saved before
		this.loadFromFile();
		
	}

	public static void main(String[] args) {
		new UserInterface();
    }

}



