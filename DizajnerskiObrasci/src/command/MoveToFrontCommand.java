package command;

import java.util.ArrayList;

import geometry.Shape;
import mvc.Model;

public class MoveToFrontCommand implements ICommand{

	private Shape selectedShape;
	private int index;
	private ArrayList<Shape> shapes;
	
	public MoveToFrontCommand(Shape selectedShape,ArrayList<Shape> shapes) {
		this.selectedShape = selectedShape;
		this.shapes=shapes;
	}
	
	@Override
	public void execute() {
		index = shapes.indexOf(selectedShape);
		Shape aboveShape = shapes.get(index+1);
		shapes.set(index+1, selectedShape);
		shapes.set(index, aboveShape);
		
	}

	@Override
	public void unexecute() {
		Shape belowShape = shapes.get(index);
		shapes.set(index, selectedShape);
		shapes.set(index +1, belowShape);
		
	}
	
}
