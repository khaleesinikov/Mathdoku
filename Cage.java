import java.util.ArrayList;
import java.util.Collections;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Cage {
	
	Grid board;
	char op;
	int tar;
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private Game manager;
	
	public Cage(String[] cells, char operator, int target, Grid board, Game manager) {
		this.board = board;
		this.op = operator;
		this.tar = target;
		this.manager = manager;
		for(String cell : cells) { //makes list of cells within cage
			this.cells.add(new Cell(Integer.parseInt(cell), this.board, this.manager));
		}
		labelCell();
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
	}
	
	public int getSum() {
		int counter = 0;
		for(Cell cell : cells) {
			counter += cell.getInput();
		}
		return counter;
	}
	
	public int getSub() {
		ArrayList<Integer> subs = new ArrayList<>();
		for(Cell cell : cells) {
			subs.add(cell.getInput());
		}
		Collections.sort(subs);
		Collections.reverse(subs);
		int sum = subs.get(0);
		for(int i=1; i<subs.size(); i++) {
			sum = sum - subs.get(i);
		}
		return sum;
	}
	
	public int getMul() {
		int counter = 1;
		for(Cell cell : cells) {
			counter = counter*cell.getInput();
		}
		return counter;
	}
	
	public int getDiv() {
		ArrayList<Integer> subs = new ArrayList<>();
		for(Cell cell : cells) {
			subs.add(cell.getInput());
		}
		Collections.sort(subs);
		Collections.reverse(subs);
		int div = subs.get(0);
		for(int i=1; i<subs.size(); i++) {
			if(subs.get(i) == 0) {
				return 0;
			}
			div = div/subs.get(i);
		}
		return div;
	}
	
	public void labelCell() {
		int smol = 0;
		for(Cell cell : cells) {
			int compare = cell.getID();
			if(compare < smol || smol == 0) {
				smol = cell.getID();
			}
		}
		String labStr = Integer.toString(tar) + op;
		Label task = new Label(labStr);
		for(Cell cell : cells) {
			if(cell.getID() == smol) {
				StackPane.setAlignment(task, Pos.TOP_LEFT);
				cell.getChildren().add(task);
			}
		}
	}
	
	public void borderTime() {
		int w = board.acquireWidth();
		for(Cell cell : cells) {
			double top = 2;
			double bot = 2;
			double lef = 2;
			double rit = 2;
			for(Cell aCell : cells) {
				if(cell.getID() == (aCell.getID() - 1) && (cell.getID() % w) != 0) {
					rit = 0.1;
				}
				if(cell.getID() == (aCell.getID() + 1) && (aCell.getID() % w) != 0) {
					lef = 0.1;
				}
				if(cell.getID() == aCell.getID() - w) {
					bot = 0.1;
				}
				if(cell.getID() == aCell.getID() + w) {
					top = 0.1;
				}
			}
			BorderStroke b = new BorderStroke(Color.SEAGREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(top, rit, bot, lef));
			cell.setBorder(new Border(b));
		}
	}
	
	public int getTar() {
		return tar;
	}
	
	public char getOp() {
		return op;
	}

}
