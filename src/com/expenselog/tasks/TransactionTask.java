package com.expenselog.tasks;

import com.expenselog.tasks.exception.IncompleteTaskException;

/**
 * Created by javdaniel on 17/10/15.
 */
public class TransactionTask extends Task {

    private boolean expense = true;

    @Override
    public boolean execute() {
        try {
            validateTask();
        }
        catch (IncompleteTaskException ite) {
            System.out.println("Task is invalid, there are missing parameters");
            ite.printStackTrace();
            return false;
        }

        if (amount == 0) {
            System.out.println("Amount is zero for Transaction, which is not allowed.");
            return false;
        }
        else if (amount > 0) {
            expense = false;
        }



        return false;
    }

}
