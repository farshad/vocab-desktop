package com.vocab.view;

import com.vocab.NavigationController;
import com.vocab.enums.SettingType;
import com.vocab.model.Chapter;
import com.vocab.model.Course;
import com.vocab.model.Setting;
import com.vocab.model.Word;
import com.vocab.repository.SettingDAO;
import com.vocab.repository.WordDAO;
import com.vocab.utils.Constants;
import com.vocab.utils.VoiceHelper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/4/25 - 8:52 AM
 */
public class WordViewer {
    @Getter
    private final Scene scene;
    private int currentIndex;
    private Label title = new Label();
    private Label translate = new Label();
    private Label fav = new Label("*");
    private final ObservableList<Word> words;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private String locale;
    private AtomicBoolean autoSpeak = new AtomicBoolean(true);
    private AtomicBoolean reverseCard = new AtomicBoolean(false);
    private AtomicBoolean autoStart = new AtomicBoolean(false);
    private AtomicLong autoPlayTime = new AtomicLong(Constants.AUTO_START_TIME);

    public WordViewer(NavigationController navigationController, Course course, Chapter chapter, int passedIndex) {
        List<Setting> settings = SettingDAO.fetchAll();

        settings.forEach(setting -> {
            if (setting.getKey().equals(SettingType.AUTO_SPEAK)) {
                autoSpeak.set(Boolean.parseBoolean(setting.getValue()));
            } else if (setting.getKey().equals(SettingType.REVERSE_CARD)) {
                reverseCard.set(Boolean.parseBoolean(setting.getValue()));
            } else if (setting.getKey().equals(SettingType.AUTO_START)) {
                autoStart.set(Boolean.parseBoolean(setting.getValue()));
            } else if (setting.getKey().equals(SettingType.AUTO_PLAY_TIME)) {
                autoPlayTime.set(Long.parseLong(setting.getValue()));
            }
        });

        currentIndex = passedIndex;
        locale = course.getLocale().toString();
        words = WordDAO.fetchWordsByChapterId(chapter.getId());
        updateWordsDisplay();

        Button previousButton = new Button("Pre");
        previousButton.setFocusTraversable(Boolean.FALSE);
        previousButton.setOnAction(event -> showPreviousWord());

        Button nextButton = new Button("Next");
        nextButton.setFocusTraversable(Boolean.FALSE);
        nextButton.setOnAction(event -> showNextWord());

        Button soundButton = new Button("S");
        soundButton.setFocusTraversable(Boolean.FALSE);
        soundButton.setOnAction(event -> Executors.newFixedThreadPool(1).submit(() -> VoiceHelper.speak(words.get(currentIndex).getTitle(), locale)));
        Button backButton = new Button("<-");
        backButton.setFocusTraversable(Boolean.FALSE);
        backButton.setOnAction(event -> {
            scheduler.shutdownNow();
            navigationController.navigateToWords(course, chapter);
        });
        Button showButton = new Button("Show");
        showButton.setFocusTraversable(Boolean.FALSE);
        showButton.setOnAction(e -> {
            if (reverseCard.get()) {
                title.setVisible(!title.isVisible());
            } else {
                translate.setVisible(!translate.isVisible());
            }
        });
        translate.setOnMouseClicked(event -> Executors.newFixedThreadPool(1).submit(() -> VoiceHelper.speak(words.get(currentIndex).getTranslate(), "en-US")));
        translate.setWrapText(true);

        // Create a layout for the buttons
        HBox buttonBox = new HBox(10, previousButton, nextButton, showButton, soundButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        if (reverseCard.get()) {
            title.setVisible(false);
        } else {
            translate.setVisible(false);
        }
        title.setStyle("-fx-font-size: 24px;");
        translate.setStyle("-fx-font-size: 16px;");

        VBox vBox = new VBox(10, translate, fav);
        vBox.setAlignment(Pos.CENTER);

        // Create a layout for the course details
        BorderPane root = new BorderPane();

        root.setCenter(vBox);
        root.setTop(title);
        root.setBottom(buttonBox);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(translate, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20));
        BorderPane.setMargin(translate, new Insets(20));

        // Create a scene with the layout
        scene = new Scene(root, 300, 200);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case S, DOWN:
                    soundButton.fire();
                    break;
                case F, UP:
                    showButton.fire();
                    break;
                case E:
                    translate.fireEvent(new javafx.scene.input.MouseEvent(
                            javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                            0, 0, 0, 0,
                            MouseButton.PRIMARY, 1,
                            true, true, true, true,
                            true, true, true, true,
                            true, true, null
                    ));
                    break;
                case LEFT, A:
                    previousButton.fire();
                    break;
                case RIGHT, D:
                    nextButton.fire();
                    break;
                case ESCAPE:
                    backButton.fire();
                    break;
                case F1:
                    words.removeIf(w -> !w.getFav());
                    updateWordsDisplay();
                    break;
                case F2:
                    words.get(currentIndex).setFav(true);
                    WordDAO.updateWord(words.get(currentIndex));
                    updateWordsDisplay();
                    break;
                case F3:
                    words.get(currentIndex).setFav(false);
                    WordDAO.updateWord(words.get(currentIndex));
                    updateWordsDisplay();
                    break;
                default:
                    break;
            }
        });
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        if (autoStart.get()) {
            scheduler.scheduleAtFixedRate(() -> javafx.application.Platform.runLater(nextButton::fire), autoPlayTime.get(), autoPlayTime.get(), TimeUnit.SECONDS);
        }
    }

    private void updateWordsDisplay() {
        if (!words.isEmpty()) {
            Word currentWord = words.get(currentIndex);
            title.setText((currentIndex + 1) + ". " + currentWord.getTitle());
            translate.setText(currentWord.getTranslate());
            if (currentWord.getFav()) {
                fav.setStyle("-fx-font-size: 24px; -fx-text-fill: yellow;");
            } else {
                fav.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
            }
            if (autoSpeak.get()) {
                Executors.newFixedThreadPool(1).submit(() -> VoiceHelper.speak(currentWord.getTitle(), locale));
            }
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
