package com.vocab.repository;

import com.vocab.db.DatabaseUtil;
import com.vocab.enums.SettingType;
import com.vocab.model.Setting;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 3:33 PM
 */
public class SettingDAO {

    public static void insert(Setting setting) {
        deleteByKey(setting.getKey());
        String sql = "INSERT INTO settings (key_, value_) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, setting.getKey().toString());
            stmt.setString(2, setting.getValue());
            stmt.executeUpdate();
            System.out.println("Setting inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteByKey(SettingType key) {
        String sql = "DELETE FROM settings WHERE key_ = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Setting> fetchAll() {
        ObservableList<Setting> settings = FXCollections.observableArrayList();
        String sql = "SELECT * FROM settings";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Setting setting = new Setting();
                setting.setId(rs.getString("id"));
                setting.setKey(SettingType.valueOf(rs.getString("key_")));
                setting.setValue(rs.getString("value_"));
                settings.add(setting);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return settings;
    }
}
