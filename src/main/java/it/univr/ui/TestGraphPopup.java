package it.univr.ui;

import java.io.IOException;
import java.lang.String;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList.MyEdge;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyEdgeUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyVertexUnique;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import it.univr.utils.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class TestGraphPopup extends AnchorPane {

    // JavaFX variables
    private Stage stage;
    private static DigraphEdgeList<String, String> graph = SceneReference.getGraph();
    private DigraphEdgeListUnique<String, String> supportGraph = new DigraphEdgeListUnique<>();
    private static SmartGraphPanel<String, String> graphView = SceneReference.getGrapView();
    private int numberOfVertices = 0;
    private String testWord;
    private int pointer;

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
            MyVertexUnique firstUniqueVertex = supportGraph.insertVertex(numberOfVertices + "");
            numberOfVertices++;

            firstUniqueVertex.setRealVertex(initialVertex);

            boolean path;
            testGraphWithWord(firstUniqueVertex, 0);
            // if (path) {
            // supportGraph.removeVertex(firstUniqueVertex);
            // }
            System.out.println(supportGraph);

            // removeWrongPaths();
            // greedyChoice(firstUniqueVertex);
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

    private void testGraphWithWord(MyVertexUnique vertex, int pointerSubString) {

        // boolean atLeastOnePathIsGood = false;

        for (Edge<String, String> edge : graph.outboundEdges((Vertex<String>) vertex.getRealVertex())) {
            pointer = 0;

            Vertex<String> nextVertex = ((MyEdge) edge).getInbound();

            if (testWord.substring(pointerSubString) != null) {
                pointer = compare(edge.element(), testWord.substring(pointerSubString));
                System.out.println(pointer);
            } else {
                // if (vertex.getRealVertex().isFinal()) {
                // return true;
                // } else {
                // return false;
                // }
            }

            if (pointer == edge.element().length()) {
                MyVertexUnique uniqueVertex = supportGraph.insertVertex(numberOfVertices + "");
                numberOfVertices++;

                uniqueVertex.setRealVertex(nextVertex);

                supportGraph.insertEdge(vertex, uniqueVertex, edge.element());

                boolean isThisPathGood;
                testGraphWithWord(uniqueVertex, pointer + pointerSubString);

                // if (isThisPathGood) {
                // atLeastOnePathIsGood = true;
                // } else {
                // supportGraph.removeEdge(edge);
                // supportGraph.removeVertex(uniqueVertex);
                // }

            } else {
                // return false;
            }
        }

        // return atLeastOnePathIsGood;
    }

    private int compare(String first, String second) {
        int count = 0;

        for (int i = 0; i < second.length() && i < first.length(); i++) {
            if (second.charAt(i) == first.charAt(i)) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

    private void greedyChoice(MyVertexUnique root) {
        int maxLength = 0;
        Edge<String, String> max = null;

        for (Edge<String, String> edge : graph.outboundEdges((Vertex<String>) (root.getRealVertex()))) {
            if (edge.element().length() > maxLength) {
                maxLength = edge.element().length();
                max = edge;
            }
        }

        MyVertexUnique nextVertex = ((MyEdgeUnique) max).getInboundUnique();

        if (nextVertex.getRealVertex() != null) {
            greedyChoice(nextVertex);
        } else {
            if (root.isFinal()) {

            }
        }

    }

    // private boolean removeWrongPaths() {

    // }

}
