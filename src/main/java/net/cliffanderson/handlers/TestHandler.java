package net.cliffanderson.handlers;

import net.cliffanderson.MySQLManager;
import net.cliffanderson.handlers.CommandHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by cliff on 3/24/17.
 */
public class TestHandler extends CommandHandler {

    public TestHandler(String line) {
        super(line);
    }

    public void handle() {
        System.out.println("Handling the test command");

        ResultSet tables = MySQLManager.executeQuery("show tables;");

        try {
            while (tables.next()) {
                System.out.println("MySQL Table: " + tables.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
