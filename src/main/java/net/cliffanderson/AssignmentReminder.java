package net.cliffanderson;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by cliff on 3/18/17.
 */
public class AssignmentReminder {

    static MysqlDataSource datasource;
    static Connection conn;

    public AssignmentReminder() {

    }

    void addAssignment() {

    }

    void removeAssignment() {

    }

    void getAssignments() {

    }

    void test() throws Exception {
        ResultSet results = conn.prepareStatement("select * from people;").executeQuery();

        while(results.next()) {
            System.out.printf("\tid:\t%d  \tname:\t%s  \t\tage:\t%d\n", results.getInt(1), results.getString(2), results.getInt(3));
        }
    }
}
