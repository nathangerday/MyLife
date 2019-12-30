package com.example.mylife.data;

import java.io.Serializable;

public class Todo implements Serializable {
    public String name;
    public boolean done;

    public Todo(String title) {
        this.name = title;
        this.done = false;
    }

}
