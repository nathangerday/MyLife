package com.example.mylife.ui.todos;

import android.content.Intent;
import android.os.Bundle;

import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.ui.lists.TodoListAdapter;
import com.example.mylife.utils.AppStateManager;
import com.example.mylife.utils.ListsTouchHelper;
import com.example.mylife.data.Priority;
import com.example.mylife.utils.TodoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylife.R;

import java.util.ArrayList;
import java.util.List;

public class TodosActivity extends AppCompatActivity implements AddTodoDialog.AddTodoDialogListener, ListsTouchHelper.ListsTouchHelperListener {

    private TodoList todos;
    private int todo_index;
    private TodoAdapter adapter;
    private Button quickAddButton;
    private EditText quickAddEditText;

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


        quickAddButton = findViewById(R.id.quick_add_todo_button);
        quickAddEditText = findViewById(R.id.quick_add_todo_text);

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

        quickAddEditText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                createTodo(quickAddEditText.getText().toString(), Priority.NONE);
                quickAddEditText.getText().clear();
            }
            return true;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            adapter.notifyDataSetChanged();
        }

    }

    public void quickAdd(View v){
        createTodo(quickAddEditText.getText().toString(), Priority.NONE);
        quickAddEditText.getText().clear();
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppStateManager.saveData(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_todos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_marked:
                deleteMarkedTasks();
                break;
        }
        return true;
    }

    private void deleteMarkedTasks(){
        List<Todo> toDelete = new ArrayList<>();
        for(Todo t : todos.todolist){
            if(t.done){
                toDelete.add(t);
            }
        }
        todos.todolist.removeAll(toDelete);
        adapter.notifyDataSetChanged();
    }
}
