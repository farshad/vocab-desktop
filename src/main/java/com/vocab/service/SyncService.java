package com.vocab.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocab.db.DatabaseSetup;
import com.vocab.model.Course;
import com.vocab.repository.ChapterDAO;
import com.vocab.repository.CourseDAO;
import com.vocab.repository.WordDAO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.vocab.utils.Constants.BASE_URL;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 11:31 AM
 */
public class SyncService extends Service<ObservableValue<Boolean>> {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected Task<ObservableValue<Boolean>> createTask() {
        DatabaseSetup.dropTables();
        DatabaseSetup.createTables();

        return new Task<>() {
            @Override
            protected ObservableValue<Boolean> call() {
                List<Course> courses = fetchCourses();
                importData(courses);

                return new SimpleBooleanProperty(true);
            }
        };
    }

    private void importData(List<Course> courses) {
        courses.forEach(course -> {
            CourseDAO.insert(course);
            course.getChapters().forEach(chapter -> {
                chapter.setCourseId(course.getId());
                ChapterDAO.insertChapter(chapter);
                chapter.getWords().forEach(word -> {
                    word.setChapterId(chapter.getId());
                    WordDAO.insertWord(word);
                });
            });
        });

    }

    private List<Course> fetchCourses() {
        // Create an HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "selections"))
                .GET()
                .build();

        try {
            // Send the request synchronously
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response into a list of Course objects
            return objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Course.class));
        } catch (Exception e) {
            System.err.println("Failed to fetch courses: " + e.getMessage());
            return List.of(); // Return an empty list in case of an error
        }
    }
}
