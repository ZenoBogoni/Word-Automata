package it.univr.backend;

import java.util.ArrayList;
import java.util.HashSet;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList.MyEdge;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyEdgeUnique;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeListUnique.MyVertexUnique;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphEdgeBase;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import it.univr.App;
import it.univr.Controller.panes.GraphSidePane;
import it.univr.Controller.panes.MainPane;
import it.univr.Controller.panes.SolutionPane;
import it.univr.Controller.popups.EdgePopup;
import it.univr.Controller.popups.ErrorPopup;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SceneReference {
    private static App app;
    // Constants
    public static final int HEIGHT = 768;
    public static final int WIDTH = 1024;
    public static String GRAPH_DARK;
    public static String GRAPH_LIGHT;
    // java variables
    private static boolean isEdgePressed = false;
    private static boolean isVertexPressed = false;
    private static boolean isLinkingPhase = false;
    private static boolean unsavedChanges = false;
    private static double mouseX, mouseY;

    // Components
    private static Stage stage;
    private static MainPane mainPane;
    private static DigraphEdgeList<String, String> graph;
    private static SmartGraphPanel<String, String> graphView;
    private static ContentZoomScrollPane graphPane;
    private static SolutionPane solutionPane = new SolutionPane();

    private static SmartGraphVertexNode<String> initialVertexNode;
    private static HashSet<SmartGraphVertexNode<String>> finalVerticesNodes = new HashSet<SmartGraphVertexNode<String>>();
    private static SmartGraphEdgeBase<String, String> selectedEdge;
    private static GraphSidePane graphSidePane;
    private static SmartGraphVertexNode<String> selectedVertexNode;

    // Properties
    private static SimpleBooleanProperty isVertexSelectedProperty = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty confirmToApplyProperty = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty autoLayoutProperty = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty clearTextOnClickProperty = new SimpleBooleanProperty(true);
    private static SimpleBooleanProperty initialVertexSetProperty = new SimpleBooleanProperty(false);

    private static SimpleBooleanProperty isEdgeSelectedProperty = new SimpleBooleanProperty(false);
    private static SimpleStringProperty fileNameProperty = new SimpleStringProperty("Word Automata - unnamed Automata");

    private static String testWord;

    private static ArrayList<Animation> activeAnimation = new ArrayList<>();

    private SceneReference() {
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Getters */
    /* -------------------------------------------------------------------------- */
    public static GraphSidePane getGraphSidePane() {
        return graphSidePane;
    }

    public static boolean isLinkingPhase() {
        return isLinkingPhase;
    }

    public static boolean isVertexPressed() {
        return isVertexPressed;
    }

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

    public static SimpleBooleanProperty getInitialVertexSetProperty() {
        return initialVertexSetProperty;
    }

    public static ContentZoomScrollPane getGraphPane() {
        return graphPane;
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static SmartGraphVertexNode<String> getSelectedVertexNode() {
        return selectedVertexNode;
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Setters */
    /* -------------------------------------------------------------------------- */

    public static void setLinkingPhase(boolean isLinkingPhase) {
        SceneReference.isLinkingPhase = isLinkingPhase;
    }

    public static void setVertexPressed(boolean isVertexPressed) {
        SceneReference.isVertexPressed = isVertexPressed;
    }

    public static void setGraphPane(ContentZoomScrollPane graphPane) {
        SceneReference.graphPane = graphPane;
    }

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

    public static void setApp(App app) {
        GRAPH_DARK = app.getClass().getResource("stylesheets/smartgraph-dark.css").toExternalForm();
        GRAPH_LIGHT = app.getClass().getResource("stylesheets/smartgraph-light.css").toExternalForm();
        SceneReference.app = app;
    }

    public static void setSolutionPane(SolutionPane solutionPane) {
        SceneReference.solutionPane = solutionPane;
    }

    public static void setGraphSidePane(GraphSidePane graphSidePane) {
        SceneReference.graphSidePane = graphSidePane;
    }

    public static void setMousePostion(double x, double y) {
        mouseX = x;
        mouseY = y;
    }

    public static void setSelectedVertexNode(SmartGraphVertexNode<String> vertex) {
        if (selectedVertexNode != null) {

            if (isLinkingPhase) {
                // if (graph.areAdjacent(selectedVertexNode.getUnderlyingVertex(), vertex.getUnderlyingVertex())) {
                // SceneReference.showErrorPopup("Edge already exists", "An edge from vertex " + selectedVertexNode.getAttachedLabel().getText() + " to vertex " + vertex.getAttachedLabel().getText()
                // + " already exists, please modify the existing one instead of creating a new one");
                // } else {
                SceneReference.createModal(new EdgePopup(selectedVertexNode, vertex));
                isLinkingPhase = false;
                // }
            } else {
                if (selectedVertexNode.equals(vertex)) {
                    graphSidePane.focusVertexField();
                    return;
                }
                selectedVertexNode.removeStyleClass("selectedVertex");
                isVertexSelectedProperty.set(false);
                selectedVertexNode = vertex;
                if (vertex != null) {
                    isVertexSelectedProperty.set(true);
                    selectedVertexNode.addStyleClassLast("selectedVertex");
                    graphSidePane.focusVertexField();
                }
            }
        } else {
            if (vertex == null) {
                deselectVertex();
            }
            selectedVertexNode = vertex;
            SceneReference.getIsVertexSelectedProperty().set(true);
            selectedVertexNode.addStyleClassLast("selectedVertex");
            graphSidePane.focusVertexField();
        }
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Methods */
    /* -------------------------------------------------------------------------- */
    public static void deselectVertex() {
        if (selectedVertexNode != null) {
            SceneReference.getIsVertexSelectedProperty().set(false);
            selectedVertexNode.removeStyleClass("selectedVertex");
            selectedVertexNode = null;
        }
    }

    public static void deselectNodes() {
        deselectVertex();
        if (SceneReference.isEdgeSelected()) {
            SceneReference.deselectEdge();
        }
    }

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

    public static void deleteGraph(DigraphEdgeList<String, String> graph) {
        graph.vertices().forEach(vertex -> {
            graph.removeVertex(vertex);
        });
        SceneReference.graphView.update();
        SceneReference.getSolutionPane().clearHistory();
        SceneReference.getFinalVerticesNodes().clear();
        SceneReference.setInitialVertexNode(null);
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

        PauseTransition pause = new PauseTransition(Duration.millis(750));
        addAnimation(pause);
        pause.setOnFinished(e -> {
            SmartGraphVertexNode<String> currentVertexNode = graphView.getVertexNodeOf(vertex.getRealVertex());
            currentVertexNode.addStyleClass("pathVertex");
            SceneReference.getSolutionPane().insertVertexNode(currentVertexNode);
            removeAnimation(pause);
        });

        pause.play();
    }

    public static void clearVertexAfterTime(int milliseconds, MyVertexUnique vertex) {

        PauseTransition pause = new PauseTransition(Duration.millis(milliseconds));
        addAnimation(pause);
        pause.setOnFinished(e -> {
            clearVertex(vertex);
            removeAnimation(pause);
        });
        pause.play();
    }

    public static void clearEdgeAfterTime(int milliseconds, Edge<String, String> edge) {
        PauseTransition pause = new PauseTransition(Duration.millis(milliseconds));
        addAnimation(pause);
        pause.setOnFinished(e -> {
            Vertex<String> inbound = ((MyEdgeUnique) edge).getInboundUnique().getRealVertex();
            Vertex<String> outbound = ((MyEdgeUnique) edge).getOutboundUnique().getRealVertex();
            Edge<String, String> currentEdge = graph.outboundEdges(outbound).stream()
                    .filter(edgeFilter -> ((MyEdge) edgeFilter).getInbound().equals(inbound) && edgeFilter.element() == edge.element())
                    .findFirst().orElse(null);
            clearEdge(currentEdge);
            removeAnimation(pause);
        });
        pause.play();
    }

    private static void clearVertex(MyVertexUnique vertex) {
        graphView.getVertexNodeOf(vertex.getRealVertex()).removeStyleClass("pathVertex");
    }

    private static void clearEdge(Edge<String, String> edge) {
        graphView.getEdgeNodeOf(edge).removeStyleClass("pathEdge");
    }

    public static void addAnimation(Animation animation) {
        activeAnimation.add(animation);
    }

    public static void removeAnimation(Animation animation) {
        activeAnimation.remove(animation);
    }

    public static void stopAllAnimations() {
        for (Animation animation : activeAnimation) {
            animation.stop();
        }
        graph.vertices().forEach(vertex -> graphView.getVertexNodeOf(vertex).removeStyleClass("pathVertex"));
        graph.edges().forEach(edge -> graphView.getEdgeNodeOf(edge).removeStyleClass("pathEdge"));
        activeAnimation.clear();
        getSolutionPane().clearHistory();
    }

}
