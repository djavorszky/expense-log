package com.expenselog;

import com.expenselog.persistence.CSVPersistence;
import com.expenselog.tasks.Task;

/**
 * Created by djavorszky on 03/10/15.
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Process command.

        Task task = CommandProcessor.processArguments(args);

        CSVPersistence.readFile(task.getProfileName());
        // Step 2: Execute the command

        boolean success = task.execute();


    }
}
