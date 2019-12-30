package com.example.mylife.utils;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylife.R;
import com.example.mylife.data.Todo;

import java.util.List;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private final List<Todo> todos;


    public static class ViewHolder extends RecyclerView.ViewHolder{
        int position;
        public TextView name;
        public CheckBox checkbox;

        public ViewHolder(View v) {
            super(v);
            this.name = v.findViewById(R.id.todo_name);

            this.checkbox = v.findViewById(R.id.checkbox);
        }
    }


    public TodoAdapter(@NonNull List<Todo> todos) {
        this.todos = todos;
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Todo todo = get(position);

        holder.position = position;
        holder.name.setText(todo.name);
        holder.checkbox.setChecked(todo.done);
        if (todo.done){
            holder.name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.name.setPaintFlags(0);
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        vh.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo t = get(vh.position);
                t.done = !t.done;
                notifyDataSetChanged();
            }
        });
        return vh;


    }

    public Todo get(int position) {
        return todos.get(position);
    }

}
