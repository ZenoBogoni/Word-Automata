package it.univr.ui.sidePanes;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.ui.MainPane;
import it.univr.utils.SceneReference;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GraphSidePane extends VBox {
    // components
    private MainPane mainPane;
    private SmartGraphVertexNode selectedVertexNode;

    // properties
    private SimpleBooleanProperty isVertexSelectedProperty;

    // FXML
    @FXML
    private TextField vertexLabelTextField;

    public GraphSidePane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graphSidePane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize() {
        mainPane = SceneReference.getMainPane();
        selectedVertexNode = mainPane.getSelectedVertexNode();
        isVertexSelectedProperty = SceneReference.getIsVertexSelectedProperty();

        // text field
        vertexLabelTextField.disableProperty().bind(Bindings.not(isVertexSelectedProperty));
        vertexLabelTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        vertexLabelTextField.setAlignment(Pos.CENTER);
        vertexLabelTextField.setPromptText("no vertex selected");

        vertexLabelTextField.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vertexLabelTextField.setText("");
                vertexLabelTextField.setPromptText("no vertex selected");
            } else {
                vertexLabelTextField.setText(mainPane.getSelectedVertexNode().getAttachedLabel().getText());
            }
        });
    }
}
