package it.univr.ui;

import java.io.IOException;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import it.univr.App;
import it.univr.utils.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainPane extends BorderPane {

    private static int count = 0;
    private static int countSelected = 0;
    private static boolean isLinkingPhase = false;
    private boolean isSideHidden = false;

    private App app;
    private Digraph<String, String> graph = new DigraphEdgeList<>();
    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph, initialPlacement);
    private ContentZoomScrollPane graphPane = new ContentZoomScrollPane(graphView);
    private Button addVertex = new Button("Add Vertex");
    private Button linkVertices = new Button("Link Verteces");
    private TextField TextField = new TextField();
    private String nodeName, edgeName;
    private Vertex fromVertex, toVertex;
    private SmartGraphVertexNode selectedVertexNode; // ui component
    private Vertex selectedVertex; // backend component

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
    @FXML
    private Label nodeNameLabel;

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Constructor */
    /* -------------------------------------------------------------------------- */

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

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Inititalization */
    /* -------------------------------------------------------------------------- */
    public void initMainPane() {
        initMenuBar();
        initSideMenu();
        graphView.init();
        // graphView.setStyle("-fx-background-color: transparent");
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
            graphView.setAutomaticLayoutStrategy(new ForceDirectedSpringGravityLayoutStrategy(50.0, 0.5, 5.0, 0.5, 0.01));
            graphView.setAutomaticLayout(autoLayout.isSelected());
            graphView.update();
        });

    }

    private void initGraph() {
        SceneReference.setGrapView(graphView);
        this.setCenter(graphPane);
        graphPane.setPadding(new Insets(10));
        graphView.setVertexDoubleClickAction(graphVertex -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
        });
    }

    private void initSideMenu() {
        sideMenuHidedable.getChildren().addAll(addVertex);
        sideMenuHidedable.getChildren().addAll(linkVertices);
        sideMenuHidedable.getChildren().addAll(TextField);

        nodeNameLabel.setVisible(false);
        // nodeNameLabel.textProperty().bind(selectedVertexNode.getAttachedLabel().textProperty());

        this.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.CONTROL) {
                isLinkingPhase = true;
            }
        });

        this.setOnKeyReleased(key -> {
            if (key.getCode() == KeyCode.CONTROL) {
                isLinkingPhase = false;
            }
        });

        linkVertices.setOnAction(e -> {
            if (isLinkingPhase) {
                edgeName = TextField.getText();
                graph.insertEdge(fromVertex, toVertex, edgeName);
                graphView.update();
            }
            isLinkingPhase = !isLinkingPhase;
        });

        graphView.setOnMouseClicked(e -> {
            System.out.println("grapView pressed");
            if (isLinkingPhase) {
                if (countSelected == 0) {
                    fromVertex = selectedVertex;
                    toVertex = selectedVertex;
                    countSelected++;
                } else {
                    toVertex = selectedVertex;
                }
            }
        });

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

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Getters */
    /* -------------------------------------------------------------------------- */

    public SmartGraphVertexNode getSelectedVertexNode() {
        return selectedVertexNode;
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Setters */
    /* -------------------------------------------------------------------------- */

    public void setSelectedVertexNode(SmartGraphVertexNode vertex) {
        if (selectedVertexNode != null) {
            selectedVertexNode.setStyleClass("vertex");
            if (isLinkingPhase) {
                graph.insertEdge(selectedVertex, vertex.getUnderlyingVertex(), count + "");
                graphView.update();
                isLinkingPhase = false;
                count++;
            }

        }
        selectedVertexNode = vertex;
        selectedVertex = vertex.getUnderlyingVertex();
        nodeNameLabel.textProperty().bind(selectedVertexNode.getAttachedLabel().textProperty());
        nodeNameLabel.setVisible(true);

        selectedVertexNode.setStyleClass("selectedVertex");
    }
}
