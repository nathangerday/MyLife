package com.example.mylife.ui.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylife.R;
import com.example.mylife.data.Todo;
import com.example.mylife.ui.TodoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ListsFragment extends Fragment {

    private ArrayList<Todo> list;
    private TodoAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lists, container, false);

        getActivity().setTitle("Lists");

        RecyclerView rv = root.findViewById(R.id.todosListView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        for(int i=0; i<1000; i++){
            list.add(new Todo("Hello"+i));
        }

        adapter = new TodoAdapter(list);
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
        list.add(new Todo(name));
        adapter.notifyItemInserted(list.size() - 1);
    }
}