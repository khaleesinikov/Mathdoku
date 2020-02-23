import java.util.ArrayList;
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
	
	public Cage(String[] cells, char operator, int target, Grid board) {
		this.board = board;
		this.op = operator;
		this.tar = target;
		for(String cell : cells) { //makes list of cells within cage
			this.cells.add(new Cell(Integer.parseInt(cell), this.board));
		}
		labelCell();
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
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

}
