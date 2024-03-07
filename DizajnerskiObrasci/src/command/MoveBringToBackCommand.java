package command;

import java.util.ArrayList;

import geometry.Shape;

public class MoveBringToBackCommand implements ICommand{

	private Shape selectedShape;
	private int index;
	private ArrayList<Shape> shapes;
	
	public MoveBringToBackCommand(Shape selectedShape,ArrayList<Shape> shapes) {
		this.selectedShape = selectedShape;
		this.shapes = shapes;
	}
	@Override
	public void execute() {
		index = shapes.indexOf(selectedShape);
		int firstIndex = 0;
		shapes.remove(selectedShape);
		shapes.add(firstIndex, selectedShape);
		
	}

	@Override
	public void unexecute() {
		shapes.remove(selectedShape);
		shapes.add(index, selectedShape);
		
	}

}
