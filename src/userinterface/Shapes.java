package userinterface;
/**
 * interface of each shape class, aims to sumarize all kinds of shapes,
 * including Line, Rect, Poly, Oval, and Arc class
 */
public interface Shapes {
	public void draw();
	public void fill();
	public void erase();
	public void invert();

}
