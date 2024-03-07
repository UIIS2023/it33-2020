package command;

import java.util.ArrayList;

import geometry.Shape;

public class MoveBringToFrontCommand implements ICommand {

	private Shape selectedShape;
	private int index;
	private ArrayList<Shape> shapes;
	
	public MoveBringToFrontCommand(Shape selectedShape, ArrayList<Shape> shapes) {
		this.selectedShape=selectedShape;
		this.shapes=shapes;
	}
	
	
	@Override
	public void execute() {
		index = shapes.indexOf(selectedShape);
		int lastIndex = shapes.size() - 1;
		shapes.remove(index);
		shapes.add(lastIndex,selectedShape);
		
	}

	@Override
	public void unexecute() {
		shapes.remove(selectedShape);
		shapes.add(index, selectedShape);
		
	}

}
