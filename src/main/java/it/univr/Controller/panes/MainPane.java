package it.univr.Controller.panes;

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
import it.univr.Controller.popups.EdgePopup;
import it.univr.Controller.popups.TutorialPopup;
import it.univr.Controller.popups.VertexPopup;
import it.univr.Controller.smallComponents.IconButton;
import it.univr.backend.GraphSaver;
import it.univr.backend.SceneReference;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class MainPane extends BorderPane {

    // ANCHOR - Java variables
    private boolean isSideHidden = false; // is the side panel hidden?

    // ANCHOR - JavaFX variables
    private App app;
    private DigraphEdgeList<String, String> graph = new DigraphEdgeList<>();
    private SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(graph);
    private ContentZoomScrollPane graphPane = new ContentZoomScrollPane(graphView);
    private VBox centerPane = new VBox();
    private SolutionPane solutionPane = SceneReference.getSolutionPane();

    // ANCHOR - Properties
    private SimpleBooleanProperty isVertexSelectedProperty = SceneReference.getIsVertexSelectedProperty();
    private SimpleBooleanProperty confirmToApplyProperty = SceneReference.getConfirmToApplyProperty();
    private SimpleBooleanProperty autoLayoutProperty = SceneReference.getAutoLayoutProperty();
    private SimpleBooleanProperty clearTextOnClickProperty = SceneReference.getClearTextOnClickProperty();

    // ANCHOR - Icons
    IconButton nodeIcon;
    IconButton magicIcon;

    // ANCHOR - Side Panes
    MagicLayoutSidePane magicLayoutSidePane;
    GraphSidePane graphSidePane;

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
    @FXML
    private MenuItem tutorial;

    private void initializeFXMLVariables() {
        file = new Menu("File");
        options = new Menu("Options");
        help = new Menu("Help");

        theme = new CheckMenuItem("Dark Mode");
        autoLayout = new CheckMenuItem("Automatic Layout");
        confirmToApply = new CheckMenuItem("Confirm to apply");
        clearTextOnClick = new CheckMenuItem("Clear values on input");

        tutorial = new MenuItem("Tutorial");
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
        SceneReference.setGraph(graph);
        SceneReference.setGraphView(graphView);
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

        help.getItems().addAll(tutorial);

        tutorial.setOnAction(e -> {
            SceneReference.createModal(new TutorialPopup());
        });

        importGraph.setOnAction(e -> {
            FileChooser fileChooser = GraphSaver.initFileChooser();

            File file = fileChooser.showOpenDialog(SceneReference.getStage());

            if (file != null) {
                GraphSaver.createGraphFromFile(file.getAbsolutePath());
                SceneReference.setFileName(file.getName().substring(0, file.getName().lastIndexOf(".") == -1 ? file.getName().length() : file.getName().lastIndexOf(".")));
                SceneReference.setUnsavedChanges(false);
            }
        });

        exportGraph.setOnAction(e -> {
            FileChooser fileChooser = GraphSaver.initFileChooser();

            File file = fileChooser.showSaveDialog(SceneReference.getStage());

            if (file != null) {
                if (file.exists()) {
                    GraphSaver.createFileFromGraph(graph, file.getAbsolutePath());
                    SceneReference.setFileName(file.getName().substring(0, file.getName().lastIndexOf(".") == -1 ? file.getName().length() : file.getName().lastIndexOf(".")));
                    SceneReference.setUnsavedChanges(false);
                } else {
                    GraphSaver.createFileFromGraph(graph, file.getAbsolutePath() + ".json");
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
            if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2 && SceneReference.getSelectedVertexNode() == null) {
                SceneReference.setMousePostion(e.getX(), e.getY());
                SceneReference.createModal(new VertexPopup());
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
                SceneReference.setLinkingPhase(true);
            }
        });

        this.setOnKeyReleased(key -> {
            if (key.getCode() == KeyCode.CONTROL) {
                SceneReference.setLinkingPhase(false);
            }
        });

        graphPane.setOnMousePressed(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (!SceneReference.isVertexPressed() && !SceneReference.isEdgePressed()) {
                    SceneReference.deselectNodes();
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
}