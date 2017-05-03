package net.cliffanderson.handlers;

import net.cliffanderson.BarManager;
import net.cliffanderson.obj.Drink;
import net.cliffanderson.obj.Person;

public class ListHandler extends CommandHandler {

    public ListHandler(String s) {
        super(s);
    }

    @Override
    public void handle() {
        System.out.println("People:");
        for(Person p : BarManager.instance.getPeople()) {
            System.out.printf("id: %d, name: %s%n", p.getId(), p.getName());
        }

        System.out.println("\nDrinks:");
        for(Drink d : BarManager.instance.getDrinks()) {
            System.out.printf("id: %d, name: %s, price: %f%n", d.getId(), d.getName(), d.getPrice());
        }
    }
}
