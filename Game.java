import java.io.File;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Game extends Application {
	
	String[] sizeTest = {"3/ 1,2", "2- 3,4", "9+ 5,9,13", "12x 6,10,11", "2- 7,8", "2/ 12,16", "6+ 14,15"};
	String[] examplePuzzle = {"11+ 1,7", "2÷ 2,3", "20x 4,10", "6x 5,6,12,18", "3- 8,9", "3÷ 11,17", "240x 13,14,19,20", "6x 15,16", "6x 21,27", "7+ 22,28,29", "30x 23,24", "6x 25,26", "9+ 30,36", "8+ 31,32,33", "2÷ 34,35"};
	String[] puzzle;
	Grid board;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage s) {
		this.board = new Grid(sizeTest);
		
		MenuBar mb = new MenuBar();
		Menu m1 = new Menu("Load");
		Menu m2 = new Menu("Edit");
		MenuItem m11 = new MenuItem("Load from file...");
		MenuItem m12 = new MenuItem("Load from text...");
		MenuItem m21 = new MenuItem("Undo");
		MenuItem m22 = new MenuItem("Redo");
		MenuItem m23 = new MenuItem("Clear");
		MenuItem m24 = new MenuItem("Show errors");
		mb.getMenus().addAll(m1,m2);
		m1.getItems().addAll(m11,m12);
		m2.getItems().addAll(m21,m22,m23,m24);
		VBox vb = new VBox(mb, board);
		VBox.setVgrow(board, Priority.ALWAYS);
		
		m11.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
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
					puzzle = f.getArray();
					board = new Grid(puzzle);
					vb.getChildren().add(board);
					VBox.setVgrow(board, Priority.ALWAYS);
				}
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

}
