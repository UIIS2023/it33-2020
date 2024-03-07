package mvc;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import command.AddCommand;
import command.DeleteCommand;
import command.ICommand;
import command.ModifyCircleCommand;
import command.ModifyDonutCommand;
import command.ModifyHexagonCommand;
import command.ModifyLineCommand;
import command.ModifyPointCommand;
import command.ModifyRectangleCommand;
import command.MoveBringToBackCommand;
import command.MoveBringToFrontCommand;
import command.MoveToBackCommand;
import command.MoveToFrontCommand;
import drawing.DlgCircle;
import drawing.DlgDonut;
import drawing.DlgHexagonAdapter;
import drawing.DlgLine;
import drawing.DlgPoint;
import drawing.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.HexagonAdapter;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import observer.IObservable;
import observer.IObserver;
import strategy.ImportExportSerialization;
import strategy.ImportExportStrategy;
import strategy.ImportExportText;

public class Controller implements IObservable {
	
	private FrameDrawing frameDrawing;
	private Model model;	
	
	private Shape selectedShape;
	
	private ArrayList<Shape> shapes ;
	private ArrayList<Shape> selectedShapes;
	private ArrayList<IObserver> observers;
	private ArrayList<ICommand> executedCommands;
	private ArrayList<ICommand> unexecutedCommands;
	private DefaultListModel<String> logList;
	private ArrayList<String> logListStepByStep;
	
	private Point startPoint;
	
	public Controller() {
		
	}
	
	public Controller(FrameDrawing frameDrawing,Model model) {
		this.frameDrawing=frameDrawing;
		this.model=model;
		this.shapes=model.getShapes();
		this.selectedShapes=model.getSelectedShapes();
		this.observers=new ArrayList<IObserver>();
		this.executedCommands=new ArrayList<ICommand>();
		this.unexecutedCommands=new ArrayList<ICommand>();
		this.logList=frameDrawing.getLogList();
	}
	
	protected void thisMouseClicked(MouseEvent me) {
		Shape newShape = null;
		Point click = new Point(me.getX(), me.getY());

		if (frameDrawing.getTglbtnSelect().isSelected()) {
			selectedShape = null;
			ListIterator<Shape> iterator = shapes.listIterator(shapes.size());
			
			while (iterator.hasPrevious()) {
				Shape shape = iterator.previous();
				if (shape.contains(click.getX(), click.getY()) && shape.isSelected() == false) {
					selectedShape = shape;
					break;
				}	
				else if (shape.contains(click.getX(), click.getY()))
				{
					shape.setSelected(false);
					selectedShapes.remove(shape);
					logList.addElement("Unselect " + shape.toString());
					frameDrawing.repaint();
					notifyObservers();
					return;
				}
			}

			if (selectedShape != null)			
			{
				selectedShape.setSelected(true);
				selectedShapes.add(selectedShape);
				logList.addElement("Select " + selectedShape.toString());
			}
			else 
			{
				for (Shape shape : selectedShapes)
				{
					shape.setSelected(false);
					if(selectedShapes.size()==1) {
						logList.addElement("Unselect " + shape.toString());
					}
				}
				if(selectedShapes.size()>1) {
					logList.addElement("UnselectAll selected shapes");
				}
				
				
				selectedShapes.clear();
			}
			System.out.println(selectedShapes.size());
			
			notifyObservers();

		} else if (frameDrawing.getTglbtnPoint().isSelected()) {

			newShape = new Point(click.getX(), click.getY(), false, frameDrawing.getOutlineColor());

		} else if (frameDrawing.getTglbtnLine().isSelected()) {

			if (startPoint == null)
				startPoint = click;
			else {
				newShape = new Line(startPoint, new Point(me.getX(), me.getY()), false, frameDrawing.getOutlineColor());
				startPoint = null;
			}

		} else if (frameDrawing.getTglbtnCircle().isSelected()) {
			
			DlgCircle dialog = new DlgCircle();

			dialog.getTxtX().setText("" + Integer.toString(click.getX()));
			dialog.getTxtX().setEditable(false);
			dialog.getTxtY().setText("" + Integer.toString(click.getY()));
			dialog.getTxtY().setEditable(false);
			dialog.getBtnOutlineColor().setBackground(frameDrawing.getOutlineColor());
			dialog.getBtnInnerColor().setBackground(frameDrawing.getInnerColor());
			dialog.setVisible(true);
			
			if (dialog.isConfirm()) {
				newShape = dialog.getCircle();
				frameDrawing.setInnerColor(dialog.getBtnInnerColor().getBackground());
				frameDrawing.setOutlineColor(dialog.getBtnOutlineColor().getBackground());
			}

		} else if (frameDrawing.getTglbtnDonut().isSelected()) {
			
			DlgDonut dialog = new DlgDonut();
			dialog.setModal(true);
			dialog.getTxtX().setText("" + Integer.toString(click.getX()));
			dialog.getTxtX().setEditable(false);
			dialog.getTxtY().setText("" + Integer.toString(click.getY()));
			dialog.getTxtY().setEditable(false);
			dialog.getBtnOutlineColor().setBackground(frameDrawing.getOutlineColor());
			dialog.getBtnInnerColor().setBackground(frameDrawing.getInnerColor());
			dialog.setVisible(true);

			if (dialog.isConfirm()) {
				newShape = dialog.getDonut();
				frameDrawing.setInnerColor(dialog.getBtnInnerColor().getBackground());
				frameDrawing.setOutlineColor(dialog.getBtnOutlineColor().getBackground());	
			}
			
		} else if (frameDrawing.getTglbtnRectangle().isSelected()) {
	
			DlgRectangle dialog = new DlgRectangle();
			dialog.setModal(true);
			dialog.getTxtX().setText("" + Integer.toString(me.getX()));
			dialog.getTxtX().setEditable(false);
			dialog.getTxtY().setText("" + Integer.toString(me.getY()));
			dialog.getTxtY().setEditable(false);
			dialog.getBtnOutlineColor().setBackground(frameDrawing.getOutlineColor());
			dialog.getBtnInnerColor().setBackground(frameDrawing.getInnerColor());
			dialog.setVisible(true);

			if (dialog.isConfirm()) {
				newShape = dialog.getRect();
				frameDrawing.setInnerColor(dialog.getBtnInnerColor().getBackground());
				frameDrawing.setOutlineColor(dialog.getBtnOutlineColor().getBackground());
			}

			
			
		} else if (frameDrawing.getTglbtnHexagonAdapter().isSelected()) {
			
			DlgHexagonAdapter dialog = new DlgHexagonAdapter();

			dialog.getTxtX().setText("" + Integer.toString(click.getX()));
			dialog.getTxtX().setEditable(false);
			dialog.getTxtY().setText("" + Integer.toString(click.getY()));
			dialog.getTxtY().setEditable(false);
			dialog.getBtnOutlineColor().setBackground(frameDrawing.getOutlineColor());
			dialog.getBtnInnerColor().setBackground(frameDrawing.getInnerColor());
			dialog.setVisible(true);
			
			if (dialog.isConfirm()) {
				newShape = dialog.getHexagon();
				frameDrawing.setInnerColor(dialog.getBtnInnerColor().getBackground());
				frameDrawing.setOutlineColor(dialog.getBtnOutlineColor().getBackground());
				
			}
					
		}	

		if (newShape != null)
		{
			AddCommand addCommand = new AddCommand(shapes,selectedShapes,newShape);
			addCommand.execute();
			logList.addElement("Add " + newShape.toString());
			executedCommands.add(addCommand);
			unexecutedCommands.clear();
			notifyObservers();
		}
			
		
		frameDrawing.repaint();

	}
	
	protected void delete() {

		if (selectedShapes.isEmpty() == false) {
			int selectedOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Warning message",
					JOptionPane.YES_NO_OPTION);
			if (selectedOption == JOptionPane.YES_OPTION) {
				DeleteCommand deleteCommand = new DeleteCommand(shapes,selectedShapes);
				logList.addElement("Delete " + selectedShapes.size() + " selected shapes" );
				deleteCommand.execute();
				executedCommands.add(deleteCommand);
				unexecutedCommands.clear();
				notifyObservers();
			}
		} else {
			JOptionPane.showMessageDialog(null, "You haven't selected any shape!", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
		selectedShape = null;
		frameDrawing.repaint();
	}
	
	protected void modify() {

		
		if (selectedShapes.isEmpty() == false && selectedShapes.size() == 1) {
			selectedShape = selectedShapes.get(0);

			if (selectedShape instanceof Point) {

				Point p = (Point) selectedShape;
				DlgPoint dialog = new DlgPoint();

				dialog.getTxtX().setText("" + Integer.toString(p.getX()));
				dialog.getTxtY().setText("" + Integer.toString(p.getY()));
				dialog.getBtnColor().setBackground(p.getColor());
				dialog.setVisible(true);

				if (dialog.isConfirm()) {
					ModifyPointCommand modifyPoint = new ModifyPointCommand(p,dialog.getP());
					modifyPoint.execute();
					logList.addElement("Modify " + dialog.getP().toString());
					executedCommands.add(modifyPoint);
					unexecutedCommands.clear();
					frameDrawing.repaint();
				}

			} else if (selectedShape instanceof Donut) {

				Donut donut = (Donut) selectedShape;
				DlgDonut dialogd = new DlgDonut();

				dialogd.getTxtX().setText("" + Integer.toString(donut.getCenter().getX()));
				dialogd.getTxtY().setText("" + Integer.toString(donut.getCenter().getY()));
				dialogd.getTxtR().setText("" + Integer.toString(donut.getRadius()));
				dialogd.getTxtInnerR().setText("" + Integer.toString(donut.getInnerRadius()));
				dialogd.getBtnInnerColor().setBackground(donut.getInnerColor());
				dialogd.getBtnOutlineColor().setBackground(donut.getColor());
				dialogd.setModal(true);
				dialogd.setVisible(true);

				if (dialogd.isConfirm()) {
					ModifyDonutCommand modifyDonut = new ModifyDonutCommand(donut,dialogd.getDonut());
					modifyDonut.execute();
					logList.addElement("Modify " + dialogd.getDonut().toString());
					executedCommands.add(modifyDonut);
					unexecutedCommands.clear();
					frameDrawing.repaint();
				}
			} else if (selectedShape instanceof Circle) {

				Circle circle = (Circle) selectedShape;
				DlgCircle dialog = new DlgCircle();

				dialog.getTxtX().setText("" + Integer.toString(circle.getCenter().getX()));
				dialog.getTxtY().setText("" + Integer.toString(circle.getCenter().getY()));
				dialog.getTxtR().setText("" + Integer.toString(circle.getRadius()));
				dialog.getBtnInnerColor().setBackground(circle.getInnerColor());
				dialog.getBtnOutlineColor().setBackground(circle.getColor());

				dialog.setVisible(true);
				dialog.setModal(true);

				if (dialog.isConfirm()) {
					ModifyCircleCommand modifyCircle = new ModifyCircleCommand(circle,dialog.getCircle());
					modifyCircle.execute();
					logList.addElement("Modify " + dialog.getCircle().toString());
					executedCommands.add(modifyCircle);
					unexecutedCommands.clear();
					frameDrawing.repaint();
				}

			} else if (selectedShape instanceof Line) {

				Line line = (Line) selectedShape;
				DlgLine dialog = new DlgLine();

				dialog.getTxtSPX().setText("" + Integer.toString(line.getStartPoint().getX()));
				dialog.getTxtSPY().setText("" + Integer.toString(line.getStartPoint().getY()));
				dialog.getTxtEPX().setText("" + Integer.toString(line.getEndPoint().getX()));
				dialog.getTxtEPY().setText("" + Integer.toString(line.getEndPoint().getY()));
				dialog.getBtnOutlineColor().setBackground(line.getColor());

				dialog.setVisible(true);

				if (dialog.isConfirm()) {

					ModifyLineCommand modifyLine = new ModifyLineCommand(line,dialog.getLine());
					modifyLine.execute();
					logList.addElement("Modify " + dialog.getLine().toString());
					executedCommands.add(modifyLine);
					unexecutedCommands.clear();
					frameDrawing.repaint();
				}

			} else if (selectedShape instanceof Rectangle) {

				Rectangle rect = (Rectangle) selectedShape;
				DlgRectangle dialog = new DlgRectangle();

				dialog.getTxtX().setText("" + Integer.toString(rect.getUpperLeftPoint().getX()));
				dialog.getTxtY().setText("" + Integer.toString(rect.getUpperLeftPoint().getY()));
				dialog.getTxtHeight().setText("" + Integer.toString(rect.getHeight()));
				dialog.getTxtWidth().setText("" + Integer.toString(rect.getWidth()));
				dialog.getBtnInnerColor().setBackground(rect.getInnerColor());
				dialog.getBtnOutlineColor().setBackground(rect.getColor());
				dialog.setModal(true);
				dialog.setVisible(true);

				if (dialog.isConfirm()) {
					ModifyRectangleCommand modifyRectangle = new ModifyRectangleCommand(rect, dialog.getRect());
					modifyRectangle.execute();
					logList.addElement("Modify " + dialog.getRect().toString());
					executedCommands.add(modifyRectangle);
					unexecutedCommands.clear();
					frameDrawing.repaint();
				}
			} else if (selectedShape instanceof HexagonAdapter ) {
				
				HexagonAdapter hexagon = (HexagonAdapter) selectedShape;
				DlgHexagonAdapter dialog = new DlgHexagonAdapter();

				dialog.getTxtX().setText("" + Integer.toString(hexagon.getCenter().getX()));
				dialog.getTxtY().setText("" + Integer.toString(hexagon.getCenter().getY()));
				dialog.getTxtR().setText("" + Integer.toString(hexagon.getRadius()));
				dialog.getBtnInnerColor().setBackground(hexagon.getInnerColor());
				dialog.getBtnOutlineColor().setBackground(hexagon.getColor());

				dialog.setVisible(true);
				dialog.setModal(true);

				if (dialog.isConfirm()) {
					ModifyHexagonCommand modifyHexagon = new ModifyHexagonCommand(hexagon, dialog.getHexagon());
					modifyHexagon.execute();
					logList.addElement("Modify " + dialog.getHexagon().toString());
					executedCommands.add(modifyHexagon);
					unexecutedCommands.clear();
					frameDrawing.repaint();
				}
			}
			notifyObservers();

		}
		

	}
	
	protected void moveShapeToFront() {
		if (selectedShapes.isEmpty() == false && selectedShapes.size() == 1) {
			selectedShape = selectedShapes.get(0);
			if(shapes.get(shapes.size()-1).equals(selectedShape))
			{
				JOptionPane.showMessageDialog(null, "Your shape is on the top!", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
			else {
				MoveToFrontCommand moveToFrontCommand = new MoveToFrontCommand(selectedShape,shapes);
				moveToFrontCommand.execute();
				logList.addElement("ToFront " + selectedShape.toString());
				executedCommands.add(moveToFrontCommand);
				unexecutedCommands.clear();
				frameDrawing.repaint();
				notifyObservers();
			}

		} else {
			JOptionPane.showMessageDialog(null, "Please select only one shape!", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	protected void moveShapeToBack() {
		if (selectedShapes.isEmpty() == false && selectedShapes.size() == 1) {
			selectedShape = selectedShapes.get(0);
			if(shapes.get(0).equals(selectedShape))
			{
				JOptionPane.showMessageDialog(null, "Your shape is on the bottom!", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
			else {
				MoveToBackCommand moveToBackCommand = new MoveToBackCommand(selectedShape,shapes);
				moveToBackCommand.execute();
				logList.addElement("ToBack " + selectedShape.toString());
				executedCommands.add(moveToBackCommand);
				unexecutedCommands.clear();
				frameDrawing.repaint();
				notifyObservers();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please select only one shape!", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	protected void moveShapeBringToFront() {
		if (selectedShapes.isEmpty() == false && selectedShapes.size() == 1) {
			selectedShape = selectedShapes.get(0);
			
			if(shapes.get(shapes.size()-1).equals(selectedShape))
			{
				JOptionPane.showMessageDialog(null, "Your shape is on the top!", "Error",
						JOptionPane.WARNING_MESSAGE);
			} else {
				MoveBringToFrontCommand moveBringToFrontCommand = new MoveBringToFrontCommand(selectedShape,shapes);
				moveBringToFrontCommand.execute();
				logList.addElement("BringToFront  " + selectedShape.toString());
				executedCommands.add(moveBringToFrontCommand);
				unexecutedCommands.clear();
				frameDrawing.repaint();
				notifyObservers();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please select only one shape!", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	
	protected void moveShapeBringToBack() {
		if (selectedShapes.isEmpty() == false && selectedShapes.size() == 1) {
			selectedShape = selectedShapes.get(0);
			if(shapes.get(0).equals(selectedShape))
			{
				JOptionPane.showMessageDialog(null, "Your shape is on the bottom!", "Error",
						JOptionPane.WARNING_MESSAGE);
			} else {
				MoveBringToBackCommand moveBringToBackCommand = new MoveBringToBackCommand(selectedShape,shapes);
				moveBringToBackCommand.execute();
				logList.addElement("BringToBack " + selectedShape.toString());
				executedCommands.add(moveBringToBackCommand);
				unexecutedCommands.clear();
				frameDrawing.repaint();
				notifyObservers();
			}	
		} else {
			JOptionPane.showMessageDialog(null, "Please select only one shape!", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	protected void undoCommand() {
		if(executedCommands.size()>0)
		{
			ICommand command = executedCommands.get(executedCommands.size()-1);
			command.unexecute();
			logList.addElement("Undo " + command.toString().substring(0, command.toString().indexOf("@")));
			executedCommands.remove(executedCommands.size()-1);
			unexecutedCommands.add(command);
			frameDrawing.repaint();
			notifyObservers();
		}
		
	}
	
	protected void redoCommand() {
		if(unexecutedCommands.size()>0)
		{
			ICommand command = unexecutedCommands.get(unexecutedCommands.size()-1);
			command.execute();
			logList.addElement("Redo " + command.toString().substring(0, command.toString().indexOf("@")));
			unexecutedCommands.remove(unexecutedCommands.size()-1);
			executedCommands.add(command);
			frameDrawing.repaint();
			notifyObservers();
		}
		
	}
	
	protected void saveSerialization(String filePath) {
		if(shapes.isEmpty()) {
			
			JOptionPane.showMessageDialog(frameDrawing, "Cannot save empty shapes list");
		}	
		else {
			
			ImportExportStrategy strategy = new ImportExportSerialization(shapes);
			strategy.exportFile(filePath);
			
		}
		
	}
	
	protected void saveText(String filePath) {
		if(logList.size() <= 1) {
			
			JOptionPane.showMessageDialog(frameDrawing, "Cannot save empty log list");
		}else {
			ImportExportStrategy strategy = new ImportExportText(Collections.list(logList.elements()));
			strategy.exportFile(filePath + ".txt");
		}
		
	}
	
	protected void loadFile(String filePath) {	
		if(filePath.endsWith(".txt") == false)
		{
			ImportExportStrategy strategy = new ImportExportSerialization(shapes);
			strategy.importFile(filePath);
		}else
		{
			ImportExportText strategy = new ImportExportText();
			strategy.importFile(filePath);
			logListStepByStep = strategy.getLogList();
		}

		frameDrawing.repaint();
		
	}
	
	protected void stepByStep() {
		
		if(logListStepByStep == null || logListStepByStep.size() == 0 )
		{
			JOptionPane.showMessageDialog(frameDrawing, "Cannot load step by step log list is empty ");
		}else{
			String lineExecute = logListStepByStep.get(0);
			
			String[] elements = lineExecute.split(" ");
			if(elements[0].equals("Add")) {
				Shape newShape = null;
				if(elements[1].contains("Point")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[6]);
					int color = Integer.parseInt(elements[9]);
					newShape = new Point(x,y,false,new Color(color)); 
				}else if(elements[1].contains("Line")) {
					int startX = Integer.parseInt(elements[3]);
					int startY = Integer.parseInt(elements[5]);
					int endX = Integer.parseInt(elements[8]);
					int endY = Integer.parseInt(elements[10]);
					int color = Integer.parseInt(elements[13]);
					newShape = new Line(new Point(startX,startY),new Point(endX,endY),false,
							new Color(color));
				}else if(elements[1].contains("Circle") && (newShape instanceof Donut) == false) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int color = Integer.parseInt(elements[11]);
					int innerColor =Integer.parseInt(elements[14]);
					newShape = new Circle(new Point(x,y),radius,false,new Color(color),
							new Color(innerColor));
				}else if(elements[1].contains("Donut")) {
						int x = Integer.parseInt(elements[3]);
						int y = Integer.parseInt(elements[5]);
						int radius = Integer.parseInt(elements[8]);
						int innerRadius = Integer.parseInt(elements[11]);
						int color = Integer.parseInt(elements[14]);
						int innerColor =Integer.parseInt(elements[17]);
						newShape = new Donut(new Point(x,y),radius,innerRadius,false,new Color(color),
								new Color(innerColor));	
				}else if(elements[1].contains("Rectangle")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int width = Integer.parseInt(elements[8]);
					int hight = Integer.parseInt(elements[11]);
					int color = Integer.parseInt(elements[14]);
					int innerColor =Integer.parseInt(elements[17]);
					newShape = new Rectangle(new Point(x,y),hight,width,false,new Color(color),
							new Color(innerColor));
				}else if(elements[1].contains("Hexagon")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int color = Integer.parseInt(elements[11]);
					int innerColor =Integer.parseInt(elements[14]);
					newShape = new HexagonAdapter(new Point(x,y),radius,false,new Color(color),
							new Color(innerColor));
				}
					
			

				if (newShape != null)
				{
					AddCommand addCommand = new AddCommand(shapes,selectedShapes,newShape);
					addCommand.execute();
					logList.addElement("Add " + newShape.toString());
					executedCommands.add(addCommand);
					unexecutedCommands.clear();
					notifyObservers();
				}
			}else if(elements[0].equals("Select")) 
			{
				if(elements[1].contains("Point")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[6]);
					int color = Integer.parseInt(elements[9]);
					Point point = new Point(x,y,false,new Color(color));
					selectedShape = shapes.get(shapes.indexOf(point));	
				}else if(elements[1].contains("Line")) {
					int startX = Integer.parseInt(elements[3]);
					int startY = Integer.parseInt(elements[5]);
					int endX = Integer.parseInt(elements[8]);
					int endY = Integer.parseInt(elements[10]);
					int color = Integer.parseInt(elements[13]);
					Line line = new Line(new Point(startX,startY),new Point(endX,endY),false,
							new Color(color));
					selectedShape = shapes.get(shapes.indexOf(line));	
				}else if(elements[1].contains("Circle")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int color = Integer.parseInt(elements[11]);
					int innerColor =Integer.parseInt(elements[14]);
					Circle circle = new Circle(new Point(x,y),radius,false,new Color(color),
							new Color(innerColor));
					selectedShape = shapes.get(shapes.indexOf(circle));	
				}else if(elements[1].contains("Donut")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int innerRadius = Integer.parseInt(elements[11]);
					int color = Integer.parseInt(elements[14]);
					int innerColor =Integer.parseInt(elements[17]);
					Donut donut = new Donut(new Point(x,y),radius,innerRadius,false,new Color(color),
							new Color(innerColor));
					selectedShape = shapes.get(shapes.indexOf(donut));	
				}else if(elements[1].contains("Rectangle")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int width = Integer.parseInt(elements[8]);
					int hight = Integer.parseInt(elements[11]);
					int color = Integer.parseInt(elements[14]);
					int innerColor =Integer.parseInt(elements[17]);
					Rectangle rectangle = new Rectangle(new Point(x,y),hight,width,false,new Color(color),
							new Color(innerColor));
					selectedShape = shapes.get(shapes.indexOf(rectangle));	
				}else if(elements[1].contains("Hexagon")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int color = Integer.parseInt(elements[11]);
					int innerColor =Integer.parseInt(elements[14]);
					HexagonAdapter hexagon = new HexagonAdapter(new Point(x,y),radius,false,new Color(color),
							new Color(innerColor));
					selectedShape = shapes.get(shapes.indexOf(hexagon));	
				}
					
			

				if (selectedShape != null)
				{
					selectedShape.setSelected(true);
					selectedShapes.add(selectedShape);
					logList.addElement("Select " + selectedShape.toString());
					notifyObservers();
				}
			}else if(elements[0].equals("Unselect")) 
			{
				if(elements[1].contains("Point")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[6]);
					int color = Integer.parseInt(elements[9]);
					Point point = new Point(x,y,false,new Color(color));
					selectedShape = shapes.get(shapes.indexOf(point));	
				}else if(elements[1].contains("Line")) {
					int startX = Integer.parseInt(elements[3]);
					int startY = Integer.parseInt(elements[5]);
					int endX = Integer.parseInt(elements[8]);
					int endY = Integer.parseInt(elements[10]);
					int color = Integer.parseInt(elements[13]);
					Line line = new Line(new Point(startX,startY),new Point(endX,endY),false,
							new Color(color));
					selectedShape = shapes.get(shapes.indexOf(line));	
				}else if(elements[1].contains("Circle")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int color = Integer.parseInt(elements[11]);
					int innerColor =Integer.parseInt(elements[14]);
					Circle circle = new Circle(new Point(x,y),radius,false,new Color(color),
							new Color(innerColor));
					selectedShape = shapes.get(shapes.indexOf(circle));	
				}else if(elements[1].contains("Donut")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int innerRadius = Integer.parseInt(elements[11]);
					int color = Integer.parseInt(elements[14]);
					int innerColor =Integer.parseInt(elements[17]);
					Donut donut = new Donut(new Point(x,y),radius,innerRadius,false,new Color(color),
							new Color(innerColor));
					selectedShape = shapes.get(shapes.indexOf(donut));	
				}else if(elements[1].contains("Rectangle")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int width = Integer.parseInt(elements[8]);
					int hight = Integer.parseInt(elements[11]);
					int color = Integer.parseInt(elements[14]);
					int innerColor =Integer.parseInt(elements[17]);
					Rectangle rectangle = new Rectangle(new Point(x,y),hight,width,false,new Color(color),
							new Color(innerColor));
					selectedShape = shapes.get(shapes.indexOf(rectangle));	
				}else if(elements[1].contains("Hexagon")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int color = Integer.parseInt(elements[11]);
					int innerColor =Integer.parseInt(elements[14]);
					HexagonAdapter hexagon = new HexagonAdapter(new Point(x,y),radius,false,new Color(color),
							new Color(innerColor));
					selectedShape = shapes.get(shapes.indexOf(hexagon));	
				}

				if (selectedShape != null)
				{
					selectedShape.setSelected(false);
					selectedShapes.remove(selectedShape);
					logList.addElement("Unselect " + selectedShape.toString());
					notifyObservers();
				}
			}else if(elements[0].equals("UnselectAll")) {
				
				for (Shape shape : selectedShapes)
				{
					shape.setSelected(false);
					
				}
				
					logList.addElement("UnselectAll selected shapes");				
				
				    selectedShapes.clear();
				    notifyObservers();
			}else if(elements[0].equals("Modify")) {
				if(elements[1].contains("Point")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[6]);
					int color = Integer.parseInt(elements[9]);
					Point point = new Point(x,y,false,new Color(color));
					
					ModifyPointCommand modifyPoint = new ModifyPointCommand((Point)selectedShape,point);
					modifyPoint.execute();
					logList.addElement("Modify " + point.toString());
					executedCommands.add(modifyPoint);
					unexecutedCommands.clear();
					
				}else if(elements[1].contains("Line")) {
					int startX = Integer.parseInt(elements[3]);
					int startY = Integer.parseInt(elements[5]);
					int endX = Integer.parseInt(elements[8]);
					int endY = Integer.parseInt(elements[10]);
					int color = Integer.parseInt(elements[13]);
					Line line = new Line(new Point(startX,startY),new Point(endX,endY),false,
							new Color(color));
					
					ModifyLineCommand modifyLine = new ModifyLineCommand((Line)selectedShape,line);
					modifyLine.execute();
					logList.addElement("Modify " + line.toString());
					executedCommands.add(modifyLine);
					unexecutedCommands.clear();
				}else if(elements[1].contains("Circle")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int color = Integer.parseInt(elements[11]);
					int innerColor =Integer.parseInt(elements[14]);
					Circle circle = new Circle(new Point(x,y),radius,false,new Color(color),
							new Color(innerColor));
					
					ModifyCircleCommand modifyCircle = new ModifyCircleCommand((Circle)selectedShape,circle);
					modifyCircle.execute();
					logList.addElement("Modify " + circle.toString());
					executedCommands.add(modifyCircle);
					unexecutedCommands.clear();
				}else if(elements[1].contains("Donut")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int innerRadius = Integer.parseInt(elements[11]);
					int color = Integer.parseInt(elements[14]);
					int innerColor =Integer.parseInt(elements[17]);
					Donut donut = new Donut(new Point(x,y),radius,innerRadius,false,new Color(color),
							new Color(innerColor));
					
					ModifyDonutCommand modifyDonut = new ModifyDonutCommand((Donut)selectedShape,donut);
					modifyDonut.execute();
					logList.addElement("Modify " + donut.toString());
					executedCommands.add(modifyDonut);
					unexecutedCommands.clear();
				}else if(elements[1].contains("Rectangle")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int width = Integer.parseInt(elements[8]);
					int hight = Integer.parseInt(elements[11]);
					int color = Integer.parseInt(elements[14]);
					int innerColor =Integer.parseInt(elements[17]);
					Rectangle rectangle = new Rectangle(new Point(x,y),hight,width,false,new Color(color),
							new Color(innerColor));
					
					ModifyRectangleCommand modifyRectangle = new ModifyRectangleCommand((Rectangle)selectedShape,rectangle);
					modifyRectangle.execute();
					logList.addElement("Modify " + rectangle.toString());
					executedCommands.add(modifyRectangle);
					unexecutedCommands.clear();
				}else if(elements[1].contains("Hexagon")) {
					int x = Integer.parseInt(elements[3]);
					int y = Integer.parseInt(elements[5]);
					int radius = Integer.parseInt(elements[8]);
					int color = Integer.parseInt(elements[11]);
					int innerColor =Integer.parseInt(elements[14]);
					HexagonAdapter hexagon = new HexagonAdapter(new Point(x,y),radius,false,new Color(color),
							new Color(innerColor));
					
					ModifyHexagonCommand modifyHexagon = new ModifyHexagonCommand((HexagonAdapter)selectedShape,hexagon);
					modifyHexagon.execute();
					logList.addElement("Modify " + hexagon.toString());
					executedCommands.add(modifyHexagon);
					unexecutedCommands.clear();
					
				}
					
			}else if(elements[0].equals("Delete")) {
				DeleteCommand deleteCommand = new DeleteCommand(shapes,selectedShapes);
				logList.addElement("Delete " + selectedShapes.size() + " selected shapes" );
				deleteCommand.execute();
				executedCommands.add(deleteCommand);
				unexecutedCommands.clear();
				notifyObservers();
			}else if(elements[0].equals("ToFront")) {
				MoveToFrontCommand moveToFrontCommand = new MoveToFrontCommand(selectedShape,shapes);
				moveToFrontCommand.execute();
				logList.addElement("ToFront " + selectedShape.toString());
				executedCommands.add(moveToFrontCommand);
				unexecutedCommands.clear();
				notifyObservers();
			}else if(elements[0].equals("ToBack")) {
				MoveToBackCommand moveToBackCommand = new MoveToBackCommand(selectedShape,shapes);
				moveToBackCommand.execute();
				logList.addElement("ToBack " + selectedShape.toString());
				executedCommands.add(moveToBackCommand);
				unexecutedCommands.clear();
				notifyObservers();
			}else if(elements[0].equals("BringToFront")) {
				MoveBringToFrontCommand moveBringToFrontCommand = new MoveBringToFrontCommand(selectedShape,shapes);
				moveBringToFrontCommand.execute();
				logList.addElement("BringToFront  " + selectedShape.toString());
				executedCommands.add(moveBringToFrontCommand);
				unexecutedCommands.clear();
				notifyObservers();
			}else if(elements[0].equals("BringToBack")) {
				MoveBringToBackCommand moveBringToBackCommand = new MoveBringToBackCommand(selectedShape,shapes);
				moveBringToBackCommand.execute();
				logList.addElement("BringToBack " + selectedShape.toString());
				executedCommands.add(moveBringToBackCommand);
				unexecutedCommands.clear();
				notifyObservers();
			}else if(elements[0].equals("Undo")) {
				ICommand command = executedCommands.get(executedCommands.size()-1);
				command.unexecute();
				logList.addElement("Undo " + command.toString().substring(0, command.toString().indexOf("@")));
				executedCommands.remove(executedCommands.size()-1);
				unexecutedCommands.add(command);
				notifyObservers();
			}else if(elements[0].equals("Redo")) {
				ICommand command = unexecutedCommands.get(unexecutedCommands.size()-1);
				command.execute();
				logList.addElement("Redo " + command.toString().substring(0, command.toString().indexOf("@")));
				unexecutedCommands.remove(unexecutedCommands.size()-1);
				executedCommands.add(command);
				notifyObservers();
			}
			
			frameDrawing.repaint();
			
			logListStepByStep.remove(0);
		}
		
		
	}
	
	@Override
	public void addObserver(IObserver observer) {
		observers.add(observer);
		
	}

	@Override
	public void removeObserver(IObserver observer) {
		observers.remove(observer);
		
	}
	
	@Override
	public void notifyObservers() {

		boolean modify = false ;
		boolean delete = false;
		boolean undo = false;
		boolean redo = false;
		
		if(selectedShapes.size() > 1)
		{
			delete = true;
		}
		else if(selectedShapes.size() == 1) {
			modify = true;
			delete = true;	
		}
		
		if(executedCommands.size()>0)
		{
			undo=true;
			
		}
		if (unexecutedCommands.size() > 0){
			redo=true;
		} 
		
		for(IObserver observer : observers)
		{
			observer.update(modify, delete,undo, redo);
			
		}
		
		
		
	}
}
