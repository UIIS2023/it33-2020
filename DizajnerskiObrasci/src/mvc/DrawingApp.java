package mvc;

public class DrawingApp {

	public static void main(String[] args) {
		System.out.println("Dobrodosli na vezbe iz predmeta Dizajnerski obrasci.");
		
		Model model = new Model();
		
		FrameDrawing frameDrawing = new FrameDrawing();
		
		View view = frameDrawing.getPnlDrawing();
		
		view.setModel(model);
		
		Controller controller =  new Controller(frameDrawing,model);
		
		controller.addObserver(frameDrawing);
		
		frameDrawing.setVisible(true);
		
		frameDrawing.setController(controller);
	}

}
