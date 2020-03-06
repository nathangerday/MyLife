package com.example.mylife.ui.lists;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mylife.R;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class AddListDialog extends AppCompatDialogFragment {

    private EditText newListName;
    private AddListDialogListener listener;
    private View colorIndicator;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddListDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddTodoDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_list_dialog, null);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("New List")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                colorIndicator.setOnClickListener(v -> {
                    new ColorPickerDialog.Builder(v.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("ColorPicker Dialog")
                            .setPreferenceName("Pick a list color")
                            .setPositiveButton(getString(R.string.confirm),
                                    new ColorEnvelopeListener() {
                                        @Override
                                        public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                            colorIndicator.setBackgroundColor(envelope.getColor());
                                        }
                                    })
                            .setNegativeButton(getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                            .attachAlphaSlideBar(true) // default is true. If false, do not show the AlphaSlideBar.
                            .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                            .show();
                });

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String name = newListName.getText().toString();

                        if (name.isEmpty()) {
                            Toast.makeText(getActivity(), "No name entered", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int currentColor = ((ColorDrawable) colorIndicator.getBackground()).getColor();
                        listener.createList(newListName.getText().toString(), currentColor);

                        //Dismiss once everything is OK.
                        dialog.dismiss();
                    }
                });
            }
        });

        colorIndicator = view.findViewById(R.id.color_indicator);
        newListName = view.findViewById(R.id.edit_list_name);
        return dialog;
    }

    public interface AddListDialogListener {
        void createList(String name, int color);
    }
}
