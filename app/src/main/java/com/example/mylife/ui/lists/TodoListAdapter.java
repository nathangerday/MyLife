package com.example.mylife.ui.lists;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mylife.R;
import com.example.mylife.data.TodoList;
import com.example.mylife.ui.todos.TodosActivity;
import com.example.mylife.utils.ListItemWithBGViewHolder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    private final List<TodoList> lists;
    private final Context context;
    private View parent = null;

    private TodoList lastDeletedItem = null;
    private int lastDeletedItemPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder implements ListItemWithBGViewHolder {
        int position;
        public View view;
        public TextView name;
        public RelativeLayout foreground, background;

        public ViewHolder(View v) {
            super(v);
            this.view = v;
            this.name = v.findViewById(R.id.list_name);
            this.foreground = v.findViewById(R.id.list_item_foreground);
            this.background = v.findViewById(R.id.list_item_background);
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


    public TodoListAdapter(@NonNull List<TodoList> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void removeItem(int position){
        this.lastDeletedItem = this.lists.get(position);
        this.lastDeletedItemPosition = position;
        this.lists.remove(position);
        notifyItemRemoved(position);
        activateUndo();
    }

    private void activateUndo(){
        Snackbar snackbar = Snackbar.make(parent, "\"" +lastDeletedItem.name + "\" deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> undoRemove());
        snackbar.show();
    }

    private void undoRemove(){
        this.lists.add(lastDeletedItemPosition,
                lastDeletedItem);
        notifyItemInserted(lastDeletedItemPosition);
    }

    public void restoreItem(TodoList item, int position){
        this.lists.add(position, item);
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final TodoList list = get(position);

        holder.position = position;
        holder.name.setText(list.name);
        holder.foreground.setBackgroundColor(list.color);


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        vh.view.setOnClickListener((v) -> {
            Intent intent = new Intent(context, TodosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("todo_index", vh.position);
            context.startActivity(intent);
        });


        return vh;


    }

    public TodoList get(int position) {
        return lists.get(position);
    }

}
