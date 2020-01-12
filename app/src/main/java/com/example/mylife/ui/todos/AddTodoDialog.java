package com.example.mylife.ui.todos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mylife.R;
import com.example.mylife.utils.Priority;

import java.util.ArrayList;

public class AddTodoDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText newTodoName;
    private AddTodoDialogListener listener;
    private Priority currentPriority = Priority.NONE;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddTodoDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddTodoDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_todo_dialog, null);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("New Task")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String name = newTodoName.getText().toString();

                        if(name.isEmpty()){
                            Toast.makeText(getActivity(), "No name entered", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        listener.createTodo(newTodoName.getText().toString(), currentPriority);

                        //Dismiss once everything is OK.
                        dialog.dismiss();
                    }
                });
            }
        });

        Spinner spinner = view.findViewById(R.id.priority_spinner);
        ArrayList<String> priorities_list = new ArrayList<>();
        //spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, priorities_list));
        spinner.setOnItemSelectedListener(this);

        newTodoName = view.findViewById(R.id.edit_todo_name);
        return dialog;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                this.currentPriority = Priority.NONE;
                break;
            case 1:
                this.currentPriority = Priority.HIGH;
                break;
            case 2:
                this.currentPriority = Priority.MEDIUM;
                break;
            case 3:
                this.currentPriority = Priority.LOW;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface AddTodoDialogListener {
        void createTodo(String name, Priority priority);
    }
}
