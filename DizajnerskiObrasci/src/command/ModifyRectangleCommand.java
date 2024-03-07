package command;

import geometry.Rectangle;

public class ModifyRectangleCommand implements ICommand{
	private Rectangle currentRectangle;
	private Rectangle previousRectangle;
	private Rectangle nextRectangle;
	
	public ModifyRectangleCommand(Rectangle previousRectangle, Rectangle nextRectangle) {
		this.previousRectangle=previousRectangle;
		this.nextRectangle=nextRectangle;
	}
	@Override
	public void execute() {
		currentRectangle = previousRectangle.clone();
		previousRectangle.setUpperLeftPoint(nextRectangle.getUpperLeftPoint());
		previousRectangle.setHeight(nextRectangle.getHeight());
		previousRectangle.setWidth(nextRectangle.getWidth());
		previousRectangle.setColor(nextRectangle.getColor());
		previousRectangle.setInnerColor(nextRectangle.getInnerColor());
		
	}

	@Override
	public void unexecute() {
		previousRectangle.setUpperLeftPoint(currentRectangle.getUpperLeftPoint());
		previousRectangle.setHeight(currentRectangle.getHeight());
		previousRectangle.setWidth(currentRectangle.getWidth());
		previousRectangle.setColor(currentRectangle.getColor());
		previousRectangle.setInnerColor(currentRectangle.getInnerColor());
		
	}

}
