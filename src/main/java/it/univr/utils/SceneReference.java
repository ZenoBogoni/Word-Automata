package it.univr.utils;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import it.univr.ui.MainPane;

public class SceneReference {
    private static MainPane mainPane;
    private static SmartGraphPanel grapView;

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Getters */
    /* -------------------------------------------------------------------------- */
    public static MainPane getMainPane() {
        return mainPane;
    }

    public static SmartGraphPanel getGrapView() {
        return grapView;
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
}
