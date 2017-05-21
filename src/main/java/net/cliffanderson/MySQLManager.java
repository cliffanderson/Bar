package net.cliffanderson;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLManager {

    private static Connection connection;
    private static String mysqlUsername, mysqlPassword, mysqlHostname, mysqlDatabase = "";

    MySQLManager(CustomProperties customProperties) {

        // Add defaults
        customProperties.addDefault("mysql.username", "username");
        customProperties.addDefault("mysql.password", "password");
        customProperties.addDefault("mysql.hostname", "hostname");
        customProperties.addDefault("mysql.database", "db");
        customProperties.addDefault("mysql.password.prompt", "true");

        customProperties.save();

        // Load properties
        mysqlUsername = customProperties.get("mysql.username");
        mysqlHostname = customProperties.get("mysql.hostname");
        mysqlDatabase = customProperties.get("mysql.database");

        if(customProperties.get("mysql.password.prompt").equals("true")) {
            mysqlPassword = getPassword();
        } else {
            mysqlPassword = customProperties.get("mysql.password");
        }
    }

    private void setupConnection() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(mysqlUsername);
        dataSource.setPassword(mysqlPassword);
        dataSource.setServerName(mysqlHostname);
        dataSource.setDatabaseName(mysqlDatabase);

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("Could not create MySQL connection:");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Created new MySQL connection: " + connection);
    }

    private static String getPassword() {
        JPasswordField passwordField = new JPasswordField();
        int response = JOptionPane.showConfirmDialog(null, passwordField, "Enter the database password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(response == JOptionPane.OK_OPTION) {
            return new String(passwordField.getPassword());
        }

        System.err.println("User did not enter password");
        System.exit(0);
        return "";
    }

    public ResultSet executeQuery(String sql, Object...args) {
        if(connection == null) {
            setupConnection();
        }

        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(sql);

            for(int i = 1; i <= args.length; i++) {
                statement.setObject(i, args[i - 1]);
            }

            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean executeSQL(String sql, Object...args) {
        if(connection == null) {
            setupConnection();
        }

        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(sql);

            for (int i = 1; i <= args.length; i++) {
                statement.setObject(i, args[i - 1]);
            }

            statement.execute();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
