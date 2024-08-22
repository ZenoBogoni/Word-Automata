package it.univr.utils;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import it.univr.ui.MainPane;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;

public class SceneReference {
    private static MainPane mainPane;
    private static SmartGraphPanel grapView;
    private static DigraphEdgeList<String, String> graph;
    private static SimpleBooleanProperty confirmToApplyProperty;
    private static SimpleBooleanProperty autoLayoutProperty;

    private static SimpleBooleanProperty clearTextOnClickProperty;

    private static Stage stage;

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Getters */
    /* -------------------------------------------------------------------------- */
    public static MainPane getMainPane() {
        return mainPane;
    }

    public static SmartGraphPanel getGrapView() {
        return grapView;
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

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Setters */
    /* -------------------------------------------------------------------------- */

    public static void setMainPane(MainPane pane) {
        mainPane = pane;
    }

    public static void setGrapView(SmartGraphPanel grapView) {
        SceneReference.grapView = grapView;
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
}
