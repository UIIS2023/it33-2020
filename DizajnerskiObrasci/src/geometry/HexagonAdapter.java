package geometry;

import java.awt.Color;
import java.awt.Graphics;

import hexagon.Hexagon;

public class HexagonAdapter extends SurfaceShape implements Cloneable {
	
	private Hexagon hexagon ;
	
	public HexagonAdapter()
	{
		
	}
	
	public HexagonAdapter(Point center, int radius, boolean selected, Color color, Color innerColor) {
		hexagon = new Hexagon(center.getX(),center.getY(),radius);
		hexagon.setSelected(selected);
		hexagon.setBorderColor(color);
		hexagon.setAreaColor(innerColor);	
	}

	@Override
	public void moveBy(int byX, int byY) {
		this.getCenter().moveBy(byX, byY);
		
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof HexagonAdapter) {
			return (this.getRadius() - ((HexagonAdapter) o).getRadius());
		}
		return 0;
	}

	@Override
	public double area() {
		
		return 0;
	}

	@Override
	public void fill(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);	
	}

	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
		
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof HexagonAdapter) {
			HexagonAdapter prosledjeni = (HexagonAdapter) obj;
			if (this.getCenter().equals(prosledjeni.getCenter()) &&
					this.getRadius() == prosledjeni.getRadius()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public Point getCenter() {
		Point center = new Point(hexagon.getX(),hexagon.getY());
		return center;
				
	}
	
	public void setCenter(int x,int y) {
		hexagon.setX(x);
		hexagon.setY(y);
	}
	
	public int getRadius() {
		return hexagon.getR();
	}
	
	public void setRadius(int radius) {
		hexagon.setR(radius);
	}
	
	public Color getColor() {
		return hexagon.getBorderColor();
	}
	
	public void setColor(Color color) {
		hexagon.setBorderColor(color);
	}
	
	public Color getInnerColor() {
		return hexagon.getAreaColor();
	}
	
	public void setInnerColor(Color innerColor) {
		hexagon.setAreaColor(innerColor);
	}
	
	@Override
	public HexagonAdapter clone() {
		HexagonAdapter hexagon = new HexagonAdapter(this.getCenter(),this.getRadius(),
				this.isSelected(),this.getColor(),this.getInnerColor());
		return hexagon ;
	}

	public void setSelected(boolean selected) {
		hexagon.setSelected(selected);
		super.setSelected(selected);
	}

	@Override
	public String toString() {
		return "Hexagon: center= " + this.getCenter().getX() + " , " + this.getCenter().getY() + 
				" , radius= " + this.getRadius() + " , color= " + this.getColor().getRGB() +
				" , innerColor= " + this.getInnerColor().getRGB() ;
	}
	
	

}
