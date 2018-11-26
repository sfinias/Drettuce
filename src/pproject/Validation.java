package pproject;

import database.Database;
import users.User;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Validation {
    private static Scanner scanner;

    private Validation() {
    }

    //Reads the user's inputs and makes it sure that he has typed in a valid integer
    public static int readInt() {
        String st = readString();
        if (st == null || !st.matches("[0-9]+")) return -1;
        return Integer.valueOf(st);
    }


    //Reads the usernames the user has typed in and returns a list of the users found
    public static List<User> readUsers(User sender) {
        List<User> users = new ArrayList<>();
        scanner = new Scanner(readString());
        while (scanner.hasNext()) {
            String username = scanner.next();
            User user = Database.getUser(username);
            if (user == null) {
                System.out.println("There is no user " + username);
            } else if (user.equals(sender)) System.out.println("You can't send to yourself");
            else users.add(user);
        }
        return users;
    }

    //Checks if the input contains both numbers and letters and only that
    public static String readUserPass(int min, int max) {
        for (int i = 0; i < 3; i++) {
            String st = readString();
            if (!st.matches("^(?=.*[a-zA-Z])(?=.*[0-9])[_a-zA-Z0-9]+$") || st.length() < min || st.length() > max) {
                System.out.println("It must be between " + min + " and " + max +
                        " characters and must be consisted of characters and numbers");
            } else return st;
        }
        return null;
    }

    //Checks if the name typed si according to the constraints provided
    public static String readName(int min, int max) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            scanner = new Scanner(readString());
            while (scanner.hasNext()) {
                String str = scanner.next();
                if (str.length() < min) {
                    sb.setLength(0);
                    System.out.println("Name must be longer than " + min + " characters");
                    break;
                } else if (!str.matches("[a-zA-Z]+")) {
                    sb.setLength(0);
                    System.out.println("Name must only have characters");
                    break;
                } else {
                    str = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
                    sb.append(str + " ");
                }
            }
            if (sb.length() != 0) {
                sb.deleteCharAt(sb.length() - 1);
                if (sb.length() <= max) {
                    scanner.close();
                    return sb.toString();
                } else System.out.println("Name must be shorter than " + max + " characters");
            }
        }
        return null;
    }

    public static double readDouble() {
        String st = readString();
        if (st == null || !st.matches("[0-9]+[.]?[0-9]*")) return -1;
        return Double.parseDouble(st);
    }

    public static String readString() {
        System.out.print(">");
        scanner = new Scanner(System.in);
        String st = scanner.nextLine();
        return st;
    }

    public static void pauseExecution() {
        scanner = new Scanner(System.in);
        System.out.print("Press Enter to Continue... ");
        scanner.nextLine();
    }

    public static boolean confirmAction(String message) {
        while (true) {
            System.out.print(message);
            String st = readString().toLowerCase();
            if (st.equals("y")) return true;
            else if (st.equals("n")) return false;
            else System.out.println("Incorrect input");
        }
    }

    public static String readPassword() {
        Console console = System.console();
        if (console == null){
            return readString();
        }
            char[] password = null;
            try {
                System.out.print(">");
                while (password == null || password.length == 0){
                    password = console.readPassword();
                }
                return new String(password);
            }catch(Exception ex) {
                ex.printStackTrace();
                return null;
            }
    }

}
