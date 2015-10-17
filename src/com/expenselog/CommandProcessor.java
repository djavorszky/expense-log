package com.expenselog;

import com.expenselog.tasks.ListTask;
import com.expenselog.tasks.Task;
import com.expenselog.tasks.TransactionTask;

import java.util.Date;

/**
 * Created by javdaniel on 15/10/15.
 */
public class CommandProcessor {

    public static Task processArguments(String[] args) {

        Task task = null;
        if (args[0].equals("list")) {
            task = new ListTask();
        }
        else if (args[0].equals("add-expense") || args[0].equals("add-income")) {
            task = new TransactionTask();
            if (args[0].equals("add-expense")) {
                task.setAmount(Double.valueOf(args[1]) * -1);
            }
            else {
                task.setAmount(Double.valueOf(args[1]));
            }
        }

        task.setCategoryName("Default category");
        task.setDate(new Date());
        task.setDescription("some description");
        task.setProfileName("default2");
        return task;
    }

}
