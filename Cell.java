import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Cell extends StackPane {
	
	private int id;
	private int xCo;
	private int yCo;
	
	public Cell(int id) {
		this.id = id;
		BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 1));
        setBorder(new Border(borderStroke));
        setMinSize(60,60);
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setAlignment(Pos.CENTER);
        
        Label egg = new Label(Integer.toString(id)); //labels cell; use for testing
        egg.setAlignment(Pos.CENTER);
        getChildren().add(egg);
	}
	
	public int getID() {
		return id;
	}
	
	public int getX() {
		return xCo;
	}
	
	public void setX(int x) {
		this.xCo = x;
	}
	
	public int getY() {
		return yCo;
	}
	
	public void setY(int y) {
		this.yCo = y;
	}
	
	public void labelCo() {
		Label omelette = new Label(Integer.toString(xCo) + "," + Integer.toString(yCo));
		StackPane.setAlignment(omelette, Pos.BOTTOM_RIGHT);
		getChildren().add(omelette);
	}
	
}
