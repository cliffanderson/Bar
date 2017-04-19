package net.cliffanderson;

import com.mysql.cj.jdbc.MysqlDataSource;

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
        dataSource.setUser("root");
        dataSource.setPassword("Puppet123!");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("bar");

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Creating new MySQL connection: " + connection);
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
