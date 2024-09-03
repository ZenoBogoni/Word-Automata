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

        setStyleDark();

        mainPane = new MainPane(this);
        mainPane.initSceneReference();

        this.scene = new Scene(mainPane, Constants.WIDTH, Constants.HEIGHT);

        this.scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.F11 || key.isAltDown() && key.getCode() == KeyCode.ENTER) {
                this.stage.setFullScreen(!this.stage.isFullScreen());
            }
        });

        addStyleSheetToScene("stylesheets/mainPane-dark.css");

        initStage(stage);

        mainPane.initMainPane();
    }

    private void initStage(Stage stage) {
        this.stage.setScene(scene);
        this.stage.setTitle("Word Automata");
        this.stage.setMinHeight(Constants.HEIGHT);
        this.stage.setMinWidth(Constants.WIDTH);
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void changeTheme() {
        graphView = SceneReference.getGraphView();
        if (isDarkMode) {
            setLightMode();
        } else {
            setDarkMode();
        }
        graphView.changeGraphTheme();
        isDarkMode = !isDarkMode;
    }

    private void setDarkMode() {
        setStyleDark();
        addStyleSheetToScene("stylesheets/mainPane-dark.css");
        removeStyleSheetFromScene("stylesheets/mainPane-light.css");
    }

    private void setLightMode() {
        setStyleLight();
        addStyleSheetToScene("stylesheets/mainPane-light.css");
        removeStyleSheetFromScene("stylesheets/mainPane-dark.css");
    }

    private void setStyleDark() {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
    }

    private void setStyleLight() {
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
    }

    private void addStyleSheetToScene(String cssStyle) {
        scene.getStylesheets().add(getClass().getResource(cssStyle).toExternalForm());
    }

    private void removeStyleSheetFromScene(String cssStyle) {
        scene.getStylesheets().removeAll(getClass().getResource(cssStyle).toExternalForm());
    }

    public void runApp(String[] args) {
        launch(args);
    }

}