package it.univr.ui;

import java.io.IOException;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import it.univr.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

public class MainPane extends BorderPane {

    private static int count = 0;

    private App app;
    private Digraph<String, String> graph = new DigraphEdgeList<>();
    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph, initialPlacement);
    private ContentZoomScrollPane graphPane = new ContentZoomScrollPane(graphView);
    private Button addVertex = new Button("Add Vertex");

    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu file = new Menu("File"), view = new Menu("View"), help = new Menu("Help");
    @FXML
    private CheckMenuItem theme = new CheckMenuItem("Dark Mode"), autoLayout = new CheckMenuItem("Automatic Layout");

    public MainPane(App app) {
        this.app = app;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        initGraph();
    }

    public void initMainPane() {
        initMenuBar();
        initSideMenu();
        graphView.init();
        graphView.setStyle("-fx-background-color: transparent");
        graphView.update();
    }

    private void initMenuBar() {
        // MenuBar
        menuBar.getMenus().addAll(file, view, help);

        // View
        view.getItems().addAll(theme, autoLayout);

        // DarkMode
        theme.setSelected(true);
        theme.setOnAction(e -> {
            app.changeTheme();
        });

        // Auto Layout
        autoLayout.setOnAction(e -> {
            graphView.setAutomaticLayout(autoLayout.isSelected());
            graphView.update();
        });

    }

    private void initGraph() {
        this.setCenter(graphPane);
        this.setBottom(addVertex);
    }

    private void initSideMenu() {
        addVertex.setOnAction(e -> {
            graph.insertVertex(count + "");
            graphView.update();
            count++;
        });
    }

}
