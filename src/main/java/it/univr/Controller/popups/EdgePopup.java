package it.univr.Controller.popups;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.Controller.panes.MainPane;
import it.univr.backend.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EdgePopup extends AnchorPane {
    private Stage stage;
    private MainPane mainPane = SceneReference.getMainPane();
    private SmartGraphVertexNode<String> from;
    private SmartGraphVertexNode<String> to;

    @FXML
    private Button cancelButton, submitButton;
    @FXML
    private TextField edgeNameField;
    @FXML
    private Text errorText;

    public EdgePopup(SmartGraphVertexNode<String> from, SmartGraphVertexNode<String> to) {
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

    private void addEdge() {
        String edgeName = edgeNameField.getText();
        if (edgeName.equals("")) {
            errorText.setText("Enter a non blank edge name");
            edgeNameField.setText("");
        } else {
            if (!SceneReference.getGraph().isDeterministic(from.getUnderlyingVertex(), edgeName)) {
                errorText.setText("This vertex has an outgoing edge with the same name");
                edgeNameField.setText("");
                return;
            }
            SceneReference.stopAllAnimations();
            Edge<String, String> newEgde = SceneReference.getGraph().insertEdge(from.getUnderlyingVertex(), to.getUnderlyingVertex(), edgeName);
            SceneReference.getGraphView().updateAndWait();
            SceneReference.setSelectedEdge(SceneReference.getGraphView().getEdgeNodeOf(newEgde));
            SceneReference.setUnsavedChanges(true);
            stage.close();
        }
    }

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            addEdge();
        });

        edgeNameField.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            addEdge();
        });

        cancelButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            stage.close();
        });
    }
}
