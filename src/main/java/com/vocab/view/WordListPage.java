package com.vocab.view;

import com.vocab.NavigationController;
import com.vocab.model.Chapter;
import com.vocab.model.Course;
import com.vocab.model.Word;
import com.vocab.repository.WordDAO;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import lombok.Getter;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 10:35 AM
 */
@Getter
public class WordListPage {

    private final Scene scene;

    public WordListPage(NavigationController navigationController, Course course, Chapter chapter) {
        ListView<Word> wordListView = new ListView<>();
        ObservableList<Word> words = WordDAO.fetchWordsByChapterId(chapter.getId());
        wordListView.setItems(words);
        wordListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Word> call(ListView<Word> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Word word, boolean empty) {
                        super.updateItem(word, empty);
                        if (empty || word == null) {
                            setText(null);
                        } else {
                            int index = words.indexOf(word) + 1;
                            setText(index + ". " + word.getTitle());
                        }
                    }
                };
            }
        });

        wordListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> navigationController.navigateToWordViewer(course, chapter, words.indexOf(newValue) ));

        Label courseLabel = new Label(chapter.getTitle());

        // Create a Button to navigate back to the course list
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> navigationController.navigateToChapters(course));

        // Create a layout and add the label and button
        BorderPane root = new BorderPane();
        root.setTop(courseLabel);
        root.setCenter(wordListView);
        root.setBottom(backButton);

        // Create a scene with the layout
        scene = new Scene(root, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
    }
}
