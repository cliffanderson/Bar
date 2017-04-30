package net.cliffanderson.obj;

public class Alias {

    private Drink drink;
    private String name;

    public Alias(String name, Drink drink) {
        this.name = name;
        this.drink = drink;
    }

    public Drink getDrink() {
        return drink;
    }

    public String getName() {
        return name;
    }
}
