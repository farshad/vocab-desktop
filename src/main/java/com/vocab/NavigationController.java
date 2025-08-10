package com.vocab;

import com.vocab.model.Chapter;
import com.vocab.model.Course;
import com.vocab.view.*;
import javafx.stage.Stage;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/4/25 - 8:24 AM
 */
public class NavigationController {
    private final Stage primaryStage;

    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void navigateToCourses() {
        CourseListPage courseListPage = new CourseListPage(this);
        primaryStage.setScene(courseListPage.getScene());
    }

    public void navigateToChapters(Course course) {
        ChapterListPage detailsPage = new ChapterListPage(this, course);
        primaryStage.setScene(detailsPage.getScene());
    }

    public void navigateToWords(Course course, Chapter chapter) {
        WordListPage detailsPage = new WordListPage(this, course, chapter);
        primaryStage.setScene(detailsPage.getScene());
    }

    public void navigateToWordViewer(Course course, Chapter chapter, int currentIndex) {
        WordViewer detailsPage = new WordViewer(this, course, chapter, currentIndex);
        primaryStage.setScene(detailsPage.getScene());
    }

    public void navigateToSettings() {
        SettingsPage settingsPage = new SettingsPage(this);
        primaryStage.setScene(settingsPage.getScene());
    }
}
