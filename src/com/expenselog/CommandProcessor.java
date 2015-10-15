package com.expenselog;

/**
 * Created by javdaniel on 15/10/15.
 */
public class CommandProcessor {

    public static Command processArguments(String[] args) {

        Command c = null;
        if (args[0] == "list") {
            c = new ListCommand();

        }
        else if (args[0] == "add-expense") {
            c = new ExpenseCommand();
        }

        c.setAmount(Double.valueOf(args[1]));

        return c;
    }

}
