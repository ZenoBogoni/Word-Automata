package it.univr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ConstrainedColumnResizeBase;
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

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import it.univr.ui.MainPane;
import it.univr.utils.Constants;

/**
 * JavaFX App
 */
public class App extends Application {

    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        // ATLANTA
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        this.scene = new Scene(new MainPane());
        stage.setScene(scene);
        stage.setTitle("Word Automata");
        stage.setMinHeight(Constants.HEIGHT);
        stage.setMinWidth(Constants.WIDTH);
        stage.setScene(scene);
        stage.show();

    }

    public void runApp(String[] args) {
        launch(args);
    }

}