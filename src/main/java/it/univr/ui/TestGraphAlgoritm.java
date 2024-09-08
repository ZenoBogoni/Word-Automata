package it.univr.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList.MyEdge;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList.MyVertex;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyEdgeUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyVertexUnique;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeBase;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.utils.SceneReference;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class TestGraphAlgoritm {

    // JavaFX variables
    private static DigraphEdgeList<String, String> graph;
    private static DigraphEdgeListUnique<String, String> supportGraph;
    private static SmartGraphPanel<String, String> graphView;
    private static int numberOfVertices;
    private static String testWord;
    private static int pointer;

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField graphTestWordNameField;

    public static void testGraph() {
        SceneReference.getSolutionPane().clearHistory();
        try {
            initialize();

            MyVertexUnique initialVertexUnique = creatingVertexUnique(SceneReference.getInitialVertexNode().getUnderlyingVertex());
            boolean isThereValidPath = createGraphOfAllPossiblePaths(initialVertexUnique, 0);

            choosePath(isThereValidPath, initialVertexUnique);
        } catch (Exception e) {
            // Gestione delle eccezioni
            e.printStackTrace();
        }
    }

    private static void initialize() {
        graphView = SceneReference.getGraphView();
        graph = SceneReference.getGraph();
        testWord = SceneReference.getTestWord();

        supportGraph = new DigraphEdgeListUnique<>();
        pointer = 0;
        numberOfVertices = 0;
    }

    private static void choosePath(boolean isThereValidPath, MyVertexUnique initialVertexUnique) {
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
    private static MyVertexUnique creatingVertexUnique(Vertex<String> Vertex) {

        MyVertexUnique VertexUnique = supportGraph.insertVertex(numberOfVertices + "");
        numberOfVertices++;

        VertexUnique.setRealVertex(Vertex);

        return VertexUnique;
    }

    private static boolean createGraphOfAllPossiblePaths(MyVertexUnique currentVertex, int pointerSubString) {

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
                return currentVertex.getRealVertex().isFinal();
            } else {
                return false;
            }
        }

        return atLeastOnePathIsGood;
    }

    private static int compareStrings(String firstString, String secondString) {
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

    private static void greedyChoice(MyVertexUnique vertex) {

        Collection<Edge<String, String>> edges = supportGraph.outboundEdgesUnique(vertex);
        Edge<String, String> edgeWithLongestElement = returnEdgeWithLongestElement(edges);

        colorVertexAfterTime(750, vertex);
        clearVertexAfterTime(2250, vertex);

        if (edgeWithLongestElement != null) {

            colorEdgeAfterTime(1500, edgeWithLongestElement);
            clearEdgeAfterTime(3000, edgeWithLongestElement);

        }
    }

    private static void colorVertexAfterTime(int milliseconds, MyVertexUnique vertex) {

        PauseTransition pause = new PauseTransition(Duration.millis(750));
        pause.setOnFinished(e -> {
            SmartGraphVertexNode<String> currentVertexNode = graphView.getVertexNodeOf(vertex.getRealVertex());
            currentVertexNode.addStyleClass("pathVertex");
            SceneReference.getSolutionPane().insertVertexNode(currentVertexNode);
        });

        pause.play();
    }

    private static void colorEdgeAfterTime(int milliseconds, Edge<String, String> edge) {

        PauseTransition pause = new PauseTransition(Duration.millis(milliseconds));
        pause.setOnFinished(e -> {
            Vertex<String> inbound = ((MyEdgeUnique) edge).getInboundUnique().getRealVertex();
            Vertex<String> outbound = ((MyEdgeUnique) edge).getOutboundUnique().getRealVertex();
            Edge<String, String> currentEdge = graph.outboundEdges(outbound).stream().filter(edgeFilter -> ((MyEdge) edgeFilter).getInbound().equals(inbound)).findFirst().orElse(null);
            SmartGraphEdgeBase<String, String> currentEdgeNode = graphView.getEdgeNodeOf(currentEdge);
            currentEdgeNode.addStyleClass("pathEdge");

            greedyChoice(((MyEdgeUnique) edge).getInboundUnique());
            SceneReference.getSolutionPane().insertEdgeNode(currentEdgeNode);
        });

        pause.play();
    }

    private static void clearVertexAfterTime(int milliseconds, MyVertexUnique vertex) {

        PauseTransition pause = new PauseTransition(Duration.millis(milliseconds));
        pause.setOnFinished(e -> {
            clearVertex(vertex);
        });
        pause.play();
    }

    private static void clearEdgeAfterTime(int milliseconds, Edge<String, String> edge) {
        PauseTransition pause = new PauseTransition(Duration.millis(milliseconds));
        pause.setOnFinished(e -> {
            Vertex<String> inbound = ((MyEdgeUnique) edge).getInboundUnique().getRealVertex();
            Vertex<String> outbound = ((MyEdgeUnique) edge).getOutboundUnique().getRealVertex();
            Edge<String, String> currentEdge = graph.outboundEdges(outbound).stream().filter(edgeFilter -> ((MyEdge) edgeFilter).getInbound().equals(inbound)).findFirst().orElse(null);
            clearEdge(currentEdge);
        });
        pause.play();
    }

    private static void clearVertex(MyVertexUnique vertex) {
        graphView.getVertexNodeOf(vertex.getRealVertex()).removeStyleClass("pathVertex");
    }

    private static void clearEdge(Edge edge) {
        graphView.getEdgeNodeOf(edge).removeStyleClass("pathEdge");
    }

    private static Edge<String, String> returnEdgeWithLongestElement(Collection<Edge<String, String>> edges) {
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
