package it.univr.ui;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

import javax.swing.plaf.basic.BasicTreeUI.SelectionModelPropertyChangeHandler;

import org.kordamp.ikonli.javafx.FontIcon;

import it.univr.utils.SceneReference;
import javafx.css.converter.InsetsConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;

public class IconButton extends Button {

    private static IconButton selectedTab;
    private MainPane mainPane;

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
        mainPane = SceneReference.getMainPane();

        this.getStyleClass().clear(); // clear default button style
        setPrefSize(50.0, 50.0); // set button size
        icon.getStyleClass().add("icon"); // style icon
        setPadding(Insets.EMPTY); // remove padding
        setGraphic(icon);

        setOnMouseClicked(e -> {
            if (selectedTab != null && !selectedTab.equals(this)) {
                removeTabSelection();
                setSelectedtab();
            } else if (selectedTab == null) {
                setSelectedtab();
                mainPane.hideSidePanel();
            } else {
                mainPane.hideSidePanel();
                removeTabSelection();
                selectedTab = null;
            }
        });
    }

    void setSelectedtab() {
        selectedTab = this;
        selectedTab.getStyleClass().add("selected-tab");
        selectedTab.getStyleClass().removeAll("tab");
        selectedTab.getIcon().getStyleClass().add("selected-icon");
        selectedTab.getIcon().getStyleClass().removeAll("icon");
    }

    void removeTabSelection() {
        selectedTab.getStyleClass().removeAll("selected-tab");
        selectedTab.getStyleClass().add("tab");
        selectedTab.getIcon().getStyleClass().removeAll("selected-icon");
        selectedTab.getIcon().getStyleClass().add("icon");
    }

    FontIcon getIcon() {
        return icon;
    }
}
