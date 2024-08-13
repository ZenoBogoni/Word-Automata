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
    private boolean isDarkMode = true;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        // ATLANTA
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        MainPane mainPane = new MainPane(this);
        this.scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.setTitle("Word Automata");
        stage.setMinHeight(Constants.HEIGHT);
        stage.setMinWidth(Constants.WIDTH);
        stage.setScene(scene);
        stage.show();

        mainPane.initMainPane();

    }

    public void changeTheme() {
        if (isDarkMode) {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        }
        isDarkMode = !isDarkMode;
    }

    public void runApp(String[] args) {
        launch(args);
    }

}