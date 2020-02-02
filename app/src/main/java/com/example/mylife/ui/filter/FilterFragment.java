package com.example.mylife.ui.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylife.R;
import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.utils.AppStateManager;
import com.example.mylife.utils.ListsTouchHelper;
import com.example.mylife.data.Priority;
import com.example.mylife.utils.TodoAdapter;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment implements ListsTouchHelper.ListsTouchHelperListener{

    private Priority priority;
    private List<TodoList> list;
    private TodoAdapter adapter;

    public FilterFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_filter, container, false);

        priority = Priority.values()[getArguments().getInt("priority")];

        getActivity().setTitle("Filter " + priority.toString().toLowerCase());

        RecyclerView rv = root.findViewById(R.id.filterListView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        list = AppStateManager.listsOfTodos;

        ArrayList<Todo> todoWithPriority = new ArrayList<Todo>();
        for(TodoList tl : this.list){
            for(Todo t: tl.todolist){
                if(t.priority == this.priority){
                    todoWithPriority.add(t);
                }
            }
        }

        adapter = new TodoAdapter(todoWithPriority, getActivity());
        rv.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ListsTouchHelper<TodoAdapter.ViewHolder>(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);


        return root;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position){
        if(viewHolder instanceof TodoAdapter.ViewHolder){
            String name = this.list.get(viewHolder.getAdapterPosition()).name;

            //TODO Find the right item to delete, to do this, we need to change how item are deleted in the adapter
            final TodoList deleted = this.list.get(viewHolder.getAdapterPosition());
            final int deletedPosition = viewHolder.getAdapterPosition();

            this.adapter.removeItem(viewHolder.getAdapterPosition());

        }
    }
}