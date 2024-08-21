package it.univr.ui.sidePanes;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.ForceDirectedLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.text.DecimalFormat;

public class MagicLayoutSidePane extends VBox {
    // FXML variables
    @FXML
    private TextField repulsiveForceTextField, attractiveForceTextField, attractionScaleTextField, accellerationTextField;
    @FXML
    private Slider repulsiveForceSlider, attractiveForceSlider, attractionScaleSlider, accellerationSlider;
    @FXML
    private Button applyButton;

    // Java variables
    private final double defaultRepulsiveForce = 50;
    private final double defaultAttractiveForce = 0.5;
    private final double defaultAttractionScale = 5;
    private final double defaultAccelleration = 0.5;

    // Properties
    private SimpleDoubleProperty repulsiveForce = new SimpleDoubleProperty(defaultRepulsiveForce);
    private SimpleDoubleProperty attractiveForce = new SimpleDoubleProperty(defaultAttractiveForce);
    private SimpleDoubleProperty attractionScale = new SimpleDoubleProperty(defaultAttractionScale);
    private SimpleDoubleProperty accelleration = new SimpleDoubleProperty(defaultAccelleration);

    // JavaFX variables
    private Label applyLabel = new Label("Apply");
    private ForceDirectedSpringGravityLayoutStrategy springLayout = new ForceDirectedSpringGravityLayoutStrategy(repulsiveForce.get(), 0.5, 5.0, 0.5, 0.001);

    public MagicLayoutSidePane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("magicLayoutSidePane.fxml"));
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
        // apply button
        applyButton.setGraphic(applyLabel);

        applyButton.setOnAction(e -> {
            springLayout.updateForces(repulsiveForce.get(), 0.5, 5.0, 0.5);
            fadeTextChange(applyLabel, "Changes applied", 200);

            PauseTransition timer = new PauseTransition(Duration.seconds(2));
            timer.setOnFinished(event -> {
                fadeTextChange(applyLabel, "Apply", 150);
            });
            timer.play();

        });

        // Repulsive force
        repulsiveForceSlider.setMin(1);
        repulsiveForceSlider.setMax(9999.9);
        repulsiveForceSlider.setValue(repulsiveForce.get());
        repulsiveForce.bindBidirectional(repulsiveForceSlider.valueProperty());

        repulsiveForceTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        repulsiveForceTextField.setAlignment(Pos.CENTER);
        repulsiveForceTextField.promptTextProperty().bind(repulsiveForce.asString("%.1f"));

        // Attractive force
        attractiveForceSlider.setMin(0.1);
        attractiveForceSlider.setMax(1000);
        attractiveForceSlider.setValue(attractiveForce.get());
        attractiveForce.bindBidirectional(attractiveForceSlider.valueProperty());

        attractiveForceTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        attractiveForceTextField.setAlignment(Pos.CENTER);
        attractiveForceTextField.promptTextProperty().bind(attractiveForce.asString("%.1f"));

        // Attraction scale
        attractionScaleSlider.setMin(0.1);
        attractionScaleSlider.setMax(1000);
        attractionScaleSlider.setValue(attractionScale.get());
        attractionScale.bindBidirectional(attractionScaleSlider.valueProperty());

        attractionScaleTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        attractionScaleTextField.setAlignment(Pos.CENTER);
        attractionScaleTextField.promptTextProperty().bind(attractionScale.asString("%.1f"));

        // accelleration
        accellerationSlider.setMin(0.1);
        accellerationSlider.setMax(1000);
        accellerationSlider.setValue(accelleration.get());
        accelleration.bindBidirectional(accellerationSlider.valueProperty());

        accellerationTextField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        accellerationTextField.setAlignment(Pos.CENTER);
        accellerationTextField.promptTextProperty().bind(accelleration.asString("%.1f"));

    }

    private void fadeTextChange(Label label, String newText, double time) {
        // Create a fade-out transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(time), label);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // When the fade-out is finished, change the text and fade back in
        fadeOut.setOnFinished(event -> {
            label.setText(newText);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(time), label);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });

        fadeOut.play();
    }

    @FXML
    private void resetForcesToDefault() {
        System.out.println("forces resetted to default values");
        repulsiveForce.set(defaultRepulsiveForce);
        attractiveForce.set(defaultAttractiveForce);
        attractionScale.set(defaultAttractionScale);
        accelleration.set(defaultAccelleration);
        springLayout.updateForces(defaultRepulsiveForce, defaultAttractiveForce, defaultAttractionScale, defaultAccelleration);
    }

    public ForceDirectedLayoutStrategy getMagicLayout() {
        return springLayout;
    }
}
