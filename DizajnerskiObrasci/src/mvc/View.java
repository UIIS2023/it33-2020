package mvc;

import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JPanel;

import geometry.Shape;

public class View extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Model model = new Model();
	/**
	 * Create the panel.
	 */
	public View() {

	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Iterator<Shape> iterator = model.getShapes().iterator();
		while (iterator.hasNext())
			iterator.next().draw(g);

	}

	
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	

}
