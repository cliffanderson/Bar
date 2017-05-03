package net.cliffanderson.handlers;

import net.cliffanderson.CommandManager;

public class HelpHandler extends CommandHandler {

    private CommandManager manager;

    public HelpHandler(String s, CommandManager manager) {
        super(s);

        this.manager = manager;
    }

    @Override
    public void handle() {
        System.out.println("Commands: ");
        for(String s : this.manager.getCommands()) {
            System.out.println(s);
        }
    }
}
