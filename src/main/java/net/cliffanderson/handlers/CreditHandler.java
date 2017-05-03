package net.cliffanderson.handlers;

import net.cliffanderson.BarManager;
import net.cliffanderson.obj.Person;

public class CreditHandler extends CommandHandler {

    public CreditHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        // name amount desc.

        if(args.size() < 3) {
            System.err.println("Too few arguments. Correct usage: credit [person] [amount] [description]");
            return;
        }

        // Validate person
        Person person = BarManager.instance.getPerson(args.get(0));
        if(person == null) {
            System.out.printf("Error: Person %s not found%n", args.get(0));
            return;
        }

        // Validate amount
        double amount;
        try {
            amount = Double.parseDouble(args.get(1));
        } catch (Exception e) {
            System.out.printf("Error: %s is not a valid amount%n", args.get(1));
            return;
        }

        if(amount <= 0) {
            System.out.printf("Error: Amount must be greater than 0%n");
            return;
        }

        // Assemble description
        StringBuilder builder = new StringBuilder();
        for(int i = 2; i < args.size(); i++) {
            builder.append(args.get(i));
            builder.append(' ');
        }

        String description = builder.toString();

        BarManager.instance.registerCredit(person, amount, description);
    }
}
