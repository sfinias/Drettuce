package users;

import database.Database;
import pproject.Menu;
import pproject.Request;
import pproject.Transfer;
import pproject.Validation;

public class SuperUser extends ParentUser {

    @Override
    public void showMenu() {
        new Menu(this);
    }

    //Edits the transfers' message
    public void editTransferMessage(Transfer transfer) {
        System.out.println("Type in the new message");
        String message = Validation.readString();
        if (message.length() > Database.MAX_MESSAGE_LENGTH) {
            System.out.println("Your new message is too long. It must be fewer than " + Database.MAX_MESSAGE_LENGTH +
                    " characters");
            return;
        }
        String title = "Confirm edit of message(Y/N): ";
        if(Validation.confirmAction(title)){
            if (Database.editTransferMessage(transfer, message)) System.out.println("Transfer edited successfully");
            else System.out.println("Failed to edit transfer");
        }else System.out.println("Edit of transfer message aborted");
    }

    //Method to edit the amount of the transfer
    //If the users can't afford the edit it is aborted
    // Have put a limit to only change the amount by the editLimitRatio at a time for security reasons
    public void editTransferAmount(Transfer transfer) {
        float editLimitRatio = 0.5f;
        System.out.println("Type in the new amount");
        double amount = Validation.readDouble();
        if (amount == transfer.getAmount()) {
            System.out.println("The amount is the same, no edit needed");
            return;
        } else if (amount == 0) {
            System.out.println("The new amount can't be 0");
            return;
        }
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
        double changedAmount = amount - transfer.getAmount();
        double editRatio = changedAmount / transfer.getAmount();
        if (Math.abs(editRatio) > editLimitRatio) {
            System.out.println("You can only edit the amount by " + (int) (editLimitRatio * 100) + "%");
        } else if (editRatio > 0 && sender.getBalance() < changedAmount) {
            System.out.println("The sender doesn't have the available balance for this change");
        } else if (editRatio < 0 && receiver.getBalance() < changedAmount) {
            System.out.println("The receiver doesn't have the available balance for this change");
        } else {
            String title = "Confirm edit of transfer(Y/N): ";
            if (Validation.confirmAction(title)) {
                Database.editTransferAmount(transfer, changedAmount);
                System.out.println("Edit of transfer completed successfully");
            } else System.out.println("Edit of transfer aborted");
        }
    }

    //Edit's the request's message
    public void editRequestMessage(Request request) {
        System.out.println("Type in the new message");
        String message = Validation.readString();
        if (message.length() > Database.MAX_MESSAGE_LENGTH) {
            System.out.println("Your new message is too long. It must be fewer than " + Database.MAX_MESSAGE_LENGTH +
                    " characters");
            return;
        }
        String title = "Confirm edit of message(Y/N): ";
        if(Validation.confirmAction(title)) {
            if (Database.editRequestMessage(request, message)) System.out.println("Request edited successfully");
            else System.out.println("Failed to edit request");
        }else System.out.println("Edit of request message aborted");
    }

    //Method to edit the amount requested
    // Have put a limit to only change the amount by the editLimitRatio at a time for security reasons
    public void editRequestAmount(Request request) {
        float editLimitRatio = 0.5f;
        System.out.println("Type in the new amount");
        double amount = Validation.readDouble();
        if (amount == request.getAmount()) {
            System.out.println("The amount is the same, no edit needed");
            return;
        } else if (amount == 0) {
            System.out.println("The new amount can't be 0");
            return;
        }
        double changedAmount = amount - request.getAmount();
        double editRatio = changedAmount / request.getAmount();
        if (Math.abs(editRatio) > editLimitRatio) {
            System.out.println("You can only edit the amount by " + (int) (editLimitRatio * 100) + "%");
        } else {
            String title = "Confirm edit of request(Y/N): ";
            if (Validation.confirmAction(title)) {
                Database.editRequestAmount(request, amount);
                System.out.println("Edit of transfer completed successfully");
            } else System.out.println("Edit of transfer aborted");
        }
    }


}
