package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class OracleConnectionExample {
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "SYSTEM";
    private static final String PASSWORD = "123456";

    public Connection connectToOracle() {
        Connection connection = null;
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");


            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertRandomRecords(Connection connection, int count) {
        String insertQuery = "INSERT INTO BOOK (ID, NAME, ISBN, CREATE_DATE) VALUES (?, ?, ?, sysdate)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                preparedStatement.setInt(1, i + 1); // ID
                preparedStatement.setString(2, generateRandomString(128)); // NAME
                preparedStatement.setString(3, generateRandomString(32)); // ISBN
                preparedStatement.executeUpdate();
            }
            System.out.println(count + " kayıt başarıyla eklendi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayRecords(Connection connection) {
        String selectQuery = "SELECT * FROM BOOK";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String isbn = resultSet.getString("ISBN");
                System.out.println("ID: " + id + ", NAME: " + name + ", ISBN: " + isbn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connectAndProcess() {
        Connection connection = connectToOracle();
        if (connection != null) {
            insertRandomRecords(connection, 100);
            displayRecords(connection);

            try {

                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }
        return randomString.toString();
    }
}
