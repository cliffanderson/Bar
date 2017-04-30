package net.cliffanderson.handlers;

/**
 * Created by cliff on 4/30/17.
 */
public class ExitHandler extends CommandHandler {

    public ExitHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        System.exit(0);
    }
}
