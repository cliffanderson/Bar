package net.cliffanderson.handlers;

import net.cliffanderson.MySQLManager;

import java.sql.ResultSet;

/**
 * Created by cliff on 4/18/17.
 */
public class TabHandler extends CommandHandler {

    public TabHandler(String line) {
        super(line);
    }

    public void handle() {
        // Print out all the tabs
        String sql = "select people.name, sum(price * quantity) as sum from purchases inner join people on " +
               "people.id = purchases.personid inner join drinks on purchases.drinkid = drinks.id group by personid;";

        try {
            ResultSet results = MySQLManager.executeQuery(sql);

            while(results.next()) {
                String name = results.getString(1);
                Double tab = results.getDouble(2);

                System.out.printf("%s's tab: %f%n", name, tab);
            }
        } catch (Exception e) {
            System.err.println("Error getting people's tabs");
            e.printStackTrace();
        }
    }
}
