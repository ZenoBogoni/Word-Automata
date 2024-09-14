package it.univr;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import it.univr.Controller.panes.MainPane;
import it.univr.Controller.popups.ClosePopup;
import it.univr.backend.SceneReference;
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
    private MainPane mainPane;
    private static boolean isDarkMode = true;

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

        this.scene = new Scene(mainPane, SceneReference.WIDTH, SceneReference.HEIGHT);

        this.scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.F11 || key.isAltDown() && key.getCode() == KeyCode.ENTER) {
                this.stage.setFullScreen(!this.stage.isFullScreen());
            }
        });

        SceneReference.applyDarkStyleSheet(scene);

        // Stage initialization
        this.stage.setScene(scene);
        this.stage.getIcons().add(new Image(getClass().getResource("assets/WordAutomataIcon7.png").toExternalForm()));
        this.stage.titleProperty().bind(SceneReference.getFileNameProperty());
        this.stage.setMinHeight(SceneReference.HEIGHT);
        this.stage.setMinWidth(SceneReference.WIDTH);
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
        SmartGraphPanel<String, String> graphView = SceneReference.getGraphView();

        if (isDarkMode()) {
            Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
            SceneReference.applyLightStyleSheet(scene);
        } else {
            Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
            SceneReference.applyDarkStyleSheet(scene);
        }
        graphView.changeGraphTheme();

        isDarkMode = !isDarkMode();
    }

    public static boolean isDarkMode() {
        return isDarkMode;
    }

    public void runApp(String[] args) {
        launch(args);
    }

}