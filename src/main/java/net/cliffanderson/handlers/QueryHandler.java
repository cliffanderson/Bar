package net.cliffanderson.handlers;

import net.cliffanderson.MySQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryHandler extends CommandHandler {

    public QueryHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        StringBuilder builder = new StringBuilder();

        for(String s : this.args) {
            builder.append(s);
            builder.append(" ");
        }


        ResultSet results = MySQLManager.executeQuery(builder.toString().trim());

        if(results == null) {
            // There was some error while executing the SQL
            // MySQLManager takes care of exception reporting
            return;
        }

        try {
            while (results.next()) {
                for(int i = 1; i <= results.getMetaData().getColumnCount(); i++) {
                    System.out.print(results.getObject(i) + "   ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
