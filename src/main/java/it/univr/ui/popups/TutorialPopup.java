package it.univr.ui.popups;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class TutorialPopup extends AnchorPane {
    public TutorialPopup() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tutorialPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
