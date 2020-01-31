package com.example.mylife.data;

import com.example.mylife.utils.Priority;

import java.io.Serializable;
import java.util.Calendar;

public class Todo implements Serializable {
    public String name;
    public boolean done;
    public Priority priority;
    public TodoList parentList;
    public long deadline;

    public Todo(String title) {
        this.name = title;
        this.done = false;
        this.priority = Priority.NONE;
        this.parentList = null;
        this.deadline = -1;
    }

    public Todo(String title, Priority priority, TodoList parentList){
        this.name = title;
        this.done = false;
        this.priority = priority;
        this.parentList = parentList;
        this.deadline = -1;
    }

    public void setDeadline(Calendar c){
        this.deadline = c.getTimeInMillis();
    }

    public Calendar getDeadline(){
        Calendar res = Calendar.getInstance();
        res.setTimeInMillis(this.deadline);
        return res;
    }

    public void removeDeadline(){
        this.deadline = -1;
    }

    public boolean isDeadline(){
        return this.deadline >= 0;
    }



}
