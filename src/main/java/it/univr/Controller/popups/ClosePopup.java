package it.univr.Controller.popups;

import java.io.File;
import java.io.IOException;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;

import it.univr.backend.GraphSaver;
import it.univr.backend.SceneReference;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ClosePopup extends AnchorPane {

    @FXML
    private Button dontButton, cancelButton, saveButton;

    public ClosePopup() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("closePopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize() {
        Platform.runLater(() -> saveButton.requestFocus());

        dontButton.setOnAction(e -> {
            Platform.exit();
        });

        cancelButton.setOnAction(e -> {
            ((Stage) cancelButton.getScene().getWindow()).close();
        });

        saveButton.setOnAction(e -> {
            FileChooser fileChooser = GraphSaver.initFileChooser();
            File file = fileChooser.showSaveDialog(SceneReference.getStage());
            DigraphEdgeList<String, String> graph = SceneReference.getGraph();

            if (file != null) {
                if (file.exists()) {
                    GraphSaver.createFileFromGraph(graph, file.getAbsolutePath());
                    Platform.exit();
                } else {
                    GraphSaver.createFileFromGraph(graph, file.getAbsolutePath() + ".json");
                    Platform.exit();
                }
            }
        });
    }
}
