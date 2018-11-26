package pproject;

import users.User;

import java.io.*;

//This class handles the writing of the logs
public class FileManager {

    public static final String TRANSACTIONS_FILE_PATH = "transactions.txt";
    public static final String REQUESTS_FILE_PATH = "requests.txt";

    private FileManager() {
    }

    public static void writeTransaction(String message) {
        writeData(message, TRANSACTIONS_FILE_PATH);
    }

    public static void writeRequest(String message) {
        writeData(message, REQUESTS_FILE_PATH);
    }

    private static void writeData(String message, String transactionsFilePath) {
        try {
            PrintWriter fileWriter = new PrintWriter(new FileOutputStream(transactionsFilePath, true));
            fileWriter.println(message);
            fileWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}