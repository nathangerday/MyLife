package com.example.mylife.ui.filter;

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
import java.util.concurrent.TimeUnit;

public class FilterUrgentFragment extends Fragment{

    private List<TodoList> list;
    private TodoAdapter adapter;

    public FilterUrgentFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_filter, container, false);


        getActivity().setTitle("Urgent");

        RecyclerView rv = root.findViewById(R.id.filterListView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        list = AppStateManager.listsOfTodos;

        ArrayList<Todo> todoWithPriority = new ArrayList<Todo>();
        for(TodoList tl : this.list){
            for(Todo t: tl.todolist){
                if(t.isDeadline() && TimeUnit.MILLISECONDS.toHours(t.deadline - System.currentTimeMillis()) <= 24 && !t.done){
                    todoWithPriority.add(t);
                }
            }
        }

        adapter = new TodoAdapter(todoWithPriority, getActivity(), true);
        rv.setAdapter(adapter);


        return root;
    }

}