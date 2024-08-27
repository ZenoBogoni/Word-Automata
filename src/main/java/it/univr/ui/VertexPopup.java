package it.univr.ui;

import java.io.IOException;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.utils.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class VertexPopup extends AnchorPane {
    private Stage stage;
    private static DigraphEdgeList<String, String> graph = SceneReference.getGraph();
    private static SmartGraphPanel<String, String> graphView = SceneReference.getGrapView();
    private static SmartGraphVertexNode<String> initialVertexNode = SceneReference.getInitialVertexNode();

    private static List<SmartGraphVertexNode<String>> finalVerticesNodes = SceneReference.getFinalVerticesNodes();
    private SmartGraphVertexNode<String> candidateVertex;
    private static boolean isThereAnInitialVertex = false;

    private String vertexName;

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField vertexNameField;
    @FXML
    private CheckBox checkBoxInitialVertex, checkBoxFinalVertex;

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
                graph.insertVertex(vertexName);
                graphView.updateAndWait();
                checkForNewSpecialVertex(vertexName);
                stage.close();
            }

        }
    }

    private void checkForNewSpecialVertex(String candidateVertexName) {
        candidateVertex = graphView.getVertexByName(graph.vertexOf(candidateVertexName));

        if (checkBoxFinalVertex.isSelected()) {
            finalVerticesNodes.add(candidateVertex);
        }

        if (checkBoxInitialVertex.isSelected() && !isThereAnInitialVertex) {
            initialVertexNode = candidateVertex;
            isThereAnInitialVertex = true;
        }
    }

    private void resetNameFieldWithErrorMessage(String error) {
        vertexNameField.setText("");
        vertexNameField.setPromptText(error);
    }

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        vertexNameField.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        checkBoxInitialVertex.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();

            eventDeselectOppositeCheckBox(checkBoxFinalVertex);
        });

        checkBoxFinalVertex.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();

            eventDeselectOppositeCheckBox(checkBoxInitialVertex);
        });

        cancelButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            stage.close();
        });
    }

    private void eventDeselectOppositeCheckBox(CheckBox oppositeCheckBox) {
        if (oppositeCheckBox.isSelected()) {
            oppositeCheckBox.setSelected(false);
        }
    }

}
