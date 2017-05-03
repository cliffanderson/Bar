package net.cliffanderson.handlers;

import net.cliffanderson.BarManager;
import net.cliffanderson.MySQLManager;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cliff on 4/18/17.
 */
public class TabHandler extends CommandHandler {

    public TabHandler(String line) {
        super(line);
    }

    public void handle() {
        Map<Integer, Double> tabs = new HashMap<Integer, Double>();

        String tabSQL = "select people.id, sum(drinks.price * purchases.quantity) as sum from purchases inner join people on " +
               "people.id = purchases.personid inner join drinks on purchases.drinkid = drinks.id group by purchases.personid;";

        String creditSQL = "select personid, sum(amount) from credit group by personid";


        try {
            // Put sum of purchases into hashmap
            ResultSet purchaseResults = MySQLManager.executeQuery(tabSQL);

            while(purchaseResults.next()) {
                int personid = purchaseResults.getInt(1);
                Double purchaseSum = purchaseResults.getDouble(2);

                tabs.put(personid, purchaseSum);
            }


            // Add the credits
            ResultSet creditResults = MySQLManager.executeQuery(creditSQL);

            while(creditResults.next()) {
                int personid = creditResults.getInt(1);
                double creditSum = creditResults.getDouble(2);

                if(tabs.containsKey(personid)) {
                    // Update value in map

                    double purchaseSum = tabs.get(personid);
                    purchaseSum -= creditSum;
                    tabs.put(personid, purchaseSum);
                }
            }

            // Print the results
            for(int personid : tabs.keySet()) {
                System.out.printf("%s: %.2f%n", BarManager.instance.getPerson(personid).getName(), tabs.get(personid));
            }
        } catch (Exception e) {
            System.err.println("Error getting people's tabs");
            e.printStackTrace();
        }
    }
}
