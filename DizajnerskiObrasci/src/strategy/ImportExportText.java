package strategy;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class ImportExportText implements ImportExportStrategy {

	private ArrayList<String> logList;
	
	public ImportExportText() {
		this.logList=new ArrayList<String>();
	}
	
	public ImportExportText(ArrayList<String> logList) {
		
		this.logList=logList;
	}
	
	
	@Override
	public void exportFile(String filePath) {
		try(FileWriter fileWriter = new FileWriter(filePath)){
			for(String s : logList) {
				fileWriter.write(s + "\n");		
			}
			
			fileWriter.close();	
		}catch(Exception exception){
			
			JOptionPane.showMessageDialog(null, "Canceled save");
		}
		
		
	}

	@Override
	public void importFile(String filePath) {
		try{
			File file = new File(filePath);
			try (Scanner scanner = new Scanner(file)) {
				while(scanner.hasNextLine()) {
					logList.add(scanner.nextLine());
				}
			}catch(Exception exception){
				JOptionPane.showMessageDialog(null, "Canceled load");
			}
			if(logList.size()>1)
			{
				logList.remove(0);
			}
			
		}catch(Exception exception) {
			
			JOptionPane.showMessageDialog(null, "Canceled load");
		}
		
	}

	public ArrayList<String> getLogList() {
		return logList;
	}

	public void setLogList(ArrayList<String> logList) {
		this.logList = logList;
	}

}
