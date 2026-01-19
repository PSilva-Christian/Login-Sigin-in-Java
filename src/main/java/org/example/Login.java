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

            if (!isValidEmail(email))
                IO.println("Invalid email address, try again.");
        } while(!isValidEmail(email));
        return email;

    }

    boolean isValidEmail(String email) {
        List<String> domainsValid = Arrays.asList(
                "@gmail.com",
                "@yahoo.com",
                "@hotmail.com");

        String domain = email.substring(email.indexOf("@"));

        return email.contains("@") && domainsValid.contains(domain);
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
    User user = null;
    Login userLog = null;

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

boolean insertUserDataBase(User user, ConnectDB sqliteDB, Connection connection) {

    if (connection != null) {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate
                ("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, email TEXT, password TEXT, username TEXT)");
            //IO.println("Table created successfully");

            String inputSigUser = String.format
                    ("INSERT INTO users (email, password, username) VALUES ('%s', '%s', '%s')",
                            user.email, user.password, user.user);

            statement.executeUpdate(inputSigUser);
            IO.println("Signed successfully");
            return true;


        } catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            sqliteDB.closeConnection();
        }
    }
    else {
        //IO.println("Connection with database failed, try again later.");
    }
    return false;
}

String checkIfHaveAccount(Login user, ConnectDB sqliteDB, Connection connection){

    if (connection != null) {
        try {
            Statement statement = connection.createStatement();
            String querySql = String.format("SELECT username FROM users WHERE username = '%s' AND password = '%s'", user.name, user.password);

            ResultSet queryLogin = statement.executeQuery(querySql);

            if (queryLogin != null) {
                //IO.println("Login Successful");
                return queryLogin.getString("username");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            //IO.println("Closing database connection");
            sqliteDB.closeConnection();
        }

    }
    else{
        //IO.println("Connection failed.");
    }

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