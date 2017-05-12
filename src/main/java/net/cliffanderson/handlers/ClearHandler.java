package net.cliffanderson.handlers;

public class ClearHandler extends CommandHandler {

    public ClearHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        for(int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
}
