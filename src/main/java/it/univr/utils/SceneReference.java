package it.univr.utils;

import java.io.File;
import java.util.HashSet;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList.MyEdge;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyEdgeUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyVertexUnique;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeBase;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.univr.App;
import it.univr.Controller.panes.GraphSidePane;
import it.univr.Controller.panes.MainPane;
import it.univr.Controller.panes.SolutionPane;
import it.univr.Controller.popups.ErrorPopup;
import it.univr.backend.GraphToFile;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SceneReference {
    // java variables
    private static boolean isEdgePressed = false;
    private static boolean unsavedChanges = false;

    // Components
    private static Application app;
    private static Stage stage;
    private static MainPane mainPane;
    private static SmartGraphPanel<String, String> graphView;
    private static DigraphEdgeList<String, String> graph;
    private static SmartGraphVertexNode<String> initialVertexNode;
    private static HashSet<SmartGraphVertexNode<String>> finalVerticesNodes;
    private static SmartGraphEdgeBase<String, String> selectedEdge;
    private static SolutionPane solutionPane;
    private static GraphSidePane graphSidePane;

    // Properties
    private static SimpleBooleanProperty isVertexSelectedProperty;
    private static SimpleBooleanProperty confirmToApplyProperty;
    private static SimpleBooleanProperty autoLayoutProperty;
    private static SimpleBooleanProperty clearTextOnClickProperty;
    private static SimpleBooleanProperty initialVertexSetProperty;
    private static SimpleBooleanProperty isEdgeSelectedProperty = new SimpleBooleanProperty(false);
    private static SimpleStringProperty fileNameProperty = new SimpleStringProperty("Word Automata - unnamed Automata");

    private static String testWord;

    private SceneReference() {
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Getters */
    /* -------------------------------------------------------------------------- */
    public static MainPane getMainPane() {
        return mainPane;
    }

    public static String getTestWord() {
        return testWord;
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

    public static boolean getUnsavedChanges() {
        return unsavedChanges;
    }

    public static SimpleStringProperty getFileNameProperty() {
        return fileNameProperty;
    }

    public static Application getApp() {
        return app;
    }

    public static SolutionPane getSolutionPane() {
        return solutionPane;
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Setters */
    /* -------------------------------------------------------------------------- */

    public static void setMainPane(MainPane pane) {
        mainPane = pane;
    }

    public static void setTestWord(String word) {
        SceneReference.testWord = word;
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
                graphSidePane.focusEdgeField();
            }
        } else {
            if (selectedEdge != null) {
                SceneReference.selectedEdge = selectedEdge;
                SceneReference.selectedEdge.addStyleClass("selectedEdge");
                isEdgeSelectedProperty.set(true);
                graphSidePane.focusEdgeField();
            }
        }
    }

    public static void setEdgePressed(boolean bool) {
        isEdgePressed = bool;
    }

    public static void setUnsavedChanges(boolean bool) {
        unsavedChanges = bool;
    }

    public static void setFileName(String fileName) {
        fileNameProperty.set("Word Automata - " + fileName);
    }

    public static void setApp(Application app) {
        SceneReference.app = app;
    }

    public static void setSolutionPane(SolutionPane solutionPane) {
        SceneReference.solutionPane = solutionPane;
    }

    public static void setGraphSidePane(GraphSidePane graphSidePane) {
        SceneReference.graphSidePane = graphSidePane;
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
            mapper.writeValue(new File(fileName), graphToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteGraph(DigraphEdgeList<String, String> graph) {
        graph.vertices().forEach(vertex -> {
            graph.removeVertex(vertex);
        });
        SceneReference.graphView.update();
        SceneReference.getSolutionPane().clearHistory();
        SceneReference.getFinalVerticesNodes().clear();
        SceneReference.setInitialVertexNode(null);
    }

    public static void createGraphFromFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        GraphToFile graphToFile;
        try {
            graphToFile = mapper.readValue(new File(fileName), GraphToFile.class);
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
            SceneReference.showErrorPopup("File not compatible", fileName + "\ndoes not contain a valid Automata.");
            e.printStackTrace();
        }
    }

    /**
     * Create a modal window popup
     * 
     * @param root the popup component
     */
    public static void createModal(Parent root) {
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        // Scene Style
        if (App.isDarkMode()) {
            applyDarkStyleSheet(scene);
        } else {
            applyLightStyleSheet(scene);
        }
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);

        stage.setOnShown(event -> {
            // Size the stage to the scene
            stage.sizeToScene();

            // Get the width and height after sizing the stage
            double width = stage.getWidth();
            double height = stage.getHeight();

            // Put this window in the middle of the primary window
            Stage primaryStage = SceneReference.getStage();
            double centerX = primaryStage.getX() + (primaryStage.getWidth() - width) / 2;
            double centerY = primaryStage.getY() + (primaryStage.getHeight() - height) / 2;
            stage.setX(centerX);
            stage.setY(centerY);
        });

        stage.show();
    }

    /**
     * Shows an error message popup
     * 
     * @param errorMsg the error message to display to th user
     */
    public static void showErrorPopup(String errorType, String errorMsg) {
        createModal(new ErrorPopup(errorType, errorMsg));
    }

    public static FileChooser initFileChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser;
    }

    public static void applyDarkStyleSheet(Scene scene) {
        scene.getStylesheets().add(SceneReference.getApp().getClass().getResource("stylesheets/mainPane-dark.css").toExternalForm());
        scene.getStylesheets().add(SceneReference.getApp().getClass().getResource("stylesheets/smartgraph-dark.css").toExternalForm());
        scene.getStylesheets().removeAll(SceneReference.getApp().getClass().getResource("stylesheets/mainPane-light.css").toExternalForm());
        scene.getStylesheets().removeAll(SceneReference.getApp().getClass().getResource("stylesheets/smartgraph-light.css").toExternalForm());
    }

    public static void applyLightStyleSheet(Scene scene) {
        scene.getStylesheets().add(SceneReference.getApp().getClass().getResource("stylesheets/mainPane-light.css").toExternalForm());
        scene.getStylesheets().add(SceneReference.getApp().getClass().getResource("stylesheets/smartgraph-light.css").toExternalForm());
        scene.getStylesheets().removeAll(SceneReference.getApp().getClass().getResource("stylesheets/mainPane-dark.css").toExternalForm());
        scene.getStylesheets().removeAll(SceneReference.getApp().getClass().getResource("stylesheets/smartgraph-dark.css").toExternalForm());

    }

    public static void colorVertexAfterTime(int milliseconds, MyVertexUnique vertex) {

        SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();

        PauseTransition pause = new PauseTransition(Duration.millis(750));
        pause.setOnFinished(e -> {
            SmartGraphVertexNode<String> currentVertexNode = graphView.getVertexNodeOf(vertex.getRealVertex());
            currentVertexNode.addStyleClass("pathVertex");
            SceneReference.getSolutionPane().insertVertexNode(currentVertexNode);
        });

        pause.play();
    }

    public static Edge colorEdgeAfterTime(int milliseconds, Edge<String, String> edge) {

        SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();
        DigraphEdgeList<String, String> graph = SceneReference.getGraph();

        PauseTransition pause = new PauseTransition(Duration.millis(milliseconds));
        pause.setOnFinished(e -> {
            Vertex<String> inbound = ((MyEdgeUnique) edge).getInboundUnique().getRealVertex();
            Vertex<String> outbound = ((MyEdgeUnique) edge).getOutboundUnique().getRealVertex();
            Edge<String, String> currentEdge = graph.outboundEdges(outbound).stream().filter(edgeFilter -> ((MyEdge) edgeFilter).getInbound().equals(inbound)).findFirst().orElse(null);
            SmartGraphEdgeBase<String, String> currentEdgeNode = graphView.getEdgeNodeOf(currentEdge);
            currentEdgeNode.addStyleClass("pathEdge");

            SceneReference.getSolutionPane().insertEdgeNode(currentEdgeNode);
        });

        pause.play();
        return edge;
    }

    public static void clearVertexAfterTime(int milliseconds, MyVertexUnique vertex) {

        PauseTransition pause = new PauseTransition(Duration.millis(milliseconds));
        pause.setOnFinished(e -> {
            clearVertex(vertex);
        });
        pause.play();
    }

    public static void clearEdgeAfterTime(int milliseconds, Edge<String, String> edge) {

        DigraphEdgeList<String, String> graph = SceneReference.getGraph();
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
        SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();
        graphView.getVertexNodeOf(vertex.getRealVertex()).removeStyleClass("pathVertex");
    }

    private static void clearEdge(Edge edge) {
        SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();
        graphView.getEdgeNodeOf(edge).removeStyleClass("pathEdge");
    }

}
