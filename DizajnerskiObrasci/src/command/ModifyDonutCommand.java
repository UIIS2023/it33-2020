package command;

import geometry.Donut;

public class ModifyDonutCommand implements ICommand {
	private Donut currentDonut;
	private Donut previousDonut;
	private Donut nextDonut;
	
	public ModifyDonutCommand(Donut previousDonut, Donut nextDonut) {
		this.previousDonut=previousDonut;
		this.nextDonut=nextDonut;
	}
	@Override
	public void execute() {
		currentDonut = previousDonut.clone();
		previousDonut.setCenter(nextDonut.getCenter());
		previousDonut.setRadius(nextDonut.getRadius());
		previousDonut.setInnerRadius(nextDonut.getInnerRadius());
		previousDonut.setColor(nextDonut.getColor());
		previousDonut.setInnerColor(nextDonut.getInnerColor());
		
	}

	@Override
	public void unexecute() {
		previousDonut.setCenter(currentDonut.getCenter());
		previousDonut.setRadius(currentDonut.getRadius());
		previousDonut.setInnerRadius(currentDonut.getInnerRadius());
		previousDonut.setColor(currentDonut.getColor());
		previousDonut.setInnerColor(currentDonut.getInnerColor());
		
	}

}
