import javafx.geometry.*;
import javafx.scene.layout.*;

public class Grid extends GridPane {

	int width;
	
	public Grid(int width) {
		this.width = width;
		setAlignment(Pos.CENTER);
		int id = 1;
		for(int y=0; y<width; y++) {
		    for(int x=0; x<width; x++) {
		        Cell cell = new Cell(x, y, id);
		        add(cell, x, y);
		        id++;
		    }
		    ColumnConstraints c = new ColumnConstraints();
		    c.setPercentWidth(100/width);
		    getColumnConstraints().add(c);
		    RowConstraints r = new RowConstraints();
		    r.setPercentHeight(100/width);
		    getRowConstraints().add(r);
		}
		
	}
	
	
}
