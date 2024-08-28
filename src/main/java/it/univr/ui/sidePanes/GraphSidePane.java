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
import javafx.scene.control.RadioButton;
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
    private SimpleBooleanProperty initialvertexSetProperty = SceneReference.getnitialVertexSetProperty();

    // java variables
    private String textFieldVertexName;

    // FXML
    @FXML
    private TextField vertexLabelTextField;
    @FXML
    private Button deleteVertexButton;
    @FXML
    private RadioButton initialNodeRadioButton, finalNodeRadioButton;

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
        // hide when vertex is not selected
        deleteVertexButton.visibleProperty().bind(isVertexSelectedProperty);
        initialNodeRadioButton.visibleProperty().bind(isVertexSelectedProperty);
        finalNodeRadioButton.visibleProperty().bind(isVertexSelectedProperty);

        // radio buttons
        isVertexSelectedProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                updateRadioButtons();
            } else {
                initialNodeRadioButton.setDisable(true);
            }
        });

        initialNodeRadioButton.setOnAction(e -> {
            if (initialNodeRadioButton.isSelected()) {
                if (selectedVertexNode.getUnderlyingVertex().isFinal()) {
                    finalNodeRadioButton.setSelected(false);
                    SceneReference.removeFinalVertex(selectedVertexNode);
                }
                SceneReference.setInitialVertexNode(selectedVertexNode);
            } else {
                SceneReference.setInitialVertexNode(null);
            }
        });

        finalNodeRadioButton.setOnAction(e -> {
            if (finalNodeRadioButton.isSelected()) {
                if (selectedVertexNode.getUnderlyingVertex().isInitial()) {
                    initialNodeRadioButton.setSelected(false);
                    SceneReference.setInitialVertexNode(null);
                }
                SceneReference.addFinalvertex(selectedVertexNode);
            } else {
                SceneReference.removeFinalVertex(selectedVertexNode);
            }
        });

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
            if (!vertexLabelTextField.getText().equals(textFieldVertexName)) {
                updateVertexName();
            }
        });

        vertexLabelTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !confirmToApplyProperty.get()) {
                if (!vertexLabelTextField.getText().equals(textFieldVertexName)) {
                    updateVertexName();
                }
            } else if (newValue) {
                textFieldVertexName = new String(vertexLabelTextField.getText());
            }
        });

        // delete vertex button
        deleteVertexButton.setOnAction(e -> {
            if (selectedVertexNode != null) {
                if (selectedVertexNode.equals(SceneReference.getInitialVertexNode())) {
                    SceneReference.setInitialVertexNode(null);
                }
                graph.removeVertex(selectedVertexNode.getUnderlyingVertex());
                System.out.println(graph.toString());
                graphView.update();
                mainPane.deselectVertex();
            }

        });
    }

    private void updateRadioButtons() {
        if (initialvertexSetProperty.get() && !selectedVertexNode.getUnderlyingVertex().isInitial()) {
            initialNodeRadioButton.setSelected(false);
            initialNodeRadioButton.setDisable(true);
        } else {
            initialNodeRadioButton.setDisable(false);
            initialNodeRadioButton.setSelected(selectedVertexNode.getUnderlyingVertex().isInitial());
        }
        finalNodeRadioButton.setSelected(selectedVertexNode.getUnderlyingVertex().isFinal());
    }

    private void updateVertexName() {
        if (vertexLabelTextField.getText().isBlank()) {
            return;
        }
        graph.replace(graph.vertexOf(textFieldVertexName), vertexLabelTextField.getText());
        Vertex<String> newVertex = graph.vertexOf(vertexLabelTextField.getText());
        if (newVertex == null) {
            return;
        }
        graphView.updateAndWait();
        mainPane.setSelectedVertexNode(graphView.getVertexByName(newVertex));
        System.out.println(SceneReference.getGraph().toString());
        textFieldVertexName = new String(newVertex.element());
    }
}
