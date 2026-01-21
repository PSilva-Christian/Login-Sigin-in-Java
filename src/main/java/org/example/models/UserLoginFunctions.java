package org.example.models;

import org.example.models.UserModels.*;
import java.util.List;
import java.util.Arrays;

public class UserLoginFunctions {

    public static User createNewUser() {
        String name = createUsername();
        String password = createPassword();
        String email = createEmail();
        return new User(name, password, email);
    }

    public static Login createNewLogin() {
        String name = createUsername();
        String password = createPassword();
        return new Login(name, password);
    }

    public static String createUsername() {

        String username;
        IO.print("\nUsername: ");
        username = IO.readln();

        return username;
    }

    public static String createPassword() {

        String password;

        do {
            IO.print("\nPassword: ");
            password = IO.readln();

            if (password.length() < 8)
                IO.println("\nPassword must be at least 8 characters.");

        } while (password.length() < 8);
        return password;
    }

    public static String createEmail() {

        String email;
        do {
            IO.print("\nEmail: ");
            email = IO.readln();

            if (isValidEmail(email))
                IO.println("Invalid email address, try again.");
        } while (isValidEmail(email));
        return email;

    }

    public static boolean isValidEmail(String email) {
        List<String> domainsValid = Arrays.asList(
                "@gmail.com",
                "@yahoo.com",
                "@hotmail.com");

        String domain = email.substring(email.indexOf("@"));

        return !email.contains("@") || !domainsValid.contains(domain);
    }
}