package com.example.mylife.ui;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mylife.R;
import com.example.mylife.data.Todo;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private final List<Todo> todos;
    private final int layoutRessource;

    class ViewHolder {
        int position;
        public TextView name;
        public CheckBox checkbox;
    }

    public TodoAdapter(@NonNull Context context, int ressource, @NonNull List<Todo> todos){
        super(context, ressource, todos);
        this.layoutRessource = ressource;
        this.todos = todos;
    }

    public Todo get(int position){
        return todos.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = createView();
        }

        final Todo todo = get(position);
        ViewHolder holder = (ViewHolder)convertView.getTag();
        holder.position = position;

        holder.name.setText(todo.name);
        if (todo.done){
            holder.name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.name.setPaintFlags(0);
        }
        holder.checkbox.setChecked(todo.done);

        return convertView;
    }

    private View createView(){
        View convertView = LayoutInflater.from(getContext()).inflate(this.layoutRessource, null);

        final ViewHolder holder = new ViewHolder();
        convertView.setTag(holder);

        holder.name = convertView.findViewById(R.id.todoName);
        holder.checkbox = convertView.findViewById(R.id.checkbox);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo todo = todos.get(holder.position);
                todo.done = !todo.done;
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

}
