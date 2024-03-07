package command;

import geometry.Circle;


public class ModifyCircleCommand implements ICommand{
	private Circle currentCircle;
	private Circle previousCircle;
	private Circle nextCircle;
	
	public ModifyCircleCommand(Circle previousCircle, Circle nextCircle) {
		this.previousCircle=previousCircle;
		this.nextCircle=nextCircle;
	}
	@Override
	public void execute() {
		currentCircle = previousCircle.clone();
		previousCircle.setCenter(nextCircle.getCenter());
		previousCircle.setRadius(nextCircle.getRadius());
		previousCircle.setColor(nextCircle.getColor());
		previousCircle.setInnerColor(nextCircle.getInnerColor());
		
	}

	@Override
	public void unexecute() {
		previousCircle.setCenter(currentCircle.getCenter());
		previousCircle.setRadius(currentCircle.getRadius());
		previousCircle.setColor(currentCircle.getColor());
		previousCircle.setInnerColor(currentCircle.getInnerColor());
		
	}

}
