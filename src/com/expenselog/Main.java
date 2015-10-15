package com.expenselog;

/**
 * Created by djavorszky on 03/10/15.
 */
public class Main {

    public static void main(String[] args) {

        // Step 1: Process command.

        Command c = null;
        try {
            c = CommandProcessor.processArguments(args);
        }
        catch (InvalidCommandException ice) {
            // something
        }

        // Step 2: Execute the command

        boolean success = c.execute();

        // Step 3: Post-process-
    }
}
