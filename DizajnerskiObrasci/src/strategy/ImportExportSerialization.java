package strategy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import geometry.Shape;

public class ImportExportSerialization implements ImportExportStrategy {

	private ArrayList<Shape> shapes ;
	public ImportExportSerialization(ArrayList<Shape> shapes) {
		this.shapes=shapes;
		
	}
	
	@Override
	public void exportFile(String filePath) {
		try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			
			objectOutputStream.writeObject(shapes);
			
			fileOutputStream.close();
		}catch(Exception exception){
			JOptionPane.showMessageDialog(null, "Canceled save");
		}
		
	}

	@Override
	public void importFile(String filePath) {
		try(FileInputStream fileInputStream = new FileInputStream(filePath)){
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			
			ArrayList<Shape> fileShapes = (ArrayList<Shape>) objectInputStream.readObject();
			
			for(Shape shape : fileShapes)
			{
				shapes.add(shape);
			}
			
			fileInputStream.close();
			
		}catch(Exception excepton) {
			JOptionPane.showMessageDialog(null, "Canceled load");
		}
		
	}

}
