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
	
	public String[] getArray() {
		return puzzle;
	}

}
