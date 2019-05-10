package application;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*Still need to: 
 * setup display for the drawBox -- I added border and used padding. Will this work?
 * add second shape if you wanna do extra credit stuff -- maybe circle?
 * 
 * I also don't really like how hers is aligned, tbh. I guess we can just roll with it anyway.
 * 
 */
public class DrawRectangleGUI extends Application {
	Button clearButton;
	RadioButton red, yellow, blue, thinBorder, thickBorder;
	CheckBox fillShape;
	Rectangle dragBox;
	Circle dragCircle; // extra credit?

	static final double THIN_BORDER_WIDTH = 0.5;
	static final double THICK_BORDER_WIDTH = 2.0;

	public void start(Stage primaryStage) throws Exception {

		VBox mainPane = new VBox();
		
		Text newText = new Text("Meow");
		HBox drawBox = new HBox(newText);
		drawBox.setAlignment(Pos.CENTER);
		drawBox.setSpacing(10);
		drawBox.setPadding(new Insets(197));
		String cssLayout = "-fx-border-color: black;\n" + "-fx-border-insets: 7;\n" + "-fx-border-width: 2;\n";
		
		drawBox.setStyle(cssLayout);

		dragBox = new Rectangle(0, 0, 0, 0);
		dragBox.setVisible(false);
		dragBox.setFill(Color.TRANSPARENT);
		dragBox.setStroke(Color.RED);
		
		dragCircle = new Circle(0, 0, 0); // extra credit?
		dragCircle.setVisible(false);
		dragCircle.setFill(Color.TRANSPARENT);
		dragCircle.setStroke(Color.RED);
		
		
		// color option radio buttons
		red = new RadioButton("Red");
		yellow = new RadioButton("Yellow");
		blue = new RadioButton("Blue");

		// border option radio buttons for shape
		thinBorder = new RadioButton("Thin Border");
		thickBorder = new RadioButton("Thick Border");

		fillShape = new CheckBox("Fill"); // fill or no fill

		HBox colorSelectionBox = new HBox(red, yellow, blue);
		colorSelectionBox.setSpacing(5);
		colorSelectionBox.setAlignment(Pos.BOTTOM_CENTER);
		mainPane.getChildren().add(colorSelectionBox);

		HBox borderSizeBox = new HBox(thinBorder, thickBorder, fillShape);
		borderSizeBox.setSpacing(10);
		borderSizeBox.setAlignment(Pos.BOTTOM_CENTER);
		borderSizeBox.setPadding(new Insets(10));
		mainPane.getChildren().add(borderSizeBox);

		// ToggleGroups for selections
		ToggleGroup colorToggleGroup = new ToggleGroup();
		red.setToggleGroup(colorToggleGroup);
		yellow.setToggleGroup(colorToggleGroup);
		blue.setToggleGroup(colorToggleGroup);
		red.setSelected(true);

		ToggleGroup borderToggleGroup = new ToggleGroup();
		thinBorder.setToggleGroup(borderToggleGroup);
		thickBorder.setToggleGroup(borderToggleGroup);
		thinBorder.setSelected(true);
		
		//is this the right pane?-------------------------------------------------
		mainPane.setOnMouseClicked(this::handleMouse);
		mainPane.setOnMouseMoved(this::handleMouse);
		mainPane.setOnMouseReleased(this::handleMouse);
		mainPane.getChildren().add(dragBox);

		red.setOnAction(this::handleColorToggleGroup);
		blue.setOnAction(this::handleColorToggleGroup);
		yellow.setOnAction(this::handleColorToggleGroup);

		thinBorder.setOnAction(this::handleBorderToggleGroup);
		thickBorder.setOnAction(this::handleBorderToggleGroup);

		fillShape.setOnAction(this::handleFillCheckBox);

		// clear drawing button
		clearButton = new Button("Clear");
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				drawBox.getChildren().clear();
			}
		});

		// nest the panes together
		VBox nestedPanes = new VBox(colorSelectionBox, borderSizeBox, clearButton);
		nestedPanes.setAlignment(Pos.BOTTOM_CENTER);
		nestedPanes.setPadding(new Insets(-7));

		// add panes to mainPane
		mainPane.getChildren().addAll(drawBox, nestedPanes);

		Scene scene = new Scene(mainPane, 500, 500);
		primaryStage.setTitle("Rectangles!");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	private void clearButtonHandler(ActionEvent event) {

	}

	// set pen/stroke color
	public void handleColorToggleGroup(ActionEvent event) {
		if (red.isSelected()) {
			dragBox.setStroke(Color.RED);
		} else if (blue.isSelected()) {
			dragBox.setStroke(Color.BLUE);
		} else if (yellow.isSelected()) {
			dragBox.setStroke(Color.YELLOW);
		}
	}

	// set border size
	public void handleBorderToggleGroup(ActionEvent event) {
		if (thinBorder.isSelected()) {
			dragBox.setStrokeWidth(THIN_BORDER_WIDTH);
		} else if (thickBorder.isSelected()) {
			dragBox.setStrokeWidth(THICK_BORDER_WIDTH);
		}
	}

	// set shape fill on or off
	public void handleFillCheckBox(ActionEvent event) {
		if (fillShape.isSelected()) {
			dragBox.setFill(Color.BLACK);
		} else {
			dragBox.setFill(Color.TRANSPARENT);
		}
	}

	// draw lines when mouse clicked
	// stop the lines when mouse button released
	public void handleMouse(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			dragBox.setVisible(true);
			dragBox.setTranslateX(event.getX());
			dragBox.setTranslateY(event.getY());
		}
		if (event.getEventType() == MouseEvent.MOUSE_MOVED && dragBox.isVisible()) {
			dragBox.setWidth(event.getX() - dragBox.getTranslateX());
			dragBox.setHeight(event.getY() - dragBox.getTranslateY());
		}
		if (event.getEventType() == MouseEvent.MOUSE_RELEASED) { //probably need to change this to allow for boxes stay drawn
			dragBox.setVisible(false);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
