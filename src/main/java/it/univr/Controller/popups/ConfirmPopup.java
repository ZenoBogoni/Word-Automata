package it.univr.Controller.popups;

import java.io.IOException;

import it.univr.backend.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConfirmPopup extends AnchorPane {
    private String confirmType;
    private String confirmString;
    private Runnable onConfirm;

    @FXML
    private Text confirmText, confirmTypeText;
    @FXML
    private Button confirmButton, cancelButton;

    public ConfirmPopup(String confirmType, String confirmString, Runnable onConfirm) {
        this.confirmType = confirmType;
        this.confirmString = confirmString;
        this.onConfirm = onConfirm;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("confirmPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize() {
        confirmTypeText.setText(confirmType);
        confirmText.setText(confirmString);

        cancelButton.setOnAction(e -> {
            ((Stage) getScene().getWindow()).close();
        });

        confirmButton.setOnAction(e -> {
            SceneReference.stopAllAnimations();
            onConfirm.run();
            ((Stage) getScene().getWindow()).close();
        });
    }
}
