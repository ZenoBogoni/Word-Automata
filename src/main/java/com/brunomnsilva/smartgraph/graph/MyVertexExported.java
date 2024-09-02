package com.brunomnsilva.smartgraph.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MyVertexExported {

    String element;
    boolean finalNode = false;
    boolean initialNode = false;
    double xPosition, yPosition;

    @JsonCreator
    public MyVertexExported(
            @JsonProperty("element") String element,
            @JsonProperty("initialNode") boolean initialNode,
            @JsonProperty("finalNode") boolean finalNode,
            @JsonProperty("xPosition") double xPosition,
            @JsonProperty("yPosition") double yPosition) {
        this.element = element;
        this.finalNode = finalNode;
        this.initialNode = initialNode;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public MyVertexExported(String element) {
        this.element = element;
    }

    public String getElement() {
        return this.element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setFinalNode(boolean bool) {
        finalNode = bool;
    }

    public void setInitialNode(boolean bool) {
        initialNode = bool;
    }

    public boolean isFinalNode() {
        return finalNode;
    }

    public boolean isInitialNode() {
        return initialNode;
    }

    public double getxPosition() {
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }
}
