import org.example.models.ConnectDB;

import java.sql.*;

public static class User {

    String user;
    String password;
    String email;

    User() {
        this.user = createUsername();
        this.password = createPassword();
        this.email = createEmail();
    }

    String createUsername() {

        String username;
        IO.print("\nUsername: ");
        username = IO.readln();

        return username;
    }

    String createPassword() {

        String password;

        do {
            IO.print("\nPassword: ");
            password = IO.readln();

            if (password.length() < 8)
                IO.println("\nPassword must be at least 8 characters.");

        } while (password.length() < 8);
        return password;
    }

    String createEmail() {

        String email;
        do {
            IO.print("\nEmail: ");
            email = IO.readln();

            if (isValidEmail(email))
                IO.println("Invalid email address, try again.");
        } while(isValidEmail(email));
        return email;

    }

    boolean isValidEmail(String email) {
        List<String> domainsValid = Arrays.asList(
                "@gmail.com",
                "@yahoo.com",
                "@hotmail.com");

        String domain = email.substring(email.indexOf("@"));

        return !email.contains("@") || !domainsValid.contains(domain);
    }

}

public static class Login {
    String name;
    String password;

    Login() {
        this.name = keepUsername();
        this.password = keepPassword();
    }

    String keepUsername() {

        String username;
        IO.print("\nUsername: ");
        username = IO.readln();

        return username;
    }

    String keepPassword() {

        String password;

        do {
            IO.print("\nPassword: ");
            password = IO.readln();

            if (password.length() < 8)
                IO.println("\nPassword must be at least 8 characters.");

        } while (password.length() < 8);
        return password;
    }
}

void main(){

    int choose = menu();

    if (choose == 0) {
        IO.println("\n\tClosing program...");
        return;
    }

    ConnectDB sqliteDB = new ConnectDB();
    Connection connection = sqliteDB.getConnection();
    User user;
    Login userLog;

    if (choose == 1){

        printCenteredMessage("Log-in Page");
        userLog = new Login();
        String logged = checkIfHaveAccount(userLog, sqliteDB, connection);
        if (logged != null) {
            printCenteredMessage("Welcome back, " + logged);
        }
    }
    else if (choose == 2) {

        printCenteredMessage("Sig-in Page");
        user = new User();
        insertUserDataBase(user, sqliteDB, connection);
    }
}

void insertUserDataBase(User user, ConnectDB sqliteDB, Connection connection) {
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
            preparedStatementSig.setString(1, user.email);
            preparedStatementSig.setString(2, user.password);
            preparedStatementSig.setString(3, user.user);

            if (preparedStatementSig.executeUpdate() > 0) {
                IO.println("Signed successfully");
            }
        }

    } catch(SQLException e) {
        e.printStackTrace();
    }
    finally {
        IO.println("Closing Connection");
        sqliteDB.closeConnection();

    }
}

String checkIfHaveAccount(Login user, ConnectDB sqliteDB, Connection connection){
        if (connection == null) {
            IO.println("Connection failed.");
            return null;
        }

        String querySql = "SELECT username FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(querySql)) {

            pstmt.setString(1, user.name);
            pstmt.setString(2, user.password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    IO.println("Login Successful");
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            IO.println("Closing database connection");
            sqliteDB.closeConnection();
        }

        IO.println("Account not found or invalid credentials.");
        return null;
    }

boolean checkChoice(String choice) {
    return !choice.toUpperCase().contains("LOG") && !choice.toUpperCase().contains("SIG")
            && !choice.toUpperCase().contains("EXI");
}

int menu (){
    String choose;
    int returnMenu = 0;
    printCenteredMessage("Log-in Page");

    do {
        IO.print("\tSelect:\n\t- Log -> Login\n\t- Sig -> Sign-in\n\t- Exi -> Exit\n\n\tEnter your choice: ");
        choose = IO.readln();

        if (checkChoice(choose)){
            IO.println("\nWrong choice, try again.\n");
        }

    } while (checkChoice(choose));

    if (choose.toUpperCase().contains("SIG"))
        returnMenu = 2;
    else if (choose.toUpperCase().contains("LOG"))
        returnMenu = 1;

    return returnMenu;
}

void printCenteredMessage(String message){
    int times = message.length() + 8;
    IO.print("\n\t\t\t");
    for (int i = 0; i < times; i++) {
        IO.print("-");
    }
    System.out.printf("\n\t\t\t--- %s ---\n\t\t\t", message);
    for (int j = 0; j < times; j++) {
        IO.print("-");
    }
    IO.print("\n\n");
}