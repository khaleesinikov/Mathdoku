import java.io.File;
import java.util.Date;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Game extends Application {
	
	String[] easy = {"3/ 1,2", "2- 3,4", "9+ 5,9,13", "12x 6,10,11", "2- 7,8", "2/ 12,16", "6+ 14,15"};
	String[] medium = {"11+ 1,7", "2÷ 2,3", "20x 4,10", "6x 5,6,12,18", "3- 8,9", "3÷ 11,17", "240x 13,14,19,20", "6x 15,16", "6x 21,27", "7+ 22,28,29", "30x 23,24", "6x 25,26", "9+ 30,36", "8+ 31,32,33", "2÷ 34,35"};
	String[] hard = {"1- 1,9", "3+ 2,3", "21+ 4,5,13", "7+ 6,14", "11+ 7,15", "6+ 8,16", "10+ 10,11", "3+ 12,20", "1- 17,18", "11+ 19,27,35", "4+ 21,29", "12+ 22,30", "7- 23,31", "1- 24,32", "10+ 25,26", "3- 28,36", "1- 33,34", "15+ 37,38,45", "3 39", "7- 40,48" ,"6+ 41,42", "5- 43,51", "2- 44,52", "15+ 46,47", "7- 49,57", "2- 50,58", "5 59", "9+ 60,61,62", "10+ 53,54", "5- 55,56", "7+ 63,64"};
	String[] puzzle;
	Grid board;
	MenuItem m21, m22, m23, m42, m43;
	CheckMenuItem m41;
	VBox vb;
	Stage s, newWindow, genWindow;
	TextArea ta;
	int fontSize = 12;
	int genSize = 6;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage s) {
		this.s = s;
		
		Stage intro = new Stage();
		intro.initModality(Modality.APPLICATION_MODAL);
		Label diffLabel = new Label("Select starting puzzle:");
		RadioButton buttonEasy = new RadioButton("Easy (4x4)");
		buttonEasy.setPadding(new Insets(5,0,5,0));
		RadioButton buttonMed = new RadioButton("Medium (6x6)");
		RadioButton buttonHard = new RadioButton("Hard (8x8)");
		buttonHard.setPadding(new Insets(5,0,10,0));
		ToggleGroup diffGroup = new ToggleGroup();
		buttonEasy.setToggleGroup(diffGroup);
		buttonMed.setToggleGroup(diffGroup);
		buttonHard.setToggleGroup(diffGroup);
		buttonEasy.setSelected(true);
		Button confirm = new Button("Confirm");
		confirm.prefWidth(100);
		confirm.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(buttonEasy.isSelected()) {
					puzzle = easy;
				} else if(buttonMed.isSelected()) {
					puzzle = medium;
				} else if(buttonHard.isSelected()) {
					puzzle = hard;
				}
				intro.close();
			}
		});
		VBox diff = new VBox(diffLabel, buttonEasy, buttonMed, buttonHard, confirm);
		diff.setPadding(new Insets(0,30,0,0));
		diff.setAlignment(Pos.CENTER);
		Image egg = new Image(this.getClass().getResourceAsStream("/eggicon.png"), 100, 100, false, false);
		ImageView iv = new ImageView(egg);
		HBox welcome = new HBox(diff, iv);
		welcome.setPadding(new Insets(15));
		intro.setScene(new Scene(welcome, 300, 145));
		intro.setTitle("Welcome to EggDoKu");
		intro.getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
		intro.setResizable(false);
		intro.showAndWait();
		
		try {
			this.board = new Grid(puzzle, this);
		} catch(NullPointerException npe) {
			Platform.exit();
	        System.exit(0);
		}
		
		Solver solver = new Solver(board);
		Date start = new Date();
		if(solver.solve(1,0) == 1) {
			solver.solve(1);
			System.out.println("Solution found");
			Date end = new Date();
			System.out.println("Solver took " + (end.getTime()-start.getTime()) + " milliseconds to find solution.");
		} else
			System.out.println("There is literally no way to trigger this message and yet you've somehow done it.");
		solver.clean();
		
		MenuBar mb = new MenuBar();
		Menu m0 = new Menu("New");
		Menu m1 = new Menu("Load");
		Menu m2 = new Menu("Edit");
		Menu m3 = new Menu("Text Size");
		Menu m4 = new Menu("Tools");
		MenuItem m01 = new MenuItem("Generate new game");
		MenuItem m11 = new MenuItem("Load from file...");
		MenuItem m12 = new MenuItem("Load from text...");
		m21 = new MenuItem("Undo");
		m21.setDisable(true);
		m22 = new MenuItem("Redo");
		m22.setDisable(true);
		m23 = new MenuItem("Clear");
		m41 = new CheckMenuItem("Show errors");
		ToggleGroup tg = new ToggleGroup();
		RadioMenuItem m31 = new RadioMenuItem("Small");
		m31.setToggleGroup(tg);
		m31.setSelected(true);
		RadioMenuItem m32 = new RadioMenuItem("Medium");
		m32.setToggleGroup(tg);
		RadioMenuItem m33 = new RadioMenuItem("Large");
		m33.setToggleGroup(tg);
		m42 = new MenuItem("Hint");
		m43 = new MenuItem("Show solution");
		mb.getMenus().addAll(m0,m1,m2,m3,m4);
		m0.getItems().add(m01);
		m1.getItems().addAll(m11,m12);
		m2.getItems().addAll(m21,m22,m23);
		m3.getItems().addAll(m31,m32,m33);
		m4.getItems().addAll(m41,m42,m43);
		vb = new VBox(mb, board);
		VBox.setVgrow(board, Priority.ALWAYS);
		
		m01.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				generateNew();
			}
		});
		
		m11.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				loadFromFile();
			}
		});
		
		m12.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				loadFromText();
			}
		});
		
		m21.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				board.undo();
				m22.setDisable(false);
				if(board.getMist() == true) {
					for(Cell cell : board.cellArray) {
						cell.setBackground(new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY)));
					}
					board.highRows();
					board.highCols();
					board.highCages();
				}
				if(board.undo.empty()) {
					m21.setDisable(true);
				}
			}
		});
		
		m21.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
		
		m22.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				board.redo();
				m21.setDisable(false);
				if(board.getMist() == true) {
					for(Cell cell : board.cellArray) {
						cell.setBackground(new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY)));
					}
					board.highRows();
					board.highCols();
					board.highCages();
				}
				if(board.redo.empty()) {
					m22.setDisable(true);
				}
			}
		});
		
		m22.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
		
		m23.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Clear confirmation");
				alert.setHeaderText("Are you sure you want to clear the board?");
				alert.setContentText("Please confirm action to proceed");
				((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    board.clear();
				    for(Cell cell : board.cellArray) {
						cell.setBackground(new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY)));
					}
				}
			}
		});
		
		m41.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Boolean b = board.getMist();
				board.setMist(!b);
				//System.out.println(board.getMist());
				if(board.getMist() == true) {
					board.highRows();
					board.highCols();
					board.highCages();
				}
				if(!board.getMist()) {
					for(Cell cell : board.cellArray) {
						cell.setBackground(new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY)));
					}
				}
			}
		});
		
		m31.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				fontSize = 12;
				board.changeFont();
			}
		});
		
		m32.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				fontSize = 15;
				board.changeFont();
			}
		});
		
		m33.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				fontSize = 18;
				board.changeFont();
			}
		});
		
		m42.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				board.hint();
			}
		});
		
		m43.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				board.autoSolve();
				m21.setDisable(true);
				m22.setDisable(true);
				m42.setDisable(true);
				m43.setDisable(true);
			}
		});
		
		s.setMinHeight(300);
		s.setMinWidth(300);
		s.minHeightProperty().bind(s.widthProperty());
        s.maxHeightProperty().bind(s.widthProperty());
        s.setTitle("EggDoKu");
        s.getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
        Scene scene = new Scene(vb, 600, 600);
        s.setScene(scene);
        s.show();
	}
	
	public void loadFromFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File to Load");
		ExtensionFilter txtFilter = new ExtensionFilter("Text files", "*.txt");
		fileChooser.getExtensionFilters().add(txtFilter);
		File file = fileChooser.showOpenDialog(s);
		if(file == null) {
			return;
		}
		if(file != null) {
			vb.getChildren().remove(board);
			FileParser f = new FileParser(file);
			f.makeArray();
			f.fillArray();
			m41.setSelected(false);
			if(f.checkIfValid()) {
				puzzle = f.getArray();
				try { 
					board = new Grid(puzzle, this);
					vb.getChildren().add(board);
					VBox.setVgrow(board, Priority.ALWAYS);
				} catch(Exception ee) {
					configFailAlert();
					loadFromFile();
					return;
				}
			} else {
				configFailAlert();
				loadFromFile();
				return;
			}
		}
		Solver solver = new Solver(board);
		Date start = new Date();
		int solverResult = solver.solve(1, 0);
		if(solverResult == 1) {
			solver.solve(1);
			System.out.println("Unique solution found");
			Date end = new Date();
			System.out.println("Solver took " + (end.getTime()-start.getTime()) + " milliseconds to find solution.");
		} else if(solverResult > 1) {
			System.out.println("WARNING: puzzle has multiple solutions");
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Bad puzzle");
			alert.setHeaderText("WARNING: puzzle has multiple solutions");
			alert.setContentText("Hint and auto-solve will be disabled. Continue anyway?");
			ButtonType no = new ButtonType("No");
			ButtonType yes = new ButtonType("Yes");
			alert.getButtonTypes().setAll(no, yes);
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == no) {
				vb.getChildren().remove(board);
				loadFromFile();
				return;
			} else {
				try {
		        	newWindow.close();
		        	m42.setDisable(true);
		    		m43.setDisable(true);
		    		return;
		        } catch(NullPointerException npe) {
		        	
		        }
			}
		} else {
			System.out.println("WARNING: puzzle can't be solved");
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Unsolvable puzzle");
			alert.setHeaderText("WARNING: puzzle can't be solved");
			alert.setContentText("Hint and auto-solve will be disabled. Continue anyway?");
			ButtonType no = new ButtonType("No");
			ButtonType yes = new ButtonType("Yes");
			alert.getButtonTypes().setAll(no, yes);
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == no) {
				vb.getChildren().remove(board);
				loadFromFile();
				return;
			} else {
				try {
		        	newWindow.close();
		        	m42.setDisable(true);
		    		m43.setDisable(true);
		    		return;
		        } catch(NullPointerException npe) {
		        	
		        }
			}
		}
			
		solver.clean();
		m21.setDisable(true);
		m22.setDisable(true);
		m23.setDisable(true);
		m41.setDisable(false);
		m42.setDisable(false);
		m43.setDisable(false);
	}
	
	public void loadFromText() {
		Label enter = new Label("Enter config text here:");
		ta = new TextArea();
		HBox tex = new HBox(ta);
		HBox.setHgrow(ta, Priority.ALWAYS);
		Button conf = new Button("Create puzzle");
		Pane spacer = new Pane();
		spacer.setMinSize(10, 1);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		HBox inpt = new HBox(enter, spacer, conf);
		inpt.setPadding(new Insets(0,0,5,0));
		VBox inpWin = new VBox(inpt, tex);
		inpWin.setPadding(new Insets(10));
		VBox.setVgrow(tex, Priority.ALWAYS);
		newWindow = new Stage();
		newWindow.setTitle("Text config");
		newWindow.getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
		Scene textWindow = new Scene(inpWin, 300, 400);
		newWindow.setScene(textWindow);
		newWindow.initModality(Modality.APPLICATION_MODAL);
		newWindow.show();
		
		conf.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
		        alsoLoadFromText(ta.getText(), 0);
		        //newWindow.close();
		    }
		});
	}
	
	public void alsoLoadFromText(String text, int source) {
        TextParser tp = new TextParser(text);
        vb.getChildren().remove(board);
        m41.setSelected(false);
        if(tp.checkIfValid()) {
	        puzzle = tp.getPuzzle();
	        try {
	        	board = new Grid(puzzle, this);
	        	vb.getChildren().add(board);
				VBox.setVgrow(board, Priority.ALWAYS);
	        } catch(Exception ee) {
	        	configFailAlert();
	        	return;
	        }
	        try {
	        	newWindow.close();
	        } catch(NullPointerException npe) {
	        	
	        }
        } else {
        	configFailAlert();
        	return;
        }
        Solver solver = new Solver(board);
		Date start = new Date();
		int solverResult = solver.solve(1, 0);
		if(solverResult == 1) {
			solver.solve(1);
			System.out.println("Unique solution found");
			Date end = new Date();
			System.out.println("Solver took " + (end.getTime()-start.getTime()) + " milliseconds to find solution.");
		} else if(solverResult > 1) {
			if(source == 1) {
				System.out.println("Generated puzzle has multiple solutions; rerolling.");
				Generator gen = new Generator(genSize);
				alsoLoadFromText(gen.sendToParse(),1);
			} else {
				System.out.println("WARNING: puzzle has multiple solutions");
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Bad puzzle");
				alert.setHeaderText("WARNING: puzzle has multiple solutions");
				alert.setContentText("Hint and auto-solve will be disabled. Continue anyway?");
				ButtonType no = new ButtonType("No");
				ButtonType yes = new ButtonType("Yes");
				alert.getButtonTypes().setAll(no, yes);
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == no) {
					vb.getChildren().remove(board);
					if(source == 0) {
						loadFromText();
						return;
					} else {
						genWindow.close();
						generateNew();
					}
				} else {
					try {
			        	newWindow.close();
			        	m42.setDisable(true);
			    		m43.setDisable(true);
			    		return;
			        } catch(NullPointerException npe) {
			        	
			        }
				}
			}
		} else {
			System.out.println("WARNING: puzzle can't be solved");
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Unsolvable puzzle");
			alert.setHeaderText("WARNING: puzzle can't be solved");
			alert.setContentText("Hint and auto-solve will be disabled. Continue anyway?");
			ButtonType no = new ButtonType("No");
			ButtonType yes = new ButtonType("Yes");
			alert.getButtonTypes().setAll(no, yes);
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == no) {
				vb.getChildren().remove(board);
				if(source == 0) {
					loadFromText();
					return;
				} else {
					genWindow.close();
					generateNew();
				}
			} else {
				try {
		        	newWindow.close();
		        	m42.setDisable(true);
		    		m43.setDisable(true);
		    		return;
		        } catch(NullPointerException npe) {
		        	
		        }
			}
		}
		
		solver.clean();
        m21.setDisable(true);
		m22.setDisable(true);
		m23.setDisable(true);
		m41.setDisable(false);
		m42.setDisable(false);
		m43.setDisable(false);
	}
	
	public void configFailAlert() {
		m41.setDisable(true);
		m42.setDisable(true);
		m43.setDisable(true);
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Config Failed");
		alert.setHeaderText("Your config is bad and you should feel bad");
		alert.setContentText("There was a mistake in your config text that meant the puzzle could not be created");
		Image image = new Image(this.getClass().getResourceAsStream("/crackedeggicon.png"),80,80,false,false);
		ImageView imageView = new ImageView(image);
		alert.setGraphic(imageView);
		((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
		alert.showAndWait();
	}
	
	public void generateNew() {
		genWindow = new Stage();
		genWindow.setTitle("Puzzle generator");
		genWindow.getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
		Label sizeLabel = new Label("Grid Size");
		RadioButton buttonEasy = new RadioButton("Easy (4x4)");
		buttonEasy.setPadding(new Insets(5,0,5,0));
		RadioButton buttonMed = new RadioButton("Medium (6x6)");
		RadioButton buttonHard = new RadioButton("Hard (8x8)");
		buttonHard.setPadding(new Insets(5,0,10,0));
		ToggleGroup diffGroup = new ToggleGroup();
		buttonEasy.setToggleGroup(diffGroup);
		buttonMed.setToggleGroup(diffGroup);
		buttonHard.setToggleGroup(diffGroup);
		buttonEasy.setSelected(true);
		Button confirm = new Button("Confirm");
		confirm.prefWidth(100);
		confirm.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(buttonEasy.isSelected()) {
					genSize = 4;
					Generator gen = new Generator(4);
					alsoLoadFromText(gen.sendToParse(),1);
				} else if(buttonMed.isSelected()) {
					genSize = 6;
					Generator gen = new Generator(6);
					alsoLoadFromText(gen.sendToParse(),1);
				} else if(buttonHard.isSelected()) {
					genSize = 8;
					Generator gen = new Generator(8);
					alsoLoadFromText(gen.sendToParse(),1);
				}
				genWindow.close();
			}
			
		});
		VBox diff = new VBox(sizeLabel, buttonEasy, buttonMed, buttonHard, confirm);
		diff.setPadding(new Insets(0,30,0,0));
		diff.setAlignment(Pos.CENTER);
		Image egg = new Image(this.getClass().getResourceAsStream("/eggicon.png"), 100, 100, false, false);
		ImageView iv = new ImageView(egg);
		HBox welcome = new HBox(diff, iv);
		welcome.setPadding(new Insets(15));
		genWindow.setScene(new Scene(welcome, 300, 145));
		genWindow.setTitle("Welcome to EggDoKu");
		genWindow.getIcons().add(new Image(this.getClass().getResourceAsStream("/eggicon.png")));
		genWindow.setResizable(false);
		genWindow.showAndWait();
	}

}
