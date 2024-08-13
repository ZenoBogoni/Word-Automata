package it.univr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.example.City;
import com.brunomnsilva.smartgraph.example.Distance;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

/**
 * JavaFX App
 */
public class App extends Application {

    // SmartPlacementStrategy initialPlacement = new
    // SmartCircularSortedPlacementStrategy();
    // SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g,
    // initialPlacement);
    // Scene scene = new Scene(graphView, 1024, 768);

    // IMPORTANT! - Called after scene is displayed, so we can initialize the graph
    // visualization

    @Override
    public void start(Stage s) throws IOException {
        Graph<City, Distance> distances = new GraphEdgeList<>();

        Vertex<City> prague = distances.insertVertex(new City("Prague", 1.3f));
        Vertex<City> tokyo = distances.insertVertex(new City("Tokyo", 37.5f));
        Vertex<City> beijing = distances.insertVertex(new City("Beijing", 21.5f));
        Vertex<City> newYork = distances.insertVertex(new City("New York", 19.5f));
        Vertex<City> london = distances.insertVertex(new City("London", 14.4f));
        Vertex<City> helsinky = distances.insertVertex(new City("Helsinky", 0.6f));

        distances.insertEdge(tokyo, newYork, new Distance(10838));
        distances.insertEdge(beijing, newYork, new Distance(11550));
        distances.insertEdge(beijing, tokyo, new Distance(1303));
        distances.insertEdge(london, newYork, new Distance(5567));
        distances.insertEdge(london, prague, new Distance(1264));
        distances.insertEdge(helsinky, tokyo, new Distance(7815));
        distances.insertEdge(prague, helsinky, new Distance(1845));
        distances.insertEdge(beijing, london, new Distance(8132));

        /* Only Java 15 allows for multi-line strings. */
        String customProps = "edge.label = true" + "\n" + "edge.arrow = true";

        SmartGraphProperties properties = new SmartGraphProperties(customProps);

        SmartGraphPanel<City, Distance> graphView = new SmartGraphPanel<>(distances, properties,
                new SmartCircularSortedPlacementStrategy());

        Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph City Distances");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        graphView.init();

        // graphView.setAutomaticLayout(true);

        /*
         * You can manually place vertices at any time. However, these are
         * absolute coordinates inside the container panel.
         * Careful choice of a panel's background image can allow you to overlay
         * the vertices over a, e.g., world map.
         */
        graphView.setVertexPosition(beijing, 100, 100);
        graphView.setVertexPosition(helsinky, 924, 100);
        graphView.setVertexPosition(london, 200, 668);
        graphView.setVertexPosition(prague, 824, 668);
        graphView.setVertexPosition(tokyo, 512, 200);
        graphView.setVertexPosition(newYork, 512, 400);

        /*
         * This illustrates setting an image to the background of a node.
         * By default, the css class "vertex" is applied to all vertices.
         * Note that all inline styles have
         * priority over any properties set in css classes, even if they are applied
         * cumulatively through
         * .addStyleClass(class). However, inline styles can be overwritten by using
         * .setStyleInline(css);
         * also, when you use .setStyleClass(class), all previous styles will be
         * discarded, including inline.
         */
        // graphView.getStylableVertex(tokyo).setStyleInline("-fx-fill:
        // url(\"file:squares.jpg\");");
        // graphVertex.setStyleInline("-fx-fill: red;"); //this will overwrite the
        // property later on

        graphView.setVertexDoubleClickAction(graphVertex -> {
            graphVertex.setStyleClass("myVertex");
        });

    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void runApp() {
        launch();
    }

}