package pproject;


import database.Database;
import users.*;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private User user;
    private List<MenuItem> mainMenu;
    private List<MenuItem> transferMenu;
    private List<MenuItem> requestMenu;
    private List<MenuItem> adminMenu;


    //Constructors for the different users
    //It designs the main menu for each one and every other sub menu
    public Menu(User user) {
        this.user = user;
        designSimpleUserMainMenu();
        mainMenu();
    }

    public Menu(ParentUser user) {
        this.user = user;
        designParentUserMainMenu();
        mainMenu();
    }

    public Menu(SuperUser user) {
        this.user = user;
        designParentUserMainMenu();
        designSuperUserSubmenu();
        mainMenu();
    }

    public Menu(UltraUser user) {
        this.user = user;
        designParentUserMainMenu();
        designUltraUserSubmenu();
        mainMenu();
    }

    public Menu(Admin user) {
        this.user = user;
        designAdminMenu();
        designUltraUserSubmenu();
        mainMenu();
    }


    private void designSimpleUserMainMenu() {
        mainMenu = new ArrayList<>();
        mainMenu.add(new MenuItem("Personal Information", "personalInfo", this));
        mainMenu.add(new MenuItem("Check your transfers", "transferMenu", this, true));
        mainMenu.add(new MenuItem("Check your requests", "requestMenu", this, true));
        mainMenu.add(new MenuItem("Make a transfer", "newTransfer", this));
        mainMenu.add(new MenuItem("Send a request", "newRequest", this));
    }

    private void designParentUserMainMenu() {
        designSimpleUserMainMenu();
        mainMenu.add(new MenuItem("Check other user's transfers", "transferMenu", this, false));
        mainMenu.add(new MenuItem("Check other user's requests", "requestMenu", this, false));
    }

    private void designSuperUserSubmenu() {
        transferMenu = new ArrayList<>();
        transferMenu.add(new MenuItem("Edit this transfer", "editTransfer", this));
        transferMenu.add(new MenuItem("Return"));
        requestMenu = new ArrayList<>();
        requestMenu.add(new MenuItem("Edit this request", "editRequest", this));
        requestMenu.add(new MenuItem("Return"));
    }

    private void designUltraUserSubmenu() {
        transferMenu = new ArrayList<>();
        transferMenu.add(new MenuItem("Edit this transfer", "editTransfer", this));
        transferMenu.add(new MenuItem("Delete this transfer", "deleteTransfer", this.user));
        transferMenu.add(new MenuItem("Return"));
        requestMenu = new ArrayList<>();
        requestMenu.add(new MenuItem("Edit this request", "editRequest", this));
        requestMenu.add(new MenuItem("Delete this request", "deleteRequest", this.user));
        requestMenu.add(new MenuItem("Return"));
    }

    private void designAdminMenu() {
        designParentUserMainMenu();
        mainMenu.add(new MenuItem("Administrative functions", "adminMenu", this));
        adminMenu = new ArrayList<>();
        adminMenu.add(new MenuItem("Print all the users", "getUsers", this.user));
        adminMenu.add(new MenuItem("Create a new user", "addUser", this.user));
        adminMenu.add(new MenuItem("Edit an existing user", "editUser", this));
        adminMenu.add(new MenuItem("Delete a user", "deleteUser", this.user));
        adminMenu.add(new MenuItem("Return"));
    }

    //The menu starts here
    private void mainMenu() {
        mainMenu.add(new MenuItem("Exit program"));
        System.out.println("Welcome " + user.getFirstName());
        checkUnreadRequests();
        String title = "\t***MAIN MENU***";
        while (true) {
            if (!showMenu(title, mainMenu)) return;
        }
    }

    //Here the menu loops and returns only if the user decides so
    private boolean showMenu(String title, List<MenuItem> menu) {
        if (title != null) {
            System.out.println(title);
        }
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ": " + menu.get(i));
        }
        int choice = Validation.readInt();
        if (choice < 0 || choice > menu.size()) {
            System.out.println("Incorrect input");
            return true;
        } else if (choice == menu.size()) return false;
        else {
            menu.get(choice - 1).execute();
            return true;
        }
    }

    //Here the menu loops until the user picks a choice or the decides to return to the previous menu
    private boolean showSubMenu(String title, List<MenuItem> menu) {
        if (title != null) {
            System.out.println(title);
        }
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ": " + menu.get(i));
        }
        int choice = Validation.readInt();
        if (choice < 0 || choice > menu.size()) {
            System.out.println("Incorrect input");
            return true;
        } else if (choice == menu.size()) return false;
        else {
            menu.get(choice - 1).execute();
            return false;
        }
    }

    //Prints the info of the current user
    public void personalInfo() {
        System.out.println("\t***PERSONAL INFORMATION***");
        System.out.println(this.user.getUserInfo());
        Validation.pauseExecution();
    }

    //Handles the showing of the transfers, be it the current user's or another's
    public void transferMenu(Boolean own) {
        User user;
        if (own) user = Database.getUser(this.user.getId());
        else {
            user = User.getUser();
            if (user == null) {
                Validation.pauseExecution();
                return;
            }
        }
        while (true) {
            String title = "\t\t\t\t\t***" + user.getUsername() + "'s TRANSFERS***";
            List<Transfer> transfers = this.user.getTransfers(user);
            if (transfers == null) {
                Validation.pauseExecution();
                return;
            }
            List<MenuItem> menu = new ArrayList<>();
            double newBalance = user.getBalance();
            for (int i = 0; i < transfers.size(); i++) {
                Transfer transfer = transfers.get(i);
                menu.add(new MenuItem(transfer.getInfo(user) +
                        "\t|\tNew Balance = " + String.format("%.2f", newBalance),
                        "checkTransfer", this, user, transfer));
                newBalance += transfer.getAmount();
            }
            menu.add(new MenuItem("Return to previous menu"));
            if (!showMenu(title, menu)) return;
        }
    }

    //Shows details of the picked transfer and shows the different options the current user has
    public void checkTransfer(User user, Transfer transfer) {
        while (true) {
            System.out.println("***TRANSFER DETAILS***");
            System.out.println(transfer.getDetails(user));
            if (transferMenu == null) {
                Validation.pauseExecution();
                return;
            } else {
                for (MenuItem item : transferMenu) {
                    item.setArg(transfer);
                }
                if (!showSubMenu(null, transferMenu)) {
                    Validation.pauseExecution();
                    return;
                }
            }
        }
    }

    //Handles the showing of the requests, be it the current user's or another's
    public void requestMenu(Boolean own) {
        User user;
        boolean incoming;
        if (own) user = Database.getUser(this.user.getId());
        else {
            user = User.getUser();
            if (user == null) {
                Validation.pauseExecution();
                return;
            }
        }
        System.out.println("\t***REQUESTS***\n1: Incoming Requests\n2: Outgoing Requests");
        int choice = Validation.readInt();
        if (choice < 0 || choice > 2) {
            System.out.println("Incorrect choice");
            return;
        } else {
            incoming = (choice == 1);
            while (true) {
                List<Request> requests = this.user.getRequests(user, incoming);
                if (requests == null) {
                    Validation.pauseExecution();
                    return;
                }
                List<MenuItem> menu = new ArrayList<>();
                for (Request request : requests) {
                    menu.add(new MenuItem(request.getInfo(user), "checkRequest", this, user, request));
                }
                menu.add(new MenuItem("Return to previous menu"));
                String title;
                if (incoming) title = "\t\t\t\t\t***" + user.getUsername() + "'S INCOMING REQUESTS***";
                else title = "\t\t\t\t\t***" + user.getUsername() + "'S OUTGOING REQUESTS***";
                if (!showMenu(title, menu)) return;
            }
        }
    }

    //Shows details of the picked request and shows the different options the current user has
    public void checkRequest(User user, Request request) {
        while (true) {
//            String details = this.user.getRequestDetails(user, request);
            System.out.println("***REQUEST DETAILS***");
            System.out.println(request.getDetails(user));
            if (this.user.getUsername().equals(request.getReceiverUsername())) {
                this.user.checkIfRead(request);
                if (request.getStatus().equals("PENDING")) {
                    requestReply(request);
                    return;
                }
            }
            if (requestMenu == null) {
                Validation.pauseExecution();
                return;
            } else {
                for (MenuItem item : requestMenu) {
                    item.setArg(request);
                }
                if (!showSubMenu(null, requestMenu)) {
                    Validation.pauseExecution();
                    return;
                }
            }
        }
    }

    //Calls the user's method for a new transfer
    public void newTransfer() {
        System.out.println("\t***NEW TRANSFER***");
        this.user.newTransfer();
        Validation.pauseExecution();
    }

    //Calls the user's method for a new request
    public void newRequest() {
        System.out.println("\t***NEW REQUEST***");
        this.user.newRequest();
        Validation.pauseExecution();
    }

    //Menu for editing transfers
    public void editTransfer(Transfer transfer) {
        List<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuItem("Amount = " + transfer.getAmount(), "editTransferAmount", this.user, transfer));
        menu.add(new MenuItem("Message = " + transfer.getMessage(), "editTransferMessage", this.user, transfer));
        menu.add(new MenuItem("Return"));
        String title = "***EDITING TRANSFER***";
        while (true) {
            if (!showSubMenu(title, menu)) {

                return;
            }
        }
    }

    //Menu for editing requests
    public void editRequest(Request request) {
        List<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuItem("Amount = " + request.getAmount(), "editRequestAmount", this.user, request));
        menu.add(new MenuItem("Message = " + request.getMessage(), "editRequestMessage", this.user, request));
        menu.add(new MenuItem("Return"));
        String title = "***EDITING REQUEST***";
        while (true) {
            if (!showSubMenu(title, menu)) return;
        }
    }

    //Menu for replying to an incoming request
    private void requestReply(Request request) {
        List<MenuItem> menu = new ArrayList<>();
        menu.add(new MenuItem("Accept Request", "acceptRequest", this.user, request));
        menu.add(new MenuItem("Deny Request", "denyRequest", this.user, request));
        menu.add(new MenuItem("Continue"));
        while (true) {
            if (!showSubMenu(null, menu)) {
                Validation.pauseExecution();
                return;
            }
        }
    }

    //Checks at login if the user has any unread requests and prompts them if they want to check them
    private void checkUnreadRequests() {
        List<Request> requests = this.user.getUnreadRequests();
        if (requests == null || requests.size() == 0) return;
        String title = "You have " + requests.size() + " new requests. Do you want to check them?(Y/N): ";
        if (!Validation.confirmAction(title)) return;
        User user = Database.getUser(this.user.getId());
        do {
            List<MenuItem> menu = new ArrayList<>();
            for (Request request : requests) {
                menu.add(new MenuItem(request.getInfo(user), "checkRequest", this, user, request));
            }
            menu.add(new MenuItem("Continue"));
            title = "\t\t\t\t\t***" + this.user.getUsername() + "'s NEW INCOMING REQUESTS***";
            if (!showMenu(title, menu)) return;
            requests = this.user.getUnreadRequests();
            if (requests == null || requests.size() == 0) return;
        } while (true);
    }

    //Sub menu of the admin
    public void adminMenu() {
        String title = "***ADMINISTRATIVE FUNCTIONS***";
        while (true) {
            if (showMenu(title, adminMenu)) Validation.pauseExecution();
            else return;
        }
    }

    //Sub menu for editing users
    public void editUser() {
        User user = User.getUser();
        if (user == null) return;
        while (true) {
            List<MenuItem> menu = new ArrayList<>();
            menu.add(new MenuItem("Username = " + user.getUsername(), "editUsername", this.user, user));
            menu.add(new MenuItem("Password = ***", "editPassword", this.user, user));
            menu.add(new MenuItem("First Name = " + user.getFirstName(), "editFirstName", this.user, user));
            menu.add(new MenuItem("Last Name = " + user.getLastName(), "editLastName", this.user, user));
            menu.add(new MenuItem("Access Level = " + user.getVip(), "editVip", this.user, user));
            menu.add(new MenuItem("Return"));
            String title = "***" + user.getUsername() + " EDIT***";
            if (!showSubMenu(title, menu)) return;

        }
    }


}
