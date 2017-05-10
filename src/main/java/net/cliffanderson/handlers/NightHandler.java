package net.cliffanderson.handlers;

import net.cliffanderson.BarManager;
import net.cliffanderson.MySQLManager;
import net.cliffanderson.obj.Drink;
import net.cliffanderson.obj.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class NightHandler extends CommandHandler {

    public NightHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        // Use optional name specifier
        String name = "%";

        if(args.size() >= 1) {
            Person person = BarManager.instance.getPerson(args.get(0));
            if(person == null) {
                System.err.println("Error: " + args.get(0) + " is not a valid person");
                return;
            }
            name = name + args.get(0);
        }

        String sql = "select p.name, d.name, pur.quantity, d.price from purchases pur " +
                "inner join people p on p.id = pur.personid " +
                "inner join drinks d on d.id = pur.drinkid " +

                "where pur.time > DATE_SUB(NOW(), INTERVAL 96 HOUR) and " +
                "p.name like ?";

        ResultSet results = MySQLManager.executeQuery(sql, name);
        double totalCost = 0.0;

        try {
            while (results.next()) {
                Person person = BarManager.instance.getPerson(results.getString(1));
                Drink drink = BarManager.instance.getDrink(results.getString(2));
                double cost = results.getInt(3) * results.getDouble(4);

                totalCost += cost;

                System.out.printf("Name: %s%sDrink: %s%sCost: %s%n", person.getName(), String.format("%" + (15 - person.getName().length()) + "s", " "), drink.getName(), String.format("%" + (15 - drink.getName().length()) + "s", " "),  new DecimalFormat("##0.00").format(cost));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.printf("%nTotal: %10.2f%n", totalCost);
    }
}
