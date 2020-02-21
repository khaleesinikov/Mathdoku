import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Cell extends StackPane {
	
	private int id;
	private int xCo;
	private int yCo;
	
	public Cell(int x, int y, int id) {
		this.id = id;
		this.xCo = x;
		this.yCo = y;
		BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 1));
        setBorder(new Border(borderStroke));
        setMinSize(60,60);
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setAlignment(Pos.CENTER);
	}
	
}
