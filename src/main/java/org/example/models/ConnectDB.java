package org.example.models;

// Uncomment line 3 to import all classes from java.sql.* for database operations.

//import java.sql.*;

// Alternatively, import only the necessary classes.

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;


public class ConnectDB {

    private Connection connection;

    public ConnectDB() {

        String url = "jdbc:sqlite:usersData.db"; // Specify your database URL

        try {

            connection = DriverManager.getConnection(url);

            //IO.println("Connected to database successfully");

        } catch (SQLException e) {

            //IO.println("Unable to establish database connection");

            e.printStackTrace();

        }

    }
    public Connection getConnection() {

        return connection;

    }


    public void closeConnection() {

        try {

            if (connection != null) {

                //IO.println("Connection closed successfully");

                connection.close();

            }

        } catch (SQLException e) {
            //IO.println("Unable to close connection");
            e.printStackTrace();
        }

    }

}