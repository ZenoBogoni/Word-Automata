package it.univr.Controller.popups;

import java.io.IOException;

import it.univr.Controller.smallComponents.PathVertexNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TutorialPopup extends AnchorPane {

    @FXML
    private HBox nodesImagesBox;
    @FXML
    private TextFlow tutorial, rules;
    @FXML
    private Button closeButton;

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

    public void initialize() {
        closeButton.setOnAction(e -> {
            ((Stage) getScene().getWindow()).close();
        });
        vertexLegend();
    }

    private void vertexLegend() {
        nodesImagesBox.getChildren().addAll(new PathVertexNode("Initial", true, false));
        nodesImagesBox.getChildren().addAll(new PathVertexNode("Normal", false, false));
        PathVertexNode vertex = new PathVertexNode("Selected", false, false);
        vertex.circle().getStyleClass().add("selectedVertex");
        nodesImagesBox.getChildren().addAll(vertex);
        nodesImagesBox.getChildren().addAll(new PathVertexNode("Final", false, true));
    }
}
