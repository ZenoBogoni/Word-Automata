package it.univr.ui;

import java.io.IOException;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class MainPane extends BorderPane {
    private App app;
    private Graph<String, String> graph = new GraphEdgeList<>();
    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph, initialPlacement);
    private ContentZoomScrollPane graphPane = new ContentZoomScrollPane(graphView);
    private boolean isBottomClosed = false;

    @FXML
    private MenuBar menuBar;
    @FXML
    private HBox bottomPane, bottomBar;
    @FXML
    private Button closePaneButton;
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
        initBottomBar();
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
        graph.insertVertex("A");
        graph.insertVertex("B");
        graph.insertVertex("C");
        graph.insertVertex("D");
        graph.insertVertex("E");
        graph.insertVertex("F");
        graph.insertVertex("G");

        graph.insertEdge("A", "B", "1");
        graph.insertEdge("A", "C", "2");
        graph.insertEdge("A", "D", "3");
        graph.insertEdge("A", "E", "4");
        graph.insertEdge("A", "F", "5");
        graph.insertEdge("A", "G", "6");

        graph.insertVertex("H");
        graph.insertVertex("I");
        graph.insertVertex("J");
        graph.insertVertex("K");
        graph.insertVertex("L");
        graph.insertVertex("M");
        graph.insertVertex("N");

        graph.insertEdge("H", "I", "7");
        graph.insertEdge("H", "J", "8");
        graph.insertEdge("H", "K", "9");
        graph.insertEdge("H", "L", "10");
        graph.insertEdge("H", "M", "11");
        graph.insertEdge("H", "N", "12");

        graph.insertEdge("A", "H", "0");

        this.setCenter(graphPane);
    }

    private void initBottomBar() {
        closePaneButton.setOnAction(e -> {
            if (isBottomClosed) {
                bottomBar.setPrefHeight(30);
                bottomPane.setPrefHeight(0);
            }
        });
    }

}
