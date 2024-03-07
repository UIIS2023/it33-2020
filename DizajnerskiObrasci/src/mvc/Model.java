package mvc;

import java.util.ArrayList;

import geometry.Shape;

public class Model {
	
	private ArrayList<Shape> shapes;

	private ArrayList<Shape> selectedShapes;
	
	public Model()
	{
		shapes= new ArrayList<Shape>();
		selectedShapes = new ArrayList<Shape>();
	}


	public ArrayList<Shape> getShapes() {
		return shapes;
	}


	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
	}
	
	public void addShape(Shape newShape)
	{
		shapes.add(newShape);
	}
	
	public void deleteShape(Shape shape)
	{
		shapes.remove(shape);
	}

	public ArrayList<Shape> getSelectedShapes() {
		return selectedShapes;
	}


	public void setSelectedShapes(ArrayList<Shape> selectedShapes) {
		this.selectedShapes = selectedShapes;
	}
	
	public void addSelectedShape(Shape newShape)
	{
		selectedShapes.add(newShape);
	}
	
	public void deleteSelectedShape(Shape shape)
	{
		selectedShapes.remove(shape);
	}
	
}

