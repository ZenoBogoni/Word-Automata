package it.univr.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList.MyEdge;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyEdgeUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyVertexUnique;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeBase;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

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
    private static SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();
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

    private void checkName() {// TODO: handle exception
        testWord = graphTestWordNameField.getText();

        if (testWord.equals("")) {
            resetTestWordNameFieldWithErrorMessage("Enter a valid word");
        } else {

            MyVertexUnique initialVertexUnique = creatingVertexUnique(SceneReference.getInitialVertexNode().getUnderlyingVertex()); // and adding it to the supportGraph

            boolean isThereValidPath = createGraphOfAllPossiblePaths(initialVertexUnique, 0);

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

    /*
     * Creates a MyVertexUnique, adds it to the Support Graph
     * and set as real Vertex the argument vertex
     */
    private MyVertexUnique creatingVertexUnique(Vertex<String> Vertex) {

        MyVertexUnique VertexUnique = supportGraph.insertVertex(numberOfVertices + "");
        numberOfVertices++;

        VertexUnique.setRealVertex(Vertex);

        return VertexUnique;
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
            SceneReference.createFileFromGraph(graph, "test");
        });

        graphTestWordNameField.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        cancelButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            SceneReference.createGraphFromFile("test");
            stage.close();
        });
    }

    private boolean createGraphOfAllPossiblePaths(MyVertexUnique currentVertex, int pointerSubString) {

        boolean atLeastOnePathIsGood = false;

        Collection<Edge<String, String>> edges = graph.outboundEdges((Vertex<String>) currentVertex.getRealVertex());
        int testWordSubStringLength = testWord.substring(pointerSubString).length();

        if (!edges.isEmpty()) {

            for (Edge<String, String> edge : edges) {
                pointer = 0;

                Vertex<String> nextVertex = ((MyEdge) edge).getInbound();

                if (testWordSubStringLength != 0) {
                    pointer = compareStrings(edge.element(), testWord.substring(pointerSubString));
                } else {
                    atLeastOnePathIsGood = currentVertex.getRealVertex().isFinal();
                }

                if (pointer == edge.element().length()) {

                    MyVertexUnique nextVertexUnique = creatingVertexUnique(nextVertex);

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

            if (testWordSubStringLength == 0) {
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

        Timer timerVertex = new Timer();
        timerVertex.schedule(new TimerTask() {
            @Override
            public void run() {
                SmartGraphVertexNode<String> currentVertexNode = graphView.getVertexNodeOf(vertex.getRealVertex());
                currentVertexNode.addStyleClass("pathVertex");
                timerVertex.cancel(); // Stop the timer
            }
        }, 750);

        if (edgeWithLongestElement != null) {

            Timer timerEdge = new Timer();
            timerEdge.schedule(new TimerTask() {
                @Override
                public void run() {
                    Vertex<String> inbound = ((MyEdgeUnique) edgeWithLongestElement).getInboundUnique().getRealVertex();
                    Vertex<String> outbound = ((MyEdgeUnique) edgeWithLongestElement).getOutboundUnique().getRealVertex();
                    Edge<String, String> currentEdge = graph.outboundEdges(outbound).stream().filter(edge -> ((MyEdge) edge).getInbound().equals(inbound)).findFirst().orElse(null);
                    SmartGraphEdgeBase<String, String> currentEdgeNode = graphView.getEdgeNodeOf(currentEdge);
                    currentEdgeNode.addStyleClass("pathEdge");

                    greedyChoice(((MyEdgeUnique) edgeWithLongestElement).getInboundUnique());
                    timerEdge.cancel(); // Stop the timer
                }
            }, 1500);
        } else {
            Timer timerClear = new Timer();
            timerClear.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Clearing path");
                    clearPath();
                    timerClear.cancel(); // Stop the timer
                }
            }, 2000);
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

    private void clearPath() {
        graph.vertices().forEach(vertex -> {
            graphView.getVertexNodeOf(vertex).removeStyleClass("pathVertex");
        });

        graph.edges().forEach(edge -> {
            graphView.getEdgeNodeOf(edge).removeStyleClass("pathEdge");
        });
    }

}
