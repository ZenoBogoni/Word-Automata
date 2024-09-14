package it.univr.Controller.popups;

import java.io.IOException;

import it.univr.backend.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorPopup extends BorderPane {
    private String errorMsg, errorType;
    private Stage stage;
    @FXML
    private Text errorText, titleText;
    @FXML
    private Button closeButton;

    public ErrorPopup(String errorType, String errorMsg) {
        this.errorMsg = errorMsg;
        this.errorType = errorType;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("errorPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize() {
        closeButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            SceneReference.setLinkingPhase(false);
            stage.close();
        });
        titleText.setText(errorType);
        errorText.setText(errorMsg);
    }
}
