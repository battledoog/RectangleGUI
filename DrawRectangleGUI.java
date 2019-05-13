import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DrawRectangleGUI extends Application {
	private Pane drawBox;
	private Button clearButton;
	private RadioButton red, yellow, blue, thinBorder, thickBorder;
	private CheckBox fillShape;
	private Rectangle dragBox;
	private List<Rectangle> dragBoxes;
	private Circle dragCircle; // extra credit?
	private Boolean isDragging = false;
	private double dragStartX = 0.0;
	private double dragStartY = 0.0;
	private Color color = Color.RED;
	private Boolean fill = false;
	private double strokeWidth = THIN_BORDER_WIDTH;

	private static final double THIN_BORDER_WIDTH = 0.5;
	private static final double THICK_BORDER_WIDTH = 5.0;

	public void start(Stage primaryStage) throws Exception {

		VBox mainPane = new VBox();
		
		drawBox = new Pane();
		drawBox.setPrefHeight(430);
		String cssLayout = "-fx-border-color: black;\n" + "-fx-border-insets: 7;\n" + "-fx-border-width: 2;\n";
		
		drawBox.setStyle(cssLayout);

		dragBoxes = new ArrayList<>();

		dragBox = new Rectangle();
		dragBox.setFill(Color.TRANSPARENT);
		dragBox.setStroke(Color.RED);
		dragBox.setVisible(false);
		
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

		mainPane.setOnMouseClicked(this::handleMouse);
		mainPane.setOnMouseMoved(this::handleMouse);
		mainPane.setOnMouseReleased(this::handleMouse);
		drawBox.getChildren().add(dragBox);

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

	// set pen/stroke color
	private void handleColorToggleGroup(ActionEvent event) {
		if (red.isSelected()) {
			color = Color.RED;
		} else if (blue.isSelected()) {
			color = Color.BLUE;
		} else if (yellow.isSelected()) {
			color = Color.YELLOW;
		}
	}

	// set border size
	private void handleBorderToggleGroup(ActionEvent event) {
		if (thinBorder.isSelected()) {
			strokeWidth = THIN_BORDER_WIDTH;
		} else if (thickBorder.isSelected()) {
			strokeWidth = THICK_BORDER_WIDTH;
		}
	}

	// set shape fill on or off
	private void handleFillCheckBox(ActionEvent event) {
		fill = fillShape.isSelected();
	}

	// draw lines when mouse clicked
	// stop the lines when mouse button released
	private void handleMouse(MouseEvent event) {
		if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
			if (isDragging) {
				isDragging = false;
			}
			else {
				Rectangle newDragBox = new Rectangle(event.getX(), event.getY(), 0, 0);
				newDragBox.setFill(fill ? color : Color.TRANSPARENT);
				newDragBox.setStroke(color);
				newDragBox.setStrokeWidth(strokeWidth);
				newDragBox.setVisible(true);
				dragBoxes.add(newDragBox);
				dragBox = newDragBox;
				drawBox.getChildren().add(dragBox);
				dragStartX = event.getX();
				dragStartY = event.getY();
				isDragging = true;
			}
		}
		if (event.getEventType() == MouseEvent.MOUSE_MOVED && isDragging) {
			if (event.getX() < dragStartX) {
				dragBox.setX(event.getX());
				dragBox.setWidth(dragStartX - event.getX());
			}
			else {
				dragBox.setWidth(event.getX() - dragStartX);
			}

			if (event.getY() < dragStartY) {
				dragBox.setY(event.getY());
				dragBox.setHeight(dragStartY - event.getY());
			}
			else {
				dragBox.setHeight(event.getY() - dragStartY);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}