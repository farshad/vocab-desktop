package com.vocab.view;

import com.vocab.NavigationController;
import com.vocab.model.Chapter;
import com.vocab.model.Course;
import com.vocab.model.Word;
import com.vocab.repository.WordDAO;
import com.vocab.utils.VoiceHelper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

import java.util.concurrent.Executors;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/4/25 - 8:52 AM
 */
public class WordViewer {
    @Getter
    private final Scene scene;
    private int currentIndex = 0;
    private Label title = new Label();
    private Label translate = new Label();
    private final ObservableList<Word> words;
    private String locale;

    public WordViewer(NavigationController navigationController, Course course, Chapter chapter, int currentIndex) {
        this.currentIndex = currentIndex;
        locale = course.getLocale().toString();
        words = WordDAO.fetchWordsByChapterId(chapter.getId());
        updateWordsDisplay();

        Button previousButton = new Button("Pre");
        previousButton.setOnAction(event -> showPreviousWord());

        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> showNextWord());

        Button soundButton = new Button("S");
        soundButton.setOnAction(event -> Executors.newFixedThreadPool(1).submit(() -> VoiceHelper.speak(words.get(this.currentIndex).getTitle(), locale)));
        Button backButton = new Button("<-");
        backButton.setOnAction(event -> navigationController.navigateToWords(course, chapter));
        Button showButton = new Button("Show");
        showButton.setOnAction(e -> translate.setVisible(!translate.isVisible()));

        // Create a layout for the buttons
        HBox buttonBox = new HBox(10, previousButton, nextButton, showButton, soundButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        translate.setVisible(false);
        title.setStyle("-fx-font-size: 24px;");
        translate.setStyle("-fx-font-size: 16px;");

        // Create a layout for the course details
        BorderPane root = new BorderPane();

        root.setCenter(translate);
        root.setTop(title);
        root.setBottom(buttonBox);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(translate, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20));
        BorderPane.setMargin(translate, new Insets(20));

        // Create a scene with the layout
        scene = new Scene(root, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
    }

    private void updateWordsDisplay() {
        if (!words.isEmpty()) {
            Word currentWord = words.get(currentIndex);
            title.setText((currentIndex + 1) + ". " + currentWord.getTitle());
            translate.setText(currentWord.getTranslate());

            Executors.newFixedThreadPool(1).submit(() -> VoiceHelper.speak(currentWord.getTitle(), locale));

        }
    }

    private void showNextWord() {
        if (currentIndex < words.size() - 1) {
            currentIndex++;
            updateWordsDisplay();
        }
    }

    private void showPreviousWord() {
        if (currentIndex > 0) {
            currentIndex--;
            updateWordsDisplay();
        }
    }
}
