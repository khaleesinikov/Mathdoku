import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Game extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage s) {
		Grid board = new Grid(6);
		
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
