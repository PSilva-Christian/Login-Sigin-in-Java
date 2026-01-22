package org.example.models;

import java.sql.*;

public class DatabaseFunctionsUserRelated {

    public static void insertUserDataBase(UserModels.User user, Connection connection) {
        if (connection == null){
            IO.println("Connection failed.");
            return;
        }

        try {

            String inputSigUser = "INSERT INTO users (email, password, username) VALUES (?, ?, ?)";
            String createTable = "CREATE TABLE IF NOT EXISTS users " +
                    "(id INTEGER PRIMARY KEY, email TEXT UNIQUE, password TEXT, username TEXT UNIQUE)";

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTable);
            }

            try (PreparedStatement prepared = connection.prepareStatement(inputSigUser)) {
                prepared.setString(1, user.getEmail());
                prepared.setString(2, user.getPassword());
                prepared.setString(3, user.getUser());

                if (prepared.executeUpdate() > 0) {
                    IO.println("Signed successfully");
                }
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static String checkIfHaveAccount(UserModels.Login user, Connection connection){
        if (connection == null) {
            IO.println("Connection failed.");
            return null;
        }

        String querySql = "SELECT username FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement prepared = connection.prepareStatement(querySql)) {

            prepared.setString(1, user.getName());
            prepared.setString(2, user.getPassword());

            try (ResultSet rs = prepared.executeQuery()) {
                if (rs.next()) {
                    IO.println("\n\t -- Login Successful --");
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        IO.println("\n\nAccount not found or invalid credentials.");
        return null;
    }

    public static void printUserInfo(UserModels.Login user, Connection connection){
        if(connection == null){
            IO.println("\n\tConnection failed.");
            return;
        }

        MenuAndInterface.printCenteredMessage("User information");

        String querySql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement prepared = connection.prepareStatement(querySql)){
            prepared.setString(1, user.getName());
            prepared.setString(2, user.getPassword());

            try (ResultSet result =  prepared.executeQuery()){
                IO.println("\t\tUsername: " + result.getString("username"));
                IO.println("\t\tPassword: " + result.getString("password"));
                IO.println("\t\tEmail: " + result.getString("email"));
                MenuAndInterface.userConfirmNextPage();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        IO.println("Error trying to search user info");
    }

    public static void passwordReset(UserModels.Login user, Connection connection, String newPassword){
        if (connection == null) {
            IO.println("\n\tConnection failed.");
            return;
        }

        MenuAndInterface.printCenteredMessage("Password reset");

        String querySql = "UPDATE users SET password = ? WHERE username = ? AND password = ?";

        try(PreparedStatement prepared = connection.prepareStatement(querySql)){
            prepared.setString(1, newPassword);
            prepared.setString(2, user.getName());
            prepared.setString(3, user.getPassword());

            if (prepared.executeUpdate() > 0){
                IO.println("Password reset successful");
                return;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        IO.println("\n\tPassword reset failed, try again.");

    }
}
