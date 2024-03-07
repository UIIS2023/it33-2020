package command;

import geometry.Point;

public class ModifyPointCommand implements ICommand {

	private Point currentPoint;
	private Point previousPoint;
	private Point nextPoint;
	
	public ModifyPointCommand(Point previousPoint, Point nextPoint) {
		this.previousPoint=previousPoint;
		this.nextPoint=nextPoint;
	}

	@Override
	public void execute() {
		currentPoint = previousPoint.clone();
		previousPoint.setX(nextPoint.getX());
		previousPoint.setY(nextPoint.getY());
		previousPoint.setColor(nextPoint.getColor());
		
	}

	@Override
	public void unexecute() {
		previousPoint.setX(currentPoint.getX());
		previousPoint.setY(currentPoint.getY());
		previousPoint.setColor(currentPoint.getColor());
		
	}
	
}
