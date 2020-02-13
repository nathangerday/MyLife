package com.example.mylife.ui.lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylife.R;
import com.example.mylife.data.TodoList;
import com.example.mylife.utils.AppStateManager;
import com.example.mylife.utils.ListsTouchHelper;
import com.example.mylife.utils.TodoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListsFragment extends Fragment implements ListsTouchHelper.ListsTouchHelperListener {

    private List<TodoList> list;
    private TodoListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lists, container, false);

        getActivity().setTitle("Lists");

        RecyclerView rv = root.findViewById(R.id.todosListView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        list = AppStateManager.listsOfTodos;
        adapter = new TodoListAdapter(list, getActivity());
        rv.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ListsTouchHelper<TodoAdapter.ViewHolder>(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });


        return root;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position){
        if(viewHolder instanceof TodoListAdapter.ViewHolder){
            String name = this.list.get(viewHolder.getAdapterPosition()).name;

            final TodoList deleted = this.list.get(viewHolder.getAdapterPosition());
            final int deletedPosition = viewHolder.getAdapterPosition();

            this.adapter.removeItem(viewHolder.getAdapterPosition());

        }
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