import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FileParser {
	
	private BufferedReader br;
	private BufferedReader br2;
	private String[] puzzle;
	private int counter = 0;
	
	public FileParser(File file) {
		System.out.println(file.getAbsolutePath() + " selected.");
		try {
			br = new BufferedReader(new FileReader(file));
			br2 = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			e.printStackTrace();
		}
	}
	
	public String getLine() {
		String newLine = null;
        try {
            newLine = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newLine;
	}
	
	public String getLineAgain() {
		String newLine = null;
        try {
            newLine = br2.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newLine;
	}
	
	public Boolean fileIsReady() {
        Boolean ready = false;
        try {
            ready = br.ready(); //will change 'ready' to true
        } catch (Exception e) {
            System.err.println("config file not readable");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("The config file could not be read");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
        return ready;
    }
	
	@SuppressWarnings("unused")
	public void makeArray() {
		while(fileIsReady() == true) {
			String line = getLine();
			counter++;
		}
		puzzle = new String[counter];
	}
	
	public void fillArray() {
		for(int i=0; i<counter; i++) {
			String line = getLineAgain();
			puzzle[i] = line;
		}
	}
	
	public boolean checkIfValid() {
		double counter = 0;
		boolean adj = false;
		for(String str : puzzle) {
			String[] test = str.split(",");
			counter += test.length;
		}
		for(String str : puzzle) {
			String[] test = str.split(" ");
			test = test[1].split(",");
			for(String cell : test) {
				for(String aCell : test) {
					if((Integer.parseInt(cell) == (Integer.parseInt(aCell) - 1) && (Integer.parseInt(cell) % Math.sqrt(counter)) != 0) || (Integer.parseInt(cell) == (Integer.parseInt(aCell) + 1) && (Integer.parseInt(aCell) % Math.sqrt(counter)) != 0) || (Integer.parseInt(cell) == Integer.parseInt(aCell) - Math.sqrt(counter)) || (Integer.parseInt(cell) == Integer.parseInt(aCell) + Math.sqrt(counter))) {
						adj = true;
					}
				}
			}
		}
		if(Math.floor(Math.sqrt(counter)) == Math.sqrt(counter) && adj) {
			return true;
		} else
			return false;
	}
	
	public String[] getArray() {
		return puzzle;
	}

}
