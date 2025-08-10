package com.vocab.view;

import com.vocab.NavigationController;
import com.vocab.model.Course;
import com.vocab.repository.CourseDAO;
import com.vocab.service.SyncService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.Getter;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 10:35 AM
 */
public class CourseListPage {
    @Getter
    private final Scene scene;
    private final SyncService syncService = new SyncService();

    public CourseListPage(NavigationController navigationController) {
        ListView<Course> courseListView = new ListView<>();
        ObservableList<Course> courses = CourseDAO.fetchAll();
        courseListView.setItems(courses);
        // Create a Button named "Explore"
        Button exploreButton = new Button("Explore");
        Button settingsButton = new Button("Settings");
        Label statusLabel = new Label();
        statusLabel.setVisible(false);
        statusLabel.getStyleClass().add("sync-status");

        exploreButton.setOnAction(event -> {
            exploreButton.setDisable(true);
            statusLabel.setVisible(true);
            statusLabel.setText("Syncing...");
            statusLabel.getStyleClass().remove("error");

            syncService.restart();
            syncService.setOnSucceeded(e -> {
                exploreButton.setDisable(false);
                statusLabel.setText("Sync completed successfully!");
                courseListView.setItems(CourseDAO.fetchAll()); // Refresh the list

                // Auto-hide after 3 seconds
                new Timeline(new KeyFrame(Duration.seconds(3),
                        ae -> statusLabel.setVisible(false))).play();
            });

            syncService.setOnFailed(e -> {
                exploreButton.setDisable(false);
                statusLabel.setText("Sync failed: " + syncService.getException().getMessage());
                statusLabel.getStyleClass().add("error");
            });
        });

        courseListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Course> call(ListView<Course> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Course course, boolean empty) {
                        super.updateItem(course, empty);
                        if (empty || course == null) {
                            setText(null);
                        } else {
                            setText(course.getTitle());
                        }
                    }
                };
            }
        });

        courseListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> navigationController.navigateToChapters(newValue));

        settingsButton.setOnAction(event -> navigationController.navigateToSettings());

        // Create a BorderPane layout
        HBox centerContainer = new HBox(exploreButton, settingsButton, statusLabel);
        BorderPane root = new BorderPane();
        root.setCenter(courseListView);
        root.setBottom(centerContainer);

        // Create a scene with the layout
        scene = new Scene(root, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
    }
}
