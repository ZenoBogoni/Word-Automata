package it.univr;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import it.univr.ui.MainPane;
import it.univr.utils.Constants;
import it.univr.utils.SceneReference;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private Stage stage;
    private Scene scene;
    private boolean isDarkMode = true;
    private MainPane mainPane;
    SmartGraphPanel<String, String> graphView;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        SceneReference.setStage(stage);

        // ATLANTA
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

        mainPane = new MainPane(this);
        mainPane.initSceneReference();

        this.scene = new Scene(mainPane, Constants.WIDTH, Constants.HEIGHT);
        this.scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.F11 || key.isAltDown() && key.getCode() == KeyCode.ENTER) {
                this.stage.setFullScreen(!this.stage.isFullScreen());
            }
        });
        String css = getClass().getResource("stylesheets/mainPane-dark.css").toExternalForm();
        this.scene.getStylesheets().add(css);
        this.stage.setScene(scene);
        this.stage.setTitle("Word Automata");
        this.stage.setMinHeight(Constants.HEIGHT);
        this.stage.setMinWidth(Constants.WIDTH);
        this.stage.setScene(scene);
        this.stage.show();

        mainPane.initMainPane();

    }

    public void changeTheme() {
        graphView = SceneReference.getGraphView();
        if (isDarkMode) {
            Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
            scene.getStylesheets().add(getClass().getResource("stylesheets/mainPane-light.css").toExternalForm());
            scene.getStylesheets().removeAll(getClass().getResource("stylesheets/mainPane-dark.css").toExternalForm());
        } else {
            Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
            scene.getStylesheets().add(getClass().getResource("stylesheets/mainPane-dark.css").toExternalForm());
            scene.getStylesheets().removeAll(getClass().getResource("stylesheets/mainPane-light.css").toExternalForm());
        }
        graphView.changeGraphTheme();
        isDarkMode = !isDarkMode;
    }

    public void runApp(String[] args) {
        launch(args);
    }

}