package users;

import database.Database;
import pproject.*;

import java.util.List;
import java.util.Objects;

public class User {

    //Restrictions for the users
    static final int MIN_USERNAME_LENGTH = 5;
    static final int MIN_PASSWORD_LENGTH = 5;
    static final int MIN_FIRST_NAME_LENGTH = 3;
    static final int MIN_LAST_NAME_LENGTH = 3;

    private String firstName;
    private String lastName;
    private String username;
    private int id;
    private double balance;
    private int vip;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        this.balance = Database.getBalance(this);
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public void showMenu() {
        Menu menu = new Menu(this);
    }

    //Makes a new transfer to the users you provide and are on the database
    public boolean newTransfer() {
        System.out.println("Type the username(s) of the receiver(s): ");
        List<User> receivers = Validation.readUsers(this);
        if (receivers.size() == 0) {
            System.out.println("There are no user valid receivers");
            return false;
        }
        System.out.println("Type the amount you want to send: ");
        double amount = Validation.readDouble();
        double limit = Math.pow(10, Database.MAX_AMOUNT_DIGITS - 2);
        if (amount <= 0 || amount >= limit) {
            System.out.println("Not viable amount, must be positive and lower than " + String.format("%.2f", limit));
            return false;
        } else if (amount * receivers.size() > this.getBalance()) {
            System.out.println("Insufficient funds, required " + String.format("%.2f", amount * receivers.size()) +
                    ", available " + this.balance);
            return false;
        }
        System.out.println("Type your message: ");
        String message = Validation.readString();
        if (message.length() > Database.MAX_MESSAGE_LENGTH) {
            System.out.println("Message is too long");
            return false;
        }
        String title = "Please confirm transfer of " + String.format("%.2f", amount) + " to " + receivers.size() + " users(Y/N): ";
        if (Validation.confirmAction(title)) {
            int executed = 0;
            for (User receiver : receivers) {
                String time = Database.sendMoney(this, receiver, amount, message);
                if (time != null) {
                    this.balance -= amount;
                    String data = time + "\t" + username + " -> " + receiver.getUsername() + "\t" +
                            String.format("%.2f", amount) + "\t" + message;
                    FileManager.writeTransaction(data);
                    executed++;
                } else System.out.println("Transfer to " + receiver.getUsername() + " failed");
            }
            if (executed == 0) {
                System.out.println("Transfer failed");
                return false;
            } else {
                System.out.println("Successfully transferred to " + executed + "/" + receivers.size() + " transfers");
                return true;
            }
        } else {
            System.out.println("Transfer aborted");
            return false;
        }
    }

    //Makes a new transfer to the users you provide and are on the database
    public boolean newRequest() {
        System.out.println("Type the username(s) of the receiver(s): ");
        List<User> receivers = Validation.readUsers(this);
        if (receivers.size() == 0) {
            System.out.println("There are no user valid receivers");
            return false;
        }
        System.out.println("Type the amount you want to request: ");
        double amount = Validation.readDouble();
        double limit = Math.pow(10, Database.MAX_AMOUNT_DIGITS - 2);
        if (amount <= 0 || amount >= limit) {
            System.out.println("Not viable amount, must be positive and lower than " + String.format("%" + Database.MAX_AMOUNT_DIGITS + ".2f", limit));
            return false;
        }
        System.out.println("Type your message: ");
        String message = Validation.readString();
        if (message.length() > Database.MAX_MESSAGE_LENGTH) {
            System.out.println("Message is too long");
            return false;
        }
        String title = "Please confirm request of " + String.format("%.2f", amount) + " to " + receivers.size() + " users(Y/N):";
        if (Validation.confirmAction(title)) {
            int executed = 0;
            for (User receiver : receivers) {
                String time = Database.sendRequest(this, receiver, amount, message);
                if (time != null) {
                    this.balance -= amount;
                    String data = time + "\t" + username + " -> " + receiver.getUsername() + "\t" +
                            String.format("%.2f", amount) + "\t" + message;
                    FileManager.writeRequest(data);
                    executed++;
                } else System.out.println("Request to " + receiver.getUsername() + " failed");
            }
            if (executed == 0) {
                System.out.println("Requests failed");
                return false;
            } else {
                System.out.println("Successfully sent " + executed + "/" + receivers.size() + " requests");
                return true;
            }
        } else {
            System.out.println("Request aborted");
            return false;
        }
    }

    //Returns a list of the transfers if the user provided is the one who requested them
    public List<Transfer> getTransfers(User user) {
        if (!this.equals(user)) {
            System.out.println("You don't have access to check " + user.getUsername() + "'s transactions");
            return null;
        }
        return getTransfers();
    }

    public List<Transfer> getTransfers() {
        List<Transfer> transfers = Database.getTransfers(this);
        if (transfers.size() == 0) {
            System.out.println("There are no transfers");
            return null;
        }
        return transfers;
    }

    //Returns a list of the requests if the user provided is the one who requested them
    public List<Request> getRequests(User user, boolean incoming) {
        if (!this.equals(user)) {
            System.out.println("You don't have access to check " + user.getUsername() + "'s requests");
            return null;
        }
        return getRequests(incoming);
    }

    //Returns a list of the requests of the user
    public List<Request> getRequests(boolean incoming) {
        List<Request> requests = Database.getRequests(this, incoming);
        if (requests.size() == 0) {
            String inc;
            if (incoming) inc = "incoming";
            else inc = "outgoing";
            System.out.println("There are no " + inc + " requests");
            return null;
        }
        return requests;
    }

    //Checks if the request the user is current seeing has been read before
    //If not it marks it as read
    public void checkIfRead(Request request) {
        if (this.username.equals(request.getReceiverUsername()) && !request.isRead()) {
            Database.markReadRequest(request);
        }
    }

    //Returns a list of the unread requests of the user
    public List<Request> getUnreadRequests() {
        List<Request> requests = Database.getUnreadRequests(this);
        return requests;
    }


    public boolean acceptRequest(Request request) {
        if (request.getSenderUsername().equals("Deleted_User")) {
            System.out.println("Can't accept request due to deletion of the sender");
            return false;
        }
        if (balance < request.getAmount()) {
            System.out.println("You have insufficient funds");
            return false;
        }
        System.out.println("Type in your message");
        String message = Validation.readString();
        if (message.length() > Database.MAX_MESSAGE_LENGTH) {
            System.out.println("The message is too long");
            return false;
        }
        String title = "Please confirm transfer of " + request.getAmount() + " to " + request.getSenderUsername() + "(Y/N)";
        if (Validation.confirmAction(title)) {
            String time = Database.acceptRequest(request, message);
            if (time != null) {
                this.balance -= request.getAmount();
                String write = time + "\t" + username + " -> " + request.getSenderUsername() + "\t" +
                        String.format("%.2f", request.getAmount()) + "\t" + message;
                FileManager.writeTransaction(write);
                System.out.println("Request accepted");
                return true;
            }
            System.out.println("Could not accept request");
            return false;
        }
        System.out.println("Accepting of request aborted");
        return false;
    }

    public void denyRequest(Request request) {
        Database.denyRequest(request);
        System.out.println("Request Denied");
    }

    public String getUserInfo() {
        String info = "ID = " + id + "\nUsername = " + username + "\nAccess level = " + vip +
                "\nBalance = " + String.format("%.2f", getBalance()) + "\nFirst Name = " + firstName + "\nLast Name = " + lastName;
        return info;
    }

    public static User getUser() {
        System.out.println("Type in the username: ");
        String username = Validation.readString();
        User user = Database.getUser(username);
        if (user == null) {
            System.out.println("No user " + username + " found");
        }
        return user;
    }

    @Override
    public String toString() {
        String st = "ID = " + id + "\t|\tUsername = " + username + "\t|\tAccess level = " + vip +
                "\t|\tBalance = " + String.format("%.2f", balance) + "\t|\tFirst Name = " + firstName + "\t|\tLast Name = " + lastName;
        return st;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}