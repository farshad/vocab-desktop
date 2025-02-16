package com.vocab.view;

import com.vocab.NavigationController;
import com.vocab.model.Chapter;
import com.vocab.model.Course;
import com.vocab.repository.ChapterDAO;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 10:35 AM
 */
@Getter
public class ChapterListPage {

    private final Scene scene;

    public ChapterListPage(NavigationController navigationController, Course course) {
        ListView<Chapter> chapterListView = new ListView<>();
        ObservableList<Chapter> chapters = ChapterDAO.fetchChaptersByCourseId(course.getId());
        chapterListView.setItems(chapters);
        chapterListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Chapter> call(ListView<Chapter> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Chapter course, boolean empty) {
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

        Label courseLabel = new Label(course.getTitle());

        // Create a Button to navigate back to the course list
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> navigationController.navigateToCourses());

        chapterListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> navigationController.navigateToWords(course, newValue));

        // Create a layout and add the label and button
        BorderPane root = new BorderPane();
        root.setTop(courseLabel);
        root.setCenter(chapterListView);
        root.setBottom(backButton);

        // Create a scene with the layout
        scene = new Scene(root, 300, 200);
        scene.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ESCAPE) {
                backButton.fire();
            }
        });
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
    }
}
