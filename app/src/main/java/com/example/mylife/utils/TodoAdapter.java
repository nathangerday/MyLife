package com.example.mylife.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylife.R;
import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.ui.todos.TodosActivity;
import com.example.mylife.ui.totodetail.TodoDetailActivity;

import java.util.List;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private final List<Todo> todos;
    private final Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder implements ListItemWithBGViewHolder{
        int position;
        public TextView name;
        public CheckBox checkbox;
        public RelativeLayout foreground, background;
        public View view;

        public ViewHolder(View v) {
            super(v);
            this.view = v;
            this.name = v.findViewById(R.id.todo_name);
            this.checkbox = v.findViewById(R.id.checkbox);
            this.foreground = v.findViewById(R.id.todo_item_foreground);
            this.background = v.findViewById(R.id.todo_item_background);
        }

        @Override
        public RelativeLayout getBackground() {
            return background;
        }

        @Override
        public RelativeLayout getForeground(){
            return foreground;
        }
    }


    public TodoAdapter(@NonNull List<Todo> todos, Context context) {
        this.todos = todos;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void removeItem(int position){
        this.todos.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Todo item, int position){
        this.todos.add(position, item);
        notifyItemInserted(position);
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
        vh.view.setOnClickListener((v) -> {
            Intent intent = new Intent(context, TodoDetailActivity.class);
            intent.putExtra("todo_index", vh.position);
            intent.putExtra("todolist_name", get(vh.position).parentList.name);
            context.startActivity(intent);
        });
        return vh;


    }

    public Todo get(int position) {
        return todos.get(position);
    }

}
