package com.example.mylife.data;

import com.example.mylife.utils.Priority;

import java.io.Serializable;

public class Todo implements Serializable {
    public String name;
    public boolean done;
    public Priority priority;

    public Todo(String title) {
        this.name = title;
        this.done = false;
        this.priority = Priority.NONE;
    }

    public Todo(String title, Priority priority){
        this.name = title;
        this.done = false;
        this.priority = priority;
    }

}
