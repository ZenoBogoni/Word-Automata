package it.univr.ui;

import java.io.IOException;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.utils.SceneReference;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class VertexPopup extends AnchorPane {

    // JavaFX variables
    private Stage stage;
    private static DigraphEdgeList<String, String> graph = SceneReference.getGraph();
    private static SmartGraphPanel<String, String> graphView = SceneReference.getGrapView();

    // Java variables
    private static List<SmartGraphVertexNode<String>> finalVerticesNodes = SceneReference.getFinalVerticesNodes();
    private String vertexName;

    // properties
    private static SimpleBooleanProperty initialVertexSetProperty = SceneReference.getnitialVertexSetProperty();

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField vertexNameField;
    @FXML
    private CheckBox initialVertexCheckBox, finalVertexCheckBox;

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

    private void checkName() {

        vertexName = vertexNameField.getText();

        if (vertexName.equals("")) {
            resetNameFieldWithErrorMessage("Enter a valid vertex name");
        } else {
            if (graph.existsVertexWith(vertexName)) {
                resetNameFieldWithErrorMessage("Vertex name already taken");
            } else {
                Vertex<String> newVertex = graph.insertVertex(vertexName);
                graphView.updateAndWait();
                updateSpecialVertices(newVertex);
                stage.close();
            }
        }
    }

    private void updateSpecialVertices(Vertex<String> newVertex) {
        SmartGraphVertexNode<String> newVertexNode = graphView.getVertexByName(newVertex);
        if (finalVertexCheckBox.isSelected()) {
            finalVerticesNodes.add(newVertexNode);
        }
        if (initialVertexCheckBox.isSelected()) {
            SceneReference.setInitialVertexNode(newVertexNode);
        }
    }

    private void resetNameFieldWithErrorMessage(String error) {
        vertexNameField.setText("");
        vertexNameField.setPromptText(error);
    }

    @FXML
    public void initialize() {

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
            checkName();
        });

        vertexNameField.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        cancelButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            stage.close();
        });
    }

}
