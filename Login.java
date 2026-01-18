public static class SignIn {

    String user;
    String password;
    String email;

    SignIn() {
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

            if (password.length() <= 8)
                IO.println("\nPassword must be at least 8 characters.");

        } while (password.length() > 8);
        return password;
    }

    String createEmail() {

        String email;
        do {
            IO.print("\nEmail: ");
            email = IO.readln();

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

    void printUser(){
        IO.println("\n\t\t\t-----------------\n\t\t\t--- User Page ---\n\t\t\t-----------------\n\n");
        IO.print("Username: " + user);
        IO.print("\nE-mail: " + email);
    }
}

void main(){

    String choose;
    IO.println("\n\t\t\t------------------\n\t\t\t--- Login Page ---\n\t\t\t------------------\n\n");

    do {
        IO.print("\tSelect:\n\t- Log -> Login\n\t- Sig -> Sign-in\n\n\tEnter your choice: ");
        choose = IO.readln();

        if (checkChoice(choose)){
            IO.println("\nWrong choice, try again.\n");
        }

    } while (checkChoice(choose));

    if (choose.equalsIgnoreCase("Log")){

        IO.println("\n\t\t\t-------------------\n\t\t\t--- Log-in Page ---\n\t\t\t-------------------\n\n");
        SignIn userLog = new SignIn();


    }
    else {

        IO.println("\n\t\t\t-------------------\n\t\t\t--- Sig-in Page ---\n\t\t\t-------------------\n\n");
        SignIn userSig = new SignIn();

        userSig.printUser();
    }
}

boolean checkChoice(String choice) {
    return !choice.toUpperCase().contains("LOG") && !choice.toUpperCase().contains("SIG");
}