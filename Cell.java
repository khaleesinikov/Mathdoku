import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Cell extends StackPane {
	
	private int id;
	private int xCo;
	private int yCo;
	private int input;
	private int answer;
	private Label l = null;
	private Grid board;
	private Game manager;
	
	public Cell(int id, Grid board, Game manager) {
		this.board = board;
		this.id = id;
		this.manager = manager;
		BorderStroke borderStroke = new BorderStroke(Color.SEAGREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 1, 1, 1));
        setBorder(new Border(borderStroke));
        setMinSize(60,60);
        setBackground(new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY)));
        setAlignment(Pos.CENTER);
        
        input = 0;
        setOnMouseClicked(e -> select());
        
        /*Label egg = new Label(Integer.toString(id)); //labels cell; use for testing
        egg.setAlignment(Pos.CENTER);
        getChildren().add(egg);
        System.out.println(egg.getFont());*/
	}
	
	public void enter(int num) {
		Font font = new Font("System Regular", manager.fontSize);
		if(num > 0 && num <= board.acquireWidth()) {
			if(this.getChildren().contains(l)) {
				getChildren().remove(l);
			}
			this.l = new Label(Integer.toString(num));
			StackPane.setAlignment(l, Pos.CENTER);
			l.setFont(font);
			getChildren().add(l);
			HistObj h = new HistObj(id, input);
			board.undo.push(h);
			manager.m21.setDisable(false);
			board.redo.clear();
			manager.m22.setDisable(true);
			manager.m23.setDisable(false);
			this.input = num;
		} else if(num == 9 && this.getChildren().contains(l)) {
			getChildren().remove(l);
			this.input = 0;
		}
		if(board.getMist()) {
			for(Cell cell : board.cellArray) {
				cell.setBackground(new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY)));
			}
			board.highRows();
			board.highCols();
			board.highCages();
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
    				if(board.getMist()) {
    					board.highRows();
    				}
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
		    g.setHgap(10);
		    g.setVgap(10);
		    g.setPadding(new Insets(10));
        }
        TextField tf = new TextField();
        tf.setPrefSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        tf.setPromptText("Enter value");
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
        tf.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {
                if (ke.getCode().equals(KeyCode.ENTER))
                {
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
                }
            }
        });
        g.add(ent, 2, 3);
        RowConstraints r = new RowConstraints();
	    r.setPercentHeight(100.0/4.0);
	    g.getRowConstraints().add(r);
        chooser.setTitle("Entering value at (" + Integer.toString(xCo) + "," + Integer.toString(yCo) + ")");
        chooser.getIcons().add(new Image("file:eggicon.png"));
        chooser.setScene(new Scene(g, 210, 275));
        chooser.show();
	}
	
	public void changeFont() {
		Font font = new Font("System Regular", manager.fontSize);
		if(this.getChildren().contains(l)) {
			getChildren().remove(l);
		}
		if(input != 0) {
			this.l = new Label(Integer.toString(input));
			l.setFont(font);
			StackPane.setAlignment(l, Pos.CENTER);
			this.getChildren().add(l);
		} else if(board.isMouseTransparent()) {
			this.l = new Label(Integer.toString(answer));
			l.setFont(font);
			StackPane.setAlignment(l, Pos.CENTER);
			this.getChildren().add(l);
		}
	}
	
	public void showHint() {
		manager.m42.setDisable(true);
		this.l = new Label(Integer.toString(answer));
		l.setTextFill(Color.DARKRED);
		l.setFont(Font.font("System Regular", FontWeight.BOLD, manager.fontSize));
		StackPane.setAlignment(l, Pos.CENTER);
		this.getChildren().add(l);
		PauseTransition visiblePause = new PauseTransition(
		        Duration.seconds(3)
		);
		visiblePause.setOnFinished(
		        event -> {
		        	this.getChildren().remove(l);
		        	manager.m42.setDisable(false);
		        }
		);
		visiblePause.play();
	}
	
	public void clear() {
		if(this.getChildren().contains(l)) {
			this.getChildren().remove(l);
		}
		input = 0;
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
	
	public void setInput(int i) {
		this.input = i;
	}
	
	public String getLabel() {
		return l.getText();
	}
	
	public void setLabel(int i) {
		Font font = new Font("System Regular", manager.fontSize);
		String num = Integer.toString(i);
		if(this.getChildren().contains(l)) {
			getChildren().remove(l);
		}
		this.l = new Label(num);
		l.setFont(font);
		StackPane.setAlignment(l, Pos.CENTER);
		getChildren().add(l);
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

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}
	
}
