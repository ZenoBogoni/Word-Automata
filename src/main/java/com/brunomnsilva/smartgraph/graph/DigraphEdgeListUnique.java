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
public class DigraphEdgeListUnique<V, E> implements Digraph<V, E> {

    /*
     * inner classes are defined at the end of the class, so are the auxiliary methods
     */
    private final Map<V, Vertex<V>> vertices;
    private final Map<Integer, Edge<E, V>> edges;
    private static int id = 0;

    /**
     * Default constructor that initializes an empty digraph.
     */
    public DigraphEdgeListUnique() {
        this.vertices = new HashMap<>();
        this.edges = new HashMap<>();
    }

    @Override
    public synchronized Collection<Edge<E, V>> incidentEdges(Vertex<V> inbound) throws InvalidVertexException {
        checkVertex(inbound);

        List<Edge<E, V>> incidentEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {

            if (((MyEdgeUnique) edge).getInbound() == inbound) {
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

            if (((MyEdgeUnique) edge).getOutbound() == outbound) {
                outboundEdges.add(edge);
            }
        }
        return outboundEdges;
    }

    public synchronized Collection<Edge<E, V>> outboundEdgesUnique(MyVertexUnique outbound) throws InvalidVertexException {

        List<Edge<E, V>> outboundEdges = new ArrayList<>();
        for (Edge<E, V> edge : edges.values()) {

            if (((MyEdgeUnique) edge).getOutboundUnique() == outbound) {
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
            if (((MyEdgeUnique) edge).getOutbound() == outbound && ((MyEdgeUnique) edge).getInbound() == inbound) {
                return true;
            }
        }
        return false;
    }

    public synchronized MyEdgeUnique insertEdge(MyVertexUnique outbound, MyVertexUnique inbound, E edgeElement) throws InvalidVertexException, InvalidEdgeException {

        // ! non fa nulla in teoria
        if (existsEdgeWith(id)) {
            throw new InvalidEdgeException("There's already an edge with this element.");
        }
        // !========================

        MyEdgeUnique newEdge = new MyEdgeUnique(id, edgeElement, outbound, inbound);

        edges.put(id, newEdge);
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

        MyVertexUnique outVertex = vertexOf(outboundElement);
        MyVertexUnique inVertex = vertexOf(inboundElement);

        MyEdgeUnique newEdge = new MyEdgeUnique(id, edgeElement, outVertex, inVertex);
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
        MyEdgeUnique edge = checkEdge(e);

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
    public synchronized MyVertexUnique insertVertex(V vElement) throws InvalidVertexException {
        if (existsVertexWith(vElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        MyVertexUnique newVertex = new MyVertexUnique(vElement);

        vertices.put(vElement, newVertex);

        return newVertex;
    }

    @Override
    public synchronized V removeVertex(Vertex<V> v) throws InvalidVertexException {
        // checkVertex(v);

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
        // checkEdge(e);

        E element = e.element();
        edges.remove(e.getId());

        return element;
    }

    @Override
    public V replace(Vertex<V> v, V newElement) throws InvalidVertexException {
        if (existsVertexWith(newElement)) {
            throw new InvalidVertexException("There's already a vertex with this element.");
        }

        MyVertexUnique vertex = checkVertex(v);

        V oldElement = vertex.element;
        vertex.element = newElement;
        vertices.put(newElement, vertex);
        vertices.remove(oldElement);

        return oldElement;
    }

    // ! forse l'ho fixata
    @Override
    public E replace(Edge<E, V> e, E newElement) throws InvalidEdgeException {
        // if (existsEdgeWith(e.getId())) {
        // throw new InvalidEdgeException("There's already an edge with this element.");
        // }

        MyEdgeUnique edge = checkEdge(e);

        E oldElement = edge.element;
        edge.element = newElement;

        return oldElement;
    }

    // ! HO CAMBIATO DA PRIVATE A PUBLIC
    public MyVertexUnique vertexOf(V vElement) {
        for (Vertex<V> v : vertices.values()) {
            if (v.element().equals(vElement)) {
                return (MyVertexUnique) v;
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

    public class MyVertexUnique implements Vertex<V> {

        V element;
        boolean finalNode = false;
        boolean initialNode = false;
        Vertex<V> realVertex;

        public MyVertexUnique(V element) {
            this.element = element;
        }

        @Override
        public V element() {
            return this.element;
        }

        @Override
        public void setElement(V element) {
            this.element = element;
        }

        @Override
        public String toString() {
            return "UniqueVertex{" + element + " " + realVertex + '}';
        }

        @Override
        public void setFinal(boolean bool) {
            finalNode = bool;
        }

        @Override
        public void setInitial(boolean bool) {
            initialNode = bool;
        }

        @Override
        public boolean isFinal() {
            return finalNode;
        }

        @Override
        public boolean isInitial() {
            return initialNode;
        }

        public void setRealVertex(Vertex<V> vertex) {
            this.realVertex = vertex;
        }

        public Vertex<V> getRealVertex() {
            return realVertex;
        }

    }

    public class MyEdgeUnique implements Edge<E, V> {

        int id;
        E element;
        MyVertexUnique vertexOutbound;
        MyVertexUnique vertexInbound;

        public MyEdgeUnique(int id, E element, MyVertexUnique vertexOutbound, MyVertexUnique vertexInbound) {
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
            Vertex<V>[] v = new Vertex[1];
            return v;
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

        public MyVertexUnique getOutboundUnique() {
            return (MyVertexUnique) vertexOutbound;
        }

        public MyVertexUnique getInboundUnique() {
            return (MyVertexUnique) vertexInbound;
        }

        public void setInbound(MyVertexUnique inbound) {
            this.vertexInbound = inbound;
        }

        public void setOutbound(MyVertexUnique outbound) {
            this.vertexOutbound = outbound;
        }
    }

    /**
     * Checks whether a given vertex is valid and belongs to this graph.
     *
     * @param v the vertex to check
     * @return the reference of the vertex, with cast to the underlying implementation of {@link Vertex}
     * @throws InvalidVertexException if the vertex is <code>null</code> or does not belong to this graph
     */
    private MyVertexUnique checkVertex(Vertex<V> v) throws InvalidVertexException {
        if (v == null)
            throw new InvalidVertexException("Null vertex.");

        MyVertexUnique vertex;
        try {
            vertex = (MyVertexUnique) v;
        } catch (ClassCastException e) {
            throw new InvalidVertexException("Not a vertex.");
        }

        if (!vertices.containsKey(vertex.element())) {
            throw new InvalidVertexException("Vertex does not belong to this graph.");
        }

        return vertex;
    }

    private MyEdgeUnique checkEdge(Edge<E, V> e) throws InvalidEdgeException {
        if (e == null)
            throw new InvalidEdgeException("Null edge.");

        MyEdgeUnique edge;
        try {
            edge = (MyEdgeUnique) e;
        } catch (ClassCastException ex) {
            throw new InvalidVertexException("Not an adge.");
        }

        if (!edges.containsKey(edge.getId())) {
            throw new InvalidEdgeException("Edge does not belong to this graph.");
        }

        return edge;
    }

    public Edge<E, V> getEdgeById(int id) {
        if (edges.containsKey(id)) {
            return edges.get(id);
        }
        return null;
    }

    @Override
    public Edge<E, V> insertEdge(Vertex<V> outbound, Vertex<V> inbound, E edgeElement) throws InvalidVertexException, InvalidEdgeException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertEdge'");
    }
}
