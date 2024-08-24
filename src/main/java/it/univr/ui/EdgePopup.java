package it.univr.ui;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.SmartGraphVertexNode;

import it.univr.utils.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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

    private void checkName() {

        String edgeName = edgeNameField.getText();
        if (edgeName.equals("")) {
            edgeNameField.setPromptText("Enter a valid edge name");
            edgeNameField.setText("");
        } else {
            mainPane.setEdgeName(edgeName); // TODO - controllare che sia possibile creare questo edge
            mainPane.addEdge(from, to);
            stage.close();
        }
    }

    @FXML
    public void initialize() {
        submitButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        edgeNameField.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            checkName();
        });

        cancelButton.setOnAction(e -> {
            this.stage = (Stage) getScene().getWindow();
            stage.close();
        });
    }
}
