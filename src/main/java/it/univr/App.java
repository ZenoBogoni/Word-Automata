package it.univr;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import it.univr.ui.MainPane;
import it.univr.utils.Constants;

/**
 * JavaFX App
 */
public class App extends Application {

    private Stage stage;
    private Scene scene;
    private boolean isDarkMode = true;
    private MainPane mainPane;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        // ATLANTA
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

        mainPane = new MainPane(this);

        this.scene = new Scene(mainPane, Constants.WIDTH, Constants.HEIGHT);
        this.stage.setScene(scene);
        this.stage.setTitle("Word Automata");
        this.stage.setMinHeight(Constants.HEIGHT);
        this.stage.setMinWidth(Constants.WIDTH);
        this.stage.setScene(scene);
        this.stage.show();

        mainPane.initMainPane();

    }

    public void changeTheme() {
        if (isDarkMode) {
            Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        }
        isDarkMode = !isDarkMode;
    }

    public void runApp(String[] args) {
        launch(args);
    }

}