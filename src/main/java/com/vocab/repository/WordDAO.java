package com.vocab.repository;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 5:56 PM
 */

import com.vocab.db.DatabaseUtil;
import com.vocab.model.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordDAO {

    // Method to insert a Word
    public static void insertWord(Word word) {
        String sql = "INSERT INTO words (id, chapter_id, title, translate, example) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, word.getId());
            stmt.setString(2, word.getChapterId());
            stmt.setString(3, word.getTitle());
            stmt.setString(4, word.getTranslate());
            stmt.setString(5, word.getExample());
            stmt.executeUpdate();
            System.out.println("Word inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch all Words
    public static List<Word> fetchAllWords() {
        List<Word> words = new ArrayList<>();
        String sql = "SELECT * FROM words";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Word word = new Word();
                word.setId(rs.getString("id"));
                word.setChapterId(rs.getString("chapter_id"));
                word.setTitle(rs.getString("title"));
                word.setTranslate(rs.getString("translate"));
                word.setExample(rs.getString("example"));
                words.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return words;
    }

    // Method to fetch Words by Chapter ID
    public static ObservableList<Word> fetchWordsByChapterId(String chapterId) {
        ObservableList<Word> words = FXCollections.observableArrayList();
        String sql = "SELECT * FROM words WHERE chapter_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, chapterId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Word word = new Word();
                    word.setId(rs.getString("id"));
                    word.setChapterId(rs.getString("chapter_id"));
                    word.setTitle(rs.getString("title"));
                    word.setTranslate(rs.getString("translate"));
                    word.setExample(rs.getString("example"));
                    words.add(word);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return words;
    }
}

