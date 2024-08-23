package it.univr.ui;

import java.io.IOException;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import it.univr.App;
import it.univr.ui.sidePanes.GraphSidePane;
import it.univr.ui.sidePanes.MagicLayoutSidePane;
import it.univr.utils.SceneReference;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainPane extends BorderPane {

    // ANCHOR - Java variables
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
    private SimpleBooleanProperty confirmToApplyProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty autoLayoutProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty clearTextOnClickProperty = new SimpleBooleanProperty(true);

    // ANCHOR - Icons
    IconButton magicIcon;
    IconButton nodeIcon;

    // ANCHOR - Side Panes
    MagicLayoutSidePane magicLayoutSidePane;
    GraphSidePane graphSidePane;

    // Vertex
    @SuppressWarnings("rawtypes")
    private SmartGraphVertexNode selectedVertexNode; // selected UI vertex

    // ANCHOR - FXML elements
    @FXML
    private MenuBar menuBar;
    @FXML
    private VBox sideMenuStatic;
    @FXML
    private ScrollPane sideMenuHidable;
    @FXML
    private HBox sideMenu;
    @FXML
    private Menu file = new Menu("File"), options = new Menu("Options"), help = new Menu("Help");
    @FXML
    private CheckMenuItem theme = new CheckMenuItem("Dark Mode"), autoLayout = new CheckMenuItem("Automatic Layout"), confirmToApply = new CheckMenuItem("Confirm to apply");
    @FXML
    private CheckMenuItem clearTextOnClick = new CheckMenuItem("Clear text on input");

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
    public void initSceneReference() {
        SceneReference.setMainPane(this);
        SceneReference.setAutoLayoutProperty(autoLayoutProperty);
        SceneReference.setConfirmToApplyProperty(confirmToApplyProperty);
        SceneReference.setClearTextOnClickProperty(clearTextOnClickProperty);
        SceneReference.setGraph(graph);
        SceneReference.setGrapView(graphView);
        SceneReference.setIsVertexSelectedProperty(isVertexSelectedProperty);
    }

    public void initMainPane() {
        magicLayoutSidePane = new MagicLayoutSidePane();
        graphSidePane = new GraphSidePane();
        initMenuBar();
        initSideMenu();
        graphView.init();
        graphView.setAutomaticLayoutStrategy(magicLayoutSidePane.getMagicLayout());
        graphView.update();
    }

    private void initMenuBar() {
        // MenuBar
        menuBar.getMenus().addAll(file, options, help);

        // View
        options.getItems().addAll(theme, autoLayout, confirmToApply, clearTextOnClick);

        // DarkMode
        theme.setSelected(true);
        theme.setOnAction(e -> {
            app.changeTheme();
        });

        // Auto Layout
        autoLayout.selectedProperty().bindBidirectional(autoLayoutProperty);
        graphView.automaticLayoutProperty().bindBidirectional(autoLayoutProperty);

        // Confirm to apply
        confirmToApply.selectedProperty().bindBidirectional(confirmToApplyProperty);

        // Clear text on click/input
        clearTextOnClick.selectedProperty().bindBidirectional(clearTextOnClickProperty);

    }

    private void initGraph() {
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

        // icons
        magicIcon = new IconButton("ci-magic-wand-filled", magicLayoutSidePane);
        nodeIcon = new IconButton("ci-text-creation", graphSidePane);
        sideMenuStatic.getChildren().addAll(nodeIcon, magicIcon);

        // layout
        magicLayoutSidePane.setPrefHeight(sideMenuHidable.USE_COMPUTED_SIZE);
        magicLayoutSidePane.setPrefWidth(sideMenu.USE_COMPUTED_SIZE);
        graphSidePane.setPrefHeight(sideMenuHidable.USE_COMPUTED_SIZE);
        graphSidePane.setPrefWidth(sideMenu.USE_COMPUTED_SIZE);
        sideMenuHidable.setFitToWidth(true); // remove space for scrollbar
        sideMenuHidable.setFitToHeight(true); // remove space for scrollbar

        // initial state
        magicIcon.setSelectedtab();
        sideMenuHidable.setContent(magicLayoutSidePane);

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

        graphPane.setOnMousePressed(e -> {
            if (!isVertexPressed) {
                deselectVertex();
            }
        });

    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Methods */
    /* -------------------------------------------------------------------------- */
    public void hideSidePane(VBox sidePane) {
        if (isSideHidden) {
            setSidePane(sidePane);
            sideMenu.getChildren().add(sideMenuHidable);
            sideMenu.setPrefWidth(350);
        } else {
            sideMenu.getChildren().remove(sideMenuHidable);
            sideMenu.setPrefWidth(sideMenuStatic.getWidth());
        }
        isSideHidden = !isSideHidden;
    }

    public void setSidePane(VBox sidePane) {
        sideMenuHidable.setContent(sidePane);
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

    public void setIsLinkingPhase(boolean bool) {
        isLinkingPhase = bool;
    }

}
