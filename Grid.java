import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Grid extends GridPane {

	private int width; //NxN dimensions
	private ArrayList<Cage> cageList; //holds list of cages associated with board
	private HashMap<Integer, Cell> hash = new HashMap<>(); //temp hashmap to make board
	String[] examplePuzzle = {"11+ 1,7", "2÷ 2,3", "20x 4,10", "6x 5,6,12,18", "3- 8,9", "3÷ 11,17", "240x 13,14,19,20", "6x 15,16", "6x 21,27", "7+ 22,28,29", "30x 23,24", "6x 25,26", "9+ 30,36", "8+ 31,32,33", "2÷ 34,35"};
	String[] sizeTest = {"3/ 1,2", "2- 3,4", "9+ 5,9,13", "12x 6,10,11", "2- 7,8", "2/ 12,16", "6+ 14,15"};
	String[] loaded = null;
	ArrayList<Cell> cellArray = new ArrayList<>();
	boolean showMist;
	Stack<HistObj> undo = new Stack<>();
	Stack<HistObj> redo = new Stack<>();
	private Game manager;
	
	public Grid(String[] puzzle, Game manager) {
		this.manager = manager;
		makeCages(puzzle);
		setAlignment(Pos.CENTER);
		makeCells();
		showMist = false;
		/*int id = 1; //iterates and makes a grid of cells based on width of board
		for(int y=0; y<width; y++) {
		    for(int x=0; x<width; x++) {
		        Cell cell = new Cell(x, y, id);
		        add(cell, x, y);
		        id++;
		    }
		    ColumnConstraints c = new ColumnConstraints();
		    c.setPercentWidth(100.0/width);
		    getColumnConstraints().add(c);
		    RowConstraints r = new RowConstraints();
		    r.setPercentHeight(100.0/width);
		    getRowConstraints().add(r);
		}*/
		int count = 1;
		for(int i=0; i<width; i++) {
			for (int j=0; j<width; j++) {
				Cell cell = hash.get(count);
				cell.setX(i);
				cell.setY(j);
				//cell.labelCo();
				add(cell, j, i);
				cellArray.add(cell);
				count++;
			}
			ColumnConstraints c = new ColumnConstraints();
		    c.setPercentWidth(100.0/width);
		    getColumnConstraints().add(c);
		    RowConstraints r = new RowConstraints();
		    r.setPercentHeight(100.0/width);
		    getRowConstraints().add(r);
		}
		setPadding(new Insets(10));
		for(Cage cage : cageList) {
			cage.borderTime();
		}
		
	}
	
	public void makeCages(String[] puzzle) {
		
		cageList = new ArrayList<Cage>();
		int counter = 0;
		
		for(String str : puzzle) {
			char op;
			int tar;
			String[] arr1 = str.split(" ");
			String operation = arr1[0];
			String cells = arr1[1];
			if(Character.isDigit(operation.charAt(operation.length() - 1))) {
				op = ' ';
				tar = Integer.parseInt(operation);
			} else {
				op = operation.charAt(operation.length() - 1);
				tar = Integer.parseInt(operation.substring(0, operation.length() - 1));
			}
			String[] arr2 = cells.split(",");
			counter += arr2.length;
			Cage cage = new Cage(arr2, op, tar, this, manager);
			cageList.add(cage);
			this.width = (int) Math.sqrt(counter); //roots the cell count because that's the width
		}
	}
	
	public void makeCells() {
        for(Cage cage : cageList) {
            for(Cell cell : cage.getCells()) {
                this.hash.put(cell.getID(), cell);
            }
        }
	}
	
	public void clear() {
		for(Cell cell : cellArray) {
			cell.clear();
		}
		undo.clear();
		redo.clear();
	}
	
	public boolean checkRows() {
		int target = ((width*(width+1))/2);
		for(int i=0; i<width; i++) {
			int calc = 0;
			for(Cell cell : cellArray) {
				if(cell.getX() == i) {
					calc += cell.getInput();
					//System.out.println("Calc: " + calc + " ID: " + cell.getID());
				}
			}
			if(calc != target) {
				//System.out.println("Rows bad");
				return false;
			}
		}
		return true;
	}
	
	public boolean checkCols() {
		int target = ((width*(width+1))/2);
		for(int i=0; i<width; i++) {
			int calc = 0;
			for(Cell cell : cellArray) {
				if(cell.getY() == i) {
					calc += cell.getInput();
					//System.out.println(calc);
				}
			}
			if(calc != target) {
				//System.out.println("Columns bad");
				return false;
			}
		}
		return true;
	}
	
	public boolean checkCages() {
		for(Cage cage : cageList) {
			if(cage.getOp() == ' ') {
				//System.out.println(cage.getSum());
				if(!(cage.getTar() == cage.getSum()))
					break;
			} else if(cage.getOp() == 'x' || cage.getOp() == '*') {
				//System.out.println(cage.getTar() + "Mul: " + cage.getMul());
				if(!(cage.getTar() == cage.getMul()))
					break;
			} else if(cage.getOp() == '/' || cage.getOp() == '÷') {
				//System.out.println(cage.getTar() + "Div: " + cage.getDiv());
				if(!(cage.getTar() == cage.getDiv()))
					break;
			} else if(cage.getOp() == '-') {
				//System.out.println(cage.getTar() + "Sub: " + cage.getSub());
				if(!(cage.getTar() == cage.getSub()))
					break;
			} else if(cage.getOp() == '+') {
				//System.out.println(cage.getTar() + "Sum: " + cage.getSum());
				if(!(cage.getTar() == cage.getSum()))
					break;
			}
			//System.out.println("Cages good");
			return true;
		}
		//System.out.println("Cages bad");
		return false;
	}
	
	public boolean checkWin() {
		if(checkRows() && checkCols() && checkCages()) {
			System.out.println("Win condition met");
			setMouseTransparent(true);
			colourWin();
			animateWin();
			cookEgg();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setHeaderText("You completed the puzzle correctly");
			alert.setContentText("It's gamer time");
			Image image = new Image(this.getClass().getResourceAsStream("/eggtime.png"),80,80,false,false);
			ImageView imageView = new ImageView(image);
			alert.setGraphic(imageView);
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
			alert.showAndWait();
			return true;
		} else {
			//System.out.println("Win condition not met");
			return false;
		}
	}
	
	private void colourWin() {
		for(Cage cage : cageList) {
			cage.winBorders();
		}
		for(Cell c : cellArray) {
			c.setBackground(new Background(new BackgroundFill(Color.MEDIUMVIOLETRED, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}
	
	private void cookEgg() {
		Random r = new Random();
		Pane p = new StackPane();
		this.add(p, 0, 0, width, width);
		for(int i=0; i<60; i++) {
			Image image = new Image(this.getClass().getResourceAsStream("/eggicon.png"), 40, 40, false, false);
			ImageView iv = new ImageView(image);
			iv.setTranslateX(r.nextDouble()*this.getWidth()-this.getWidth()*0.5);
			iv.setTranslateY(r.nextDouble()*this.getHeight()-this.getHeight()*0.5);
			p.getChildren().add(iv);
		}
		for(int i=0; i<30; i++) {
			Image image = new Image(this.getClass().getResourceAsStream("/eggtime.png"), 40, 40, false, false);
			ImageView iv = new ImageView(image);
			iv.setTranslateX(r.nextDouble()*this.getWidth()-this.getWidth()*0.5);
			iv.setTranslateY(r.nextDouble()*this.getHeight()-this.getHeight()*0.5);
			p.getChildren().add(iv);
		}
		Timeline tl = new Timeline();
		for(Node egg : p.getChildren()) {
			tl.getKeyFrames().addAll(
			        new KeyFrame(Duration.ZERO, // set start position at 0
			            new KeyValue(egg.translateXProperty(), r.nextDouble()*this.getWidth()-this.getWidth()*0.5),
			            new KeyValue(egg.translateYProperty(), r.nextDouble()*this.getHeight()-this.getHeight()*0.5),
			            new KeyValue(egg.rotateProperty(), 0)
			        ),
			        new KeyFrame(new Duration(20000), // set end position at 40s
			            new KeyValue(egg.translateXProperty(), r.nextDouble()*this.getWidth()-this.getWidth()*0.5),
			            new KeyValue(egg.translateYProperty(), r.nextDouble()*this.getHeight()-this.getHeight()*0.5),
			            new KeyValue(egg.rotateProperty(), (r.nextInt(360) + 720))
			        )
			    );
		}
		tl.play();
	}
	
	private void animateWin() {
		Random r = new Random();
		Pane p = new StackPane();
		this.add(p, 0, 0, width, width);
		for(int i = 0; i<200; i++) {
			Circle cir = new Circle(r.nextDouble()*40, randomColour());
			cir.setTranslateX(r.nextDouble()*this.getWidth()-this.getWidth()*0.5);
			cir.setTranslateY(r.nextDouble()*this.getHeight()-this.getHeight()*0.5);
			cir.setEffect(new BoxBlur(10, 10, 3));
			p.getChildren().add(cir);
		}
		Timeline tl = new Timeline();
		for(Node circle : p.getChildren()) {
			tl.getKeyFrames().addAll(
			        new KeyFrame(Duration.ZERO, // set start position at 0
			            new KeyValue(circle.translateXProperty(), r.nextDouble()*this.getWidth()-this.getWidth()*0.5),
			            new KeyValue(circle.translateYProperty(), r.nextDouble()*this.getHeight()-this.getHeight()*0.5)
			        ),
			        new KeyFrame(new Duration(20000), // set end position at 40s
			            new KeyValue(circle.translateXProperty(), r.nextDouble()*this.getWidth()-this.getWidth()*0.5),
			            new KeyValue(circle.translateYProperty(), r.nextDouble()*this.getHeight()-this.getHeight()*0.5)
			        )
			    );
		}
		tl.play();
	}
	
	private Color randomColour() {
		Random r = new Random();
		double red = r.nextDouble();
		double blue = r.nextDouble();
		double green = r.nextDouble();
		Color randomColor = new Color(red, green, blue, r.nextDouble());
		randomColor.brighter();
		randomColor.brighter();
		randomColor.brighter();
		randomColor.brighter();
		return randomColor;
	}
	
	public void changeFont() {
		for(Cell c : cellArray) {
			c.changeFont();
		}
		for(Cage cage : cageList) {
			cage.labelCell();
		}
	}
	
	public void highRows() {
		//Background good = new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY));
		Background bad = new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY));
		for(int i = 0; i<width; i++) {
			ArrayList<Integer> checker = new ArrayList<>();
			for(Cell cell : cellArray) {
				if(cell.getX() == i) {
					if(checker.contains(cell.getInput())) {
						for(Cell bCell : cellArray) {
							if(bCell.getX() == i) {
								bCell.setBackground(bad);
							}
						}
						//System.out.println("Egg1");
						continue;
					} else if(cell.getInput() != 0) {
						checker.add(cell.getInput());
						//System.out.println("Egg2");
					}
				}
			}
		}
	}
	
	public void highCols() {
		Background bad = new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY));
		for(int i = 0; i<width; i++) {
			ArrayList<Integer> checker = new ArrayList<>();
			for(Cell cell : cellArray) {
				if(cell.getY() == i) {
					if(checker.contains(cell.getInput())) {
						for(Cell bCell : cellArray) {
							if(bCell.getY() == i) {
								bCell.setBackground(bad);
							}
						}
						//System.out.println("Egg1");
						continue;
					} else if(cell.getInput() != 0) {
						checker.add(cell.getInput());
						//System.out.println("Egg2");
					}
				}
			}
		}
	}
	
	public void highCages() {
		Background bad = new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY));
		for(Cage cage : cageList) {
			ArrayList<Integer> checker = new ArrayList<>();
			for(Cell c : cage.getCells()) {
				if(c.getInput() != 0) {
					checker.add(c.getInput());
				}
			}
			if(cage.getOp() == ' ') {
				//System.out.println(cage.getSum());
				if(!(cage.getTar() == cage.getSum()) && checker.size() == cage.getCells().size())
					for(Cell cell : cage.getCells()) {
						cell.setBackground(bad);
					}
			} else if(cage.getOp() == 'x' || cage.getOp() == '*') {
				//System.out.println(cage.getTar() + "Mul: " + cage.getMul());
				if(!(cage.getTar() == cage.getMul()) && checker.size() == cage.getCells().size())
					for(Cell cell : cage.getCells()) {
						cell.setBackground(bad);
					}
			} else if(cage.getOp() == '/' || cage.getOp() == '÷') {
				//System.out.println(cage.getTar() + "Div: " + cage.getDiv());
				if(!(cage.getTar() == cage.getDiv()) && checker.size() == cage.getCells().size())
					for(Cell cell : cage.getCells()) {
						cell.setBackground(bad);
					}
			} else if(cage.getOp() == '-') {
				//System.out.println(cage.getTar() + "Sub: " + cage.getSub());
				if(!(cage.getTar() == cage.getSub()) && checker.size() == cage.getCells().size())
					for(Cell cell : cage.getCells()) {
						cell.setBackground(bad);
					}
			} else if(cage.getOp() == '+') {
				//System.out.println(cage.getTar() + "Sum: " + cage.getSum());
				if(!(cage.getTar() == cage.getSum()) && checker.size() == cage.getCells().size())
					for(Cell cell : cage.getCells()) {
						cell.setBackground(bad);
					}
			}
		}
	}
	
	public void undo() {
		HistObj h = undo.pop();
		Cell c = hash.get(h.id);
		redo.push(new HistObj(c.getID(), c.getInput()));
		c.setInput(h.value);
		if(h.value == 0) {
			c.clear();
		} else {
			c.setLabel(h.value);
		}
	}
	
	public void redo() {
		HistObj h = redo.pop();
		Cell c = hash.get(h.id);
		undo.push(new HistObj(c.getID(), c.getInput()));
		c.setInput(h.value);
		if(h.value == 0) {
			c.clear();
		} else {
			c.setLabel(h.value);
		}
	}
	
	public void hint() {
		Random ran = new Random();
		ArrayList<Cell> potentialCells = new ArrayList<>();
		for(Cell c : cellArray) {
			if(c.getInput() == 0) {
				potentialCells.add(c);
			}
		}
		int cellID = ran.nextInt(potentialCells.size());
		Cell hintCell = hash.get(cellID);
		hintCell.showHint();
	}
	
	public void autoSolve() {
		Background auto = new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY));
		for(Cell c : cellArray) {
			c.clear();
			c.setLabel(c.getAnswer());
			c.setBackground(auto);
		}
		for(Cage cage : cageList) {
			cage.autoBorders();
		}
		setMouseTransparent(true);
		manager.m23.setDisable(true);
		manager.m41.setDisable(true);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Auto-solved");
		alert.setHeaderText("This puzzle has been automatically solved for you");
		alert.setContentText("I'm disappointed :(");
		Image image = new Image(this.getClass().getResourceAsStream("/crackedeggicon.png"),80,80,false,false);
		ImageView imageView = new ImageView(image);
		alert.setGraphic(imageView);
		((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
		alert.showAndWait();
	}
	
	public ArrayList<Cage> getCages() {
		return cageList;
	}
	
	public int acquireWidth() {
		return this.width;
	}
	
	public HashMap<Integer, Cell> getHash() {
		return hash;
	}
	
	public void setMist(boolean b) {
		showMist = b;
	}
	
	public boolean getMist() {
		return showMist;
	}
	
}
