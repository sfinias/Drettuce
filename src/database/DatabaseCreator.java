package database;

import pproject.Validation;

import java.sql.*;

import static database.Database.*;

//This class handle's the creation of the schema
class DatabaseCreator {

    private String dbAdminUsername;
    private String dbAdminPassword;


    //Asks the users for the database's admin's credentials in order to have access and create the schema
    DatabaseCreator() {
            System.out.println("Type in the username of the database admin");
            this.dbAdminUsername = Validation.readString();
            System.out.println("Type in the password of the database admin");
            this.dbAdminPassword = Validation.readPassword();
    }


    void initialize() {
        createSchema();
        createTableUsers();
        createTableTransactions();
        createTableRequests();
        createDeleteUserProcedure();
        createTransactionProcedure();
        createAcceptRequestProcedure();
        createViewTransactions();
        createViewRequests();
        createEditTransferProcedure();
        createDeleteTransferProcedure();
        createDBUsers();
        System.out.println("DB created");
    }

    boolean establishedConnection() {
        try (Connection connection = DriverManager.getConnection(FULL_DB_URL, dbAdminUsername, dbAdminPassword)) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private void createSchema() {
        String sql = "CREATE SCHEMA " + DATABASE;
        try (Connection connection = DriverManager.getConnection(FULL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create schema: " + e.getMessage());
        }
    }

    private void createDBUsers() {
        createSimpleUser();
        createSuperUser();
        createUltraUser();
        createAdminUser();
    }

    private void createSimpleUser() {
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            String sql = "CREATE USER IF NOT EXISTS '" + DB_SIMPLE_USER + "'@'" + DB + "' IDENTIFIED BY '" + DB_SIMPLE_PASSWORD + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + TABLE_USERS + " TO '" + DB_SIMPLE_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + TABLE_TRANSFERS + " TO '" + DB_SIMPLE_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + VIEW_TRANSFERS_VIEW + " TO '" + DB_SIMPLE_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT, INSERT, UPDATE ON " + DATABASE + "." + TABLE_REQUESTS + " TO '" + DB_SIMPLE_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + VIEW_REQUEST_VIEW + " TO '" + DB_SIMPLE_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_TRANSFER + " TO '" + DB_SIMPLE_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_ACCEPT_REQUEST + " TO " + DB_SIMPLE_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Couldn't create simple user: " + e.getMessage());
        }
    }

    private void createSuperUser() {
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            String sql = "CREATE USER IF NOT EXISTS '" + DB_SUPER_USER + "'@'" + DB + "' IDENTIFIED BY '" + DB_SUPER_PASSWORD + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + TABLE_USERS + " TO '" + DB_SUPER_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT, UPDATE ON " + DATABASE + "." + TABLE_TRANSFERS + " TO '" + DB_SUPER_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + VIEW_TRANSFERS_VIEW + " TO '" + DB_SUPER_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT, INSERT, UPDATE ON " + DATABASE + "." + TABLE_REQUESTS + " TO '" + DB_SUPER_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + VIEW_REQUEST_VIEW + " TO '" + DB_SUPER_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_TRANSFER + " TO '" + DB_SUPER_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_ACCEPT_REQUEST + " TO " + DB_SUPER_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_EDIT_TRANSFER + " TO " + DB_SUPER_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Couldn't create super user: " + e.getMessage());
        }
    }

    private void createUltraUser() {
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            String sql = "CREATE USER IF NOT EXISTS '" + DB_ULTRA_USER + "'@'" + DB + "' IDENTIFIED BY '" + DB_ULTRA_PASSWORD + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + TABLE_USERS + " TO '" + DB_ULTRA_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT, UPDATE ON " + DATABASE + "." + TABLE_TRANSFERS + " TO '" + DB_ULTRA_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + VIEW_TRANSFERS_VIEW + " TO '" + DB_ULTRA_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT, INSERT, UPDATE, DELETE ON " + DATABASE + "." + TABLE_REQUESTS + " TO '" + DB_ULTRA_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + VIEW_REQUEST_VIEW + " TO '" + DB_ULTRA_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_TRANSFER + " TO '" + DB_ULTRA_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_ACCEPT_REQUEST + " TO " + DB_ULTRA_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_EDIT_TRANSFER + " TO " + DB_ULTRA_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_DELETE_TRANSFER + " TO " + DB_ULTRA_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Couldn't create ultra user: " + e.getMessage());
        }
    }

    private void createAdminUser() {
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            String sql = "CREATE USER IF NOT EXISTS '" + DB_ADMIN_USER + "'@'" + DB + "' IDENTIFIED BY '" + DB_ADMIN_PASSWORD + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT, UPDATE, INSERT ON " + DATABASE + "." + TABLE_USERS + " TO '" + DB_ADMIN_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT, UPDATE ON " + DATABASE + "." + TABLE_TRANSFERS + " TO '" + DB_ADMIN_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + VIEW_TRANSFERS_VIEW + " TO '" + DB_ADMIN_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT, INSERT, UPDATE, DELETE ON " + DATABASE + "." + TABLE_REQUESTS + " TO '" + DB_ADMIN_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT SELECT ON " + DATABASE + "." + VIEW_REQUEST_VIEW + " TO '" + DB_ADMIN_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_TRANSFER + " TO '" + DB_ADMIN_USER + "'@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_ACCEPT_REQUEST + " TO " + DB_ADMIN_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_EDIT_TRANSFER + " TO " + DB_ADMIN_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_DELETE_TRANSFER + " TO " + DB_ADMIN_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            sql = "GRANT EXECUTE ON PROCEDURE " + DATABASE + "." + PROCEDURE_DELETE_USER + " TO " + DB_ADMIN_USER + "@'" + DB + "'";
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            System.out.println("Couldn't create admin user: " + e.getMessage());
        }
    }

    private void createTableUsers() {
        String sql = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                COLUMN_USER_USERNAME + " VARCHAR(" + MAX_USERNAME_LENGTH + ") UNIQUE NOT NULL , " +
                COLUMN_USER_PASSWORD + " VARCHAR(40) NOT NULL , " +
                COLUMN_USER_FIRST_NAME + " VARCHAR(" + MAX_FIRST_NAME_LENGTH + ") NOT NULL , " +
                COLUMN_USER_LAST_NAME + " VARCHAR(" + MAX_LAST_NAME_LENGTH + ") NOT NULL , " +
                COLUMN_USER_BALANCE + " DECIMAL(" + MAX_AMOUNT_DIGITS + ",2) UNSIGNED NOT NULL, " +
                COLUMN_USER_VIP + " INTEGER(1))";
        String sql1 = "INSERT INTO " + TABLE_USERS + " (" + COLUMN_USER_USERNAME + ", " +
                COLUMN_USER_PASSWORD + ", " + COLUMN_USER_FIRST_NAME + ", " + COLUMN_USER_LAST_NAME + ", " +
                COLUMN_USER_BALANCE + ", " + COLUMN_USER_VIP + ") VALUES ('" + ADMIN_USERNAME + "', SHA('" +
                ADMIN_PASSWORD + "'), '" + ADMIN_FIRST_NAME + "', '" + ADMIN_LAST_NAME + "', " +
                ADMIN_BALANCE + ", " + ADMIN_VIP + ")";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
            statement.execute(sql1);
        } catch (SQLException e) {
            System.out.println("Couldn't create table " + TABLE_USERS + ": " + e.getMessage());
        }
    }

    private void createTableRequests() {
        String sql = "CREATE TABLE " + TABLE_REQUESTS + " (" +
                COLUMN_REQUESTS_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                COLUMN_REQUESTS_SENDER_ID + " INTEGER, " + COLUMN_REQUESTS_RECEIVER_ID + " INTEGER, " +
                COLUMN_REQUESTS_AMOUNT + " DECIMAL(" + MAX_AMOUNT_DIGITS + ",2) UNSIGNED NOT NULL, " +
                COLUMN_REQUESTS_MESSAGE + " TEXT(" + MAX_MESSAGE_LENGTH + "), " +
                COLUMN_REQUESTS_DATE + " DATETIME DEFAULT NOW() NOT NULL, " + COLUMN_REQUESTS_STATUS + " VARCHAR(20) DEFAULT 'PENDING', " +
                COLUMN_REQUESTS_READ + " BOOLEAN DEFAULT FALSE NOT NULL, " + COLUMN_REQUESTS_TRANSFERS_ID + " INTEGER, " +
                "FOREIGN KEY fk_transactionKey(" + COLUMN_REQUESTS_TRANSFERS_ID + ") REFERENCES " +
                TABLE_TRANSFERS + "(" + COLUMN_TRANSFERS_ID + ")" +
                " ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY fk_requestsKey(" + COLUMN_REQUESTS_SENDER_ID +
                ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID +
                ") ON DELETE CASCADE ON UPDATE CASCADE , FOREIGN KEY fk_requestedKey (" +
                COLUMN_REQUESTS_RECEIVER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID +
                ") ON DELETE SET NULL ON UPDATE CASCADE )";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create table " + TABLE_REQUESTS + ": " + e.getMessage());
        }
    }

    private void createTableTransactions() {
        String sql = "CREATE TABLE " + TABLE_TRANSFERS + "(" +
                COLUMN_TRANSFERS_ID + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                COLUMN_TRANSFERS_SENDER_ID + " INTEGER, " +
                COLUMN_TRANSFERS_RECEIVER_ID + " INTEGER, " +
                COLUMN_TRANSFERS_AMOUNT + " DECIMAL(" + MAX_AMOUNT_DIGITS + ",2) UNSIGNED NOT NULL, " +
                COLUMN_TRANSFERS_MESSAGE + " TEXT(" + MAX_MESSAGE_LENGTH + "), " +
                COLUMN_TRANSFERS_DATE + " DATETIME DEFAULT NOW() NOT NULL, " +
                "FOREIGN KEY fk_senderKey(" + COLUMN_TRANSFERS_SENDER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                " ON DELETE SET NULL ON UPDATE CASCADE, " +
                "FOREIGN KEY fk_receiverKey(" + COLUMN_TRANSFERS_RECEIVER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                "ON DELETE SET NULL ON UPDATE CASCADE) ";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create table " + TABLE_TRANSFERS + ": " + e.getMessage());
        }
    }

    private void createDeleteUserProcedure() {
        String sql = "CREATE PROCEDURE " + PROCEDURE_DELETE_USER + " (IN uid INTEGER) " +
                "BEGIN " +
                "DECLARE bal DECIMAL(" + MAX_AMOUNT_DIGITS + ",2) UNSIGNED;" +
                "SELECT balance INTO bal FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = uid;" +
                "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_BALANCE + " = " + COLUMN_USER_BALANCE + " + bal WHERE " +
                COLUMN_USER_ID + " = 1;" +
                "DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = uid;" +
                "END;";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create procedure: " + e.getMessage());
        }
    }

    private void createTransactionProcedure() {
        String sql = "CREATE PROCEDURE " + PROCEDURE_TRANSFER + " (IN sender_id INTEGER, IN receiver_id INTEGER, " +
                "IN amount DECIMAL(" + MAX_AMOUNT_DIGITS + ",2) UNSIGNED, " +
                "IN message TEXT(" + MAX_MESSAGE_LENGTH + "), OUT  transact_id INTEGER, OUT time_added DATETIME) " +
                "BEGIN " +
                "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_BALANCE + " = " + COLUMN_USER_BALANCE + " - amount  WHERE " +
                COLUMN_USER_ID + " = sender_id;" +
                "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_BALANCE + " = " + COLUMN_USER_BALANCE + " + amount  WHERE " +
                COLUMN_USER_ID + " = receiver_id;" +
//                "SELECT NOW() INTO time_added;" +
                "INSERT INTO " + TABLE_TRANSFERS + " (" + COLUMN_TRANSFERS_SENDER_ID + ", " +
                COLUMN_TRANSFERS_RECEIVER_ID + ", " + COLUMN_TRANSFERS_AMOUNT + ", " + COLUMN_TRANSFERS_MESSAGE +
                ") VALUES (sender_id, receiver_id, amount, message);" +
                "SELECT LAST_INSERT_ID() INTO transact_id;" +
                "SELECT " + COLUMN_TRANSFERS_DATE + " INTO time_added FROM " + TABLE_TRANSFERS +
                " WHERE " + COLUMN_TRANSFERS_ID + " = LAST_INSERT_ID();" +
                "END;";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create procedure: " + e.getMessage());
        }
    }

    private void createAcceptRequestProcedure() {
        String sql = "CREATE PROCEDURE " + PROCEDURE_ACCEPT_REQUEST + "(IN request_id INTEGER, IN message TEXT(" + MAX_MESSAGE_LENGTH + ")," +
                " OUT time_added DATETIME) " +
                "BEGIN " +
                "SELECT @sender_id:=" + COLUMN_REQUESTS_SENDER_ID + ", @receiver_id:= " + COLUMN_REQUESTS_RECEIVER_ID + ", @amount:= " +
                COLUMN_REQUESTS_AMOUNT + " FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_REQUESTS_ID + " = request_id;" +
                "CALL " + PROCEDURE_TRANSFER + "(@receiver_id, @sender_id, @amount, message, @transaction_id, time_added);" +
                "UPDATE " + TABLE_REQUESTS + " SET " + COLUMN_REQUESTS_STATUS + " = 'ACCEPTED', " +
                COLUMN_REQUESTS_TRANSFERS_ID + " = @transaction_id WHERE " +
                COLUMN_REQUESTS_ID + " = request_id;" +
                "END;";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create acceptRequest procedure: " + e.getMessage());
        }
    }

    private void createEditTransferProcedure() {
        String sql = "CREATE PROCEDURE " + PROCEDURE_EDIT_TRANSFER + "(IN transfer_id INTEGER, " +
                "IN new_amount DECIMAL(" + MAX_AMOUNT_DIGITS + ",2)) " +
                "BEGIN " +
                "SELECT @sender_id:=" + COLUMN_TRANSFERS_SENDER_ID + ", @receiver_id:= " + COLUMN_TRANSFERS_RECEIVER_ID +
                " FROM " + TABLE_TRANSFERS + " WHERE " + COLUMN_TRANSFERS_ID + " = transfer_id;" +
                "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_BALANCE + " = " +
                COLUMN_USER_BALANCE + " - new_amount WHERE " +
                COLUMN_USER_ID + " = @sender_id;" +
                "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_BALANCE + " = " +
                COLUMN_USER_BALANCE + " + new_amount WHERE " +
                COLUMN_USER_ID + " = @receiver_id;" +
                "UPDATE " + TABLE_TRANSFERS + " SET " + COLUMN_TRANSFERS_AMOUNT + " = " +
                COLUMN_TRANSFERS_AMOUNT + " + new_amount WHERE " + COLUMN_TRANSFERS_ID + " = transfer_id;" +
                "END;";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create editTransfer procedure: " + e.getMessage());
        }
    }

    private void createDeleteTransferProcedure() {
        String sql = "CREATE PROCEDURE " + PROCEDURE_DELETE_TRANSFER + "(IN transfer_id INTEGER) " +
                "BEGIN " +
                "SELECT @sender_id:=" + COLUMN_TRANSFERS_SENDER_ID + ", @receiver_id:= " + COLUMN_TRANSFERS_RECEIVER_ID +
                ", @amount:= " + COLUMN_TRANSFERS_AMOUNT +
                " FROM " + TABLE_TRANSFERS + " WHERE " + COLUMN_TRANSFERS_ID + " = transfer_id;" +
                "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_BALANCE + " = " +
                COLUMN_USER_BALANCE + " + @amount WHERE " + COLUMN_USER_ID + " = @sender_id;" +
                "UPDATE " + TABLE_USERS + " SET " + COLUMN_USER_BALANCE + " = " +
                COLUMN_USER_BALANCE + " - @amount WHERE " +
                COLUMN_USER_ID + " = @receiver_id;" +
                "DELETE FROM " + TABLE_TRANSFERS + " WHERE " +
                COLUMN_TRANSFERS_ID + " = transfer_id LIMIT 1;" +
                "END;";
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create editTransfer procedure: " + e.getMessage());
        }
    }

    private void createViewTransactions() {
        String sql = "CREATE VIEW " + VIEW_TRANSFERS_VIEW + " AS SELECT " +
                TABLE_TRANSFERS + "." + COLUMN_TRANSFERS_ID + ", " +
                TRANSFERS_VIEW_SENDER + "." + COLUMN_USER_USERNAME + " AS " + COLUMN_TRANSFERS_VIEW_SENDER_USERNAME +
                ", " + TRANSFERS_VIEW_RECEIVER + "." + COLUMN_USER_USERNAME + " AS " +
                COLUMN_TRANSFERS_VIEW_RECEIVER_USERNAME + ", CONCAT(" +
                TRANSFERS_VIEW_SENDER + "." + COLUMN_USER_FIRST_NAME + ", ' ', " +
                TRANSFERS_VIEW_SENDER + "." + COLUMN_USER_LAST_NAME + ") AS " + COLUMN_TRANSFERS_VIEW_SENDER_NAME +
                ", CONCAT(" + TRANSFERS_VIEW_RECEIVER + "." + COLUMN_USER_FIRST_NAME + ", ' ', " +
                TRANSFERS_VIEW_RECEIVER + "." + COLUMN_USER_LAST_NAME + ") AS " + COLUMN_TRANSFERS_VIEW_RECEIVER_NAME +
                ", " + TABLE_TRANSFERS + "." + COLUMN_TRANSFERS_AMOUNT + ", " + TABLE_TRANSFERS + "." +
                COLUMN_TRANSFERS_DATE + ", " + TABLE_TRANSFERS + "." + COLUMN_TRANSFERS_MESSAGE +
                " FROM " + TABLE_TRANSFERS +
                " LEFT JOIN " + TABLE_USERS + " AS " + TRANSFERS_VIEW_SENDER + " ON " + TABLE_TRANSFERS + "." +
                COLUMN_TRANSFERS_SENDER_ID + " = " + TRANSFERS_VIEW_SENDER + "." + COLUMN_USER_ID +
                " LEFT JOIN " + TABLE_USERS + " AS " + TRANSFERS_VIEW_RECEIVER + " ON " + TABLE_TRANSFERS + "." +
                COLUMN_TRANSFERS_RECEIVER_ID + " = " + TRANSFERS_VIEW_RECEIVER + "." + COLUMN_USER_ID;
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create view of transactions: " + e.getMessage());
        }
    }

    private void createViewRequests() {
        String sql = "CREATE VIEW " + VIEW_REQUEST_VIEW + " AS SELECT " +
                TABLE_REQUESTS + "." + COLUMN_REQUESTS_ID + ", " +
                REQUEST_VIEW_SENDER + "." + COLUMN_USER_USERNAME + " AS " + COLUMN_REQUEST_VIEW_SENDER_USERNAME +
                ", " + REQUEST_VIEW_RECEIVER + "." + COLUMN_USER_USERNAME + " AS " +
                COLUMN_REQUEST_VIEW_RECEIVER_USERNAME + ", CONCAT(" +
                REQUEST_VIEW_SENDER + "." + COLUMN_USER_FIRST_NAME + ", ' ', " +
                REQUEST_VIEW_SENDER + "." + COLUMN_USER_LAST_NAME + ") AS " + COLUMN_REQUEST_VIEW_SENDER_NAME +
                ", CONCAT(" + REQUEST_VIEW_RECEIVER + "." + COLUMN_USER_FIRST_NAME + ", ' ', " +
                REQUEST_VIEW_RECEIVER + "." + COLUMN_USER_LAST_NAME + ") AS " + COLUMN_REQUEST_VIEW_RECEIVER_NAME +
                ", " + TABLE_REQUESTS + "." + COLUMN_REQUESTS_AMOUNT + ", " + TABLE_REQUESTS + "." +
                COLUMN_REQUESTS_DATE + ", " + TABLE_REQUESTS + "." + COLUMN_REQUESTS_MESSAGE + ", " +
                TABLE_REQUESTS + "." + COLUMN_REQUESTS_STATUS + " AS " + COLUMN_REQUESTS_STATUS + ", " +
                TABLE_REQUESTS + "." + COLUMN_REQUESTS_READ + " AS " + COLUMN_REQUESTS_READ +
                " FROM " + TABLE_REQUESTS +
                " LEFT JOIN " + TABLE_USERS + " AS " + REQUEST_VIEW_SENDER + " ON " + TABLE_REQUESTS + "." +
                COLUMN_REQUESTS_SENDER_ID + " = " + REQUEST_VIEW_SENDER + "." + COLUMN_USER_ID +
                " LEFT JOIN " + TABLE_USERS + " AS " + REQUEST_VIEW_RECEIVER + " ON " + TABLE_REQUESTS + "." +
                COLUMN_REQUESTS_RECEIVER_ID + " = " + REQUEST_VIEW_RECEIVER + "." + COLUMN_USER_ID;
        try (Connection connection = DriverManager.getConnection(FINAL_DB_URL, dbAdminUsername, dbAdminPassword);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Couldn't create view of transactions: " + e.getMessage());
        }
    }

}
