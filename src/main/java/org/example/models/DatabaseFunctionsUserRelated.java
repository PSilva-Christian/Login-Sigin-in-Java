package org.example.models;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

import static org.example.models.MenuAndInterface.notificationMessage;

public class DatabaseFunctionsUserRelated {

    public static void insertUserDataBase(UserModels.User user, Connection connection) {
        if (connection == null){
            notificationMessage("Connection failed.");
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
                prepared.setString(2, generateBcryptPassword(user.getPassword()));
                prepared.setString(3, user.getUser());

                if (prepared.executeUpdate() > 0) {
                    notificationMessage("Signed successfully");
                }
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static String checkIfHaveAccount(UserModels.Login user, Connection connection){
        if (connection == null) {
            notificationMessage("Connection failed.");
            return null;
        }

        String querySql = "SELECT password FROM users WHERE username = ?";

        try (PreparedStatement prepared = connection.prepareStatement(querySql)) {

            prepared.setString(1, user.getName());

            try (ResultSet rs = prepared.executeQuery()) {
                if (rs.next()) {
                    if (checkPasswordMatchBcrypt(user.getPassword(), rs.getString("password"))) {
                        notificationMessage("Login Successful");
                        return "True";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        notificationMessage("Account not found or invalid credentials.");
        return null;
    }

    public static void printUserInfo(UserModels.Login user, Connection connection){
        if(connection == null){
            notificationMessage("Connection failed.");
            return;
        }

        MenuAndInterface.printCenteredMessage("User information");

        String querySql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement prepared = connection.prepareStatement(querySql)){
            prepared.setString(1, user.getName());

            try (ResultSet result =  prepared.executeQuery()){
                IO.println("\t\tUsername: " + result.getString("username"));
                IO.println("\t\tPassword: " + user.getPassword());
                IO.println("\t\tEmail: " + result.getString("email"));
                MenuAndInterface.userConfirmNextPage();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        notificationMessage("Error trying to search user info");
    }

    public static void passwordReset(UserModels.Login user, Connection connection, String newPassword){
        if (connection == null) {
            notificationMessage("Connection failed.");
            return;
        }

        String querySql = "UPDATE users SET password = ? WHERE username = ?";

        try(PreparedStatement prepared = connection.prepareStatement(querySql)){
            prepared.setString(1, newPassword);
            prepared.setString(2, user.getName());

            if (prepared.executeUpdate() > 0){
                notificationMessage("Password reset successful");
                return;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        notificationMessage("Password reset failed, try again.");

    }

    public static String generateBcryptPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkPasswordMatchBcrypt(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }

}
