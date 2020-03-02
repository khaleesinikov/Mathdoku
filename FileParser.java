import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

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
			br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
			br2 = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			System.err.println("File not found, somehow");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("I'm impressed you managed to trigger this");
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
		for(String str : puzzle) {
			String[] test = str.split(",");
			counter += test.length;
		}
		if(Math.floor(Math.sqrt(counter)) == Math.sqrt(counter) && checkIfLine() && checkIfAdj() && checkIfNumber() && checkIfOp()) {
			return true;
		} else
			return false;
	}
	
	public boolean checkIfLine() {
		for(String str : puzzle) {
			String[] split = str.split(" ");
			if(split.length != 2) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkIfAdj() {
		double counter = 0;
		for(String str : puzzle) {
			String[] count = str.split(",");
			counter += count.length;
		}
		String[] test = null;
		for(String str : puzzle) {
			int goodCount = 0;
			test = str.split(" ");
			if(test[0].length() == 1) {
				continue;
			}
			test = test[1].split(",");
			for(String cell : test) {
				for(String aCell : test) {
					//System.out.println(cell + " " + aCell);
					if(((Integer.parseInt(cell) == (Integer.parseInt(aCell) - 1)) && (Integer.parseInt(cell) % Math.sqrt(counter)) != 0) || ((Integer.parseInt(cell) == (Integer.parseInt(aCell) + 1)) && (Integer.parseInt(aCell) % Math.sqrt(counter)) != 0) || (Integer.parseInt(cell) == Integer.parseInt(aCell) - Math.sqrt(counter)) || (Integer.parseInt(cell) == Integer.parseInt(aCell) + Math.sqrt(counter))) {
						//System.out.println("If statement worked");
						goodCount++;
						break;
					}
				}
			}
			if(!(goodCount == test.length)) {
				//System.out.println("Failed");
                return false;
			}
		}
		//System.out.println("Passed");
		return true;
	}
	
	public boolean checkIfNumber() {
		String[] test = null;
		for(String str : puzzle) {
			test = str.split(" ");
			test = test[1].split(",");
			try {
				for(String s : test) {
					@SuppressWarnings("unused")
					int n = Integer.parseInt(s);
				}
			} catch(NumberFormatException nfe) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkIfOp() {
		ArrayList<String> operators = new ArrayList<>(Arrays.asList("+", "x", "*", "/", "÷", "\u00F7", "-", "1", "2", "3", "4", "5", "6", "7", "8"));
		String[] test = null;
		for(String str : puzzle) {
			test = str.split(" ");
			String toCheck = test[0];
			toCheck = String.valueOf(toCheck.charAt(toCheck.length() - 1));
			//System.out.println(toCheck);
			if(!operators.contains(toCheck))
				return false;
		}
		return true;
	}
	
	public String[] getArray() {
		return puzzle;
	}
	
}
