package com.vocab.view;

import com.vocab.NavigationController;
import com.vocab.model.Course;
import com.vocab.repository.CourseDAO;
import com.vocab.service.SyncService;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
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
        // Create a ListView to display the courses
        ListView<Course> courseListView = new ListView<>();
        ObservableList<Course> courses = CourseDAO.fetchAll();
        courseListView.setItems(courses);
        // Create a Button named "Explore"
        Button exploreButton = new Button("Explore");
        exploreButton.setOnAction(event -> syncService.start());

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

        // Create a BorderPane layout
        BorderPane root = new BorderPane();
        root.setCenter(courseListView); // Place the ListView in the center
        root.setBottom(exploreButton);  // Place the Button at the bottom

        // Create a scene with the layout
        scene = new Scene(root, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
    }
}
