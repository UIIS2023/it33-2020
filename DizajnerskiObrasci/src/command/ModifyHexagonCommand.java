package command;

import geometry.HexagonAdapter;

public class ModifyHexagonCommand implements ICommand {
	private HexagonAdapter currentHexagon;
	private HexagonAdapter previousHexagon;
	private HexagonAdapter nextHexagon;
	
	public ModifyHexagonCommand(HexagonAdapter previousHexagon, HexagonAdapter nextHexagon) {
		this.previousHexagon=previousHexagon;
		this.nextHexagon=nextHexagon;
	}
	
	
	@Override
	public void execute() {
		currentHexagon = previousHexagon.clone();
		previousHexagon.setCenter(nextHexagon.getCenter().getX(), nextHexagon.getCenter().getY());
		previousHexagon.setRadius(nextHexagon.getRadius());
		previousHexagon.setColor(nextHexagon.getColor());
		previousHexagon.setInnerColor(nextHexagon.getInnerColor());
			
	}

	@Override
	public void unexecute() {
		previousHexagon.setCenter(currentHexagon.getCenter().getX(), currentHexagon.getCenter().getY());
		previousHexagon.setRadius(currentHexagon.getRadius());
		previousHexagon.setColor(currentHexagon.getColor());
		previousHexagon.setInnerColor(currentHexagon.getInnerColor());
		
	}

}
