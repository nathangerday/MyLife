package com.example.mylife.ui.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylife.R;
import com.example.mylife.data.TodoList;
import com.example.mylife.utils.AppStateManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListsFragment extends Fragment {

    private List<TodoList> list;
    private TodoListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lists, container, false);

        getActivity().setTitle("Lists");

        RecyclerView rv = root.findViewById(R.id.todosListView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = AppStateManager.listsOfTodos;
        adapter = new TodoListAdapter(list, getActivity());
        rv.setAdapter(adapter);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        return root;

    }

    public void openDialog(){
        AddListDialog addListDialog = new AddListDialog();
        addListDialog.show(getActivity().getSupportFragmentManager(), "New List Dialog");
    }

    public void createList(String name) {
        Toast.makeText(getActivity(), "\"" + name + "\" created !", Toast.LENGTH_SHORT).show();
        list.add(new TodoList(name));
        adapter.notifyItemInserted(list.size() - 1);
    }


}