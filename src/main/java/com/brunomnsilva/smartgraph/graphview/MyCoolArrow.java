package com.brunomnsilva.smartgraph.graphview;

import javafx.scene.shape.Polygon;

public class MyCoolArrow extends Polygon implements SmartStylableNode {
    /* Styling proxy */
    private final SmartStyleProxy styleProxy;

    /**
     * Default constructor.
     * 
     * @param size determines the size of the arrow (side of the triangle in pixels)
     */
    public MyCoolArrow(double size) {

        /* Create this arrow shape */
        getPoints().addAll(
                -size, size,
                0.0, 0.0,
                -size, -size);
        // getElements().add(new LineTo(-size, size));
        // getElements().add(new MoveTo(0, 0));
        // getElements().add(new LineTo(-size, -size));

        /* Add the corresponding css class */
        styleProxy = new SmartStyleProxy(this);
        styleProxy.addStyleClass("edge"); // the same style as the edge
        styleProxy.addStyleClass("arrow"); // it can initially be styled through this class
    }

    @Override
    public void setStyleInline(String css) {
        styleProxy.setStyleInline(css);
    }

    @Override
    public void setStyleClass(String cssClass) {
        styleProxy.setStyleClass(cssClass);
    }

    @Override
    public void addStyleClass(String cssClass) {
        styleProxy.addStyleClass(cssClass);
    }

    @Override
    public boolean removeStyleClass(String cssClass) {
        return styleProxy.removeStyleClass(cssClass);
    }

}
