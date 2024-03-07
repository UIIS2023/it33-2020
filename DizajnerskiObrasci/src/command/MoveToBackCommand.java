package command;

import java.util.ArrayList;

import geometry.Shape;

public class MoveToBackCommand implements ICommand {
	private Shape selectedShape;
	private int index;
	private ArrayList<Shape> shapes;
	
	public MoveToBackCommand(Shape selectedShape,ArrayList<Shape> shapes) {
		this.selectedShape=selectedShape;
		this.shapes=shapes;
	}
	@Override
	public void execute() {
		index = shapes.indexOf(selectedShape);
		Shape belowShape = shapes.get(index-1);
		shapes.set(index-1,selectedShape);
		shapes.set(index, belowShape);
		
	}

	@Override
	public void unexecute() {
		Shape aboveShape = shapes.get(index);
		shapes.set(index, selectedShape);
		shapes.set(index - 1 , aboveShape);
		
	}

}
