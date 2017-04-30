package net.cliffanderson;

import net.cliffanderson.obj.Alias;
import net.cliffanderson.obj.Drink;
import net.cliffanderson.obj.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BarManager {

    public static BarManager instance;
    private List<Drink> drinks;
    private List<Person> people;
    private List<Alias> aliases;

    BarManager() {
        this.loadDrinks();
        this.loadPeople();
        this.loadAliases();


        instance = this;
    }

    private void loadDrinks() {
        this.drinks = new ArrayList<Drink>();

        ResultSet results = MySQLManager.executeQuery("select id, name, price from drinks;");

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

        ResultSet results = MySQLManager.executeQuery("select id, name from people;");

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

    private void loadAliases() {
        this.aliases = new ArrayList<Alias>();

        ResultSet results = MySQLManager.executeQuery("select a.name, a.drinkid from aliases a inner join drinks d on d.id = a.drinkid");

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

        if(!MySQLManager.executeSQL(sql, person.getId(), drink.getId(), amount)) {
            System.err.println("Error, could not register purchase!");
        } else {
            System.out.println("Success");
        }

    }

}