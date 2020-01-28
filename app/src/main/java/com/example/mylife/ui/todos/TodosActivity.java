package com.example.mylife.ui.todos;

import android.os.Bundle;

import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.ui.lists.TodoListAdapter;
import com.example.mylife.utils.AppStateManager;
import com.example.mylife.utils.ListsTouchHelper;
import com.example.mylife.utils.Priority;
import com.example.mylife.utils.TodoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mylife.R;

public class TodosActivity extends AppCompatActivity implements AddTodoDialog.AddTodoDialogListener, ListsTouchHelper.ListsTouchHelperListener {

    private TodoList todos;
    private int todo_index;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            todo_index = extras.getInt("todo_index");
            todos = AppStateManager.listsOfTodos.get(todo_index);
        }else{
            Log.v("TodosActivity", "Error, no TodoList associated");
            NavUtils.navigateUpFromSameTask(this);
        }
        setTitle(todos.name);

        RecyclerView rv = findViewById(R.id.todosView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new TodoAdapter(todos.todolist, this);
        rv.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ListsTouchHelper<TodoListAdapter.ViewHolder>(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);


        adapter.notifyDataSetChanged();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position){
        if(viewHolder instanceof TodoAdapter.ViewHolder){
            String name = this.todos.todolist.get(viewHolder.getAdapterPosition()).name;

            final Todo deleted = this.todos.todolist.get(viewHolder.getAdapterPosition());
            final int deletedPosition = viewHolder.getAdapterPosition();

            this.adapter.removeItem(viewHolder.getAdapterPosition());

        }
    }

    public void openDialog(){
        AddTodoDialog addTodoDialog = new AddTodoDialog();
        addTodoDialog.show(getSupportFragmentManager(), "New Todo Dialog");
    }

    public void createTodo(String name, Priority priority){
        Toast.makeText(this, "\"" + name + "\" created !", Toast.LENGTH_SHORT).show();
        todos.todolist.add(new Todo(name, priority, todos));
        adapter.notifyItemInserted(todos.todolist.size() - 1);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppStateManager.saveData(getFilesDir());
    }
}
