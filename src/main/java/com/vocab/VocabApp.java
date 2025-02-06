package com.vocab;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/4/25 - 8:25 AM
 */
public class VocabApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo_16.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo_32.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo_64.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo_128.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo_256.png")));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo_512.png")));
        primaryStage.setTitle("Vocab");
        NavigationController navigationController = new NavigationController(primaryStage);
        navigationController.navigateToCourses();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
