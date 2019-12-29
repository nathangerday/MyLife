package com.example.mylife.data;

import java.util.ArrayList;
import java.util.List;

public class TodoList {
    private String name;
    private List<Todo> todolist;

    public TodoList(String name){
        this.name = name;
        this.todolist = new ArrayList<>();
    }

}
