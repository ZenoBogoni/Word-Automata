package it.univr.ui;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
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

public class TestGraphPopup extends AnchorPane {

    // JavaFX variables
    private Stage stage;
    private static DigraphEdgeList<String, String> graph = SceneReference.getGraph();
    private static DigraphEdgeList<String, String> newGraph;
    private static SmartGraphPanel<String, String> graphView = SceneReference.getGrapView();
    private int pointer;
    private String testWord;

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField graphTestWordNameField;

    public TestGraphPopup() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("testGraphPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void checkName() {

        testWord = graphTestWordNameField.getText();

        if (testWord.equals("")) {
            resetTestWordNameFieldWithErrorMessage("Enter a valid word");
        } else {
            Vertex<String> initialVertex = SceneReference.getInitialVertexNode().getUnderlyingVertex();
            testGraphWithWord(initialVertex);
        }
        stage.close();
    }

    private void resetTestWordNameFieldWithErrorMessage(String error) {
        graphTestWordNameField.setText("");
        graphTestWordNameField.setPromptText(error);
    }

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        graphTestWordNameField.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        cancelButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            stage.close();
        });
    }

    private void testGraphWithWord(Vertex<String> vertex) {
        for (Edge<String, String> edge : graph.outboundEdges(vertex)) {
            pointer = edge.element().compareTo(testWord);

            if (pointer != 0) {
                // newGraph.insertVertex()
                // testGraphWithWord();

            }

        }
    }

}
