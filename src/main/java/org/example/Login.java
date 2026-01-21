import org.example.models.ConnectDB;
import org.example.models.UserModels.*;
import org.example.models.UserLoginFunctions;
import org.example.models.MenuAndInterface;

import java.sql.*;

void main(){

    ConnectDB sqliteDB = new ConnectDB();
    Connection connection = sqliteDB.getConnection();

    try {
        while (true) {

            int choose = MenuAndInterface.menu();
            if (choose == 0) {
                IO.println("\n\tClosing program...");
                return;
            }

            User user;
            Login userLog;

            if (choose == 1) {

                MenuAndInterface.printCenteredMessage("Log-in Page");
                userLog = UserLoginFunctions.createNewLogin();
                String logged = checkIfHaveAccount(userLog, sqliteDB, connection);
                if (logged != null) {
                    MenuAndInterface.printCenteredMessage("Welcome back, " + logged);
                    break;
                }
            } else if (choose == 2) {

                MenuAndInterface.printCenteredMessage("Sig-in Page");
                user = UserLoginFunctions.createNewUser();
                insertUserDataBase(user, sqliteDB, connection);
            }
        }
    }
    finally {
        sqliteDB.closeConnection();
    }
}

public static void insertUserDataBase(User user, ConnectDB sqliteDB, Connection connection) {
    if (connection == null){
        IO.println("Connection failed.");
        return;
    }


    try {

        String inputSigUser = "INSERT INTO users (email, password, username) VALUES (?, ?, ?)";
        String createTable = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, email TEXT, password TEXT, username TEXT)";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTable);
        }

        try (PreparedStatement preparedStatementSig = connection.prepareStatement(inputSigUser)) {
            preparedStatementSig.setString(1, user.getEmail());
            preparedStatementSig.setString(2, user.getPassword());
            preparedStatementSig.setString(3, user.getUser());

            if (preparedStatementSig.executeUpdate() > 0) {
                IO.println("Signed successfully");
            }
        }

    } catch(SQLException e) {
        e.printStackTrace();
    }
}

public static String checkIfHaveAccount(Login user, ConnectDB sqliteDB, Connection connection){
        if (connection == null) {
            IO.println("Connection failed.");
            return null;
        }

        String querySql = "SELECT username FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(querySql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            IO.println(user.getName()+ user.getPassword());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    IO.println("Login Successful");
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        IO.println("\n\nAccount not found or invalid credentials.");
        return null;
    }
