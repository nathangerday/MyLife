package com.example.mylife.utils;

import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AppStateManager {

    public static List<TodoList> listsOfTodos = new ArrayList<>();

    private static String filename = "datafile";


    public static void saveData(File path){
        try {
            ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(new File(path, filename)));
            o.writeObject(listsOfTodos);
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadData(File path){
        try{
            ObjectInputStream o = new ObjectInputStream((new FileInputStream(new File(path, filename))));
            listsOfTodos = (List<TodoList>)o.readObject();
            o.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
/*
        if(listsOfTodos.isEmpty()){
            listsOfTodos.add(new TodoList("General"));
            listsOfTodos.get(0).todolist.add(new Todo("Enjoy your life"));
            listsOfTodos.get(0).todolist.add(new Todo("Sleep"));
            listsOfTodos.add(new TodoList("Work"));
            listsOfTodos.get(1).todolist.add(new Todo("Finish this app"));
            listsOfTodos.get(1).todolist.add(new Todo("Find an internship"));
            listsOfTodos.get(1).todolist.add(new Todo("Validate the semester"));
            listsOfTodos.add(new TodoList("To Buy"));
            listsOfTodos.get(2).todolist.add(new Todo("Carottes"));
            listsOfTodos.get(2).todolist.add(new Todo("Mangas"));
            listsOfTodos.get(2).todolist.add(new Todo("Tshirts"));
            listsOfTodos.get(2).todolist.add(new Todo("Fromage"));
            listsOfTodos.get(2).todolist.add(new Todo("More fromage"));
            listsOfTodos.get(2).todolist.add(new Todo("Omelette du fromage"));
            listsOfTodos.add(new TodoList("New Year with friends"));
            listsOfTodos.get(3).todolist.add(new Todo("Wii"));
            listsOfTodos.get(3).todolist.add(new Todo("Manettes Wii"));
            listsOfTodos.get(3).todolist.add(new Todo("Chemise"));
            listsOfTodos.get(3).todolist.add(new Todo("Concoillote"));
            listsOfTodos.get(3).todolist.add(new Todo("Jeux"));

        }*/
    }

}
