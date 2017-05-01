package net.cliffanderson;

import net.cliffanderson.handlers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by cliff on 3/18/17.
 */
public class CommandManager {

    /**
     * Process command
     */

    static AssignmentReminder assignmentReminder;

    private Map<String, Class> handlers = new HashMap<String, Class>();

    public CommandManager() {
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


//        assignmentReminder = new AssignmentReminder();
//
//        String[] parts = cmd.split(" ");
//        if(parts.length <= 0) {
//            //TODO: Determine whether to send error message to console or email
//            System.err.println("Invalid command: " + cmd);
//
//            return;
//        }
//
//        String command = parts[0];
//
//        if(command.equalsIgnoreCase("add")) {
//            assignmentReminder.addAssignment();
//        } else if(command.equalsIgnoreCase("test")) {
//            assignmentReminder.test();
//        } else {
//            System.err.printf("Unknown command: %s", command);
//        }
    }

    public Set<String> getCommands() {
        return this.handlers.keySet();
    }
}
