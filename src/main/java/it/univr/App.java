package it.univr;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import it.univr.ui.MainPane;
import it.univr.ui.popups.ClosePopup;
import it.univr.utils.Constants;
import it.univr.utils.SceneReference;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private Stage stage;
    private Scene scene;
    private static boolean isDarkMode = true;
    private MainPane mainPane;
    SmartGraphPanel<String, String> graphView;

    @Override
    public void start(Stage stage) throws IOException {
        SceneReference.setApp(this);
        this.stage = stage;
        SceneReference.setStage(stage);

        // Atlanta fx default theme
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

        // Scene initialization
        mainPane = new MainPane(this);
        mainPane.initSceneReference();

        this.scene = new Scene(mainPane, Constants.WIDTH, Constants.HEIGHT);

        this.scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.F11 || key.isAltDown() && key.getCode() == KeyCode.ENTER) {
                this.stage.setFullScreen(!this.stage.isFullScreen());
            }
        });

        applyDarkStyleSheet(scene);

        // Stage initialization
        this.stage.setScene(scene);
        this.stage.getIcons().add(new Image(getClass().getResource("assets/WordAutomataIcon7.png").toExternalForm()));
        this.stage.titleProperty().bind(SceneReference.getFileNameProperty());
        this.stage.setMinHeight(Constants.HEIGHT);
        this.stage.setMinWidth(Constants.WIDTH);
        this.stage.setScene(scene);
        this.stage.setOnCloseRequest(e -> {
            if (SceneReference.getUnsavedChanges()) {
                SceneReference.createModal(new ClosePopup());
                e.consume();
            } else {
                Platform.exit();
            }
        });
        this.stage.show();

        mainPane.initMainPane();
    }

    public void changeTheme() {
        graphView = SceneReference.getGraphView();
        if (isDarkMode) {
            Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
            applyLightStyleSheet(scene);
        } else {
            Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
            applyDarkStyleSheet(scene);
        }
        graphView.changeGraphTheme();
        ;
        isDarkMode = !isDarkMode;
    }

    public void runApp(String[] args) {
        launch(args);
    }

    public static void applyDarkStyleSheet(Scene scene) {
        scene.getStylesheets().add(SceneReference.getApp().getClass().getResource("stylesheets/mainPane-dark.css").toExternalForm());
        scene.getStylesheets().add(SceneReference.getApp().getClass().getResource("stylesheets/smartgraph-dark.css").toExternalForm());
        scene.getStylesheets().removeAll(SceneReference.getApp().getClass().getResource("stylesheets/mainPane-light.css").toExternalForm());
        scene.getStylesheets().removeAll(SceneReference.getApp().getClass().getResource("stylesheets/smartgraph-light.css").toExternalForm());
    }

    public static void applyLightStyleSheet(Scene scene) {
        scene.getStylesheets().add(SceneReference.getApp().getClass().getResource("stylesheets/mainPane-light.css").toExternalForm());
        scene.getStylesheets().add(SceneReference.getApp().getClass().getResource("stylesheets/smartgraph-light.css").toExternalForm());
        scene.getStylesheets().removeAll(SceneReference.getApp().getClass().getResource("stylesheets/mainPane-dark.css").toExternalForm());
        scene.getStylesheets().removeAll(SceneReference.getApp().getClass().getResource("stylesheets/smartgraph-dark.css").toExternalForm());

    }

    public static boolean isDarkMode() {
        return isDarkMode;
    }

}