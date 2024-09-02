package it.univr.utils;

import java.io.File;
import java.util.HashSet;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeBase;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.univr.ui.MainPane;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

public class SceneReference {
    // java variables
    private static boolean isEdgePressed = false;

    // Components
    private static Stage stage;
    private static MainPane mainPane;
    private static SmartGraphPanel<String, String> graphView;
    private static DigraphEdgeList<String, String> graph;
    private static SmartGraphVertexNode<String> initialVertexNode;
    private static HashSet<SmartGraphVertexNode<String>> finalVerticesNodes;
    private static SmartGraphEdgeBase<String, String> selectedEdge;

    // Properties
    private static SimpleBooleanProperty isVertexSelectedProperty;
    private static SimpleBooleanProperty confirmToApplyProperty;
    private static SimpleBooleanProperty autoLayoutProperty;
    private static SimpleBooleanProperty clearTextOnClickProperty;
    private static SimpleBooleanProperty initialVertexSetProperty;
    private static SimpleBooleanProperty isEdgeSelectedProperty = new SimpleBooleanProperty(false);

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Getters */
    /* -------------------------------------------------------------------------- */
    public static MainPane getMainPane() {
        return mainPane;
    }

    public static SmartGraphPanel<String, String> getGraphView() {
        return graphView;
    }

    public static SmartGraphVertexNode<String> getInitialVertexNode() {
        return initialVertexNode;
    }

    public static HashSet<SmartGraphVertexNode<String>> getFinalVerticesNodes() {
        return finalVerticesNodes;
    }

    public static Stage getStage() {
        return stage;
    }

    public static DigraphEdgeList<String, String> getGraph() {
        return graph;
    }

    public static SimpleBooleanProperty getConfirmToApplyProperty() {
        return confirmToApplyProperty;
    }

    public static SimpleBooleanProperty getClearTextOnClickProperty() {
        return clearTextOnClickProperty;
    }

    public static SimpleBooleanProperty getAutoLayoutProperty() {
        return autoLayoutProperty;
    }

    public static SimpleBooleanProperty getIsVertexSelectedProperty() {
        return isVertexSelectedProperty;
    }

    public static SimpleBooleanProperty getnitialVertexSetProperty() {
        return initialVertexSetProperty;
    }

    public static SmartGraphEdgeBase<String, String> getSelectedEdge() {
        return selectedEdge;
    }

    public static boolean isEdgePressed() {
        return isEdgePressed;
    }

    public static SimpleBooleanProperty getIsEdgeSelectedProperty() {
        return isEdgeSelectedProperty;
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Setters */
    /* -------------------------------------------------------------------------- */

    public static void setMainPane(MainPane pane) {
        mainPane = pane;
    }

    public static void setInitialVertexNode(SmartGraphVertexNode<String> initialVertexNode) {
        if (initialVertexNode == null) {
            SceneReference.initialVertexSetProperty.set(false);
            if (SceneReference.initialVertexNode != null) {
                SceneReference.initialVertexNode.removeStyleClass("initialVertex");
                SceneReference.initialVertexNode.getUnderlyingVertex().setInitial(false);
            }
            SceneReference.initialVertexNode = initialVertexNode;
        } else {
            SceneReference.initialVertexSetProperty.set(true);
            SceneReference.initialVertexNode = initialVertexNode;
            SceneReference.initialVertexNode.addStyleClass("initialVertex");
            initialVertexNode.getUnderlyingVertex().setInitial(true);
        }
    }

    public static void setFinalVerticesNodes(HashSet<SmartGraphVertexNode<String>> finalVerticesNodes) {
        SceneReference.finalVerticesNodes = finalVerticesNodes;
    }

    public static void setGraphView(SmartGraphPanel<String, String> grapView) {
        SceneReference.graphView = grapView;
    }

    public static void setStage(Stage stage) {
        SceneReference.stage = stage;
    }

    public static void setGraph(DigraphEdgeList<String, String> graph) {
        SceneReference.graph = graph;
    }

    public static void setConfirmToApplyProperty(SimpleBooleanProperty confirmToApplyProperty) {
        SceneReference.confirmToApplyProperty = confirmToApplyProperty;
    }

    public static void setClearTextOnClickProperty(SimpleBooleanProperty clearTextOnClickProperty) {
        SceneReference.clearTextOnClickProperty = clearTextOnClickProperty;
    }

    public static void setAutoLayoutProperty(SimpleBooleanProperty autoLayoutProperty) {
        SceneReference.autoLayoutProperty = autoLayoutProperty;
    }

    public static void setIsVertexSelectedProperty(SimpleBooleanProperty isVertexSelectedProperty) {
        SceneReference.isVertexSelectedProperty = isVertexSelectedProperty;
    }

    public static void setInitialVertexSetProperty(SimpleBooleanProperty initialVertexSetProperty) {
        SceneReference.initialVertexSetProperty = initialVertexSetProperty;
    }

    public static void setSelectedEdge(SmartGraphEdgeBase<String, String> selectedEdge) {
        if (SceneReference.selectedEdge != null) {
            if (SceneReference.selectedEdge.equals(selectedEdge)) {
                deselectEdge();
            } else {
                deselectEdge();
                SceneReference.selectedEdge = selectedEdge;
                SceneReference.selectedEdge.addStyleClass("selectedEdge");
                isEdgeSelectedProperty.set(true);
            }
        } else {
            if (selectedEdge != null) {
                SceneReference.selectedEdge = selectedEdge;
                SceneReference.selectedEdge.addStyleClass("selectedEdge");
                isEdgeSelectedProperty.set(true);
            }
        }
    }

    public static void setEdgePressed(boolean bool) {
        isEdgePressed = bool;
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Methods */
    /* -------------------------------------------------------------------------- */

    public static boolean addFinalvertex(SmartGraphVertexNode<String> vertexNode) {
        boolean didSomething = SceneReference.finalVerticesNodes.add(vertexNode);
        if (didSomething) {
            vertexNode.addStyleClass("finalVertex");
            vertexNode.getUnderlyingVertex().setFinal(true);
        }
        return didSomething;
    }

    public static boolean removeFinalVertex(SmartGraphVertexNode<String> vertexNode) {
        boolean didSomething = SceneReference.finalVerticesNodes.remove(vertexNode);
        if (didSomething) {
            vertexNode.removeStyleClass("finalVertex");
            vertexNode.getUnderlyingVertex().setFinal(false);
        }
        return didSomething;
    }

    public static void deselectEdge() {
        isEdgeSelectedProperty.set(false);
        if (SceneReference.selectedEdge != null) {
            SceneReference.selectedEdge.removeStyleClass("selectedEdge");
        }
        SceneReference.selectedEdge = null;
    }

    public static boolean isEdgeSelected() {
        return isEdgeSelectedProperty.get();
    }

    public static void createFileFromGraph(DigraphEdgeList<String, String> graph, String fileName) {
        GraphToFile graphToFile = new GraphToFile();
        graphToFile.setVertexList(graph.getMyVertexList());
        graphToFile.setEdgeList(graph.getMyEdgeList());
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(fileName + ".json"), graphToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteGraph(DigraphEdgeList<String, String> graph) {
        graph.vertices().forEach(vertex -> {
            graph.removeVertex(vertex);
        });
        SceneReference.graphView.update();
        SceneReference.getFinalVerticesNodes().clear();
        SceneReference.setInitialVertexNode(null);
    }

    public static void createGraphFromFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        GraphToFile graphToFile;
        try {
            graphToFile = mapper.readValue(new File(fileName + ".json"), GraphToFile.class);
            deleteGraph(graph);

            graphToFile.getVertexList().forEach(vertex -> {
                Vertex<String> newVertex = SceneReference.graph.insertVertex(vertex.getElement());
                SceneReference.graphView.updateAndWait();
                SmartGraphVertexNode<String> newVertexNode = SceneReference.graphView.getVertexNodeOf(newVertex);
                newVertexNode.setCenterX(vertex.getxPosition());
                newVertexNode.setCenterY(vertex.getyPosition());
                if (vertex.isFinalNode()) {
                    SceneReference.addFinalvertex(newVertexNode);
                } else if (vertex.isInitialNode()) {
                    SceneReference.setInitialVertexNode(newVertexNode);
                }
            });

            graphToFile.getEdgeList().forEach(edge -> {
                Vertex<String> inbound = SceneReference.graph.vertexOf(edge.getInbound());
                Vertex<String> outbound = SceneReference.graph.vertexOf(edge.getOutbound());
                SceneReference.graph.insertEdge(outbound, inbound, edge.getElement());
            });

            SceneReference.graphView.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
