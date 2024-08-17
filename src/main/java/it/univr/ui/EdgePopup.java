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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EdgePopup extends AnchorPane {
    private Stage stage;
    private MainPane mainPane = SceneReference.getMainPane();
    private SmartGraphVertexNode from;
    private SmartGraphVertexNode to;
    private DigraphEdgeList<String, String> graph = SceneReference.getGraph();

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField edgeNameField;

    public EdgePopup(Stage stage, SmartGraphVertexNode from, SmartGraphVertexNode to) {
        this.stage = stage;
        this.from = from;
        this.to = to;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edgePopup.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void initialize() {

        submitButton.setOnAction(e -> {
            String edgeName = edgeNameField.getText();
            if (edgeName.equals("")) {
                edgeNameField.setPromptText("Enter a valid edge name");
                edgeNameField.setText("");
            } else {
                mainPane.setEdgeName(edgeName); // TODO - controllare che sia possibile creare questo edge
                mainPane.addEdge(from, to);
                stage.close();
            }
        });

        cancelButton.setOnAction(e -> {
            stage.close();
        });
    }
}
