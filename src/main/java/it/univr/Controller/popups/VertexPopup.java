package it.univr.Controller.popups;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.backend.SceneReference;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VertexPopup extends AnchorPane {

    // JavaFX variables
    private Stage stage;
    private static DigraphEdgeList<String, String> graph = SceneReference.getGraph();
    private static SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();

    // Java variables
    private String vertexName;
    public static int count = 0;

    // properties
    private static SimpleBooleanProperty initialVertexSetProperty = SceneReference.getnitialVertexSetProperty();

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField vertexNameField;
    @FXML
    private CheckBox initialVertexCheckBox, finalVertexCheckBox;
    @FXML
    private Text errorText;

    public VertexPopup() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vertexPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void addVertex() {

        vertexName = vertexNameField.getText();

        if (vertexName.equals("")) {
            errorText.setText("Enter a non blank vertex name");
            vertexNameField.setText("");
        } else {
            if (graph.existsVertexWith(vertexName)) {
                errorText.setText("A vertex with this name already esists");
                vertexNameField.setText("");
            } else {
                SceneReference.stopAllAnimations();
                errorText.setText("");
                Vertex<String> newVertex = graph.insertVertex(vertexName);
                graphView.updateAndWait();
                SmartGraphVertexNode<String> newVertexNode = graphView.getVertexNodeOf(newVertex);
                newVertexNode.setCenterX(SceneReference.getMouseX());
                newVertexNode.setCenterY(SceneReference.getMouseY());
                updateSpecialVertices(newVertexNode);
                SceneReference.setSelectedVertexNode(newVertexNode);
                SceneReference.setUnsavedChanges(true);
                stage.close();
            }
        }
    }

    private void updateSpecialVertices(SmartGraphVertexNode<String> newVertexNode) {
        if (finalVertexCheckBox.isSelected()) {
            SceneReference.addFinalvertex(newVertexNode);
        } else if (initialVertexCheckBox.isSelected()) {
            SceneReference.setInitialVertexNode(newVertexNode);
        }
    }

    @FXML
    public void initialize() {
        while (graph.vertexOf("vertex " + count) != null) {
            count++;
        }
        vertexNameField.setText("vertex " + count);
        initialVertexCheckBox.setSelected(!initialVertexSetProperty.get()); // if an initial vertex is not set set this as initial by default
        initialVertexCheckBox.disableProperty().bind(initialVertexSetProperty); // disable checkBox if an initial Vertex is already set

        initialVertexCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                finalVertexCheckBox.setSelected(false);
            }
        });

        finalVertexCheckBox.selectedProperty().addListener((obsevable, oldValue, newValue) -> {
            if (newValue) {
                initialVertexCheckBox.setSelected(false);
            }
        });

        submitButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            addVertex();
        });

        vertexNameField.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            addVertex();
        });

        cancelButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            stage.close();
        });
    }
}
