import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
	
	String[] sizeTest = {"3/ 1,2", "2- 3,4", "9+ 5,9,13", "12x 6,10,11", "2- 7,8", "2/ 12,16", "6+ 14,15"};
	String[] examplePuzzle = {"11+ 1,7", "2÷ 2,3", "20x 4,10", "6x 5,6,12,18", "3- 8,9", "3÷ 11,17", "240x 13,14,19,20", "6x 15,16", "6x 21,27", "7+ 22,28,29", "30x 23,24", "6x 25,26", "9+ 30,36", "8+ 31,32,33", "2÷ 34,35"};
	String[] puzzle;
	Grid board;
	MenuItem m21, m22;
	CheckMenuItem m24;
	VBox vb;
	Stage s, newWindow;
	TextArea ta;
	int fontSize = 12;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage s) {
		this.s = s;
		this.board = new Grid(examplePuzzle, this);
		
		MenuBar mb = new MenuBar();
		Menu m1 = new Menu("Load");
		Menu m2 = new Menu("Edit");
		Menu m3 = new Menu("Text Size");
		MenuItem m11 = new MenuItem("Load from file...");
		MenuItem m12 = new MenuItem("Load from text...");
		m21 = new MenuItem("Undo");
		m21.setDisable(true);
		m22 = new MenuItem("Redo");
		m22.setDisable(true);
		MenuItem m23 = new MenuItem("Clear");
		m24 = new CheckMenuItem("Show errors");
		ToggleGroup tg = new ToggleGroup();
		RadioMenuItem m31 = new RadioMenuItem("Small");
		m31.setToggleGroup(tg);
		m31.setSelected(true);
		RadioMenuItem m32 = new RadioMenuItem("Medium");
		m32.setToggleGroup(tg);
		RadioMenuItem m33 = new RadioMenuItem("Large");
		m33.setToggleGroup(tg);
		mb.getMenus().addAll(m1,m2,m3);
		m1.getItems().addAll(m11,m12);
		m2.getItems().addAll(m21,m22,m23,m24);
		m3.getItems().addAll(m31,m32,m33);
		vb = new VBox(mb, board);
		VBox.setVgrow(board, Priority.ALWAYS);
		
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
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    board.clear();
				    for(Cell cell : board.cellArray) {
						cell.setBackground(new Background(new BackgroundFill(Color.MINTCREAM, CornerRadii.EMPTY, Insets.EMPTY)));
					}
				}
			}
		});
		
		m24.setOnAction(new EventHandler<ActionEvent>() {
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
		
		s.setMinHeight(300);
		s.setMinWidth(300);
		s.minHeightProperty().bind(s.widthProperty());
        s.maxHeightProperty().bind(s.widthProperty());
        s.setTitle("EggDoKu");
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
		if(file != null) {
			vb.getChildren().remove(board);
			FileParser f = new FileParser(file);
			f.makeArray();
			f.fillArray();
			m24.setSelected(false);
			if(f.checkIfValid()) {
				puzzle = f.getArray();
				try { 
					board = new Grid(puzzle, this);
					vb.getChildren().add(board);
					VBox.setVgrow(board, Priority.ALWAYS);
				} catch(Exception ee) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Config Failed");
					alert.setHeaderText("Your config is bad and you should feel bad");
					alert.setContentText("There was a mistake in your config file that meant the puzzle could not be created");
					alert.showAndWait();
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Config Failed");
				alert.setHeaderText("Your config is bad and you should feel bad");
				alert.setContentText("There was a mistake in your config file that meant the puzzle could not be created");
				alert.showAndWait();
			}
		}
		m21.setDisable(true);
		m22.setDisable(true);
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
		Scene textWindow = new Scene(inpWin, 300, 400);
		newWindow.setScene(textWindow);
		newWindow.initModality(Modality.APPLICATION_MODAL);
		newWindow.show();
		
		conf.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
		        alsoLoadFromText();
		    }
		});
	}
	
	public void alsoLoadFromText() {
		String text = ta.getText();
        TextParser tp = new TextParser(text);
        vb.getChildren().remove(board);
        m24.setSelected(false);
        if(tp.checkIfValid()) {
	        puzzle = tp.getPuzzle();
	        try {
	        	board = new Grid(puzzle, this);
	        	vb.getChildren().add(board);
				VBox.setVgrow(board, Priority.ALWAYS);
	        } catch(Exception ee) {
	        	Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Config Failed");
				alert.setHeaderText("Your config is bad and you should feel bad");
				alert.setContentText("There was a mistake in your config text that meant the puzzle could not be created");
				alert.showAndWait();
	        }
			newWindow.close();
        } else {
        	Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Config Failed");
			alert.setHeaderText("Your config is bad and you should feel bad");
			alert.setContentText("There was a mistake in your config text that meant the puzzle could not be created");
			alert.showAndWait();
			newWindow.close();
        }
        m21.setDisable(true);
		m22.setDisable(true);
	}

}
