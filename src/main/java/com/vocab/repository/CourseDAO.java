package com.vocab.repository;

import com.vocab.db.DatabaseUtil;
import com.vocab.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 3:33 PM
 */
public class CourseDAO {

    public static void insert(Course course) {
        String sql = "INSERT INTO courses (id, title, locale) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getId());
            stmt.setString(2, course.getTitle());
            stmt.setString(3, course.getLocale() != null ? course.getLocale().toString() : null);
            stmt.executeUpdate();
            System.out.println("Course inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertAll(List<Course> courses) {
        courses.forEach(CourseDAO::insert);
    }

    // Method to fetch all Courses
    public static ObservableList<Course> fetchAll() {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        String sql = "SELECT * FROM courses";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getString("id"));
                course.setTitle(rs.getString("title"));
                String locale = rs.getString("locale");
                if (locale != null) {
                    course.setLocale(Locale.forLanguageTag(locale));
                }
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }
}
