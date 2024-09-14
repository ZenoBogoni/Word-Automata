package it.univr.Controller.smallComponents;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;

import it.univr.Controller.panes.MainPane;
import it.univr.backend.SceneReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class IconButton extends Button {

    private static IconButton selectedTab;
    private MainPane mainPane;
    private VBox sidePane;

    private FontIcon icon;

    public IconButton(String iconName, VBox sidePane) {
        icon = new FontIcon(iconName);
        this.sidePane = sidePane;

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
    public void initialize() {
        mainPane = SceneReference.getMainPane();

        this.getStyleClass().clear(); // clear default button style
        setPrefSize(50.0, 50.0); // set button size
        icon.getStyleClass().add("icon"); // style icon
        setPadding(Insets.EMPTY); // remove padding
        setGraphic(icon);

        setOnMouseClicked(e -> {
            if (selectedTab != null && !selectedTab.equals(this)) { // switch to this tab
                removeTabSelection();
                setSelectedtab();
                mainPane.setSidePane(sidePane);
            } else if (selectedTab == null) { // open side pane
                setSelectedtab();
                mainPane.hideSidePane(sidePane);
            } else { // close side pane
                mainPane.hideSidePane(sidePane);
                removeTabSelection();
                selectedTab = null;
            }
        });
    }

    public void setSelectedtab() {
        selectedTab = this;
        selectedTab.getStyleClass().add("selected-tab");
        selectedTab.getStyleClass().removeAll("tab");
        selectedTab.getIcon().getStyleClass().add("selected-icon");
        selectedTab.getIcon().getStyleClass().removeAll("icon");
    }

    public void removeTabSelection() {
        selectedTab.getStyleClass().removeAll("selected-tab");
        selectedTab.getStyleClass().add("tab");
        selectedTab.getIcon().getStyleClass().removeAll("selected-icon");
        selectedTab.getIcon().getStyleClass().add("icon");
    }

    public FontIcon getIcon() {
        return icon;
    }
}
