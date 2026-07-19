package com.foodorder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/tss_evaluation1_food_ordering_console_app";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Nensi1385";

    private DatabaseConnection() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Database Connected Successfully.");
        } catch (ClassNotFoundException e){
            System.out.println("Error " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
