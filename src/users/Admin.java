package users;

import database.Database;
import pproject.Menu;
import pproject.Validation;

import java.util.List;

public class Admin extends UltraUser {


    //Creates a new user and tops up their account
    public void addUser() {
        System.out.println("Type in their username: ");
        String username = Validation.readUserPass(MIN_USERNAME_LENGTH, Database.MAX_USERNAME_LENGTH);
        if (Database.getUser(username) != null) {
            System.out.println("User " + username + " already exists");
            return;
        }
        if (username == null) return;
        System.out.println("Type in their password: ");
        String password = Validation.readPassword();
        if (password.length()<MIN_PASSWORD_LENGTH){
            System.out.println("Password must be longer than "+ MIN_PASSWORD_LENGTH + " characters");
            return;
        }else if (password.length()>Database.MAX_PASSWORD_LENGTH){
            System.out.println("Password must be shorter than "+ Database.MAX_PASSWORD_LENGTH + " characters");
            return;
        }else if (password == null) {
            System.out.println("Edit of password aborted");
            return;
        }
        System.out.println("Type in their first name: ");
        String firstName = Validation.readName(MIN_FIRST_NAME_LENGTH, Database.MAX_FIRST_NAME_LENGTH);
        if (firstName == null) return;
        System.out.println("Type in their last name: ");
        String lastName = Validation.readName(MIN_LAST_NAME_LENGTH, Database.MAX_LAST_NAME_LENGTH);
        if (lastName == null) return;
        System.out.println("Type in how much you want to top up their account: ");
        double balance = Validation.readDouble();
        double limit = Math.pow(10, Database.MAX_AMOUNT_DIGITS - 2);
        if (balance < 0 || balance >= limit) {
            System.out.println("Not viable amount, must be positive and lower than " + String.format("%" + Database.MAX_AMOUNT_DIGITS + ".2f", limit));
            return;
        }
        System.out.println("Type his access level: ");
        int vip = Validation.readInt();
        if (vip < 0 || vip > 9) vip = 0;
        String title = "Confirm creation of " + username + "(Y/N): ";
        if (Validation.confirmAction(title)) {
            if (Database.addUser(username, password, firstName, lastName, balance, vip)) {
                System.out.println("User " + username + " created successfully");
            } else System.out.println("Failed to create new user");
        } else System.out.println("Creation of user aborted");
    }

    @Override
    public void showMenu() {
        Menu menu = new Menu(this);
    }

    //Returns a list of the users already registered
    public void getUsers() {
        List<User> users = Database.getUsers();
        if (users.size() == 0) {
            System.out.println("There are currently no registered users");
            return;
        }
        for (User user : users) {
            System.out.println(user);
        }
    }

    //Delete a user and transfer any available balance to the admin
    public void deleteUser() {
        User user = getUser();
        if (user == null) {
            return;
        }
        String title = "Confirm deletion of user " + user.getUsername() + "(Y/N): ";
        if (Validation.confirmAction(title)) {
            if (Database.deleteUser(user)) System.out.println("User " + user.getUsername() + " deleted successfully");
            else System.out.println("Failed to delete user " + user.getUsername());
        } else System.out.println("Deletion of user aborted");
    }

    //Edit's a user's username
    public void editUsername(User user) {
        System.out.println("Type the new username: ");
        String username = Validation.readUserPass(MIN_USERNAME_LENGTH, Database.MAX_USERNAME_LENGTH);
        if (username == null) {
            System.out.println("Edit of username aborted");
            return;
        } else if (username.equals(user.getUsername())) {
            System.out.println("You have typed in the already existing username, edit aborted ");
            return;
        } else {
            String title = "Confirm change of username to " + username + "(Y/N): ";
            if (Validation.confirmAction(title)) {
                if (Database.editUserUsername(user, username)) {
                    System.out.println("Username edited successfully");
                } else System.out.println("Failed to edit username");
            } else System.out.println("Edit of username aborted");
        }
    }

    //Edit's a user's password
    public void editPassword(User user) {
        System.out.println("Type the new password: ");
        String password = Validation.readPassword();
        if (password.length()<MIN_PASSWORD_LENGTH){
            System.out.println("Password must be longer than "+ MIN_PASSWORD_LENGTH + " characters");
            return;
        }else if (password.length()>Database.MAX_PASSWORD_LENGTH){
            System.out.println("Password must be shorter than "+ Database.MAX_PASSWORD_LENGTH + " characters");
            return;
        }else if (password == null) {
            System.out.println("Edit of password aborted");
            return;
        } else {
            String title = "Confirm change of password(Y/N): ";
            if (Validation.confirmAction(title)) {
                if (Database.editUserPassword(user, password)) {
                    System.out.println("Password edited successfully");
                } else System.out.println("Failed to edit password");
            } else System.out.println("Edit of password aborted");
        }
    }

    //Edit's a user's first name
    public void editFirstName(User user) {
        System.out.println("Type the new first name: ");
        String firstName = Validation.readName(MIN_FIRST_NAME_LENGTH, Database.MAX_FIRST_NAME_LENGTH);
        if (firstName == null) {
            System.out.println("Edit of first name aborted");
            return;
        } else if (firstName.equals(user.getFirstName())) {
            System.out.println("You have typed in the already existing first name, edit aborted ");
            return;
        } else {
            String title = "Confirm change of first name to " + firstName + "(Y/N): ";
            if (Validation.confirmAction(title)) {
                if (Database.editUserFirstName(user, firstName)) {
                    System.out.println("First name edited successfully");
                } else System.out.println("Failed to edit first name");
            } else System.out.println("Edit of first name aborted");
        }
    }

    //Edit's a user's last name
    public void editLastName(User user) {
        System.out.println("Type the new first name: ");
        String lastName = Validation.readName(MIN_LAST_NAME_LENGTH, Database.MAX_LAST_NAME_LENGTH);
        if (lastName == null) {
            System.out.println("Edit of last name aborted");
            return;
        } else if (lastName.equals(user.getLastName())) {
            System.out.println("You have typed in the already existing first name, edit aborted ");
            return;
        } else {
            String title = "Confirm change of last name to " + lastName + "(Y/N): ";
            if (Validation.confirmAction(title)) {
                if (Database.editUserLastName(user, lastName)) {
                    System.out.println("Last name edited successfully");
                } else System.out.println("Failed to edit last name");
            } else System.out.println("Edit of last name aborted");
        }
    }

    //Edit's a user's access level
    public void editVip(User user) {
        System.out.println("Type the new access level: ");
        int vip = Validation.readInt();
        if (vip < 0 || vip > 9) vip = 0;
        {
            String title = "Confirm change of access level to " + vip + "(Y/N): ";
            if (Validation.confirmAction(title)) {
                if (Database.editUserVIP(user, vip)) {
                    System.out.println("Access level edited successfully");
                } else System.out.println("Failed to edit access level");
            } else System.out.println("Edit of access level aborted");
        }
    }

}
