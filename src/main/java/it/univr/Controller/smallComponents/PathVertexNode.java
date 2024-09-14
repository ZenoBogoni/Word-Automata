package it.univr.Controller.smallComponents;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.backend.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class PathVertexNode extends AnchorPane {
    @FXML
    private Circle vertexCircle;
    @FXML
    private Text vertexText;

    private boolean isFinal, isInitial;
    private String label;
    private SmartGraphVertexNode<String> vertexNode;

    public PathVertexNode(String label, boolean isInitial, boolean isFinal, SmartGraphVertexNode<String> vertexNode) {
        this.isFinal = isFinal;
        this.isInitial = isInitial;
        this.label = label;
        this.vertexNode = vertexNode;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pathVertexNode.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public PathVertexNode(String label, boolean isInitial, boolean isFinal) {
        this(label, isInitial, isFinal, null);
    }

    public void initialize() {
        vertexText.setText(label);

        vertexCircle.getStyleClass().add("vertex");
        if (isFinal) {
            vertexCircle.getStyleClass().add("finalVertex");
        } else if (isInitial) {
            vertexCircle.getStyleClass().add("initialVertex");
        }

        vertexCircle.setOnMouseClicked(e -> {
            if (vertexNode != null)
                SceneReference.setSelectedVertexNode(vertexNode);
        });
    }

    public Circle circle() {
        return vertexCircle;
    }
}
