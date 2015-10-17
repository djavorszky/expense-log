package com.expenselog.persistence;


import com.expenselog.Transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by javdaniel on 17/10/15.
 */
public class CSVPersistence {

    private static String currentProfileName;
    private static ArrayList<String> fileArray;
    private static HashMap<Integer, String> transactionMap;
    private static boolean empty;
    private static boolean needUpdate = false;

    public static String readLastEntry(String profileName) {
        if (!profileName.equals(currentProfileName) || needUpdate) {
            fileArray = readFile(profileName);
        }

        if (!empty) {
            return fileArray.get(fileArray.size() - 1);
        }

        return null;
    }

    public static boolean saveTransaction(Transaction transaction) {

        if (fileArray == null || !currentProfileName.equals(transaction.getProfileName()) || needUpdate) {
            readFile(transaction.getProfileName());
        }

        try {
            Files.write(Paths.get("save/" + transaction.getProfileName() + ".csv"),
                    transaction.toString(true).getBytes(), StandardOpenOption.APPEND);
            needUpdate = true;
            return true;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return false;
    }

    public static Transaction readTransaction(int id) {



        return null;
    }

    public static boolean updateTransaction(int id, Transaction transaction) {

        return false;
    }

    public static boolean deleteTransaction(int id) {

        return false;
    }

    public static ArrayList<String> readFile(String profileName) {
        if (profileName.equals(currentProfileName) && !needUpdate) {
            return fileArray;
        }

        transactionMap = new HashMap<Integer, String>();
        currentProfileName = profileName;
        fileArray = new ArrayList<String>();

        File file = new File("save/" + profileName + ".csv");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    fileArray.add(line);
                    String[] lineArray = line.split(";");
                    transactionMap.put(Integer.valueOf(lineArray[4]), line);
                }
                reader.close();
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        empty = fileArray.isEmpty();

        return fileArray;
    }

}
