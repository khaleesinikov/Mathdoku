import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Generator {
	
	private int width;
	private Random ran;
	private ArrayList<ArrayList<Integer>> boardMatrix;
	private ArrayList<ArrayList<Integer>> cages;
	private HashMap<Integer, Integer> boardMap;

	public Generator(int size) {
		this.width = size;
		ran = new Random();
		boardMatrix = new ArrayList<>();
		cages = new ArrayList<>();
		boardMap = new HashMap<>();
		
		for(int i = 0; i<width; i++) {
			boardMatrix.add(new ArrayList<Integer>());
			for(int j = 0; j<width; j++) {
				boardMatrix.get(i).add((i + j) % size + 1);
			}
		}
		
		Collections.shuffle(boardMatrix);
		
		int temp;
		for(int i = 0; i<size; i++) {
			for(int j = 0; j < i; j++) {
				temp = boardMatrix.get(i).get(j);
				boardMatrix.get(i).set(j,  boardMatrix.get(j).get(i));
				boardMatrix.get(j).set(i, temp);
			}
		}
		
		Collections.shuffle(boardMatrix);
		
		for(int row = 0; row<width; row++) {
			for(int col = 0; col<width; col++) {
				boardMap.put(row*width+col, boardMatrix.get(row).get(col));
			}
		}
		
		float cellCountChance;
		int cellCount;
		ArrayList<Integer> untakenCells = new ArrayList<>();
		
		
		for(Integer i = 1; i<=width*width; i++) {
			untakenCells.add(i);
		}
		
		while(!untakenCells.isEmpty()) {
			ArrayList<Integer> tempCage = new ArrayList<>();
			cellCountChance = ran.nextFloat();
			if(cellCountChance < 0.05)
				cellCount = 1;
			else if(cellCountChance < 0.55)
				cellCount = 2;
			else if(cellCountChance < 0.9)
				cellCount = 3;
			else
				cellCount = 4;
			Integer currentCell = untakenCells.get(ran.nextInt(untakenCells.size()));
			tempCage.add(currentCell);
			ArrayList<Integer> potCells = new ArrayList<>();
			if(cellCount == 1) {
				cages.add(tempCage);
				untakenCells.remove(currentCell);
				continue;
			}
			
			for(Integer i : untakenCells) {
				if((currentCell == i + 1 && (i % width) != 0) || (currentCell == i - 1 && (currentCell % width) != 0) || (currentCell == i-width) || (currentCell == i+width)) {
					potCells.add(i);
				}
			}
			
			/*for(int i = 0; i<potCells.size(); i++)
				System.out.println(potCells.get(i));
			System.out.println("...");*/
			
			Collections.shuffle(potCells);
			untakenCells.remove(currentCell);
			for(int i = cellCount; i>1; i--) {
				if(potCells.isEmpty())
					break;
				Integer newCell = potCells.get(0);
				tempCage.add(newCell);
				untakenCells.remove(newCell);
				cellCount--;
				if(cellCount>1) {
					potCells.remove(0);
					for(Integer in : untakenCells) {
						if((newCell == in + 1 && (in % width) != 0) || (newCell == in - 1 && (newCell % width) != 0) || (newCell == in-width) || (newCell == in+width)) {
							potCells.add(in);
						}
					}
					
					/*for(int ing = 0; ing<potCells.size(); ing++)
						System.out.println(potCells.get(ing));
					System.out.println(".....");*/
					
					Collections.shuffle(potCells);
				}
			}
			cages.add(tempCage);
			potCells.clear();
		}
		
		//To-do list:
		//Assign operators to cages
		//Somehow map operators to cages so you can recall them
		//Calculate end goals for cages while I'm at it
		//Modify text parser so I can be lazy and load it that way maybe
		//Either way load the grid somehow
		//Check for multi solution grids
		
	}
	
	public void printBoard() {
		System.out.println("Numbers:");
		for(int i = 0; i<width; i++) {
			for(int j = 0; j<width; j++) {
				System.out.print(boardMatrix.get(i).get(j));
			}
			System.out.print("\n");
		}
		System.out.println();
	}
	
	public void printCages() {
		System.out.println("Cages:");
		for(ArrayList<Integer> al : cages) {
			for(Integer i : al) {
				System.out.print(i);
				System.out.print(", ");
			}
			System.out.print("\n");
		}
		System.out.println();
	}
	
}
