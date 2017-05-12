package net.cliffanderson.handlers;

import net.cliffanderson.BarManager;
import net.cliffanderson.obj.Drink;
import net.cliffanderson.obj.Person;

public class PurchaseHandler extends CommandHandler {

    public PurchaseHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        if(this.args.size() < 2) {
            System.err.println("Too few arguments. Usage: purchase [name] [drink name | drink alias] <number>");
            return;
        }

        String personName = this.args.get(0);
        String drinkName = this.args.get(1);
        int amount = 1;

        if(this.args.size() == 3) {
            try {
                amount = Integer.parseInt(args.get(2));
            } catch (Exception e){}
        }

        // Validate person
        Person person = BarManager.instance.getPerson(personName);
        if(person == null) {
            System.err.println("Error: Could not find person " + personName);
            return;
        }

        // Validate drink
        Drink drink = BarManager.instance.getDrink(drinkName);
        if(drink == null) {
            // Try finding drink by alias
            drink = BarManager.instance.getDrinkByAlias(drinkName);
        }
        // If it's still null, no drink was found
        if(drink == null) {
            System.err.println("Error: Could not find drink " + drinkName);
            return;
        }

        if(amount <= 0) {
            System.err.println("Error: amount must be greater than 0");
            return;
        }

        // Args are valid, make the purchase
        BarManager.instance.makePurchase(person, drink, amount);

        // Print the persons tab for the night
        BarManager.instance.printItemizedTabForPerson(person);
    }
}
