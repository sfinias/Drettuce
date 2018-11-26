package pproject;

import database.Database;
import users.User;

import java.util.List;

public class Transfer {

    private int id;
    private String senderUsername;
    private String senderName;
    private String receiverUsername;
    private String receiverName;
    private double amount;
    private String message;
    private String time;

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        if (senderUsername == null) senderUsername = "*Deleted User*";
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        if (receiverUsername == null) receiverUsername = "*Deleted User*";
        this.receiverUsername = receiverUsername;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        if (receiverName == null) receiverName = "*Deleted User*";
        this.receiverName = receiverName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        if (senderName == null) senderName = "*Deleted User*";
        this.senderName = senderName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message == null) message = "-";
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //Returns info of the transfer from the view of the user provided
    public String getInfo(User user) {
        String recipient;
        double amount;
        if (user.getUsername().equals(senderUsername)) {
            recipient = this.receiverUsername;
            amount = this.amount * -1;
        } else if (user.getUsername().equals(receiverUsername)) {
            recipient = this.senderUsername;
            amount = this.amount;
        } else return null;
        return "Date/Time = " + time + "\t|\tRecipient = " + recipient + "\t|\tAmount = " + String.format("%.2f", amount);
    }

    //Returns details of the transfer from the view of the user provided
    public String getDetails(User user) {
        String username;
        String name;
        double amount;
        if (user.getUsername().equals(senderUsername)) {
            name = receiverName;
            username = receiverUsername;
            amount = this.amount * -1;
        } else if (user.getUsername().equals(receiverUsername)) {
            name = senderName;
            username = senderUsername;
            amount = this.amount;
        } else return null;
        return "ID = " + id + "\nTime = " + time + "\nRecipient's Username = " + username +
                "\nRecipient's Name = " + name + "\nAmount = " + String.format("%.2f", amount) + "\nMessage = " + message;
    }

}
