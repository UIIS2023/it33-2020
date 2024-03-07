package geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.io.Serializable;

public class Donut extends Circle {

	private int innerRadius;
	
	public Donut() {
		
	}
	
	public Donut(Point center, int radius, int innerRadius) {
		super(center, radius);
		this.innerRadius = innerRadius;
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected) {
		this(center, radius, innerRadius);
		setSelected(selected);
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected, Color color) { 
		this(center, radius, innerRadius, selected);
		setColor(color);
	}
	
	public Donut(Point center, int radius, int innerRadius, boolean selected, Color color, Color innerColor) { 
		this(center, radius, innerRadius, selected, color);
		setInnerColor(innerColor);
	}
	
	public void draw(Graphics g) {
		Ellipse2D innerCircle = new Ellipse2D.Float(getCenter().getX() - this.innerRadius, getCenter().getY() - this.innerRadius, this.innerRadius * 2, this.innerRadius * 2);
		Ellipse2D outerCircle = new Ellipse2D.Float(getCenter().getX() - this.getRadius(), getCenter().getY() - this.getRadius(), this.getRadius() * 2, this.getRadius() * 2);
		
		Area donut = createDonut(innerCircle,outerCircle);
		fill(g,donut);
		g.setColor(getColor());
		((Graphics2D) g).draw(donut);
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(this.getCenter().getX() - getRadius() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() + getRadius() - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() - getRadius() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() + getRadius() - 3, 6, 6);
		}
		
	}
	
	private Area createDonut(Ellipse2D innerCircle,Ellipse2D outerCircle) {
		Area innerArea = new Area(innerCircle);
		Area outerArea = new Area(outerCircle);
		
		 outerArea.subtract(innerArea);
		 
		 return outerArea;
	}
	
	
	public void fill(Graphics g, Area donut) {
		g.setColor(getInnerColor());
		((Graphics2D) g).fill(donut);
		
	}
	
	public int compareTo(Object o) {
		if (o instanceof Donut) {
			return (int) (this.area() - ((Donut) o).area());
		}
		return 0;
	}
	
	public double area() {
		return super.area() - innerRadius * innerRadius * Math.PI;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Donut) {
			Donut d = (Donut) obj;
			if (this.getCenter().equals(d.getCenter()) &&
					this.getRadius() == d.getRadius() &&
					this.innerRadius == d.getInnerRadius()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean contains(int x, int y) {
		double dFromCenter = this.getCenter().distance(x, y);
		return super.contains(x, y) && dFromCenter > innerRadius;
	}
	
	public boolean contains(Point p) {
		double dFromCenter = this.getCenter().distance(p.getX(), p.getY());
		return super.contains(p.getX(), p.getY()) && dFromCenter > innerRadius;
	}
	
	public int getInnerRadius() {
		return this.innerRadius;
	}
	
	public void setInnerRadius(int innerRadius) {
		this.innerRadius = innerRadius;
	}
	
	@Override
	public Donut clone()  {
		Donut donut = new Donut(this.getCenter(),this.getRadius(),this.innerRadius,
				this.isSelected(),this.getColor(),this.getInnerColor());
		return donut;
	}

	public String toString() {
		return "Donut: center= " + this.getCenter().getX() + " , " + this.getCenter().getY() + 
				" , radius= " + this.getRadius() + " , innerRadius= "+ this.innerRadius +
				" , color= " + this.getColor().getRGB() +
				" , innerColor= " + this.getInnerColor().getRGB() ;
	}
	
}