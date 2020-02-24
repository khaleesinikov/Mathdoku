import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Cell extends StackPane {
	
	private int id;
	private int xCo;
	private int yCo;
	private int input;
	private Label l = null;
	private Grid board;
	
	public Cell(int id, Grid board) {
		this.board = board;
		this.id = id;
		BorderStroke borderStroke = new BorderStroke(Color.SEAGREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 1));
        setBorder(new Border(borderStroke));
        setMinSize(60,60);
        setBackground(new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY)));
        setAlignment(Pos.CENTER);
        
        input = 0;
        setOnMouseClicked(e -> select());
        
        /*Label egg = new Label(Integer.toString(id)); //labels cell; use for testing
        egg.setAlignment(Pos.CENTER);
        getChildren().add(egg);*/
	}
	
	public void enter(int num) {
		if(num > 0 && num <= board.acquireWidth()) {
			if(this.getChildren().contains(l)) {
				getChildren().remove(l);
			}
			this.l = new Label(Integer.toString(num));
			StackPane.setAlignment(l, Pos.CENTER);
			getChildren().add(l);
			this.input = num;
		} else if(num == 9 && this.getChildren().contains(l)) {
			getChildren().remove(l);
		}
		board.checkWin();
	}
	
	public void select() {
		Stage chooser = new Stage();
		chooser.initModality(Modality.APPLICATION_MODAL);
        GridPane g = new GridPane();
        int counter = 1;
        for(int i = 0; i<3; i++) {
        	for(int j = 0; j<3; j++) {
        		Button btn;
        		if(i==2 && j==2) {
        			btn = new Button("Clear");
        		} else {
        			btn = new Button(Integer.toString(counter));
        			if(counter>board.acquireWidth()) {
        				btn.setDisable(true);
        			}
        		}
        		int value = counter;
        		btn.setOnAction(e -> {
    				enter(value);
    				chooser.close();
    			});
        		btn.setPrefSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    			g.add(btn, j, i);
    			counter++;
        	}
        	ColumnConstraints c = new ColumnConstraints();
		    c.setPercentWidth(100.0/3.0);
		    g.getColumnConstraints().add(c);
		    RowConstraints r = new RowConstraints();
		    r.setPercentHeight(100.0/4.0);
		    g.getRowConstraints().add(r);
		    g.setHgap(10); //horizontal gap in pixels => that's what you are asking for
		    g.setVgap(10); //vertical gap in pixels
		    g.setPadding(new Insets(10));
        }
        TextField tf = new TextField();
        tf.setPrefSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        tf.setPromptText("Enter desired value");
        if(input != 0) {
        	tf.setText(Integer.toString(input));
        }
        g.add(tf, 0, 3, 2, 1);
        Button ent = new Button("Enter");
        ent.setPrefSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        ent.setOnAction(e -> {
        	String text = tf.getText();
        	if(isNumber(text)) {
        		int n = Integer.parseInt(text);
        		if(n>0 && n<=board.acquireWidth()) {
        			enter(n);
        			chooser.close();
        		} else {
        			tf.clear();
        		}
        	} else if(tf.getText().trim().isEmpty()) {
        		enter(9);
        		chooser.close();
        	} else {
        		tf.clear();
        	}
        });
        g.add(ent, 2, 3);
        RowConstraints r = new RowConstraints();
	    r.setPercentHeight(100.0/4.0);
	    g.getRowConstraints().add(r);
        chooser.setTitle("Entering value at (" + Integer.toString(xCo) + "," + Integer.toString(yCo) + ")");
        chooser.setScene(new Scene(g, 210, 275));
        chooser.show();
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
	
	public int getInput() {
		return input;
	}
	
	@SuppressWarnings("unused")
	public boolean isNumber(String s) {
		try {
			int i = Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public void labelCo() {
		Label omelette = new Label(Integer.toString(xCo) + "," + Integer.toString(yCo));
		StackPane.setAlignment(omelette, Pos.BOTTOM_RIGHT);
		getChildren().add(omelette);
	}
	
}
