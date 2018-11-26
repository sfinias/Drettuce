package pproject;


import database.Database;
import users.User;

public class Login {

    private Login() {
    }

    //Here the user tries to login, if they input wrong credentials 3 times the program returns to main and exits
    public static boolean login() {
        for (int i = 0; i < 3; i++) {
            System.out.println("Type in your username: ");
            String username = Validation.readString();
            System.out.println("Type in your password: ");
//            String password = Validation.readString();
            String password = Validation.readPassword();
            User user = Database.login(username, password);
            if (user != null) {
                user.showMenu();
                return true;
            } else System.out.println("Incorrect credentials");
        }
        return false;
    }
}
