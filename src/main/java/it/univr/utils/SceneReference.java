package it.univr.utils;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import it.univr.ui.MainPane;
import javafx.stage.Stage;

public class SceneReference {
    private static MainPane mainPane;
    private static SmartGraphPanel grapView;
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
}
