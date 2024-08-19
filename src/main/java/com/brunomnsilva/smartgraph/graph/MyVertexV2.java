package com.brunomnsilva.smartgraph.graph;

public class MyVertexV2<V> implements Vertex<V> {
    V element;

    public MyVertexV2(V element) {
        this.element = element;
    }

    @Override
    public V element() {
        return this.element;
    }

    @Override
    public String toString() {
        return "Vertex{" + element + '}';
    }
}
