package net.cliffanderson.handlers;

public class ExitHandler extends CommandHandler {

    public ExitHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        System.exit(0);
    }
}
