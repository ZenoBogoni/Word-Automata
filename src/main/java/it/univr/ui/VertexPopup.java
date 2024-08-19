package it.univr.ui;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.utils.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class VertexPopup extends AnchorPane {
    private Stage stage;
    private MainPane mainPane = SceneReference.getMainPane();
    private SmartGraphVertexNode from;
    private SmartGraphVertexNode to;
    private DigraphEdgeList<String, String> graph = SceneReference.getGraph();

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField vertexNameField;

    public VertexPopup() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vertexPopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void checkName() {
        DigraphEdgeList graph = SceneReference.getGraph();
        String vertexName = vertexNameField.getText();
        if (vertexName.equals("")) {
            vertexNameField.setPromptText("Enter a valid vertex name");
            vertexNameField.setText("");
        } else {
            if (graph.existsVertexWith(vertexName)) {
                vertexNameField.setText("");
                vertexNameField.setPromptText("Vertex name already taken");
            } else {
                graph.insertVertex(vertexName);
                SceneReference.getGrapView().update();
                stage.close();
            }
        }
    }

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        vertexNameField.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        cancelButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            stage.close();
        });
    }
}
