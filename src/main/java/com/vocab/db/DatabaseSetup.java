package com.vocab.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 5:26 PM
 */
public class DatabaseSetup {
    public static void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS courses (\n" +
                "    id VARCHAR(255) PRIMARY KEY,\n" +
                "    title VARCHAR(255) NOT NULL,\n" +
                "    locale VARCHAR(50)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS chapters (\n" +
                "    id VARCHAR(255) PRIMARY KEY,\n" +
                "    title VARCHAR(255) NOT NULL,\n" +
                "    course_id VARCHAR(255),\n" +
                "    FOREIGN KEY (course_id) REFERENCES courses(id)\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS settings (\n" +
                "    id VARCHAR(255) PRIMARY KEY,\n" +
                "    key_ VARCHAR(255) NOT NULL,\n" +
                "    value_ VARCHAR(255),\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS words (\n" +
                "    id VARCHAR(255) PRIMARY KEY,\n" +
                "    chapter_id VARCHAR(255),\n" +
                "    title VARCHAR(255) NOT NULL,\n" +
                "    translate VARCHAR(255),\n" +
                "    example VARCHAR(255),\n" +
                "    fav INTEGER default(0),\n" +
                "    FOREIGN KEY (chapter_id) REFERENCES chapters(id)\n" +
                ");\n";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dropTables() {
        String sql = "DROP TABLE IF EXISTS words;\n" +
                "DROP TABLE IF EXISTS chapters;\n" +
                "DROP TABLE IF EXISTS settings;\n" +
                "DROP TABLE IF EXISTS courses;";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table dropped successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
