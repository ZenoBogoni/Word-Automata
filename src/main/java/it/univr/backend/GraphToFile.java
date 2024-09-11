package it.univr.backend;

import java.util.Collection;

import com.brunomnsilva.smartgraph.graph.MyEdgeExported;
import com.brunomnsilva.smartgraph.graph.MyVertexExported;

public class GraphToFile {
    private Collection<MyVertexExported> vertexList;
    private Collection<MyEdgeExported> edgeList;

    /* -------------------------------------------------------------------------- */
    /* getters */
    /* -------------------------------------------------------------------------- */

    public Collection<MyEdgeExported> getEdgeList() {
        return edgeList;
    }

    public Collection<MyVertexExported> getVertexList() {
        return vertexList;
    }

    /* -------------------------------------------------------------------------- */
    /* setters */
    /* -------------------------------------------------------------------------- */

    public void setEdgeList(Collection<MyEdgeExported> edgeList) {
        this.edgeList = edgeList;
    }

    public void setVertexList(Collection<MyVertexExported> vertexList) {
        this.vertexList = vertexList;
    }
}
