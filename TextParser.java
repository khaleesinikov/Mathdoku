import java.util.ArrayList;
import java.util.Arrays;

public class TextParser {
	
	private String config;
	private String[] puzzle;

	public TextParser(String config) {
		this.config = config;
		puzzle = this.config.split("\\r?\\n");
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
					if((Integer.parseInt(cell) == (Integer.parseInt(aCell) - 1) && (Integer.parseInt(cell) % Math.sqrt(counter)) != 0) || (Integer.parseInt(cell) == (Integer.parseInt(aCell) + 1) && (Integer.parseInt(aCell) % Math.sqrt(counter)) != 0) || (Integer.parseInt(cell) == Integer.parseInt(aCell) - Math.sqrt(counter)) || (Integer.parseInt(cell) == Integer.parseInt(aCell) + Math.sqrt(counter))) {
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
				//System.out.println("Failed");
				return false;
			}
		}
		return true;
	}
	
	public boolean checkIfOp() {
		ArrayList<String> operators = new ArrayList<>(Arrays.asList("+", "x", "*", "/", "÷", "-", " ", "1", "2", "3", "4", "5", "6", "7", "8"));
		String[] test = null;
		for(String str : puzzle) {
			test = str.split(" ");
			String toCheck = test[0];
			toCheck = String.valueOf(toCheck.charAt(toCheck.length() - 1));
			if(!operators.contains(toCheck))
				//System.out.println("Failed");
				return false;
		}
		return true;
	}
	
	public String[] getPuzzle() {
		return puzzle;
	}
	
}
