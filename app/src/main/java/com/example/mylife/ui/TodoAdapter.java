package com.example.mylife.ui;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
            this.name = v.findViewById(R.id.todoName);

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

//    @NonNull
//    @Override
//    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null){
//            convertView = createView();
//        }
//
//        final Todo todo = get(position);
//        ViewHolder holder = (ViewHolder)convertView.getTag();
//        holder.position = position;
//
//        holder.name.setText(todo.name);
//        if (todo.done){
//            holder.name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        } else {
//            holder.name.setPaintFlags(0);
//        }
//        holder.checkbox.setChecked(todo.done);
//
//        return convertView;
//    }
//
//    private View createView(){
//        View convertView = LayoutInflater.from(getContext()).inflate(this.layoutRessource, null);
//
//        final ViewHolder holder = new ViewHolder();
//        convertView.setTag(holder);
//
//        holder.name = convertView.findViewById(R.id.todoName);
//        holder.checkbox = convertView.findViewById(R.id.checkbox);
//
//        holder.checkbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Todo todo = todos.get(holder.position);
//                todo.done = !todo.done;
//                notifyDataSetChanged();
//            }
//        });
//
//        return convertView;
//    }

}
