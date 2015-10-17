package com.expenselog;

import com.expenselog.tasks.Task;

/**
 * Created by djavorszky on 03/10/15.
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Process command.

        System.out.println("Printing arguments:");
        for (String s : args) {
            System.out.println(s);
        }

        Task task = CommandProcessor.processArguments(args);

        // Step 2: Check if profile csv already exists. Read if command says so.

        String profile = task.getProfileName();

        // Magic check if file exists, then read it... etc.


        // Step 3: Execute the command

        boolean success = task.execute();
    }
}
