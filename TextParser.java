
public class TextParser {
	
	private String config;
	private String[] puzzle;

	public TextParser(String config) {
		this.config = config;
		puzzle = this.config.split("\\r?\\n");
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
	
	public String[] getPuzzle() {
		return puzzle;
	}
	
}
