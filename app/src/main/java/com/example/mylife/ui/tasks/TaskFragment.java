package com.example.mylife.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylife.R;
import com.example.mylife.data.Priority;
import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.utils.AppStateManager;
import com.example.mylife.utils.TodoAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment{

    private Priority priority;
    private List<TodoList> list;
    private TodoAdapter adapter;

    public TaskFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        getActivity().setTitle("All Tasks ");

        RecyclerView rv = root.findViewById(R.id.todosTaskView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        list = AppStateManager.listsOfTodos;

        ArrayList<Todo> alltodos = new ArrayList<Todo>();
        for(TodoList tl : this.list){
            for(Todo t: tl.todolist){
                alltodos.add(t);
            }
        }

        adapter = new TodoAdapter(alltodos, getActivity(), true);
        rv.setAdapter(adapter);


        return root;
    }

}