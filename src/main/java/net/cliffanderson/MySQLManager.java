package net.cliffanderson;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLManager {

    private static Connection connection;
    private static String mysqlUsername, mysqlPassword, mysqlHostname, mysqlDatabase = "";

    private static void setupConnection() {
        loadProperties();

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

    private static void loadProperties() {
        File jarFolder;

        try {
            jarFolder = new File(MySQLManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();

            File propertiesFile = new File(jarFolder, "bar.properties");
            if(!propertiesFile.exists()) {
                if(!propertiesFile.createNewFile()) {
                    System.err.println("Could not create properties file");
                    System.exit(1);
                }
            }

            // Add defaults
            Properties properties = new Properties();
            loadProperties(properties, propertiesFile);
            addDefaultProperty("mysql.username", "username", properties);
            addDefaultProperty("mysql.password", "password", properties);
            addDefaultProperty("mysql.hostname", "hostname", properties);
            addDefaultProperty("mysql.database", "db", properties);
            addDefaultProperty("mysql.password.prompt", "true", properties);

            saveProperties(properties, propertiesFile);

            // Load properties
            mysqlUsername = properties.getProperty("mysql.username");
            mysqlHostname = properties.getProperty("mysql.hostname");
            mysqlDatabase = properties.getProperty("mysql.database");

            if(properties.getProperty("mysql.password.prompt").equals("true")) {
                mysqlPassword = getPassword();
            } else {
                mysqlPassword = properties.getProperty("mysql.password");
            }

        } catch (Exception e) {
            System.err.println("Error getting path to properties file");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void addDefaultProperty(Object key, Object value, Properties properties) {
        if(!properties.containsKey(key)) {
            properties.put(key, value);
        }
    }

    private static void loadProperties(Properties properties, File file) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Error loading properties from: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    private static void saveProperties(Properties properties, File file) {
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + file.getAbsolutePath());
            return;
        }

        try {
            properties.store(outputStream, "MySQLManager properties");
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Exception while storing properties: ");
            e.printStackTrace();
        }
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

    public static ResultSet executeQuery(String sql, Object...args) {
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

    public static boolean executeSQL(String sql, Object...args) {
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
