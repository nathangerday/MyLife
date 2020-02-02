package com.example.mylife.data;

import java.io.Serializable;
import java.util.Calendar;

public class Todo implements Serializable {
    public String name;
    public boolean done;
    public Priority priority;
    public TodoList parentList;
    public long deadline;
    public String photoPath;
    public String description;

    public Todo(String title) {
        this(title, Priority.NONE, null);
    }

    public Todo(String title, Priority priority, TodoList parentList){
        this.name = title;
        this.done = false;
        this.priority = priority;
        this.parentList = parentList;
        this.deadline = -1;
        this.photoPath = null;
        this.description = "";
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

    public boolean isPicture(){
        return this.photoPath != null;
    }



}
