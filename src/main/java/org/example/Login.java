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

void main(){

    int choose = menu();

    if (choose == 0) {
        IO.println("Closing program...");
        return;
    }

    ConnectDB sqliteDB = new ConnectDB();
    Connection connection = sqliteDB.getConnection();
    boolean alrightLog = false;
    User user = null;

    if (choose == 1){

        IO.println("\n\t\t\t-------------------\n\t\t\t--- Log-in Page ---\n\t\t\t-------------------\n\n");
        user = new User();
        alrightLog = checkIfHaveAccount(user, sqliteDB, connection);

    }
    else if (choose == 2) {

        IO.println("\n\t\t\t-------------------\n\t\t\t--- Sig-in Page ---\n\t\t\t-------------------\n\n");
        user = new User();
        alrightLog = insertUserDataBase(user, sqliteDB, connection);
    }

    if (alrightLog) {
        IO.println("\n\t\t\t---------------------------\n\t\t\t--- Welcome back, " + user.user +" ---\n\t\t\t---------------------------\n\n");
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

boolean checkIfHaveAccount(User user, ConnectDB sqliteDB, Connection connection){

    if (connection != null) {
        try {
            Statement statement = connection.createStatement();
            String querySql = String.format("SELECT id FROM users WHERE username = '%s' AND password = '%s'", user.user, user.password);

            ResultSet queryLogin = statement.executeQuery(querySql);

            if (queryLogin != null) {
                IO.println("Login Successful");
                return true;
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

    return false;
}


boolean checkChoice(String choice) {
    return !choice.toUpperCase().contains("LOG") && !choice.toUpperCase().contains("SIG")
            && !choice.toUpperCase().contains("EXI");
}

int menu (){
    String choose;
    int returnMenu = 0;
    IO.println("\n\t\t\t------------------\n\t\t\t--- Login Page ---\n\t\t\t------------------\n\n");

    do {
        IO.print("\tSelect:\n\t- Log -> Login\n\t- Sig -> Sign-in\n\t- Exi - Exit\n\n\tEnter your choice: ");
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
