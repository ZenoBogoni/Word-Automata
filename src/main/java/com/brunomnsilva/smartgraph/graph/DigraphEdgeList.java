/*
 * The MIT License
 *
 * JavaFXSmartGraph | Copyright 2023-2024  brunomnsilva@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.brunomnsilva.smartgraph.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.backend.SceneReference;

/**
 * Implementation of a digraph that adheres to the {@link Digraph} interface.
 * <br>
 * Does not allow duplicates of stored elements through <b>equals</b> criteria.
 * <br>
 * 
 * @param <V> Type of element stored at a vertex
 * @param <E> Type of element stored at an edge
 * 
 * @author brunomnsilva
 */
public class DigraphEdgeList<V, E> implements Digraph<V, E> {

    /*
     * inner classes are defined at the end of the class, so are the auxiliary methods
     */
    private final Map<V, Vertex<V>> vertices;
    private final Map<Integer, Edge<E, V>> edges;
    private static int id = 0;

    /**
     * Default constructor that initializes an empty digraph.
     */
    public DigraphEdgeList() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
    }

    @Override
    public synchronized Collection<Edge<E, V>> incidentEdges(Vertex<V> inbound) throws InvalidVertexException {
        checkVertex(inbound);

        List<Edge<E, V>> incidentEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {

            if (((MyEdge) edge).getInbound() == inbound) {
                incidentEdges.add(edge);
            }
        }
        return incidentEdges;
    }

    @Override
    public synchronized Collection<Edge<E, V>> outboundEdges(Vertex<V> outbound) throws InvalidVertexException {
        checkVertex(outbound);

        List<Edge<E, V>> outboundEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {

            if (((MyEdge) edge).getOutbound() == outbound) {
                outboundEdges.add(edge);
            }
        }
        return outboundEdges;
    }

    @Override
    public boolean areAdjacent(Vertex<V> outbound, Vertex<V> inbound) throws InvalidVertexException {
        // we allow loops, so we do not check if outbound == inbound
        checkVertex(outbound);
        checkVertex(inbound);

        /* find and edge that goes outbound ---> inbound */
        for (Edge<E, V> edge : edges.values()) {
            if (((MyEdge) edge).getOutbound() == outbound && ((MyEdge) edge).getInbound() == inbound) {
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement) throws InvalidVertexException, InvalidEdgeException {

        if (!isDeterministic(outbound, edgeElement)) {
            return null;
        }

        MyVertex outVertex = checkVertex(outbound);
        MyVertex inVertex = checkVertex(inbound);

        MyEdge newEdge = new MyEdge(id, edgeElement, outVertex, inVertex);

        edges.put(id, newEdge);
        outVertex.addEdgeToMap(newEdge);

        id++;
        return newEdge;
    }

    @Override
    public synchronized Edge<E, V> insertEdge(V outboundElement, V inboundElement, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        // ! non fa nulla in teoria
        if (existsEdgeWith(id)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }
        // !======================
        if (!existsVertexWith(outboundElement)) {
            throw new InvalidVertexException("No vertex contains " + outboundElement);
        }
        if (!existsVertexWith(inboundElement)) {
            throw new InvalidVertexException("No vertex contains " + inboundElement);
        }

        MyVertex outVertex = vertexOf(outboundElement);
        MyVertex inVertex = vertexOf(inboundElement);

        MyEdge newEdge = new MyEdge(id, edgeElement, outVertex, inVertex);
        outVertex.addEdgeToMap(newEdge);
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        edges.put(id, newEdge);
        id++;
        return newEdge;
    }

    @Override
    public int numVertices() {
        return vertices.size();
    }

    @Override
    public int numEdges() {
        return edges.size();
    }

    @Override
    public synchronized Collection<Vertex<V>> vertices() {
        return new ArrayList<>(vertices.values());
    }

    @Override
    public synchronized Collection<Edge<E, V>> edges() {
        return new ArrayList<>(edges.values());
    }

    @Override
    public synchronized Vertex<V> opposite(Vertex<V> v, Edge<E, V> e) throws InvalidVertexException, InvalidEdgeException {
        checkVertex(v);
        MyEdge edge = checkEdge(e);

        if (!edge.contains(v)) {
            return null; /* this edge does not connect vertex v */
        }

        if (edge.vertices()[0] == v) {
            return edge.vertices()[1];
        } else {
            return edge.vertices()[0];
        }

    }

    @Override
    public synchronized Vertex<V> insertVertex(V vElement) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        MyVertex newVertex = new MyVertex(vElement);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized V removeVertex(Vertex<V> v) throws InvalidVertexException {
        checkVertex(v);

        V element = v.element();

        // remove incident edges
        Collection<Edge<E, V>> inOutEdges = incidentEdges(v);
        inOutEdges.addAll(outboundEdges(v));

        for (Edge<E, V> edge : inOutEdges) {
            edges.remove(edge.getId());
        }

        vertices.remove(v.element());

        return element;
    }

    @Override
    public synchronized E removeEdge(Edge<E, V> e) throws InvalidEdgeException {
        checkEdge(e);

        E element = e.element();
        // !
        ((MyVertex) e.vertices()[0]).removeEdgeFromMap(e);
        edges.remove(e.getId());

        return element;
    }

    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if (existsVertexWith(newElement)) {
            return null;
        }

        MyVertex vertex = checkVertex(v);

        V oldElement = vertex.element;
        vertex.element = newElement;
        vertices.put(newElement, vertex);
        vertices.remove(oldElement);

        return oldElement;
    }

    // ! forse l'ho fixata
    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {

        MyEdge edge = checkEdge(e);

        if (!isDeterministic(edge.getOutbound(), newElement)) {
            return null;
        }

        E oldElement = edge.element;
        edge.element = newElement;

        return oldElement;
    }

    // ! HO CAMBIATO DA PRIVATE A PUBLIC
    public MyVertex vertexOf(V vElement) {
        for (Vertex<V> v : vertices.values()) {
            if (v.element().equals(vElement)) {
                return (MyVertex) v;
            }
        }
        return null;
    }

    public boolean existsVertexWith(V vElement) {
        return vertices.containsKey(vElement);
    }

    // ! questa non e' corretta
    private boolean existsEdgeWith(int id) {
        return edges.containsKey(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                String.format("Graph with %d vertices and %d edges:\n", numVertices(), numEdges()));

        sb.append("--- Vertices: \n");
        for (Vertex<V> v : vertices.values()) {
            sb.append("\t").append(v.toString()).append("\n");
        }
        sb.append("\n--- Edges: \n");
        for (Edge<E, V> e : edges.values()) {
            sb.append("\t").append(e.toString()).append("\n");
        }
        return sb.toString();
    }

    public class MyVertex implements Vertex<V> {

        V element;
        boolean finalNode = false;
        boolean initialNode = false;
        HashMap<Vertex<V>, List<Edge<E, V>>> edgeMap = new HashMap<>();

        public MyVertex(V element) {
            this.element = element;
        }

        @Override
        public V element() {
            return this.element;
        }

        public void setElement(V element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return "Vertex{" + element + '}';
        }

        public void setFinal(boolean bool) {
            finalNode = bool;
        }

        public void setInitial(boolean bool) {
            initialNode = bool;
        }

        public boolean isFinal() {
            return finalNode;
        }

        public boolean isInitial() {
            return initialNode;
        }

        public String getElement() {
            return element + "";
        }

        public void addEdgeToMap(Edge<E, V> edge) {
            if (!edgeMap.containsKey(edge.vertices()[1])) {
                edgeMap.put(edge.vertices()[1], new ArrayList<Edge<E, V>>());
            }
            edgeMap.get(edge.vertices()[1]).add(edge);
        }

        public void removeEdgeFromMap(Edge<E, V> edge) {
            if (edgeMap.containsKey(edge.vertices()[1])) {
                edgeMap.get(edge.vertices()[1]).remove(edge);
            }
        }

        public int getIndexOf(Edge<E, V> edge) {
            if (edgeMap.containsKey(edge.vertices()[1])) {
                return edgeMap.get(edge.vertices()[1]).indexOf(edge);
            }
            return -1;
        }
    }

    public class MyEdge implements Edge<E, V> {

        int id;
        E element;
        Vertex<V> vertexOutbound;
        Vertex<V> vertexInbound;

        public MyEdge(int id, E element, Vertex<V> vertexOutbound, Vertex<V> vertexInbound) {
            this.id = id;
            this.element = element;
            this.vertexOutbound = vertexOutbound;
            this.vertexInbound = vertexInbound;
        }

        @Override
        public E element() {
            return this.element;
        }

        @Override
        public int getId() {
            return this.id;
        }

        public boolean contains(Vertex<V> v) {
            return (vertexOutbound == v || vertexInbound == v);
        }

        @Override
        public Vertex<V>[] vertices() {
            Vertex<V>[] vertices = new Vertex[2];
            vertices[0] = vertexOutbound;
            vertices[1] = vertexInbound;

            return vertices;
        }

        @Override
        public String toString() {
            return "Edge{{" + element + "}, vertexOutbound=" + vertexOutbound.toString()
                    + ", vertexInbound=" + vertexInbound.toString() + '}';
        }

        public Vertex<V> getOutbound() {
            return vertexOutbound;
        }

        public Vertex<V> getInbound() {
            return vertexInbound;
        }

        public void setInbound(Vertex<V> inbound) {
            this.vertexInbound = inbound;
        }

        public void setOutbound(Vertex<V> outbound) {
            this.vertexOutbound = outbound;
        }

        public String getElement() {
            return element + "";
        }
    }

    /**
     * Checks whether a given vertex is valid and belongs to this graph.
     *
     * @param v the vertex to check
     * @return the reference of the vertex, with cast to the underlying implementation of {@link Vertex}
     * @throws InvalidVertexException if the vertex is <code>null</code> or does not belong to this graph
     */
    private MyVertex checkVertex(Vertex<V> v) throws InvalidVertexException {
        if (v == null)
            throw new InvalidVertexException("Null vertex.");

        MyVertex vertex;
        try {
            vertex = (MyVertex) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsKey(vertex.element())) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private MyEdge checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if (e == null)
            throw new InvalidEdgeException("Null edge.");

        MyEdge edge;
        try {
            edge = (MyEdge) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an adge.");
        }

        if (!edges.containsKey(edge.getId())) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }

    // // ! Aggiunto io
    // public Vertex<V> updateLabelFor(Vertex<V> vertex, V label) {
    // if (vertices.containsKey(label)) {
    // return null; // TODO - aggiungere feedback per utente
    // }
    // Vertex<V> newVertex = new MyVertex(label);
    // newVertex.setElement(label);
    // System.out.println("Vertex: " + vertex.toString());
    // // update incident edges
    // Collection<Edge<E, V>> inboundEdges = incidentEdges(vertex);
    // inboundEdges.forEach(edge -> {
    // ((MyEdge) edge).setInbound(newVertex);
    // });
    // // update outbound edges
    // Collection<Edge<E, V>> outboundEdges = outboundEdges(vertex);
    // outboundEdges.forEach(edge -> {
    // ((MyEdge) edge).setOutbound(newVertex);
    // });
    // vertices.remove(vertex.element());
    // vertices.put(label, newVertex);
    // SceneReference.getGrapView().updateAndWait();
    // return newVertex;
    // }

    public Edge<E, V> getEdgeById(int id) {
        if (edges.containsKey(id)) {
            return edges.get(id);
        }
        return null;
    }

    public List<MyVertexExported> getMyVertexList() {
        List<MyVertexExported> vertexList = new ArrayList<>();
        vertices().forEach(vertex -> {
            SmartGraphVertexNode<String> vertexNode = SceneReference.getGraphView().getVertexNodeOf((Vertex<String>) vertex);
            vertexList.add(new MyVertexExported(((MyVertex) vertex).getElement(), vertex.isInitial(), vertex.isFinal(), vertexNode.getCenterX(), vertexNode.getCenterY()));
        });
        return vertexList;
    }

    public List<MyEdgeExported> getMyEdgeList() {
        List<MyEdgeExported> edgeList = new ArrayList<>();
        edges().forEach(edge -> {
            String element = ((MyEdge) edge).getElement();
            String inbound = ((MyVertex) ((MyEdge) edge).getInbound()).getElement();
            String outbound = ((MyVertex) ((MyEdge) edge).getOutbound()).getElement();

            edgeList.add(new MyEdgeExported(element, inbound, outbound));
        });
        return edgeList;
    }

    public boolean isDeterministic(Vertex<V> vertex, E edgeElement) {
        return !outboundEdges(vertex).stream().anyMatch(edge -> edge.element().equals(edgeElement));
    }
}
