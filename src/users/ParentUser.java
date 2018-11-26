package users;

import pproject.Menu;
import pproject.Request;
import pproject.Transfer;

import java.util.List;

public class ParentUser extends User {

    @Override
    public void showMenu() {
        new Menu(this);
    }

    //Return a list of the transfers of the user provided
    public List<Transfer> getTransfers(User user) {
        return user.getTransfers();
    }

    //Returns a list of the requests of the user provided
    public List<Request> getRequests(User user, boolean incoming) {
        return user.getRequests(incoming);
    }

}
