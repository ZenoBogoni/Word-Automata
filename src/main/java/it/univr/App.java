package it.univr;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import it.univr.ui.MainPane;
import it.univr.utils.Constants;

/**
 * JavaFX App
 */
public class App extends Application {

    private Stage stage;
    private Scene scene;
    private boolean isDarkMode = true;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        // ATLANTA
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());

        MainPane mainPane = new MainPane(this);
        this.scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.setTitle("Word Automata");
        stage.setMinHeight(Constants.HEIGHT);
        stage.setMinWidth(Constants.WIDTH);
        stage.setScene(scene);
        stage.show();

        mainPane.initMainPane();

    }

    public void changeTheme() {
        if (isDarkMode) {
            Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
        }
        isDarkMode = !isDarkMode;
    }

    public void runApp(String[] args) {
        launch(args);
    }

}