package com.vocab.repository;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 5:51 PM
 */

import com.vocab.db.DatabaseUtil;
import com.vocab.model.Chapter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChapterDAO {

    // Method to insert a Chapter
    public static void insertChapter(Chapter chapter) {
        String sql = "INSERT INTO chapters (id, title, course_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, chapter.getId());
            stmt.setString(2, chapter.getTitle());
            stmt.setString(3, chapter.getCourseId());
            stmt.executeUpdate();
            System.out.println("Chapter inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch all Chapters
    public static List<Chapter> fetchAllChapters() {
        List<Chapter> chapters = new ArrayList<>();
        String sql = "SELECT * FROM chapters";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Chapter chapter = new Chapter();
                chapter.setId(rs.getString("id"));
                chapter.setTitle(rs.getString("title"));
                chapter.setCourseId(rs.getString("course_id"));
                chapters.add(chapter);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chapters;
    }

    // Method to fetch Chapters by Course ID
    public static ObservableList<Chapter> fetchChaptersByCourseId(String courseId) {
        ObservableList<Chapter> chapters = FXCollections.observableArrayList();
        String sql = "SELECT * FROM chapters WHERE course_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, courseId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Chapter chapter = new Chapter();
                    chapter.setId(rs.getString("id"));
                    chapter.setTitle(rs.getString("title"));
                    chapter.setCourseId(rs.getString("course_id"));
                    chapters.add(chapter);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chapters;
    }
}

