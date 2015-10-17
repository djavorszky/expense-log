package com.expenselog.tasks;

import com.expenselog.persistence.CSVPersistence;

import java.util.ArrayList;

/**
 * Created by javdaniel on 15/10/15.
 */
public class ListTask extends Task {

    private static final char SEPARATOR = '|';

    @Override
    public boolean execute() {

        ArrayList<String> file = CSVPersistence.readFile(profileName);

        System.out.println("| Date | Amount | Category | Description | id |");
        for (String line : file) {
            String[] lineArray = line.split(";");
            StringBuilder sb = new StringBuilder(11);
            sb.append(SEPARATOR);
            for (String s : lineArray) {
                s = " " + s + " ";
                sb.append(s);
                sb.append(SEPARATOR);
            }
            System.out.println(sb.toString());
        }

        return false;
    }
}
