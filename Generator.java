import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Generator {
	
	private int width;
	private Random ran;
	private ArrayList<ArrayList<Integer>> boardMatrix; //each array is a row of the board
	private ArrayList<ArrayList<Integer>> cages; //each array is the cell id of a cage
	private HashMap<Integer, Integer> boardMap; //key is cell id, value is number generated
	private ArrayList<String> operationList; //key is string of cell ids, value is target and operator

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
				boardMap.put(row*width+col + 1, boardMatrix.get(row).get(col));
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
		
		operationList = new ArrayList<>();
		
		for(ArrayList<Integer> cage : cages) {
			StringBuilder strBld = new StringBuilder();
			for(int i = 0; i<cage.size(); i++) {
				strBld.append(cage.get(i));
				strBld.append(",");
			}
			strBld.setLength(strBld.length()-1);
			
			ArrayList<Integer> numbers = new ArrayList<>();
			for(Integer cellID : cage) {
				numbers.add(boardMap.get(cellID));
			}
			
			if(cage.size() == 1) {
				strBld.insert(0, " ");
				strBld.insert(0, numbers.get(0));
				String finished = strBld.toString();
				operationList.add(finished);
				continue;
			}
			
			Collections.sort(numbers);
			Collections.reverse(numbers);
			
			String operator;
			Integer div = numbers.get(0);
			boolean divFlag = true;
			Integer sub = numbers.get(0);
			boolean subFlag = true;
			Integer add = numbers.get(0);
			Integer mul = numbers.get(0);
			for(int i = 1; i<numbers.size(); i++) {
				if(!(div % numbers.get(i) == 0))
					divFlag = false;
				else if(divFlag)
					div = div/numbers.get(i);
				sub = sub-numbers.get(i);
				add = add+numbers.get(i);
				mul = mul*numbers.get(i);
			}
			if(sub<1)
				subFlag = false;
			
			Integer target;
			if(divFlag && ran.nextDouble()>0.4) {
				operator = "÷";
				target = div;
			} else if(subFlag && ran.nextDouble()>0.6) {
				operator = "-";
				target = sub;
			} else if(ran.nextDouble()>0.5) {
				operator = "+";
				target = add;
			} else {
				operator = "x";
				target = mul;
			}
			
			strBld.insert(0, " ");
			strBld.insert(0, operator);
			strBld.insert(0, target);
			
			String finished = strBld.toString();
			operationList.add(finished);
			
		}
		
		/*for(String s : operationList) {
			System.out.println(s);
		}*/
		
	}
	
	public String sendToParse() {
		StringBuilder strBld = new StringBuilder();
		for(String s : operationList) {
			strBld.append(s);
			strBld.append("\n");
		}
		String readyToParse = strBld.toString();
		return readyToParse;
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
