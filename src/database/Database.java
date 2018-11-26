package database;


import pproject.FileManager;
import pproject.Request;
import pproject.Transfer;
import users.*;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Database {
    //Info about the database we want to connect to
    static final String DATABASE = "drettuce";
    static final String DB = "localhost";
    static final String DB_URL = "localhost:3306";
    static final String FULL_DB_URL = "jdbc:mysql://" + DB_URL +
            "?zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true&noAccessToProcedureBodies=true";
    static final String FINAL_DB_URL = "jdbc:mysql://" + DB_URL + "/" + DATABASE +
            "?zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true&noAccessToProcedureBodies=true";

    //Constraints about the data stored in the tables
    public static final int MAX_MESSAGE_LENGTH = 250;
    public static final int MAX_USERNAME_LENGTH = 25;
    public static final int MAX_PASSWORD_LENGTH = 25;
    public static final int MAX_FIRST_NAME_LENGTH = 25;
    public static final int MAX_LAST_NAME_LENGTH = 25;
    public static final int MAX_AMOUNT_DIGITS = 10;

    //Info of the admin user when the database is created
    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin";
    static final String ADMIN_FIRST_NAME = "Administrator";
    static final String ADMIN_LAST_NAME = "Administrator";
    static final int ADMIN_VIP = 9;
    static final double ADMIN_BALANCE = 1000000;

    //Below you can find the name of tables, procedures, views and columns of the data stored if the database
    //I did this in order so I won't make any wrong declaration in the code and to change on the go if needed
    static final String TABLE_USERS = "users";
    static final String COLUMN_USER_ID = "user_id";
    static final String COLUMN_USER_USERNAME = "username";
    static final String COLUMN_USER_PASSWORD = "user_password";
    static final String COLUMN_USER_FIRST_NAME = "first_name";
    static final String COLUMN_USER_LAST_NAME = "last_name";
    static final String COLUMN_USER_BALANCE = "balance";
    static final String COLUMN_USER_VIP = "vip";

    static final String TABLE_REQUESTS = "requests";
    static final String COLUMN_REQUESTS_ID = "id";
    static final String COLUMN_REQUESTS_SENDER_ID = "sender_id";
    static final String COLUMN_REQUESTS_RECEIVER_ID = "receiver_id";
    static final String COLUMN_REQUESTS_AMOUNT = "amount";
    static final String COLUMN_REQUESTS_MESSAGE = "message";
    static final String COLUMN_REQUESTS_DATE = "timed";
    static final String COLUMN_REQUESTS_READ = "is_read";
    static final String COLUMN_REQUESTS_STATUS = "request_status";
    static final String COLUMN_REQUESTS_TRANSFERS_ID = "transaction_id";

    static final String TABLE_TRANSFERS = "transactions";
    static final String COLUMN_TRANSFERS_ID = "id";
    static final String COLUMN_TRANSFERS_AMOUNT = "amount";
    static final String COLUMN_TRANSFERS_SENDER_ID = "sender_id";
    static final String COLUMN_TRANSFERS_RECEIVER_ID = "receiver_id";
    static final String COLUMN_TRANSFERS_DATE = "timed";
    static final String COLUMN_TRANSFERS_MESSAGE = "message";

    static final String VIEW_TRANSFERS_VIEW = "transactionView";
    static final String TRANSFERS_VIEW_SENDER = "sender";
    static final String TRANSFERS_VIEW_RECEIVER = "receiver";
    static final String COLUMN_TRANSFERS_VIEW_SENDER_USERNAME = "sender_username";
    static final String COLUMN_TRANSFERS_VIEW_RECEIVER_USERNAME = "receiver_username";
    static final String COLUMN_TRANSFERS_VIEW_SENDER_NAME = "sender_name";
    static final String COLUMN_TRANSFERS_VIEW_RECEIVER_NAME = "receiver_name";

    static final String VIEW_REQUEST_VIEW = "requestView";
    static final String REQUEST_VIEW_SENDER = "sender";
    static final String REQUEST_VIEW_RECEIVER = "receiver";
    static final String COLUMN_REQUEST_VIEW_SENDER_USERNAME = "sender_username";
    static final String COLUMN_REQUEST_VIEW_RECEIVER_USERNAME = "receiver_username";
    static final String COLUMN_REQUEST_VIEW_SENDER_NAME = "sender_name";
    static final String COLUMN_REQUEST_VIEW_RECEIVER_NAME = "receiver_name";

    static final String PROCEDURE_TRANSFER = "Transfer";
    static final String PROCEDURE_ACCEPT_REQUEST = "AcceptRequest";
    static final String PROCEDURE_EDIT_TRANSFER = "EditTransfer";
    static final String PROCEDURE_DELETE_TRANSFER = "DeleteTransfer";
    static final String PROCEDURE_DELETE_USER = "DeleteUser";

    private static String dbUser;
    private static String dbPass;

    //Below are the credentials of the database users
    static final String DB_SIMPLE_USER = DATABASE+"_simpleUser";
    static final String DB_SIMPLE_PASSWORD = DATABASE+"_simplePassword";
    static final String DB_SUPER_USER = DATABASE+"_superUser";
    static final String DB_SUPER_PASSWORD = DATABASE+"_superPassword";
    static final String DB_ULTRA_USER = DATABASE+"_ultraUser";
    static final String DB_ULTRA_PASSWORD = DATABASE+"_ultraPassword";
    static final String DB_ADMIN_USER = DATABASE+"_adminUser";
    static final String DB_ADMIN_PASSWORD = DATABASE+"_adminPassword";

    private Database() {
    }

    private static void setSimpleUser() {
        dbUser = DB_SIMPLE_USER;
        dbPass = DB_SIMPLE_PASSWORD;
    }

    private static void setSuperUser() {
        dbUser = DB_SUPER_USER;
        dbPass = DB_SUPER_PASSWORD;
    }

    private static void setUltraUser() {
        dbUser = DB_ULTRA_USER;
        dbPass = DB_ULTRA_PASSWORD;
    }

    private static void setAdminUser() {
        dbUser = DB_ADMIN_USER;
        dbPass = DB_ADMIN_PASSWORD;
    }

    //Checks if we can connect to the database, if not then we create the database
    public static boolean dbcheck() {
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, DB_SIMPLE_USER, DB_SIMPLE_PASSWORD)) {
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to retrieve database, initializing creation...");
            for(int i=0;i<3;i++) {
                DatabaseCreator instance = new DatabaseCreator();
                if(instance.establishedConnection()){
                    System.out.println("Connection successful");
                    instance.initialize();
                    return true;
                }else System.out.println("Couldn't establish connection");
            }
            System.out.println("Failed to establish connection with server");
            return false;
        }
    }

    //Checks if the credentials provided and if so get the user
    //Depending on his access level we select the appropriate database user
    public static User login(String username, String password) {
        ResultSet resultSet = null;
        String sql = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_USERNAME + " = ? AND " +
                COLUMN_USER_PASSWORD + " = SHA(?)";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, DB_SIMPLE_USER, DB_SIMPLE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            User user;
            if (resultSet.next()) {
                new Database();
                switch (resultSet.getInt(COLUMN_USER_VIP)) {
                    case 0:
                        user = new User();
                        setSimpleUser();
                        break;
                    case 1:
                        user = new ParentUser();
                        setSimpleUser();
                        break;
                    case 2:
                        user = new SuperUser();
                        setSuperUser();
                        break;
                    case 3:
                        user = new UltraUser();
                        setUltraUser();
                        break;
                    default:
                        user = new Admin();
                        setAdminUser();
                }
                user.setFirstName(resultSet.getString(COLUMN_USER_FIRST_NAME));
                user.setLastName(resultSet.getString(COLUMN_USER_LAST_NAME));
                user.setId(resultSet.getInt(COLUMN_USER_ID));
                user.setBalance(resultSet.getDouble(COLUMN_USER_BALANCE));
                user.setUsername(resultSet.getString(COLUMN_USER_USERNAME));
                user.setVip(resultSet.getInt(COLUMN_USER_VIP));
                return user;
            } else return null;
        } catch (SQLException e) {
            System.out.println("Couldn't login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Returns a list of the registered users except the admin
    public static List<User> getUsers() {
        String sql = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " <> 1";
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                User user = new User();
                user.setFirstName(resultSet.getString(COLUMN_USER_FIRST_NAME));
                user.setLastName(resultSet.getString(COLUMN_USER_LAST_NAME));
                user.setId(resultSet.getInt(COLUMN_USER_ID));
                user.setBalance(resultSet.getDouble(COLUMN_USER_BALANCE));
                user.setUsername(resultSet.getString(COLUMN_USER_USERNAME));
                user.setVip(resultSet.getInt(COLUMN_USER_VIP));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            System.out.println("Couldn't get users: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    //Creates a new user
    public static boolean addUser(String username, String password, String firstName, String lastName, double balance, int vip) {
        String sql = "INSERT INTO " + TABLE_USERS + " (" + COLUMN_USER_USERNAME + ", " +
                COLUMN_USER_PASSWORD + ", " + COLUMN_USER_FIRST_NAME + ", " + COLUMN_USER_LAST_NAME + ", " +
                COLUMN_USER_BALANCE + ", " + COLUMN_USER_VIP + ") VALUES (?,SHA(?),?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setDouble(5, balance);
            statement.setInt(6, vip);
            int ex = statement.executeUpdate();
            if (ex == 1) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println("Couldn't add user: " + e.getMessage());
            return false;
        }
    }


    //Calls the DeleteUser procedure in order to delete a user
    public static boolean deleteUser(User user) {
        String sql = "CALL " + PROCEDURE_DELETE_USER + " (?)";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            int ex = statement.executeUpdate();
            if (ex == 1) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println("Couldn't delete user: " + e.getMessage());
            return false;
        }
    }

    //Calls the Transfer procedure to make a new transfer
    public static String sendMoney(User sender, User receiver, double amount, String message) {
        ResultSet resultSet = null;
        String sql = "CALL " + PROCEDURE_TRANSFER + " (?,?,?,?,@transaction_id, @time_added)";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, sender.getId());
            statement.setInt(2, receiver.getId());
            statement.setDouble(3, amount);
            statement.setString(4, message);
            statement.execute();
            resultSet = statement.executeQuery("SELECT @time_added");
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Couldn't make transaction: " + e.getMessage());
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    //Inserts a new request in the appropriate table
    public static String sendRequest(User sender, User receiver, double amount, String message) {
        ResultSet resultSet = null;
        String sql = "INSERT INTO " + TABLE_REQUESTS + "(" + COLUMN_REQUESTS_SENDER_ID + ", " +
                COLUMN_REQUESTS_RECEIVER_ID + ", " + COLUMN_REQUESTS_AMOUNT + ", " +
                COLUMN_REQUESTS_MESSAGE + ") VALUES (?,?,?,?)";
        String sql1 = "SELECT " + COLUMN_REQUESTS_DATE + " FROM " + TABLE_REQUESTS + " WHERE " +
                COLUMN_REQUESTS_ID + " = LAST_INSERT_ID()";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, sender.getId());
            statement.setInt(2, receiver.getId());
            statement.setDouble(3, amount);
            statement.setString(4, message);
            statement.executeUpdate();
            resultSet = statement.executeQuery(sql1);
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Couldn't send request: " + e.getMessage());
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Queries the table of the username provided and returns the user if found
    //The admin is excluded from the query
    public static User getUser(String username) {
        ResultSet resultSet = null;
        String sql = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + "<>1 AND " +
                COLUMN_USER_USERNAME + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            return getUser(resultSet);
        } catch (SQLException e) {
            System.out.println("Couldn't find user: " + e.getMessage());
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Queries the table of the id provided and returns the user if found
    public static User getUser(int id) {
        ResultSet resultSet = null;
        String sql = "SELECT * FROM " + TABLE_USERS + " WHERE " + /*COLUMN_USER_ID + "<>1 AND "+*/
                COLUMN_USER_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            return getUser(resultSet);
        } catch (SQLException e) {
            System.out.println("Couldn't find user: " + e.getMessage());
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Helper method to create the user found from the methods ar the top
    private static User getUser(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            User user = new User();
            user.setFirstName(resultSet.getString(COLUMN_USER_FIRST_NAME));
            user.setLastName(resultSet.getString(COLUMN_USER_LAST_NAME));
            user.setId(resultSet.getInt(COLUMN_USER_ID));
            user.setBalance(resultSet.getDouble(COLUMN_USER_BALANCE));
            user.setUsername(resultSet.getString(COLUMN_USER_USERNAME));
            user.setVip(resultSet.getInt(COLUMN_USER_VIP));
            return user;
        } else return null;
    }

    //Queries the transfer view for the user's transfers and returns a list of them
    public static List<Transfer> getTransfers(User user) {
        ResultSet resultSet = null;
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM " + VIEW_TRANSFERS_VIEW + " WHERE " +
                COLUMN_TRANSFERS_VIEW_SENDER_USERNAME + " = ? OR " +
                COLUMN_TRANSFERS_VIEW_RECEIVER_USERNAME + " = ? ORDER BY " +
                COLUMN_TRANSFERS_DATE + " DESC";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getUsername());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transfer transfer = new Transfer();
                transfer.setId(resultSet.getInt(COLUMN_TRANSFERS_ID));
                transfer.setSenderUsername(resultSet.getString(COLUMN_TRANSFERS_VIEW_SENDER_USERNAME));
                transfer.setReceiverUsername(resultSet.getString(COLUMN_TRANSFERS_VIEW_RECEIVER_USERNAME));
                transfer.setSenderName(resultSet.getString(COLUMN_TRANSFERS_VIEW_SENDER_NAME));
                transfer.setReceiverName(resultSet.getString(COLUMN_TRANSFERS_VIEW_RECEIVER_NAME));
                transfer.setTime(resultSet.getString(COLUMN_TRANSFERS_DATE));
                transfer.setAmount(resultSet.getDouble(COLUMN_TRANSFERS_AMOUNT));
                transfer.setMessage(resultSet.getString(COLUMN_TRANSFERS_MESSAGE));
                transfers.add(transfer);
            }
            return transfers;
        } catch (SQLException e) {
            System.out.println("Couldn't get transfers: " + e.getMessage());
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Queries the request view for the user's requests and returns a list of them
    public static List<Request> getRequests(User user, boolean incoming) {
        ResultSet resultSet = null;
        String inc;
        if (incoming) {
            inc = COLUMN_REQUEST_VIEW_RECEIVER_USERNAME;
        } else {
            inc = COLUMN_REQUEST_VIEW_SENDER_USERNAME;
        }
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM " + VIEW_REQUEST_VIEW +
                " WHERE ? = " + inc + " ORDER BY " + COLUMN_REQUESTS_DATE + " DESC";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Request request = new Request();
                request.setId(resultSet.getInt(COLUMN_REQUESTS_ID));
                request.setTime(resultSet.getString(COLUMN_REQUESTS_DATE));
                request.setSenderName(resultSet.getString(COLUMN_REQUEST_VIEW_SENDER_NAME));
                request.setReceiverName(resultSet.getString(COLUMN_REQUEST_VIEW_RECEIVER_NAME));
                request.setSenderUsername(resultSet.getString(COLUMN_REQUEST_VIEW_SENDER_USERNAME));
                request.setReceiverUsername(resultSet.getString(COLUMN_REQUEST_VIEW_RECEIVER_USERNAME));
                request.setAmount(resultSet.getDouble(COLUMN_REQUESTS_AMOUNT));
                request.setMessage(resultSet.getString(COLUMN_REQUESTS_MESSAGE));
                request.setStatus(resultSet.getString(COLUMN_REQUESTS_STATUS));
                request.setRead(resultSet.getBoolean(COLUMN_REQUESTS_READ));
                requests.add(request);
            }
            return requests;
        } catch (SQLException e) {
            System.out.println("Couldn't get transactions: " + e.getMessage());
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Update the transfer table with the new message
    public static boolean editTransferMessage(Transfer transfer, String message) {
        String sql = "UPDATE " + TABLE_TRANSFERS + " SET " + COLUMN_TRANSFERS_MESSAGE + " = ? WHERE " +
                COLUMN_TRANSFERS_ID + " = " + transfer.getId();
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, message);
            int ex = statement.executeUpdate();
            if (ex == 1) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println("Couldn't edit transfer: " + e.getMessage());
            return false;
        }
    }

    //Calls the editTransfer procedure to edit the transfer's amount
    public static void editTransferAmount(Transfer transfer, double changedAmount) {
        String sql = "CALL " + PROCEDURE_EDIT_TRANSFER + " (?,?)";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transfer.getId());
            statement.setDouble(2, changedAmount);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't edit transfer: " + e.getMessage());
        }
    }

    //Update the request table with the new message
    public static boolean editRequestMessage(Request request, String message) {
        String sql = "UPDATE " + TABLE_REQUESTS + " SET " + COLUMN_REQUESTS_MESSAGE + " = ? WHERE " +
                COLUMN_REQUESTS_ID + " = " + request.getId();
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, message);
            int ex = statement.executeUpdate();
            if (ex == 1) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println("Couldn't edit request: " + e.getMessage());
            return false;
        }
    }

    //Update the transfer table with the new amount
    public static boolean editRequestAmount(Request request, double amount) {
        String sql = "UPDATE " + TABLE_REQUESTS + " SET " + COLUMN_REQUESTS_AMOUNT + " = ? WHERE " +
                COLUMN_REQUESTS_ID + " = " + request.getId();
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, amount);
            int ex = statement.executeUpdate();
            if (ex == 1) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println("Couldn't edit request: " + e.getMessage());
            return false;
        }
    }

    //Calls the delete transfer procedure to delete the transfer provided
    public static void deleteTransfer(Transfer transfer) {
        String sql = "CALL " + PROCEDURE_DELETE_TRANSFER + " (?)";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, transfer.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't delete transfer: " + e.getMessage());
        }
    }


    //Delete an entry from the request table
    public static boolean deleteRequest(Request request) {
        String sql = "DELETE FROM " + TABLE_REQUESTS + " WHERE " +
                COLUMN_REQUESTS_ID + " = " + request.getId() + " LIMIT 1";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             Statement statement = connection.createStatement()) {
            int ex = statement.executeUpdate(sql);
            if (ex == 1) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println("Couldn't delete request: " + e.getMessage());
            return false;
        }
    }

    //Updates the request table and sets a request as read
    public static void markReadRequest(Request request) {
        String sql = "UPDATE " + TABLE_REQUESTS + " SET " + COLUMN_REQUESTS_READ + " = TRUE WHERE " +
                COLUMN_REQUESTS_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, request.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldn't mark read: " + e.getMessage());
        }
    }

    //Calls the acceptRequest procedure which accepts an incoming request and makes a new transfer
    public static String acceptRequest(Request request, String message) {
        ResultSet resultSet = null;
        String sql = "{CALL " + PROCEDURE_ACCEPT_REQUEST + " (?,?,@time_added)}";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             CallableStatement statement = connection.prepareCall(sql)) {
            statement.setInt(1, request.getId());
            statement.setString(2, message);
            statement.execute();
            resultSet = statement.executeQuery("SELECT  @time_added");
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Couldn't accept request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Updates the request table and a request as denied by the receiver
    public static void denyRequest(Request request) {
        String sql = "UPDATE " + TABLE_REQUESTS + " SET " + COLUMN_REQUESTS_STATUS + " = 'DENIED' WHERE " +
                COLUMN_REQUESTS_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, request.getId());
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Couldn't deny request: " + e.getMessage());
        }
    }

    //Queries the request table for unread requests of the user and returns a list of them
    public static List<Request> getUnreadRequests(User user) {
        ResultSet resultSet = null;
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT * FROM " + VIEW_REQUEST_VIEW + " WHERE " + COLUMN_REQUEST_VIEW_RECEIVER_USERNAME + " =? AND " +
                COLUMN_REQUESTS_READ + " = FALSE ORDER BY " + COLUMN_REQUESTS_DATE + " DESC";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Request request = new Request();
                request.setId(resultSet.getInt(COLUMN_REQUESTS_ID));
                request.setTime(resultSet.getString(COLUMN_REQUESTS_DATE));
                request.setSenderName(resultSet.getString(COLUMN_REQUEST_VIEW_SENDER_NAME));
                request.setReceiverName(resultSet.getString(COLUMN_REQUEST_VIEW_RECEIVER_NAME));
                request.setSenderUsername(resultSet.getString(COLUMN_REQUEST_VIEW_SENDER_USERNAME));
                request.setReceiverUsername(resultSet.getString(COLUMN_REQUEST_VIEW_RECEIVER_USERNAME));
                request.setAmount(resultSet.getDouble(COLUMN_REQUESTS_AMOUNT));
                request.setMessage(resultSet.getString(COLUMN_REQUESTS_MESSAGE));
                request.setStatus(resultSet.getString(COLUMN_REQUESTS_STATUS));
                request.setRead(resultSet.getBoolean(COLUMN_REQUESTS_READ));
                requests.add(request);
            }
            return requests;
        } catch (SQLException e) {
            System.out.println("Couldn't get unread requests: " + e.getMessage());
            return null;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Below there are methods for updating the users table and editing a row
    public static boolean editUserUsername(User user, String username) {
        String sql = "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_USERNAME + " = ? WHERE " +
                COLUMN_USER_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setInt(2, user.getId());
            if (statement.executeUpdate() == 1) return true;
            return false;
        } catch (SQLException e) {
            System.out.println("Couldn't edit user: " + e.getMessage());
            return false;
        }
    }

    public static boolean editUserPassword(User user, String password) {
        String sql = "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_PASSWORD + " = SHA(?) WHERE " +
                COLUMN_USER_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, password);
            statement.setInt(2, user.getId());
            if (statement.executeUpdate() == 1) return true;
            return false;
        } catch (SQLException e) {
            System.out.println("Couldn't edit user: " + e.getMessage());
            return false;
        }
    }

    public static boolean editUserFirstName(User user, String firstName) {
        String sql = "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_FIRST_NAME + " = ? WHERE " +
                COLUMN_USER_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName);
            statement.setInt(2, user.getId());
            if (statement.executeUpdate() == 1) return true;
            return false;
        } catch (SQLException e) {
            System.out.println("Couldn't edit user: " + e.getMessage());
            return false;
        }
    }

    public static boolean editUserLastName(User user, String lastName) {
        String sql = "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_LAST_NAME + " = ? WHERE " +
                COLUMN_USER_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, lastName);
            statement.setInt(2, user.getId());
            if (statement.executeUpdate() == 1) return true;
            return false;
        } catch (SQLException e) {
            System.out.println("Couldn't edit user: " + e.getMessage());
            return false;
        }
    }

    public static boolean editUserVIP(User user, int vip) {
        String sql = "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_VIP + " = ? WHERE " +
                COLUMN_USER_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, vip);
            statement.setInt(2, user.getId());
            if (statement.executeUpdate() == 1) return true;
            return false;
        } catch (SQLException e) {
            System.out.println("Couldn't edit user: " + e.getMessage());
            return false;
        }
    }

    public static double getBalance(User user) {
        ResultSet resultSet = null;
        String sql = "SELECT " + COLUMN_USER_BALANCE + " FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USER_ID + " = ?";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getDouble(COLUMN_USER_BALANCE);
            }
            return 0;
        } catch (SQLException e) {
            System.out.println("Couldn't edit user: " + e.getMessage());
            return 0;
        }finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
