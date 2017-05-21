package net.cliffanderson;

import net.cliffanderson.obj.Alias;
import net.cliffanderson.obj.Drink;
import net.cliffanderson.obj.Person;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BarManager {

    public static BarManager instance;
    private MySQLManager mysql;

    private List<Drink> drinks;
    private List<Person> people;
    private List<Alias> aliases;

    BarManager() {
        File jarFolder;

        try {
            jarFolder = new File(MySQLManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
        } catch (URISyntaxException e) {
            System.err.println("Exception getting folder containing jar");
            return;
        }

        File propertiesFile = new File(jarFolder, "bar.properties");
        CustomProperties customProperties = new CustomProperties(propertiesFile);

        mysql = new MySQLManager(customProperties);

        this.loadDrinks();
        this.loadPeople();
        this.loadAliases();

        instance = this;
    }

    public MySQLManager getMysql() {
        return this.mysql;
    }

    private void loadDrinks() {
        this.drinks = new ArrayList<Drink>();

        ResultSet results = this.mysql.executeQuery("select id, name, price from drinks;");

        if (results == null) {
            System.err.println("Error: Could not load drinks");
            return;
        }

        try {
            while (results.next()) {
                int id = results.getInt(1);
                String drink = results.getString(2);
                Double price = results.getDouble(3);

                this.drinks.add(new Drink(id, drink, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Drink> getDrinks() {
        return this.drinks;
    }

    public Drink getDrink(String name) {
        for(Drink d : this.drinks) {
            if(d.getName().equals(name)) return d;
        }

        return null;
    }

    private void loadPeople() {
        this.people = new ArrayList<Person>();

        ResultSet results = this.mysql.executeQuery("select id, name from people;");

        try {
            while (results.next()) {
                this.people.add(new Person(results.getInt(1), results.getString(2)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Person> getPeople() {
        return this.people;
    }

    public Person getPerson(String name) {
        for(Person p : this.people) {
            if(p.getName().equals(name)) return p;
        }

        return null;
    }

    public Person getPerson(int id) {
        for(Person p : this.people) {
            if(p.getId() == id) return p;
        }

        return null;
    }

    private void loadAliases() {
        this.aliases = new ArrayList<Alias>();

        ResultSet results = this.mysql.executeQuery("select a.name, a.drinkid from aliases a inner join drinks d on d.id = a.drinkid");

        try {
            while(results.next()) {
                String aliasName = results.getString(1);
                int drinkid = results.getInt(2);

                Drink drink = null;
                for(Drink d : this.drinks) {
                    if(d.getId() == drinkid) drink = d;
                }

                // If drink is still null after the for loop, no matching drink was found
                // (this should not happen with inner join in the query)

                if(drink == null) {
                    System.err.printf("No drink with id %d was found%n", drinkid);
                    continue;
                }

                this.aliases.add(new Alias(aliasName, drink));
            }
        } catch (Exception e) {
            System.err.println("Error loading aliases:");
            e.printStackTrace();
        }
    }

    public List<Alias> getAliases() {
        return this.aliases;
    }

    public Drink getDrinkByAlias(String aliasName) {
        for(Alias alias : this.aliases) {
            if(alias.getName().equalsIgnoreCase(aliasName)) return alias.getDrink();
        }

        return null;
    }

    /**
     * Print an itemized tab for a specific person
     * @param person The person
     */
    public void printItemizedTabForPerson(Person person) {
        printItemizedTab(person.getName());
    }

    /**
     * Print an itemized tab with drinks for all people found by the search string
     * @param searchString The string to search by (may use % as wildcard)
     */
    public void printItemizedTab(String searchString) {
        System.out.println("\n\nRecent drinks for " + (searchString.equals("%") ? "everyone" : searchString.replace("%", "")) + ":");

        String sql = "select p.name, d.name, pur.quantity, d.price from purchases pur " +
                "inner join people p on p.id = pur.personid " +
                "inner join drinks d on d.id = pur.drinkid " +

                "where pur.time > DATE_SUB(NOW(), INTERVAL 96 HOUR) and " +
                "p.name like ?";

        ResultSet results = this.mysql.executeQuery(sql, searchString);
        double totalCost = 0.0;
        int totalDrinks = 0;

        try {
            while (results.next()) {
                Person person = BarManager.instance.getPerson(results.getString(1));
                Drink drink = BarManager.instance.getDrink(results.getString(2));
                int quantity = results.getInt(3);
                double cost = quantity * results.getDouble(4);

                totalCost += cost;
                totalDrinks += quantity;

                // The String.format calls are getting a number of spaces based on the length of the previous argument

                System.out.printf("Name: %s%sDrink: %s%sAmount: %d%sCost: %s%n",
                        person.getName(),
                        String.format("%" + (15 - person.getName().length()) + "s", " "),
                        drink.getName(),
                        String.format("%" + (15 - drink.getName().length()) + "s", " "),
                        quantity,
                        String.format("%" + (15 - String.valueOf(quantity).length()) + "s", " "),
                        new DecimalFormat("##0.00").format(cost));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //%10.2f: Max of 10 numbers to the left of the decimal and max of 2 numbers to the right
        System.out.printf("%nTotal cost: %10.2f%n", totalCost);
        System.out.printf("Total drinks: %5d%n", totalDrinks);
    }

    /**
     * Register a purchase. This method expects the person and drink arguments to be valid, and have been created by
     * the BarManager class
     * @param person The buyer
     * @param drink The drink
     * @param amount How manuy
     */
    public void makePurchase(Person person, Drink drink, int amount) {
        if(person == null || drink == null || amount <= 0) {
            System.err.println("Error: Invalid args for makePurchase()");
            return;
        }

        String sql = "insert into purchases (personid, drinkid, quantity) VALUES (?, ?, ?);";

        if(! this.mysql.executeSQL(sql, person.getId(), drink.getId(), amount)) {
            System.err.println("Error, could not register purchase!");
        } else {
            System.out.println("Success");
        }
    }

    /**
     * Register a credit to someone. May be in the form of cash or drink credit
     * @param person Who the credit is for
     * @param amount How much
     * @param description What it is for
     */
    public void registerCredit(Person person, double amount, String description) {
        if(person == null || amount <= 0 || description == null || description.length() == 0) {
            System.err.println("Error: invalid args for registerCredit()");
            return;
        }

        String sql = "insert into credit (personid, amount, description) VALUES (?, ?, ?);";

        if(! this.mysql.executeSQL(sql, person.getId(), amount, description)) {
            System.err.println("Error: Could not register credit");
        } else {
            System.out.println("Success");
        }
    }
}