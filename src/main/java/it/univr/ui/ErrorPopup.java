package it.univr.ui;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ErrorPopup extends BorderPane {
    private String errorMsg;
    private Stage stage;
    @FXML
    private Text errorText;
    @FXML
    private Button closeButton;

    public ErrorPopup(Stage stage, String errorMsg) {
        this.stage = stage;
        this.errorMsg = errorMsg;
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
            stage.close();
        });
        errorText.setText(errorMsg);
    }
}
