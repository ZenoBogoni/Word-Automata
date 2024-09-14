package it.univr.Controller.panes;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList.MyVertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeBase;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.Controller.smallComponents.PathEdgeNode;
import it.univr.Controller.smallComponents.PathVertexNode;
import it.univr.backend.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SolutionPane extends VBox {
    @FXML
    private HBox nodesHBox;
    @FXML
    private ScrollPane nodesScrollPane;

    public SolutionPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("solutionPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize() {
        nodesHBox.setOnScroll(event -> {
            // Scroll horizontally when the mouse wheel is used
            nodesScrollPane.setHvalue(nodesScrollPane.getHvalue() - (event.getDeltaY() * 2) / nodesScrollPane.getContent().getBoundsInLocal().getWidth());
            event.consume(); // Consume the event to prevent vertical scrolling
        });

        nodesScrollPane.setOnScroll(event -> {
            // Scroll horizontally when the mouse wheel is used
            nodesScrollPane.setHvalue(nodesScrollPane.getHvalue() - event.getDeltaY() / nodesScrollPane.getContent().getBoundsInLocal().getWidth());
            event.consume(); // Consume the event to prevent vertical scrolling
        });
    }

    public void insertVertexNode(SmartGraphVertexNode<String> vertexNode) {
        String label = vertexNode.getAttachedLabel().getText();
        boolean isInitial = ((MyVertex) vertexNode.getUnderlyingVertex()).isInitial();
        boolean isFinal = ((MyVertex) vertexNode.getUnderlyingVertex()).isFinal();

        PathVertexNode vertex = new PathVertexNode(label, isInitial, isFinal, vertexNode);
        nodesHBox.getChildren().add(vertex);
    }

    public void insertEdgeNode(SmartGraphEdgeBase<String, String> edgeNode) {
        String label = edgeNode.getAttachedLabel().getText();
        PathEdgeNode edge = new PathEdgeNode(label, edgeNode);
        nodesHBox.getChildren().add(edge);
        edge.setOnMouseClicked(e -> {
            SceneReference.setSelectedEdge(edgeNode);
        });
    }

    public void clearHistory() {
        nodesHBox.getChildren().clear();
    }

}
