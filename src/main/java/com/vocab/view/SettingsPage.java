package com.vocab.view;

import com.vocab.NavigationController;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import lombok.Getter;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 8/10/25 - 8:39 AM
 */
public class SettingsPage {
    @Getter
    private final Scene scene;

    public SettingsPage(NavigationController navigationController) {
        // Create checkboxes for settings
        CheckBox autoSpeakCheckBox = new CheckBox("Auto Speak");
        CheckBox reverseCardCheckBox = new CheckBox("Reverse Card");

        // Optional: Add labels or tooltips if needed
        Label titleLabel = new Label("Settings");
        titleLabel.getStyleClass().add("settings-title");

        // Layout container
        VBox layout = new VBox(15, titleLabel, autoSpeakCheckBox, reverseCardCheckBox);
        layout.setPadding(new javafx.geometry.Insets(20));
        layout.getStyleClass().add("settings-container");

        // Create scene
        scene = new Scene(layout, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
    }
}
