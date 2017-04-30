package net.cliffanderson.handlers;

import net.cliffanderson.BarManager;
import net.cliffanderson.obj.Alias;

/**
 * Created by cliff on 4/30/17.
 */
public class AliasesHandler extends CommandHandler {

    public AliasesHandler(String line) {
        super(line);
    }

    @Override
    public void handle() {
        for(Alias alias : BarManager.instance.getAliases()) {
            System.out.printf("Alias %s for drink %s%n", alias.getName(), alias.getDrink().getName());
        }
    }
}
