package it.univr.ui.sidePanes;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.ui.MainPane;
import it.univr.ui.testGraphAlgoritm;
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
import javafx.scene.text.Text;

public class GraphSidePane extends VBox {
    // components
    private MainPane mainPane = SceneReference.getMainPane();
    private SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();
    private DigraphEdgeList<String, String> graph = SceneReference.getGraph();
    private SmartGraphVertexNode<String> selectedVertexNode;

    // properties
    private SimpleBooleanProperty isVertexSelectedProperty = SceneReference.getIsVertexSelectedProperty();
    private SimpleBooleanProperty confirmToApplyProperty = SceneReference.getConfirmToApplyProperty();
    private SimpleBooleanProperty initialvertexSetProperty = SceneReference.getnitialVertexSetProperty();
    private SimpleBooleanProperty isEdgeSelectedProperty = SceneReference.getIsEdgeSelectedProperty();

    // java variables
    private String lastVertexName;
    private int edgeId;

    // FXML
    @FXML
    private TextField vertexLabelTextField, edgeLabelTextField, testingWordField;
    @FXML
    private Button deleteVertexButton, deleteEdgeButton, testWordButton;
    @FXML
    private RadioButton initialNodeRadioButton, finalNodeRadioButton;
    @FXML
    private Text vertexLabel, edgeLabel;

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

        testWordButton.setOnAction(e -> {
            if (testingWordField.getText().equals("")) {
                testingWordField.setPromptText("Insert a valide word");
            } else {
                SceneReference.setTestWord(testingWordField.getText());
                testGraphAlgoritm.testGraph();
            }
        });

        // hide when vertex is not selected
        // deleteVertexButton.visibleProperty().bind(isVertexSelectedProperty);
        initialNodeRadioButton.setDisable(true);
        finalNodeRadioButton.disableProperty().bind(Bindings.not(isVertexSelectedProperty));

        // radio buttons
        isVertexSelectedProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                updateRadioButtons();
            } else {
                initialNodeRadioButton.setDisable(true);
                initialNodeRadioButton.setSelected(false);
                finalNodeRadioButton.setSelected(false);
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

        // vertex text field
        vertexLabelTextField.disableProperty().bind(Bindings.not(isVertexSelectedProperty));
        vertexLabelTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        vertexLabelTextField.setAlignment(Pos.CENTER);
        vertexLabelTextField.setPromptText("no vertex selected");
        vertexLabel.setStyle("-fx-fill: gray");

        vertexLabelTextField.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vertexLabelTextField.setText("");
                vertexLabelTextField.setPromptText("no vertex selected");
                vertexLabel.setStyle("-fx-fill: gray");
            } else {
                selectedVertexNode = mainPane.getSelectedVertexNode();
                vertexLabelTextField.setText(selectedVertexNode.getAttachedLabel().getText());
                lastVertexName = (lastVertexName == null) ? new String(vertexLabelTextField.getText()) : vertexLabelTextField.getText();
                vertexLabel.setStyle(null);

            }
        });

        vertexLabelTextField.setOnAction(e -> {
            if (!vertexLabelTextField.getText().equals(lastVertexName)) {
                updateVertexName();
            }
        });

        vertexLabelTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !confirmToApplyProperty.get() && !vertexLabelTextField.getText().equals(lastVertexName)) {
                updateVertexName();
            }
        });

        // delete vertex button
        deleteVertexButton.disableProperty().bind(Bindings.not(isVertexSelectedProperty));
        deleteVertexButton.setOnAction(e -> {
            if (selectedVertexNode != null) {
                if (selectedVertexNode.equals(SceneReference.getInitialVertexNode())) {
                    SceneReference.setInitialVertexNode(null);
                }
                graph.removeVertex(selectedVertexNode.getUnderlyingVertex());
                graphView.update();
                mainPane.deselectNodes();
            }

        });

        // edge text field
        edgeLabelTextField.disableProperty().bind(Bindings.not(isEdgeSelectedProperty));
        edgeLabelTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        edgeLabelTextField.setAlignment(Pos.CENTER);
        edgeLabelTextField.setPromptText("no edge selected");
        edgeLabel.setStyle("-fx-fill: gray");

        isEdgeSelectedProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                edgeLabelTextField.setText(SceneReference.getSelectedEdge().getAttachedLabel().getText());
                edgeLabel.setStyle(null);
            } else {
                edgeLabelTextField.setText("");
                edgeLabelTextField.setPromptText("no edge selected");
                edgeLabel.setStyle("-fx-fill: gray");
            }
        });

        edgeLabelTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                edgeId = SceneReference.getSelectedEdge().getUnderlyingEdge().getId();
            } else if (!confirmToApplyProperty.get()) {
                updateEdgeName();
            }
        });

        edgeLabelTextField.setOnAction(e -> {
            updateEdgeName();
        });

        // delete edge button
        deleteEdgeButton.disableProperty().bind(Bindings.not(isEdgeSelectedProperty));

        deleteEdgeButton.setOnAction(e -> {
            graph.removeEdge(SceneReference.getSelectedEdge().getUnderlyingEdge());
            graphView.update();
            SceneReference.deselectEdge();
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
        graph.replace(graph.vertexOf(lastVertexName), vertexLabelTextField.getText());
        Vertex<String> newVertex = graph.vertexOf(vertexLabelTextField.getText());
        if (newVertex == null) {
            return;
        }
        graphView.updateAndWait();
        mainPane.setSelectedVertexNode(graphView.getVertexNodeOf(newVertex));
        lastVertexName = new String(newVertex.element());
    }

    private void updateEdgeName() {
        if (edgeLabelTextField.getText().isBlank()) {
            return;
        }
        graph.replace(graph.getEdgeById(edgeId), edgeLabelTextField.getText());
        graphView.update();
    }
}
