package net.cliffanderson.handlers;

import net.cliffanderson.BarManager;
import net.cliffanderson.obj.Person;

public class NightHandler extends CommandHandler {

    public NightHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        if(args.size() >= 1) {
            Person person = BarManager.instance.getPerson(args.get(0));
            if(person == null) {
                System.err.println("Error: " + args.get(0) + " is not a valid person");
                return;
            }

            BarManager.instance.printItemizedTabForPerson(person);
        } else {
            BarManager.instance.printItemizedTab("%");
        }
    }
}
