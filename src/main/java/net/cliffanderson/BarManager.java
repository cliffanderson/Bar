package net.cliffanderson;

import net.cliffanderson.obj.Drink;
import net.cliffanderson.obj.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarManager {

    public static BarManager instance;
    private List<Drink> drinks;
    private List<Person> people;

    public BarManager() {
        this.loadDrinks();
        this.loadPeople();

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