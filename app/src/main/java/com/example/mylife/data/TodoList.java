package com.example.mylife.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TodoList implements Serializable {
    public String name;
    public List<Todo> todolist;
    public int color;

    public TodoList(String name, int color){
        this.name = name;
        this.color = color;
        this.todolist = new ArrayList<>();
    }

}
