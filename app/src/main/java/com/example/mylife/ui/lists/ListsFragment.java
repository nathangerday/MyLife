package com.example.mylife.ui.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mylife.R;
import com.example.mylife.data.Todo;
import com.example.mylife.ui.TodoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ListsFragment extends Fragment {

    private ArrayList<Todo> list;
    private ArrayAdapter<Todo> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lists, container, false);

        getActivity().setTitle("Lists");

        ListView lv = root.findViewById(R.id.todosListView);

        list = new ArrayList<>();
        for(int i=0; i<30; i++){
            list.add(new Todo("Hello"+i));
        }

        adapter = new TodoAdapter(getActivity(), R.layout.todo_item, list);
        lv.setAdapter(adapter);


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return root;


    }
}