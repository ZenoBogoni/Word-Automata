package it.univr.Controller.panes;

import java.io.IOException;

import com.brunomnsilva.smartgraph.graphview.ForceDirectedLayoutStrategy;
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy;

import it.univr.backend.SceneReference;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MagicLayoutSidePane extends VBox {
    // FXML variables
    @FXML
    private TextField repulsiveForceTextField, attractiveForceTextField, attractionScaleTextField, accellerationTextField;
    @FXML
    private Slider repulsiveForceSlider, attractiveForceSlider, attractionScaleSlider, accellerationSlider;
    @FXML
    private Button applyButton;
    @FXML
    private CheckBox autoLayoutCheckBox;

    // Java variables
    private final double defaultRepulsiveForce = 25;
    private final double defaultAttractiveForce = 0.5;
    private final double defaultAttractionScale = 20;
    private final double defaultAccelleration = 0.5;

    // Properties
    private SimpleDoubleProperty repulsiveForce = new SimpleDoubleProperty(defaultRepulsiveForce);
    private SimpleDoubleProperty attractiveForce = new SimpleDoubleProperty(defaultAttractiveForce);
    private SimpleDoubleProperty attractionScale = new SimpleDoubleProperty(defaultAttractionScale);
    private SimpleDoubleProperty accelleration = new SimpleDoubleProperty(defaultAccelleration);

    // JavaFX variables
    private Label applyLabel = new Label("Apply");
    private ForceDirectedSpringGravityLayoutStrategy<String> springLayout = new ForceDirectedSpringGravityLayoutStrategy<>(repulsiveForce.get(), attractiveForce.get(), attractionScale.get(), accelleration.get(), 0.005);

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
        // Automatic Layout checkbox
        autoLayoutCheckBox.selectedProperty().bindBidirectional(SceneReference.getAutoLayoutProperty());

        // apply button
        applyButton.setGraphic(applyLabel);

        applyButton.setOnAction(e -> {
            springLayout.updateForces(repulsiveForce.get(), attractiveForce.get(), attractionScale.get(), accelleration.get());
            fadeTextChange(applyLabel, "Changes applied", 200);

            PauseTransition timer = new PauseTransition(Duration.seconds(2));
            timer.setOnFinished(event -> {
                fadeTextChange(applyLabel, "Apply", 150);
            });
            timer.play();

        });

        // Repulsive force
        initForce(repulsiveForce, repulsiveForceSlider, repulsiveForceTextField, 0.1, 50);

        // Attractive force
        initForce(attractiveForce, attractiveForceSlider, attractiveForceTextField, 0.1, 5);

        // Attraction scale
        initForce(attractionScale, attractionScaleSlider, attractionScaleTextField, 0.1, 50);

        // accelleration
        initForce(accelleration, accellerationSlider, accellerationTextField, 0.1, 1);
    }

    private void initForce(SimpleDoubleProperty force, Slider slider, TextField textField, double min, double max) {
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(force.get());
        force.bindBidirectional(slider.valueProperty());

        textField.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        textField.setAlignment(Pos.CENTER);
        textField.textProperty().bind(force.asString("%.1f"));
        textField.setTextFormatter(new TextFormatter<Double>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("(\\d*)(\\.?)(\\d?)")) {
                return change;
            }
            return null;
        }));

        textField.setOnAction(e -> {
            updateForceValue(force, textField, min, max);
        });

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.textProperty().unbind();
                if (SceneReference.getClearTextOnClickProperty().get()) {
                    textField.setText(""); // clear text field on input
                }
            } else {
                if (!SceneReference.getConfirmToApplyProperty().get()) {
                    updateForceValue(force, textField, min, max);
                }
                textField.textProperty().bind(force.asString("%.1f"));
            }
        });

    }

    private void updateForceValue(DoubleProperty force, TextField textField, double min, double max) {
        double value;
        if (!textField.getText().isEmpty()) {
            value = Double.parseDouble(textField.getText());
        } else {
            return;
        }

        if (value > max) {
            force.set(max);
            textField.setText(String.valueOf(max));
        } else if (value < min) {
            force.set(min);
            textField.setText(String.valueOf(min));
        } else {
            force.set(value);
        }
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

    public ForceDirectedLayoutStrategy<String> getMagicLayout() {
        return springLayout;
    }
}
