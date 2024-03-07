package command;

import geometry.Line;

public class ModifyLineCommand implements ICommand {

	private Line currentLine;
	private Line previousLine;
	private Line nextLine;
	
	public ModifyLineCommand(Line previousLine, Line nextLine) {
		this.previousLine=previousLine;
		this.nextLine=nextLine;
	}
	
	@Override
	public void execute() {
		currentLine = previousLine.clone();
		previousLine.setStartPoint(nextLine.getStartPoint());
		previousLine.setEndPoint(nextLine.getEndPoint());
		previousLine.setColor(nextLine.getColor());
		
	}

	@Override
	public void unexecute() {
		previousLine.setStartPoint(currentLine.getStartPoint());
		previousLine.setEndPoint(currentLine.getEndPoint());
		previousLine.setColor(currentLine.getColor());
		
	}

}
