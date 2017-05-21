package net.cliffanderson.handlers;

import net.cliffanderson.BarManager;
import net.cliffanderson.MySQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestHandler extends CommandHandler {

    public TestHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        System.out.println("Handling the test command");

        ResultSet tables = BarManager.instance.getMysql().executeQuery("show tables;");

        try {
            while (tables.next()) {
                System.out.println("MySQL Table: " + tables.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
