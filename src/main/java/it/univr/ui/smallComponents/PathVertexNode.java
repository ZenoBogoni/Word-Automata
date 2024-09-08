package it.univr.ui.smallComponents;

import java.io.IOException;

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

    public PathVertexNode(String label, boolean isInitial, boolean isFinal) {
        this.isFinal = isFinal;
        this.isInitial = isInitial;
        this.label = label;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("pathVertexNode.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize() {

        vertexText.getStyleClass().add("pathLabel");
        vertexText.setText(label);

        vertexCircle.getStyleClass().add("vertex");
        if (isFinal) {
            vertexCircle.getStyleClass().add("finalVertex");
        } else if (isInitial) {
            vertexCircle.getStyleClass().add("initialVertex");
        }
    }
}
