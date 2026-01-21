package org.example.models;

public class UserModels {

    public static class User {

        private final String user;
        private final String password;
        private final String email;

        User(String user, String password, String email) {
            this.user = user;
            this.password = password;
            this.email = email;
        }

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail(){
            return email;
        }
    }

    public static class Login {

        private final String name;
        private final String password;

        Login(String name,  String password) {
            this.name = name;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }

    }

}
