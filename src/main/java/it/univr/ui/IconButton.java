package it.univr.ui;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.css.converter.InsetsConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

public class IconButton extends Button {

    private static IconButton selectedIconButton;

    private FontIcon icon;

    public IconButton(String iconName) {
        icon = new FontIcon(iconName);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("iconButton.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void initialize() {
        this.getStyleClass().clear();
        setPrefSize(50.0, 50.0);
        // icon.setStyle("-fx-border-width: 0 0 0 2; -fx-border-color: red; -fx-border-style: solid;");
        icon.getStyleClass().add("icon");
        setPadding(Insets.EMPTY);
        setGraphic(icon);

        setOnMouseClicked(e -> {
            if (selectedIconButton != null && !selectedIconButton.equals(this)) {
                selectedIconButton.getStyleClass().removeAll("selected-tab");
                selectedIconButton.getStyleClass().add("tab");
                selectedIconButton.getIcon().getStyleClass().removeAll("selected-icon");
                selectedIconButton.getIcon().getStyleClass().add("icon");
                selectedIconButton = this;
                selectedIconButton.getStyleClass().add("selected-tab");
                selectedIconButton.getStyleClass().removeAll("tab");
                selectedIconButton.getIcon().getStyleClass().add("selected-icon");
                selectedIconButton.getIcon().getStyleClass().removeAll("icon");
            } else {
                selectedIconButton = this;
                selectedIconButton.getStyleClass().add("selected-tab");
                selectedIconButton.getStyleClass().removeAll("tab");
                selectedIconButton.getIcon().getStyleClass().add("selected-icon");
                selectedIconButton.getIcon().getStyleClass().removeAll("icon");
            }
        });
    }

    FontIcon getIcon() {
        return icon;
    }
}
