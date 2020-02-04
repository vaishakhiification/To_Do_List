package com.vaishakhiification.todolist;

import java.util.ArrayList;
import java.util.List;

public class TaskDetails {
    private int id;
    private String name;
    private String description;
    private boolean isCompleted;
    private int statusId;

    public static List<TaskDetails> taskList = new ArrayList<TaskDetails>();

    public final static List<String> taskStatusList = new ArrayList<String>() {
        {
            add("Created");
            add("In Progress");
            add("Completed");
            add("Cancelled");
            add("Failed");
        }
    };

    public TaskDetails(int id) {
        this.id = id;
        this.statusId = 0; // default
        this.name = "";
        this.description = "";
    }

    public TaskDetails(int taskId, String name, String description, boolean isCompleted, int taskStatusId) {
        this.id = taskId;
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
        this.statusId = taskStatusId;

        if (taskStatusId == 2) {
            this.isCompleted = true;
        }
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return this.name;
    }

    public void setChecked(boolean checked) {
        this.isCompleted = checked;
    }

    public boolean isCompleted() {
        statusId = 2;
        return isCompleted;
    }
}
