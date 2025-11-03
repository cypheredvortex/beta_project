package dao;

import java.sql.Connection;
import java.sql.SQLException;

import dao.DBConnection;

public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connection successful!");
                System.out.println("Connection object: " + conn);
            } else {
                System.out.println("❌ Connection failed!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to database:");
            e.printStackTrace();
        }
    }
}