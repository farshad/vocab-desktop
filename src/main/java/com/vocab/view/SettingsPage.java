package com.vocab.view;

import com.vocab.NavigationController;
import com.vocab.db.DatabaseSetup;
import com.vocab.enums.SettingType;
import com.vocab.model.Setting;
import com.vocab.repository.SettingDAO;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 8/10/25 - 8:39 AM
 */
public class SettingsPage {
    @Getter
    private final Scene scene;

    public SettingsPage(NavigationController navigationController) {
        DatabaseSetup.createTables();

        List<Setting> settings = SettingDAO.fetchAll();

        // Create checkboxes for settings
        CheckBox autoSpeakCheckBox = new CheckBox("Auto Speak");
        CheckBox reverseCardCheckBox = new CheckBox("Reverse Card");

        settings.forEach(setting -> {
            if (setting.getKey().equals(SettingType.AUTO_SPEAK)) {
                autoSpeakCheckBox.setSelected(Boolean.parseBoolean(setting.getValue()));
            } else if (setting.getKey().equals(SettingType.REVERSE_CARD)) {
                reverseCardCheckBox.setSelected(Boolean.parseBoolean(setting.getValue()));
            }
        });

        Button saveButton = new Button("Save");

        saveButton.setOnAction(event -> {
            Boolean isAutoSpeakEnabled = autoSpeakCheckBox.isSelected();
            Boolean isReverseCardEnabled = reverseCardCheckBox.isSelected();

            System.out.println("Auto Speak: " + isAutoSpeakEnabled);
            System.out.println("Reverse Card: " + isReverseCardEnabled);

            Setting autoSpeak = new Setting();
            autoSpeak.setKey(SettingType.AUTO_SPEAK);
            autoSpeak.setValue(isAutoSpeakEnabled.toString());

            Setting reverseCard = new Setting();
            reverseCard.setKey(SettingType.REVERSE_CARD);
            reverseCard.setValue(isReverseCardEnabled.toString());

            SettingDAO.insert(autoSpeak);
            SettingDAO.insert(reverseCard);
            navigationController.navigateToCourses();
        });

        // Optional: Add labels or tooltips if needed
        Label titleLabel = new Label("Settings");
        titleLabel.getStyleClass().add("settings-title");

        // Layout container
        // Layout
        VBox layout = new VBox(15, titleLabel, autoSpeakCheckBox, reverseCardCheckBox, saveButton);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("settings-container");

        // Create scene
        scene = new Scene(layout, 300, 200);
        scene.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ESCAPE) {
                navigationController.navigateToCourses();
            }
        });
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
    }
}
