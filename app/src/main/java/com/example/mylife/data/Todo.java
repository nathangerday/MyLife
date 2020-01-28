package com.example.mylife.data;

import com.example.mylife.utils.Priority;

import java.io.Serializable;

public class Todo implements Serializable {
    public String name;
    public boolean done;
    public Priority priority;
    public TodoList parentList;

    public Todo(String title) {
        this.name = title;
        this.done = false;
        this.priority = Priority.NONE;
        this.parentList = null;
    }

    public Todo(String title, Priority priority, TodoList parentList){
        this.name = title;
        this.done = false;
        this.priority = priority;
        this.parentList = parentList;
    }

}
