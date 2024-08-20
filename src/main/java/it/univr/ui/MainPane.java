package it.univr.ui;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;

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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    private String edgeName;
    private double mouseX;
    private double mouseY;

    // ANCHOR - JavaFX variables
    private App app;
    private DigraphEdgeList<String, String> graph = new DigraphEdgeList<>();
    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph, initialPlacement);
    private ContentZoomScrollPane graphPane = new ContentZoomScrollPane(graphView);

    // ANCHOR - Properties
    private SimpleBooleanProperty isVertexSelectedProperty = new SimpleBooleanProperty(false);

    // Icons
    FontIcon magicLayoutIcon = new FontIcon("eli-magic");

    // Buttons
    private Button finalVertex = new Button("Final Vertex");
    private Button initialVertex = new Button("Initial Vertex");
    private Button magiclayoutButton = new Button();

    // Vertex
    private SmartGraphVertexNode selectedVertexNode; // selected UI vertex
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
        graphView.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2 && selectedVertexNode == null) {
                mouseX = e.getX();
                mouseY = e.getY();
                vertexNamePopup();
            }
        });
    }

    private void initSideMenu() {
        // magiclayoutButton.getStyleClass().clear();
        // magiclayoutButton.setAlignment(Pos.CENTER);
        // magicLayoutIcon.setScaleX(1.6);
        // magicLayoutIcon.setScaleY(1.6);
        // magiclayoutButton.setGraphic(magicLayoutIcon);
        // magiclayoutButton.setPrefWidth(50);
        // magiclayoutButton.setPrefHeight(50);
        // sideMenuStatic.getChildren().addAll(magiclayoutButton);
        IconButton magicIcon = new IconButton("ci-magic-wand-filled");
        IconButton nodeIcon = new IconButton("ci-text-creation");

        sideMenuStatic.getChildren().addAll(magicIcon, nodeIcon);
        sideMenuHidedable.getChildren().addAll(finalVertex);
        sideMenuHidedable.getChildren().addAll(initialVertex);

        nodeNameLabel.visibleProperty().bind(isVertexSelectedProperty);
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
    /* //ANCHOR - Create Modal */
    /* -------------------------------------------------------------------------- */

    /**
     * Create a modal window popup
     * 
     * @param root the popup component
     */
    private void createModal(Parent root) {
        Stage stage = new Stage();
        Scene edgeScene = new Scene(root, 300, 160);

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
    /* //ANCHOR - Edge popup */
    /* -------------------------------------------------------------------------- */

    /**
     * Shows a popup asking the user to insert the name of the edge
     * 
     * @param from the outbound vertex
     * @param to   the inbound vertex
     */
    private void edgeNamePopup(SmartGraphVertexNode from, SmartGraphVertexNode to) {
        createModal(new EdgePopup(from, to));
    }

    private void deselectVertex() {
        if (selectedVertexNode != null) {
            setIsVertexSelected(false);
            selectedVertexNode.setStyleClass("vertex");
            selectedVertexNode = null;
        }
    }

    public void addEdge(SmartGraphVertexNode from, SmartGraphVertexNode to) {
        graph.insertEdge(from.getUnderlyingVertex(), to.getUnderlyingVertex(), edgeName);
        graphView.update();
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Error Pupup */
    /* -------------------------------------------------------------------------- */

    /**
     * Shows an error message popup
     * 
     * @param errorMsg the error message to display to th user
     */
    private void showErrorPopup(String errorMsg) {
        createModal(new ErrorPopup(errorMsg));
    }

    /* -------------------------------------------------------------------------- */
    /* /// ANCHOR - Vertex popup */
    /* -------------------------------------------------------------------------- */

    private void vertexNamePopup() {
        createModal(new VertexPopup());
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

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public boolean getIsVertexSelected() {
        return isVertexSelectedProperty.get();
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
                    edgeNamePopup(selectedVertexNode, vertex);
                    isLinkingPhase = false;
                }
            } else {
                selectedVertexNode.setStyleClass("vertex");
                selectedVertexNode = vertex;
                selectedVertexNode.setStyleClass("selectedVertex");
            }
        } else {
            selectedVertexNode = vertex;
            setIsVertexSelected(true);
            nodeNameLabel.textProperty().bind(selectedVertexNode.getAttachedLabel().textProperty());
            selectedVertexNode.setStyleClass("selectedVertex");
        }

    }

    public void setVertexPressed(boolean isVertexPressed) {
        this.isVertexPressed = isVertexPressed;
    }

    public void setEdgeName(String edgeName) {
        this.edgeName = edgeName;
    }

    public void setIsVertexSelected(boolean bool) {
        isVertexSelectedProperty.set(bool);
    }

}
