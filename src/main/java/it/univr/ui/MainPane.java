package it.univr.ui;

import java.io.IOException;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import it.univr.App;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainPane extends BorderPane {

    private static int count = 0;
    private boolean isLinkingPhase = false;
    private boolean isSideHidden = false;

    private App app;
    private Digraph<String, String> graph = new DigraphEdgeList<>();
    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph, initialPlacement);
    private ContentZoomScrollPane graphPane = new ContentZoomScrollPane(graphView);
    private Button addVertex = new Button("Add Vertex");
    private Button linkVertecies = new Button("Link Vertecies");
    private TextField TextField = new TextField();
    private String nodeName;
    public static SmartGraphVertexNode selectedVertexNode;

    @FXML
    private MenuBar menuBar;
    @FXML
    private VBox sideMenuHidedable, sideMenuStatic;
    @FXML
    private HBox sideMenu;
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
        graphPane.setPadding(new Insets(10));
        graphView.setVertexDoubleClickAction(graphVertex -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
        });
    }

    private void initSideMenu() {
        sideMenuHidedable.getChildren().addAll(addVertex);
        sideMenuHidedable.getChildren().addAll(linkVertecies);
        sideMenuHidedable.getChildren().addAll(TextField);

        linkVertecies.setOnAction(e -> {
            isLinkingPhase = !isLinkingPhase;
        });

        if (isLinkingPhase) {
            graphView.setOnMouseClicked(e -> {

                // graph.insertEdge(, , "ciao");
            });
        }

        addVertex.setOnAction(e -> {
            nodeName = TextField.getText();
            graph.insertVertex(nodeName);
            graphView.update();
            count++;
        });

        sideMenuStatic.setOnMouseClicked(e -> {
            if (isSideHidden) {
                sideMenu.getChildren().add(sideMenuHidedable);
                sideMenu.setPrefWidth(300);
            } else {
                sideMenu.getChildren().remove(sideMenuHidedable);
                sideMenu.setPrefWidth(sideMenuStatic.getWidth());
            }
            isSideHidden = !isSideHidden;
        });

    }

}
