package it.univr.ui;

import java.io.IOException;
import java.lang.String;
import java.util.Collection;

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

        FXMLLoader fxmlLoader = fxmlSetter();

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private FXMLLoader fxmlSetter() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("testGraphPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        return fxmlLoader;
    }

    private void checkName() {
        boolean isThereValidPath = false;
        testWord = graphTestWordNameField.getText();

        if (testWord.equals("")) {
            resetTestWordNameFieldWithErrorMessage("Enter a valid word");
        } else {

            MyVertexUnique initialVertexUnique = creatingInitialVertexUnique(); // and adding it to the supportGraph

            isThereValidPath = createGraphOfAllPossiblePaths(initialVertexUnique, 0);

            choosePath(isThereValidPath, initialVertexUnique);
        }
        stage.close();
    }

    private void choosePath(boolean isThereValidPath, MyVertexUnique initialVertexUnique) {
        if (!isThereValidPath) {
            supportGraph.removeVertex(initialVertexUnique);
        } else {
            greedyChoice(initialVertexUnique);
        }
    }

    private MyVertexUnique creatingInitialVertexUnique() {

        Vertex<String> initialVertex = SceneReference.getInitialVertexNode().getUnderlyingVertex();

        MyVertexUnique initialVertexUnique = supportGraph.insertVertex(numberOfVertices + "");
        numberOfVertices++;

        initialVertexUnique.setRealVertex(initialVertex);

        return initialVertexUnique;
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

    private boolean createGraphOfAllPossiblePaths(MyVertexUnique currentVertex, int pointerSubString) {

        boolean atLeastOnePathIsGood = false;

        Collection<Edge<String, String>> edges = graph.outboundEdges((Vertex<String>) currentVertex.getRealVertex());

        if (!edges.isEmpty()) {

            for (Edge<String, String> edge : edges) {

                pointer = 0;

                Vertex<String> nextVertex = ((MyEdge) edge).getInbound();

                if (testWord.substring(pointerSubString).length() != 0) {

                    pointer = compareStrings(edge.element(), testWord.substring(pointerSubString));

                } else {

                    if (currentVertex.getRealVertex().isFinal()) {
                        atLeastOnePathIsGood = true;
                    }
                }

                if (pointer == edge.element().length()) {
                    MyVertexUnique nextVertexUnique = supportGraph.insertVertex(numberOfVertices + "");
                    numberOfVertices++;

                    nextVertexUnique.setRealVertex(nextVertex);

                    MyEdgeUnique edgeUnique = supportGraph.insertEdge(currentVertex, nextVertexUnique, edge.element());

                    boolean isThisPathGood = createGraphOfAllPossiblePaths(nextVertexUnique, pointer + pointerSubString);

                    if (isThisPathGood) {
                        atLeastOnePathIsGood = true;
                    } else {
                        supportGraph.removeEdge(edgeUnique);
                        supportGraph.removeVertex(nextVertexUnique);
                    }

                }
            }

        } else {

            if (testWord.substring(pointerSubString).length() == 0) {
                if (currentVertex.getRealVertex().isFinal()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        return atLeastOnePathIsGood;
    }

    private int compareStrings(String firstString, String secondString) {
        int counterCharactersCompatible = 0;

        for (int i = 0; i < secondString.length() && i < firstString.length(); i++) {
            if (secondString.charAt(i) == firstString.charAt(i)) {
                counterCharactersCompatible++;
            } else {
                break;
            }
        }

        return counterCharactersCompatible;
    }

    private void greedyChoice(MyVertexUnique vertex) {

        Collection<Edge<String, String>> edges = supportGraph.outboundEdgesUnique(vertex);
        Edge<String, String> edgeWithLongestElement = returnEdgeWithLongestElement(edges);

        System.out.println(" -> " + vertex);

        if (edgeWithLongestElement != null) {
            greedyChoice(((MyEdgeUnique) edgeWithLongestElement).getInboundUnique());
        } else {
            System.out.println(" ==== > Hai finito < ====\n\n");
        }
    }

    private Edge<String, String> returnEdgeWithLongestElement(Collection<Edge<String, String>> edges) {
        int maxLength = 0;
        Edge<String, String> max = null;

        for (Edge<String, String> edge : edges) {
            if (edge.element().length() > maxLength) {
                maxLength = edge.element().length();
                max = edge;
            }
        }

        return max;
    }
}
