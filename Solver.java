import java.util.ArrayList;

public class Solver {
	
	Grid board;
	ArrayList<Integer> values;
	int width;

	public Solver(Grid board) {
		this.board = board;
		this.width = board.acquireWidth();
		
		values = new ArrayList<>();
		for(Integer i = 1; i<=width; i++)
			values.add(i);
	}
	
	public int solve(int index, int count) {
		//System.out.println("Index: " + index);
		
		if(index > board.cellArray.size()) {
			return 1+count;
		}
		
		for(Integer v : values) {
			//System.out.println("Value: " + v);
			board.getHash().get(index).setInput(v);
			if(checkCols(board.getHash().get(index).getY()) && checkRows(board.getHash().get(index).getX()) && checkCages(index) && count < 2) {
				count = solve(index+1, count);
			}
		}
	//System.out.println("bad");
	board.getHash().get(index).setInput(0);
	return count;
	}
	
	//old solve method, currently unused
	public boolean solve(int index) {
		if(index > board.cellArray.size())
			return true;

		for(Integer v : values) {
			//System.out.println("Value: " + v);
			board.getHash().get(index).setInput(v);
			if(checkCols(board.getHash().get(index).getY()) && checkRows(board.getHash().get(index).getX()) && checkCages(index)) {
				if(solve(index+1))
					return true;
			}
		}
	board.getHash().get(index).setInput(0);
	return false;
	}
	
	public boolean checkCols(int column) {
		ArrayList<Integer> checker = new ArrayList<>();
		for(Cell cell : board.cellArray) {
			if(cell.getY() == column) {
				if(checker.contains(cell.getInput())) {
					//System.out.println("bad column");
					return false;
				} else if(cell.getInput() != 0) {
					checker.add(cell.getInput());
				}
			}
		}
		return true;
	}
	
	public boolean checkRows(int row) {
		ArrayList<Integer> checker = new ArrayList<>();
		for(Cell cell : board.cellArray) {
			if(cell.getX() == row) {
				if(checker.contains(cell.getInput())) {
					//System.out.println("bad row");
					return false;
				} else if(cell.getInput() != 0) {
					checker.add(cell.getInput());
				}
			}
		}
		return true;
	}
	
	public boolean checkCages(int index) {
		Cell target = board.getHash().get(index);
		for(Cage cage : board.getCages()) {
			if(cage.getCells().contains(target)) {
				ArrayList<Integer> checker = new ArrayList<>();
				for(Cell c : cage.getCells()) {
					if(c.getInput() != 0) {
						checker.add(c.getInput());
					}
				}
				if(cage.getOp() == ' ') {
					//System.out.println(cage.getSum());
					if(!(cage.getTar() == cage.getSum()) && checker.size() == cage.getCells().size())
						return false;
				} else if(cage.getOp() == 'x' || cage.getOp() == '*') {
					//System.out.println(cage.getTar() + "Mul: " + cage.getMul());
					if(!(cage.getTar() == cage.getMul()) && checker.size() == cage.getCells().size())
						return false;
				} else if(cage.getOp() == '/' || cage.getOp() == '÷') {
					//System.out.println(cage.getTar() + "Div: " + cage.getDiv());
					if(!(cage.getTar() == cage.getDiv()) && checker.size() == cage.getCells().size())
						return false;
				} else if(cage.getOp() == '-') {
					//System.out.println(cage.getTar() + "Sub: " + cage.getSub());
					if(!(cage.getTar() == cage.getSub()) && checker.size() == cage.getCells().size())
						return false;
				} else if(cage.getOp() == '+') {
					//System.out.println(cage.getTar() + "Sum: " + cage.getSum());
					if(!(cage.getTar() == cage.getSum()) && checker.size() == cage.getCells().size())
						return false;
				}
			}

		}
		return true;
	}
	
	public void clean() {
		for(Cell c : board.cellArray) {
			c.setAnswer(c.getInput());
		}
		board.clear();
	}
	
}
