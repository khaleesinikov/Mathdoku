import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.*;
import javafx.scene.layout.*;

public class Grid extends GridPane {

	private int width; //NxN dimensions
	private ArrayList<Cage> cageList; //holds list of cages associated with board
	private HashMap<Integer, Cell> hash = new HashMap<>(); //temp hashmap to make board
	String[] examplePuzzle = {"11+ 1,7", "2÷ 2,3", "20x 4,10", "6x 5,6,12,18", "3- 8,9", "3÷ 11,17", "240x 13,14,19,20", "6x 15,16", "6x 21,27", "7+ 22,28,29", "30x 23,24", "6x 25,26", "9+ 30,36", "8+ 31,32,33", "2÷ 34,35"};
	String[] sizeTest = {"3/ 1,2", "2- 3,4", "9+ 5,9,13", "12x 6,10,11", "2- 7,8", "2/ 12,16", "6+ 14,15"};
	String[] loaded = null;
	
	public Grid(String[] puzzle) {
		makeCages(puzzle);
		setAlignment(Pos.CENTER);
		makeCells();
		/*int id = 1; //iterates and makes a grid of cells based on width of board
		for(int y=0; y<width; y++) {
		    for(int x=0; x<width; x++) {
		        Cell cell = new Cell(x, y, id);
		        add(cell, x, y);
		        id++;
		    }
		    ColumnConstraints c = new ColumnConstraints();
		    c.setPercentWidth(100.0/width);
		    getColumnConstraints().add(c);
		    RowConstraints r = new RowConstraints();
		    r.setPercentHeight(100.0/width);
		    getRowConstraints().add(r);
		}*/
		int count = 1;
		for(int i=0; i<width; i++) {
			for (int j=0; j<width; j++) {
				Cell cell = hash.get(count);
				cell.setX(i);
				cell.setY(j);
				cell.labelCo();
				add(cell, j, i);
				count++;
			}
			ColumnConstraints c = new ColumnConstraints();
		    c.setPercentWidth(100.0/width);
		    getColumnConstraints().add(c);
		    RowConstraints r = new RowConstraints();
		    r.setPercentHeight(100.0/width);
		    getRowConstraints().add(r);
		}
		setPadding(new Insets(10));
		for(Cage cage : cageList) {
			cage.borderTime();
		}
	}
	
	public void makeCages(String[] puzzle) {
		
		cageList = new ArrayList<Cage>();
		int counter = 0;
		
		for(String str : puzzle) {
			char op;
			int tar;
			String[] arr1 = str.split(" ");
			String operation = arr1[0];
			String cells = arr1[1];
			if(Character.isDigit(operation.charAt(operation.length() - 1))) {
				op = ' ';
				tar = Integer.parseInt(operation);
			} else {
				op = operation.charAt(operation.length() - 1);
				tar = Integer.parseInt(operation.substring(0, operation.length() - 1));
			}
			String[] arr2 = cells.split(",");
			counter += arr2.length;
			Cage cage = new Cage(arr2, op, tar, this);
			cageList.add(cage);
			this.width = (int) Math.sqrt(counter); //roots the cell count because that's the width
		}
	}
	
	public void makeCells() {
        for(Cage cage : cageList) {
            for(Cell cell : cage.getCells()) {
                this.hash.put(cell.getID(), cell);
            }
        }
	}
	
	public ArrayList<Cage> getCages() {
		return cageList;
	}
	
	public int acquireWidth() {
		return this.width;
	}
	
	public HashMap<Integer, Cell> getHash() {
		return hash;
	}
	
}
