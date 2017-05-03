package net.cliffanderson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public class Main {

    private static boolean inputFromEmail = false;

    public static void main(String[] args) throws Exception {
        System.out.println("Process ID: " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

        BarManager barManager = new BarManager();

        // These calls are blocking
        if(inputFromEmail) {
            listenToEmail();
        } else {
            listenToConsole();
        }
    }

    private static void listenToEmail() {

    }

    private static void listenToConsole() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        CommandManager manager = new CommandManager();
        while(true) {
            manager.processCommand(in.readLine());
        }
    }
}
