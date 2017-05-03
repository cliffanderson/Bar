package net.cliffanderson;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by cliff on 3/25/17.
 */
public class MySQLManager {
    private static Connection connection;

    private static void setupConnection() {
        MysqlDataSource dataSource;

        dataSource = new MysqlDataSource();
        dataSource.setUser("baruser");
        String pass = getPassword();
        dataSource.setPassword(pass);
        dataSource.setServerName("cliffanderson.net");
        dataSource.setDatabaseName("bar");

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println("Creating new MySQL connection: " + connection);
    }

    private static String getPassword() {
        try {
            File passwordFile = new File("/tmp/bar_password");
            if(passwordFile.exists()) {
                return FileUtils.readFileToString(passwordFile, Charset.defaultCharset()).replace("\n", "");
            }
        } catch (Exception e){} //If there is an exception, it's probably running on windows

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
