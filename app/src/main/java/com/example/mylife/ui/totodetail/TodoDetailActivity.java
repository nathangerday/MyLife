package com.example.mylife.ui.totodetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.mylife.R;
import com.example.mylife.data.Todo;
import com.example.mylife.data.TodoList;
import com.example.mylife.utils.AppStateManager;

public class TodoDetailActivity extends AppCompatActivity {

    private Todo todo;
    private TodoList parent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String todolist_name = extras.getString(("todolist_name"));
            for (TodoList t : AppStateManager.listsOfTodos){
                if(t.name.equals(todolist_name)){
                    parent = t;
                    break;
                }
            }
            todo = parent.todolist.get(extras.getInt("todo_index"));
        }else{
            Log.v("TodosActivity", "Error, no TodoList associated");
            NavUtils.navigateUpFromSameTask(this);
        }

        EditText todotitle = findViewById(R.id.todo_title);
        todotitle.setText(todo.name);
    }

}
