package it.univr.ui.sidePanes;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.ui.MainPane;
import it.univr.utils.SceneReference;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class GraphSidePane extends VBox {
    // components
    private MainPane mainPane = SceneReference.getMainPane();
    private SmartGraphPanel<String, String> graphView = SceneReference.getGrapView();
    private DigraphEdgeList<String, String> graph = SceneReference.getGraph();
    private SmartGraphVertexNode<String> selectedVertexNode;

    // properties
    private SimpleBooleanProperty isVertexSelectedProperty = SceneReference.getIsVertexSelectedProperty();
    private SimpleBooleanProperty confirmToApplyProperty = SceneReference.getConfirmToApplyProperty();

    // FXML
    @FXML
    private TextField vertexLabelTextField;
    @FXML
    private Button deleteVertexButton;

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
                selectedVertexNode = mainPane.getSelectedVertexNode();
                vertexLabelTextField.setText(selectedVertexNode.getAttachedLabel().getText());
            }
        });

        vertexLabelTextField.setOnAction(e -> {
            // DigraphEdgeList<String, String> graph = SceneReference.getGraph();
            updateVertexName();
        });

        vertexLabelTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !confirmToApplyProperty.get()) {
                updateVertexName();
            }
        });

        // delete vertex button
        deleteVertexButton.setOnAction(e -> {
            if (selectedVertexNode != null) {
                // DigraphEdgeList<String, String> graph = SceneReference.getGraph();
                graph.removeVertex(selectedVertexNode.getUnderlyingVertex());
                System.out.println(graph.toString());
                graphView.update();
                mainPane.deselectVertex();
            }

        });
    }

    private void updateVertexName() {
        if (vertexLabelTextField.getText().isBlank()) {
            return;
        }
        Vertex<String> newVertex = graph.updateLabelFor(selectedVertexNode.getUnderlyingVertex(), vertexLabelTextField.getText());
        graphView.updateAndWait();
        selectedVertexNode = graphView.getVertexByName(newVertex);
        System.out.println(selectedVertexNode.getUnderlyingVertex().element());
        System.out.println(SceneReference.getGraph().toString());
    }
}
