package it.univr.ui.popups;

import java.io.IOException;

import it.univr.ui.smallComponents.PathVertexNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TutorialPopup extends AnchorPane {

    @FXML
    private HBox nodesImagesBox;

    @FXML
    private TextFlow tutorial;
    @FXML
    private TextFlow rules;

    public TutorialPopup() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tutorialPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        initVertexImages();
        initRulesTextFlow();
        initTutorialTextFlow();
    }

    private void initVertexImages() {
        nodesImagesBox.getChildren().addAll(new PathVertexNode("Initial", true, false));
        nodesImagesBox.getChildren().addAll(new PathVertexNode("Final", false, true));
        nodesImagesBox.getChildren().addAll(new PathVertexNode("Normal", false, false));
        PathVertexNode vertex = new PathVertexNode("Selected", false, false);
        vertex.circle().getStyleClass().add("selectedVertex");
        nodesImagesBox.getChildren().addAll(vertex);
    }

    private void initRulesTextFlow() {

    }

    private void initTutorialTextFlow() {
        Text createVertex = new Text("Double click to create a vertex");
        createVertex.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-fill: gray;");
        tutorial.getChildren().add(createVertex);
    }
}
