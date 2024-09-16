package it.univr.Controller.panes;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.Controller.popups.ConfirmPopup;
import it.univr.backend.PathFinder;
import it.univr.backend.SceneReference;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
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
    private TextField vertexLabelTextField, edgeLabelTextField, testWordTextField;
    @FXML
    private Button deleteVertexButton, deleteEdgeButton, testWordButton, destroyButton;
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

        // disable when vertex is not selected
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
            SceneReference.stopAllAnimations();
            if (initialNodeRadioButton.isSelected()) {
                if (selectedVertexNode.getUnderlyingVertex().isFinal()) {
                    finalNodeRadioButton.setSelected(false);
                    SceneReference.removeFinalVertex(selectedVertexNode);
                }
                SceneReference.setInitialVertexNode(selectedVertexNode);
                SceneReference.setUnsavedChanges(true);
            } else {
                SceneReference.setInitialVertexNode(null);
                SceneReference.setUnsavedChanges(true);
            }
        });

        finalNodeRadioButton.setOnAction(e -> {
            SceneReference.stopAllAnimations();
            if (finalNodeRadioButton.isSelected()) {
                if (selectedVertexNode.getUnderlyingVertex().isInitial()) {
                    initialNodeRadioButton.setSelected(false);
                    SceneReference.setInitialVertexNode(null);
                }
                SceneReference.addFinalvertex(selectedVertexNode);
                SceneReference.setUnsavedChanges(true);
            } else {
                SceneReference.removeFinalVertex(selectedVertexNode);
                SceneReference.setUnsavedChanges(true);
            }
        });

        // vertex text field
        vertexLabelTextField.disableProperty().bind(Bindings.not(isVertexSelectedProperty));
        vertexLabelTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        vertexLabelTextField.setAlignment(Pos.CENTER);
        if (lastVertexName == null) {
            vertexLabelTextField.setPromptText("no vertex selected");
            vertexLabel.setStyle("-fx-fill: gray");
        }

        vertexLabelTextField.disableProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vertexLabelTextField.setText("");
                vertexLabelTextField.setPromptText("no vertex selected");
                vertexLabel.setStyle("-fx-fill: gray");
            } else {
                selectedVertexNode = SceneReference.getSelectedVertexNode();
                vertexLabelTextField.setText(selectedVertexNode.getAttachedLabel().getText());
                vertexLabelTextField.setPromptText("");
                lastVertexName = (lastVertexName == null) ? new String(vertexLabelTextField.getText()) : vertexLabelTextField.getText();
                vertexLabel.setStyle(null);
            }
        });

        vertexLabelTextField.setOnAction(e -> {
            if (!vertexLabelTextField.getText().equals(lastVertexName) && confirmToApplyProperty.get()) {
                updateVertexName();
            } else {
                vertexLabelTextField.getParent().requestFocus();
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
                SceneReference.stopAllAnimations();
                if (selectedVertexNode.equals(SceneReference.getInitialVertexNode())) {
                    SceneReference.setInitialVertexNode(null);
                } else if (SceneReference.getFinalVerticesNodes().contains(selectedVertexNode)) {
                    SceneReference.removeFinalVertex(selectedVertexNode);
                }
                graph.removeVertex(selectedVertexNode.getUnderlyingVertex());
                graphView.update();
                SceneReference.deselectNodes();
                SceneReference.setUnsavedChanges(true);
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
                edgeLabelTextField.setPromptText("");
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
            } else if (!confirmToApplyProperty.get() && !edgeLabelTextField.getText().equals(SceneReference.getSelectedEdge().getAttachedLabel().getText())) {
                updateEdgeName();
            }
        });

        edgeLabelTextField.setOnAction(e -> {
            if (confirmToApplyProperty.get() && !edgeLabelTextField.getText().equals(SceneReference.getSelectedEdge().getAttachedLabel().getText())) {
                updateEdgeName();
            } else {
                edgeLabelTextField.getParent().requestFocus();
            }
        });

        // delete edge button
        deleteEdgeButton.disableProperty().bind(Bindings.not(isEdgeSelectedProperty));

        deleteEdgeButton.setOnAction(e -> {
            SceneReference.stopAllAnimations();
            graph.removeEdge(SceneReference.getSelectedEdge().getUnderlyingEdge());
            graphView.update();
            SceneReference.deselectEdge();
            SceneReference.setUnsavedChanges(true);
        });

        // Automata testing
        testWordTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        testWordTextField.setAlignment(Pos.CENTER);
        testWordButton.setOnAction(e -> testGraph());
        testWordTextField.setOnAction(e -> testGraph());

        // delete automata
        destroyButton.setOnAction(e -> {
            SceneReference.createModal(new ConfirmPopup(
                    "Are you sure you want to destroy this Automata?",
                    "By clicking confirm you will completely delete ALL the vertices and edges of the current automata",
                    () -> SceneReference.deleteGraph(graph)));
            SceneReference.setUnsavedChanges(true);
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
            vertexLabelTextField.setText(lastVertexName);
            SceneReference.showErrorPopup("Invalid vertex name", "It's not possible to rename a vertex with a blank name, please insert a valid name.");
            return;
        }
        String success = graph.replace(graph.vertexOf(lastVertexName), vertexLabelTextField.getText());
        if (success == null) {
            SceneReference.showErrorPopup("Invalid vertex name", "A vertex with this name already exists, please insert a unique vertex name.");
            return;
        }
        Vertex<String> newVertex = graph.vertexOf(vertexLabelTextField.getText());
        if (newVertex == null) {
            return;
        }
        SceneReference.stopAllAnimations();
        graphView.updateAndWait();
        // mainPane.setSelectedVertexNode(graphView.getVertexNodeOf(newVertex));
        lastVertexName = new String(newVertex.element());
        SceneReference.setUnsavedChanges(true);
    }

    private void updateEdgeName() {
        if (edgeLabelTextField.getText().isBlank()) {
            edgeLabelTextField.setText(SceneReference.getSelectedEdge().getAttachedLabel().getText());
            SceneReference.showErrorPopup("Invalid edge name", "It's not possible to rename an edge with a blank name, please insert a valid name.");
            return;
        }
        String succes = graph.replace(graph.getEdgeById(edgeId), edgeLabelTextField.getText());
        if (succes == null) {
            SceneReference.showErrorPopup("Invalid edge name", "This vertex already has an outgoing edge with the same name");
            edgeLabelTextField.setText("");
            return;
        }
        SceneReference.stopAllAnimations();
        graphView.update();
        SceneReference.setUnsavedChanges(true);
    }

    private void testGraph() {
        if (!initialvertexSetProperty.get()) {
            SceneReference.showErrorPopup("Initial vertex not set", "Initial vertex not set, please set an initial vertex to start the testing from.");
        } else if (SceneReference.getFinalVerticesNodes().size() == 0) {
            SceneReference.showErrorPopup("Final vertex not set", "Final vertex not set, please select at least one final vertex.");
        } else if (testWordTextField.getText().equals("")) {
            SceneReference.showErrorPopup("Invalid testing word", "It's not possible to test the automata with an empty testing word, please insert a valid testing word.");
        } else {
            SceneReference.setTestWord(testWordTextField.getText());
            SceneReference.stopAllAnimations();
            PathFinder.getPaths();
        }
    }

    public void focusEdgeField() {
        edgeLabelTextField.requestFocus();
        edgeLabelTextField.selectAll();
    }

    public void focusVertexField() {
        vertexLabelTextField.requestFocus();
        vertexLabelTextField.selectAll();
    }
}
