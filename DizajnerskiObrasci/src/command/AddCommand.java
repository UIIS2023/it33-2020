package command;

import java.util.ArrayList;

import geometry.Shape;

public class AddCommand implements ICommand {
	
	private ArrayList<Shape> shapes;
	private Shape shape;
	private ArrayList<Shape> selectedShapes;

	public AddCommand(ArrayList<Shape> shapes,ArrayList<Shape> selectedShapes,Shape shape) {
		this.shapes=shapes;
		this.shape=shape;
		this.selectedShapes=selectedShapes;
	}
	@Override
	public void execute() {
		shapes.add(shape);
		if(shape.isSelected())
		{
			selectedShapes.add(shape);
		}
		
	}

	@Override
	public void unexecute() {
		if(shape.isSelected())
		{
			selectedShapes.remove(shape);
		}
		shapes.remove(shape);
			
	}

}
