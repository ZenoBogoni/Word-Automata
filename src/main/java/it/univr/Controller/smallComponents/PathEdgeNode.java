package it.univr.Controller.smallComponents;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeBase;

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
    private SmartGraphEdgeBase<String, String> edgeNode;

    public PathEdgeNode(String label, SmartGraphEdgeBase<String, String> edgeNode) {
        this.label = label;
        this.edgeNode = edgeNode;

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
        edgeLine.endXProperty().bind(Bindings.subtract(widthProperty(), 10));
        arrowHead.getPoints().clear();
        double x = edgeLine.getEndX();
        double y = edgeLine.getEndY();
        arrowHead.getPoints().addAll(
                x, y,
                x - size, y - size / 2,
                x - size, y + size / 2);
        edgeLine.getStyleClass().add("edge");
        arrowHead.getStyleClass().add("arrowHead");
        edgeText.hoverProperty().addListener((obs, o, n) -> {
            if (n) {
                edgeLine.setStyle("-fx-stroke-width: 4;");
                arrowHead.setStyle("-fx-stroke-width: 4;");
            } else {
                edgeLine.setStyle(null);
                arrowHead.setStyle(null);
            }
        });
    }

}
