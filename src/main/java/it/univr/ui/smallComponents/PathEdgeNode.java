package it.univr.ui.smallComponents;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class PathEdgeNode extends StackPane {

    @FXML
    private Text edgeText;
    @FXML
    private Line edgeLine;
    @FXML
    private HBox textHBox;
    @FXML
    private Polygon arrowHead;

    private String label;
    private double size = 10;

    public PathEdgeNode(String label) {
        this.label = label;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pathEdgeNode.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize() {
        edgeText.setText(label);
        edgeLine.endXProperty().bind(Bindings.subtract(widthProperty(), 6));
        arrowHead.getPoints().clear();
        double x = edgeLine.getEndX();
        double y = edgeLine.getEndY();
        arrowHead.getPoints().addAll(
                x, y,
                x - size, y - size / 2,
                x - size, y + size / 2);
        edgeLine.getStyleClass().add("edge");
        arrowHead.getStyleClass().add("arrowHead");
    }

}
