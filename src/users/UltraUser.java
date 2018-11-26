package users;


import database.Database;
import pproject.Menu;
import pproject.Request;
import pproject.Transfer;
import pproject.Validation;

public class UltraUser extends SuperUser {

    @Override
    public void showMenu() {
        Menu menu = new Menu(this);
    }


    //Deletes the transfer provided
    //If the user's cant be found or they can't afford the rollback, the deletion is aborted
    public void deleteTransfer(Transfer transfer) {
        User sender = Database.getUser(transfer.getSenderUsername());
        if (sender == null) {
            System.out.println("Failed to find sender");
            return;
        }
        User receiver = Database.getUser(transfer.getReceiverUsername());
        if (receiver == null) {
            System.out.println("Failed to find receiver");
            return;
        }
        if (receiver.getBalance() < transfer.getAmount()) {
            System.out.println("Receiver does not have the available balance to delete this transfer");
            return;
        }
        String title = "Confirm delete of transfer(Y/N): ";
        if (Validation.confirmAction(title)) {
            Database.deleteTransfer(transfer);
            System.out.println("Transfer deleted successfully");
        } else System.out.println("Delete of transfer aborted");
    }

    //Deletes the request provided
    public void deleteRequest(Request request) {
        String title = "Confirm delete of request(Y/N): ";
        if (Validation.confirmAction(title)) {
            if (Database.deleteRequest(request)) System.out.println("Request deleted successfully");
            else System.out.println("Failed to delete request");
        }else System.out.println("Delete of request aborted");
    }


}