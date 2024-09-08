package it.univr.ui;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import com.brunomnsilva.smartgraph.containers.ContentZoomScrollPane;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import it.univr.App;
import it.univr.ui.popups.EdgePopup;
import it.univr.ui.popups.VertexPopup;
import it.univr.ui.sidePanes.GraphSidePane;
import it.univr.ui.sidePanes.MagicLayoutSidePane;
import it.univr.ui.sidePanes.SolutionPane;
import it.univr.ui.smallComponents.IconButton;
import it.univr.utils.SceneReference;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class MainPane extends BorderPane {

    // ANCHOR - Java variables
    private static boolean isLinkingPhase = false; // are we trying to link vertices?
    private boolean isSideHidden = false; // is the side panel hidden?
    private boolean isVertexPressed = false; // is a vertex being pressed?
    private double mouseX;
    private double mouseY;

    // ANCHOR - JavaFX variables
    private App app;
    private DigraphEdgeList<String, String> graph = new DigraphEdgeList<>();
    private SmartPlacementStrategy initialPlacement = new SmartCircularSortedPlacementStrategy();
    private SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph, initialPlacement);
    private ContentZoomScrollPane graphPane = new ContentZoomScrollPane(graphView);
    private VBox centerPane = new VBox();
    private SolutionPane solutionPane = new SolutionPane();

    // ANCHOR - Properties
    private SimpleBooleanProperty isVertexSelectedProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty confirmToApplyProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty autoLayoutProperty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty clearTextOnClickProperty = new SimpleBooleanProperty(true);
    private SimpleBooleanProperty initialVertexSetProperty = new SimpleBooleanProperty(false);

    // ANCHOR - Icons
    IconButton nodeIcon;
    IconButton magicIcon;

    // ANCHOR - Side Panes
    MagicLayoutSidePane magicLayoutSidePane;
    GraphSidePane graphSidePane;

    // Vertex
    private SmartGraphVertexNode<String> selectedVertexNode, initialVertexNode;
    private HashSet<SmartGraphVertexNode<String>> finalVerticesNodes = new HashSet<SmartGraphVertexNode<String>>();

    // ANCHOR - FXML elements
    @FXML
    private MenuBar menuBar;
    @FXML
    private VBox sideMenuStatic;
    @FXML
    private ScrollPane sideMenuHidable;
    @FXML
    private HBox sideMenu;

    // Menus
    @FXML
    private Menu file;
    @FXML
    private Menu options;
    @FXML
    private Menu help;

    // Menu Items
    @FXML
    private CheckMenuItem theme;
    @FXML
    private CheckMenuItem autoLayout;
    @FXML
    private CheckMenuItem confirmToApply;
    @FXML
    private CheckMenuItem clearTextOnClick;
    @FXML
    private MenuItem exportGraph;
    @FXML
    private MenuItem importGraph;

    private void initializeFXMLVariables() {
        file = new Menu("File");
        options = new Menu("Options");
        help = new Menu("Help");

        theme = new CheckMenuItem("Dark Mode");
        autoLayout = new CheckMenuItem("Automatic Layout");
        confirmToApply = new CheckMenuItem("Confirm to apply");
        clearTextOnClick = new CheckMenuItem("Clear values on input");

        exportGraph = new MenuItem("Save Automata");
        importGraph = new MenuItem("Open Automata");
    }

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
        initializeFXMLVariables();
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
        SceneReference.setGraphView(graphView);
        SceneReference.setInitialVertexSetProperty(initialVertexSetProperty);
        SceneReference.setInitialVertexNode(initialVertexNode);
        SceneReference.setFinalVerticesNodes(finalVerticesNodes);
        SceneReference.setIsVertexSelectedProperty(isVertexSelectedProperty);
        SceneReference.setSolutionPane(solutionPane);
    }

    public void initMainPane() {
        magicLayoutSidePane = new MagicLayoutSidePane();
        graphSidePane = new GraphSidePane();
        SceneReference.setGraphSidePane(graphSidePane);
        initMenuBar();
        initSideMenu();
        graphView.init();
        graphView.setAutomaticLayoutStrategy(magicLayoutSidePane.getMagicLayout());
        graphView.update();
    }

    private void initMenuBar() {
        // MenuBar
        menuBar.getMenus().addAll(file, options, help);

        // file managment
        file.getItems().addAll(importGraph, exportGraph);

        importGraph.setOnAction(e -> {
            FileChooser fileChooser = SceneReference.initFileChooser();

            File file = fileChooser.showOpenDialog(SceneReference.getStage());

            if (file != null) {
                SceneReference.createGraphFromFile(file.getAbsolutePath());
                SceneReference.setFileName(file.getName().substring(0, file.getName().lastIndexOf(".") == -1 ? file.getName().length() : file.getName().lastIndexOf(".")));
                SceneReference.setUnsavedChanges(false);
            }
        });

        exportGraph.setOnAction(e -> {
            FileChooser fileChooser = SceneReference.initFileChooser();

            File file = fileChooser.showSaveDialog(SceneReference.getStage());

            if (file != null) {
                if (file.exists()) {
                    SceneReference.createFileFromGraph(graph, file.getAbsolutePath());
                    SceneReference.setFileName(file.getName().substring(0, file.getName().lastIndexOf(".") == -1 ? file.getName().length() : file.getName().lastIndexOf(".")));
                    SceneReference.setUnsavedChanges(false);
                } else {
                    SceneReference.createFileFromGraph(graph, file.getAbsolutePath() + ".json");
                    SceneReference.setFileName(file.getName().substring(0, file.getName().lastIndexOf(".") == -1 ? file.getName().length() : file.getName().lastIndexOf(".")));
                    SceneReference.setUnsavedChanges(false);
                }
            }
        });

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
        centerPane.getChildren().addAll(graphPane, solutionPane);
        VBox.setVgrow(graphPane, Priority.ALWAYS);
        // graphPane.setBackground(Background.fill(Color.RED));
        this.setCenter(centerPane);
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
        magicLayoutSidePane.setPrefHeight(-1);
        magicLayoutSidePane.setPrefWidth(-1);
        graphSidePane.setPrefHeight(-1);
        graphSidePane.setPrefWidth(-1);
        sideMenuHidable.setFitToWidth(true); // remove space for scrollbar
        sideMenuHidable.setFitToHeight(true); // remove space for scrollbar

        // initial state
        nodeIcon.setSelectedtab();
        sideMenuHidable.setContent(graphSidePane);

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
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (!isVertexPressed && !SceneReference.isEdgePressed()) {
                    deselectNodes();
                }
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
    /* //ANCHOR - Edge popup */
    /* -------------------------------------------------------------------------- */

    /**
     * Shows a popup asking the user to insert the name of the edge
     * 
     * @param from the outbound vertex
     * @param to   the inbound vertex
     */
    private void edgeNamePopup(SmartGraphVertexNode<String> from, SmartGraphVertexNode<String> to) {
        SceneReference.createModal(new EdgePopup(from, to));
    }

    public void deselectNodes() {
        deselectVertex();
        if (SceneReference.isEdgeSelected()) {
            SceneReference.deselectEdge();
        }
    }

    public void deselectVertex() {
        if (selectedVertexNode != null) {
            setIsVertexSelected(false);
            selectedVertexNode.removeStyleClass("selectedVertex");
            selectedVertexNode = null;
        }
    }

    /* -------------------------------------------------------------------------- */
    /* /// ANCHOR - Vertex popup */
    /* -------------------------------------------------------------------------- */

    private void vertexNamePopup() {
        SceneReference.createModal(new VertexPopup());
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Getters */
    /* -------------------------------------------------------------------------- */

    public SmartGraphVertexNode<String> getSelectedVertexNode() {
        return selectedVertexNode;
    }

    public SmartGraphVertexNode<String> getInitialVertexNode() {
        return initialVertexNode;
    }

    public HashSet<SmartGraphVertexNode<String>> getFinalVerticesNodes() {
        return finalVerticesNodes;
    }

    public boolean isVertexPressed() {
        return isVertexPressed;
    }

    public DigraphEdgeList<String, String> getGraph() {
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

    public boolean isLinkingPhase() {
        return isLinkingPhase;
    }

    /* -------------------------------------------------------------------------- */
    /* //ANCHOR - Setters */
    /* -------------------------------------------------------------------------- */

    public void setSelectedVertexNode(SmartGraphVertexNode<String> vertex) {
        if (selectedVertexNode != null) {

            if (isLinkingPhase) {
                if (graph.areAdjacent(selectedVertexNode.getUnderlyingVertex(), vertex.getUnderlyingVertex())) {
                    SceneReference.showErrorPopup("Edge already exists", "An edge from vertex " + selectedVertexNode.getAttachedLabel().getText() + " to vertex " + vertex.getAttachedLabel().getText()
                            + " already exists, please modify the existing one instead of creating a new one");
                } else {
                    edgeNamePopup(selectedVertexNode, vertex);
                    isLinkingPhase = false;
                }
            } else {
                if (selectedVertexNode.equals(vertex)) {
                    graphSidePane.focusVertexField();
                    return;
                }
                selectedVertexNode.removeStyleClass("selectedVertex");
                isVertexSelectedProperty.set(false);
                selectedVertexNode = vertex;
                isVertexSelectedProperty.set(true);
                selectedVertexNode.addStyleClassLast("selectedVertex");
                graphSidePane.focusVertexField();
            }
        } else {
            selectedVertexNode = vertex;
            setIsVertexSelected(true);
            selectedVertexNode.addStyleClassLast("selectedVertex");
            graphSidePane.focusVertexField();
            solutionPane.insertVertexNode(selectedVertexNode);
        }

    }

    public void setVertexPressed(boolean isVertexPressed) {
        this.isVertexPressed = isVertexPressed;
    }

    public void setEdgeName(String edgeName) {
    }

    public void setIsVertexSelected(boolean bool) {
        isVertexSelectedProperty.set(bool);
    }

    public void setIsLinkingPhase(boolean bool) {
        isLinkingPhase = bool;
    }

}