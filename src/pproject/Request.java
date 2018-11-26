package pproject;

import users.User;

public class Request {
    private int id;
    private String senderUsername;
    private String senderName;
    private String receiverUsername;
    private String receiverName;
    private double amount;
    private String message;
    private String time;
    private String status;
    private boolean read;

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
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
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    //Returns info of the request from the view of the user provided
    public String getInfo(User user) {
        if (user.getUsername().equals(senderUsername)) {
            return "Date/Time = " + time + "\t|\tRecipient = " + receiverUsername + "\t|\tAmount = " + (amount) +
                    "\t|\tStatus = " + status;
        } else if (user.getUsername().equals(receiverUsername)) {
            return "Date/Time = " + time + "\t|\tRecipient = " + senderUsername + "\t|\tAmount = " + amount +
                    "\t|\tStatus = " + status;
        }
        return null;
    }

    //Returns details of the request from the view of the user provided
    public String getDetails(User user) {
        String username = null;
        String name = null;
        if (user.getUsername().equals(senderUsername)) {
            name = receiverName;
            username = receiverUsername;
        } else if (user.getUsername().equals(receiverUsername)) {
            name = senderName;
            username = senderUsername;
        } else return null;
        String st = "ID = " + id + "\nTime = " + time + "\nStatus = " + status +
                "\nRecipient's Username = " + username +
                "\nRecipient's Name = " + name + "\nAmount = " + amount + "\nMessage = " + message;
        return st;
    }


}