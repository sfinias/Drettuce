package pproject;

import database.Database;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Drettuce\u2122");
        if(Database.dbcheck()) {
            if (!Login.login()) System.out.println("Too many tries, exiting program...");
            else System.out.println("Exiting program...");
        }
        else System.out.println("Exiting program...");
    }
}
