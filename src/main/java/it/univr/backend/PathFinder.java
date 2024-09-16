package it.univr.backend;

import java.util.Collection;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList.MyEdge;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyEdgeUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyVertexUnique;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeBase;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class PathFinder {

    private static DigraphEdgeListUnique<String, String> supportGraph;
    private static int numberOfVertices;

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField graphTestWordNameField;

    public static void getPaths() {
        SceneReference.getSolutionPane().clearHistory();

        try {
            supportGraph = new DigraphEdgeListUnique<>();
            numberOfVertices = 0;

            MyVertexUnique initialVertexUnique = creatingVertexUnique(SceneReference.getInitialVertexNode().getUnderlyingVertex());
            boolean isThereValidPath = createGraphOfAllPossiblePaths(initialVertexUnique, 0);

            choosePath(isThereValidPath, initialVertexUnique);
        } catch (Exception e) {
            // Gestione delle eccezioni
            e.printStackTrace();
        }
    }

    private static MyVertexUnique creatingVertexUnique(Vertex<String> Vertex) {

        MyVertexUnique VertexUnique = supportGraph.insertVertex(numberOfVertices + "");
        numberOfVertices++;

        VertexUnique.setRealVertex(Vertex);

        return VertexUnique;
    }

    private static boolean createGraphOfAllPossiblePaths(MyVertexUnique currentVertex, int pointerSubString) {
        int pointer = 0;
        String testWord = SceneReference.getTestWord();
        DigraphEdgeList<String, String> graph = SceneReference.getGraph();
        boolean atLeastOnePathIsGood = false;
        Collection<Edge<String, String>> edges = graph.outboundEdges((Vertex<String>) currentVertex.getRealVertex());
        int testWordSubStringLength = testWord.substring(pointerSubString).length();

        if (!edges.isEmpty()) {

            for (Edge<String, String> edge : edges) {

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

    private static void choosePath(boolean isThereValidPath, MyVertexUnique initialVertexUnique) {
        if (!isThereValidPath) {
            supportGraph.removeVertex(initialVertexUnique);
            SceneReference.showErrorPopup("No valid path found", "This automata couldn't find a valid path for the word \"" + SceneReference.getTestWord() +
                    "\".\nPlease try with another word or check if the final and initial vertices are set correctly.");
        } else {
            greedyChoice(initialVertexUnique);
        }
    }

    private static void greedyChoice(MyVertexUnique vertex) {

        SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();
        DigraphEdgeList<String, String> graph = SceneReference.getGraph();
        Collection<Edge<String, String>> edges = supportGraph.outboundEdgesUnique(vertex);
        Edge<String, String> edgeWithLongestElement = returnEdgeWithLongestElement(edges);

        SceneReference.colorVertexAfterTime(750, vertex);
        SceneReference.clearVertexAfterTime(2250, vertex);

        if (edgeWithLongestElement != null) {

            PauseTransition pause = new PauseTransition(Duration.millis(1500));
            SceneReference.addAnimation(pause);
            pause.setOnFinished(e -> {
                Vertex<String> inbound = ((MyEdgeUnique) edgeWithLongestElement).getInboundUnique().getRealVertex();
                Vertex<String> outbound = ((MyEdgeUnique) edgeWithLongestElement).getOutboundUnique().getRealVertex();
                Edge<String, String> currentEdge = graph.outboundEdges(outbound).stream()
                        .filter(edgeFilter -> ((MyEdge) edgeFilter).getInbound().equals(inbound) && edgeFilter.element() == edgeWithLongestElement.element())
                        .findFirst().orElse(null);
                SmartGraphEdgeBase<String, String> currentEdgeNode = graphView.getEdgeNodeOf(currentEdge);
                currentEdgeNode.addStyleClass("pathEdge");

                SceneReference.getSolutionPane().insertEdgeNode(currentEdgeNode);
                greedyChoice(((MyEdgeUnique) edgeWithLongestElement).getInboundUnique());
                SceneReference.removeAnimation(pause);
            });

            pause.play();

            SceneReference.clearEdgeAfterTime(3000, edgeWithLongestElement);

        }
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
