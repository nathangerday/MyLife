package com.example.mylife.data;

public class Todo {
    private String title;
    private boolean done;

    public Todo(String title) {
        this.title = title;
        this.done = false;
    }

    public String getTitle() {
        return this.title;
    }

    public boolean isDone() {
        return this.done;
    }

}
