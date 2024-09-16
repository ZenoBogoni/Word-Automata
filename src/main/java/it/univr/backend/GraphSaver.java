package it.univr.backend;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import javafx.stage.FileChooser;

public class GraphSaver {

    private static SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();
    private static DigraphEdgeList<String, String> graph = SceneReference.getGraph();

    public static void createGraphFromFile(String fileName) {
        SceneReference.stopAllAnimations();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        GraphToFile graphToFile;

        try {
            graphToFile = mapper.readValue(new File(fileName), GraphToFile.class);
            SceneReference.deleteGraph(graph);

            graphToFile.getVertexList().forEach(vertex -> {
                Vertex<String> newVertex = graph.insertVertex(vertex.getElement());
                graphView.updateAndWait();
                SmartGraphVertexNode<String> newVertexNode = graphView.getVertexNodeOf(newVertex);
                newVertexNode.setCenterX(vertex.getxPosition());
                newVertexNode.setCenterY(vertex.getyPosition());
                if (vertex.isFinalNode()) {
                    SceneReference.addFinalvertex(newVertexNode);
                } else if (vertex.isInitialNode()) {
                    SceneReference.setInitialVertexNode(newVertexNode);
                }
            });

            graphToFile.getEdgeList().forEach(edge -> {
                Vertex<String> inbound = graph.vertexOf(edge.getInbound());
                Vertex<String> outbound = graph.vertexOf(edge.getOutbound());
                graph.insertEdge(outbound, inbound, edge.getElement());
                // graphView.updateAndWait();
            });

            graphView.updateAndWait();
        } catch (Exception e) {
            SceneReference.showErrorPopup("File not compatible", fileName + "\ndoes not contain a valid Automata.");
            e.printStackTrace();
        }
    }

    public static void createFileFromGraph(DigraphEdgeList<String, String> graph, String fileName) {
        GraphToFile graphToFile = new GraphToFile();
        graphToFile.setVertexList(graph.getMyVertexList());
        graphToFile.setEdgeList(graph.getMyEdgeList());
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(fileName), graphToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileChooser initFileChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser;
    }
}
