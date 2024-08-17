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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainPane extends BorderPane {

    // ANCHOR - Java variables
    private static int count = 0;
    private static int countSelected = 0;
    private static boolean isLinkingPhase = false; // are we trying to link vertices?
    private boolean isSideHidden = false; // is the side panel hidden?
    private boolean isVertexPressed = false; // is a vertex being pressed?
    private boolean isFinalVertex = false; // is this the final vertex?
    private boolean isInitialVertex = false; // is this the starting vertex?

    // ANCHOR - JavaFX variables
    private App app;
    private DigraphEdgeList<String, String> graph = new DigraphEdgeList<>();
    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph, initialPlacement);
    private ContentZoomScrollPane graphPane = new ContentZoomScrollPane(graphView);

    // Button
    private Button addVertex = new Button("Add Vertex");
    private Button finalVertex = new Button("Final Vertex");
    private Button initialVertex = new Button("Initial Vertex");

    // Text
    private TextField TextField = new TextField();
    private String nodeName, edgeName;

    // Vertex
    private Vertex fromVertex, toVertex;
    private SmartGraphVertexNode selectedVertexNode; // ui component
    private Vertex selectedVertex; // backend component
    private Vertex finalNode, initialNode; // Algoritm Component

    // ANCHOR - FXML elements
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
        SceneReference.setGraph(graph);
        SceneReference.setGrapView(graphView);
        this.setCenter(graphPane);
        graphPane.setPadding(new Insets(10));
        graphView.setVertexDoubleClickAction(graphVertex -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
        });
    }

    private void initSideMenu() {
        sideMenuHidedable.getChildren().addAll(addVertex);
        sideMenuHidedable.getChildren().addAll(finalVertex);
        sideMenuHidedable.getChildren().addAll(initialVertex);
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

        finalVertex.setOnAction(e -> {
            isFinalVertex = !isFinalVertex;
            isInitialVertex = false;
        });

        initialVertex.setOnAction(e -> {
            isInitialVertex = !isInitialVertex;
            isFinalVertex = false;
        });

        graphPane.setOnMousePressed(e -> {
            if (!isVertexPressed) {
                deselectVertex();
            }
        });

        addVertex.setOnAction(e -> {

            nodeName = TextField.getText();
            graph.insertVertex(nodeName);

            if (isInitialVertex) {
                initialNode = graph.vertexOf(nodeName);
            } else if (isFinalVertex) {
                finalNode = graph.vertexOf(nodeName);
            }

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
    /* //ANCHOR - Popup Edge name */
    /* -------------------------------------------------------------------------- */

    private void popupEdgeName(SmartGraphVertexNode from, SmartGraphVertexNode to) {
        Stage stage = new Stage();
        EdgePopup pane = new EdgePopup(stage, from, to);
        Scene edgeScene = new Scene(pane, 300, 160);

        // Scene Style
        stage.initStyle(StageStyle.TRANSPARENT);
        edgeScene.setFill(Color.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(edgeScene);
        stage.sizeToScene();
        stage.setResizable(false);

        // put this window in the middle of the primary window
        Stage primaryStage = SceneReference.getStage();
        double centerX = primaryStage.getX() + (primaryStage.getWidth() - 300) / 2;
        double centerY = primaryStage.getY() + (primaryStage.getHeight() - 160) / 2;
        stage.setX(centerX);
        stage.setY(centerY);

        stage.show();

    }

    private void deselectVertex() {
        selectedVertexNode.setStyleClass("vertex");
        selectedVertexNode = null;
    }

    public void addEdge(SmartGraphVertexNode from, SmartGraphVertexNode to) {
        graph.insertEdge(from.getUnderlyingVertex(), to.getUnderlyingVertex(), edgeName);
        graphView.update();
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Error Pupup */
    /* -------------------------------------------------------------------------- */

    private void showErrorPopup(String erorrMsg) {
        Stage stage = new Stage();
        ErrorPopup pane = new ErrorPopup(stage, erorrMsg);
        Scene edgeScene = new Scene(pane, 300, 160);

        // Scene Style
        stage.initStyle(StageStyle.TRANSPARENT);
        edgeScene.setFill(Color.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(edgeScene);
        stage.sizeToScene();
        stage.setResizable(false);

        // put this window in the middle of the primary window
        Stage primaryStage = SceneReference.getStage();
        double centerX = primaryStage.getX() + (primaryStage.getWidth() - 300) / 2;
        double centerY = primaryStage.getY() + (primaryStage.getHeight() - 160) / 2;
        stage.setX(centerX);
        stage.setY(centerY);

        stage.show();
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Getters */
    /* -------------------------------------------------------------------------- */

    public SmartGraphVertexNode getSelectedVertexNode() {
        return selectedVertexNode;
    }

    public boolean isVertexPressed() {
        return isVertexPressed;
    }

    public DigraphEdgeList getGraph() {
        return graph;
    }

    public SmartGraphVertexNode getInitialNode() {
        return (SmartGraphVertexNode) initialNode;
    }

    public SmartGraphVertexNode getFinalNode() {
        return (SmartGraphVertexNode) finalNode;
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Setters */
    /* -------------------------------------------------------------------------- */

    public void setSelectedVertexNode(SmartGraphVertexNode vertex) {
        if (selectedVertexNode != null) {
            if (isLinkingPhase) {
                if (graph.areAdjacent(selectedVertexNode.getUnderlyingVertex(), vertex.getUnderlyingVertex())) {
                    showErrorPopup("An edge from vertex " + selectedVertexNode.getAttachedLabel().getText() + " to vertex " + vertex.getAttachedLabel().getText()
                            + " already exists, please modify the existing one instead of creating a new one");
                } else {
                    popupEdgeName(selectedVertexNode, vertex);
                    isLinkingPhase = false;
                }
            } else {
                selectedVertexNode.setStyleClass("vertex");
                selectedVertexNode = vertex;
                selectedVertexNode.setStyleClass("selectedVertex");
            }
        } else {
            selectedVertexNode = vertex;
            selectedVertex = vertex.getUnderlyingVertex();
            nodeNameLabel.textProperty().bind(selectedVertexNode.getAttachedLabel().textProperty());
            nodeNameLabel.setVisible(true);
            selectedVertexNode.setStyleClass("selectedVertex");
        }

    }

    public void setVertexPressed(boolean isVertexPressed) {
        this.isVertexPressed = isVertexPressed;
    }

    public void setEdgeName(String edgeName) {
        this.edgeName = edgeName;
    }

}
