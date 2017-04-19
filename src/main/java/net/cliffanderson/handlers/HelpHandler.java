package net.cliffanderson.handlers;

import net.cliffanderson.CommandManager;

/**
 * Created by cliff on 4/13/17.
 */
public class HelpHandler extends CommandHandler {

    private CommandManager manager;

    public HelpHandler(String s, CommandManager manager) {
        super(s);

        this.manager = manager;
    }

    public void handle() {
        System.out.println("Commands: ");
        for(String s : this.manager.getCommands()) {
            System.out.println(s);
        }
    }
}
