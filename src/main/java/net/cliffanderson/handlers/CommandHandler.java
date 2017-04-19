package net.cliffanderson.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cliff on 3/24/17.
 */
public abstract class CommandHandler {

    protected String command;
    protected List<String> args;


    public CommandHandler(String line) {
        String[] split = line.split(" ");

        this.command = split[0];
        this.args = new ArrayList<String>();

        for(int i = 0; i < split.length - 1; i++) {
            if(!split[i + 1].equals(" ") && !split[i + 1].equals("")) {
                this.args.add(split[i + 1]);
            }
        }

        //System.out.printf("[Debug][%s] Command: %s%n", this.getClass().getName(), this.command);
        //System.out.printf("[Debug][%s] Args: %s%n", this.getClass().getName(), args.toString());
    }

    public abstract void handle();
}
