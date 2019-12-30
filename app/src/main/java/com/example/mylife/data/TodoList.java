package com.example.mylife.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TodoList implements Serializable {
    public String name;
    public List<Todo> todolist;

    public TodoList(String name){
        this.name = name;
        this.todolist = new ArrayList<>();
    }

}
