package command;

import java.util.ArrayList;

import geometry.Shape;

public class DeleteCommand implements ICommand {

	private ArrayList<Shape> shapes;
	private ArrayList<Shape> selectedShapes;
	private ArrayList<Shape> previousShapes = new ArrayList<Shape>();
	
	public DeleteCommand(ArrayList<Shape> shapes,ArrayList<Shape> selectedShapes) {
		this.shapes=shapes;
		this.selectedShapes=selectedShapes;
		this.previousShapes.addAll(shapes);
	}
	
	@Override
	public void execute() {
		
		shapes.removeAll(selectedShapes);
		selectedShapes.clear();
		
	}

	@Override
	public void unexecute() {

		
		shapes.clear();
		shapes.addAll(previousShapes);
		for(Shape shape : shapes)
		{
			if(shape.isSelected())
			{
				selectedShapes.add(shape);
			}
		}
	}

}
