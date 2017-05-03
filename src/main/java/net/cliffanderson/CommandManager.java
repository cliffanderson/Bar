package net.cliffanderson;

import net.cliffanderson.handlers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CommandManager {

    private Map<String, Class> handlers = new HashMap<String, Class>();

    CommandManager() {
        this.handlers.put("test", TestHandler.class);
        this.handlers.put("query", QueryHandler.class);
        this.handlers.put("list", ListHandler.class);
        this.handlers.put("help", HelpHandler.class);
        this.handlers.put("purchase", PurchaseHandler.class);
        this.handlers.put("p", PurchaseHandler.class);
        this.handlers.put("tab", TabHandler.class);
        this.handlers.put("exit", ExitHandler.class);
        this.handlers.put("aliases", AliasesHandler.class);
        this.handlers.put("credit", CreditHandler.class);
    }

    void processCommand(String cmdString) throws Exception {

        if(cmdString.length() == 0) return;

        String commandName = cmdString.split(" ")[0].toLowerCase();

        if(! this.handlers.containsKey(commandName)) {
            System.err.println("Command not found: " + commandName);
            return;
        }

        CommandHandler handler;

        try {
            handler = (CommandHandler) handlers.get(commandName).getDeclaredConstructor(String.class, CommandManager.class).newInstance(cmdString, this);
        } catch (Exception e) {
            handler = (CommandHandler) handlers.get(commandName).getDeclaredConstructor(String.class).newInstance(cmdString);
        }

        handler.handle();
    }

    public Set<String> getCommands() {
        return this.handlers.keySet();
    }
}
