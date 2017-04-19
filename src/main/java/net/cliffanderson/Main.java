package net.cliffanderson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

public class Main {

    static boolean inputFromEmail = false;

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

    static void listenToEmail() {

    }

    static void listenToConsole() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        CommandManager manager = new CommandManager();
        while(true) {
            manager.processCommand(in.readLine());
//            if(in.ready()) {
//                manager.processCommand(in.readLine());
//            }
//
//            Thread.sleep(1000);
        }
    }
}
