package com.woefe.shoppinglist.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.woefe.shoppinglist.R;

public class TextInputDialog extends DialogFragment {
    private static final String TAG = DialogFragment.class.getSimpleName();
    private static final String KEY_MESSAGE = "MESSAGE";
    private static final String KEY_INPUT = "INPUT";
    private static final String KEY_HINT = "INPUT";
    private Listener listener;
    private String message;
    private String hint;
    private int action;
    private EditText inputField;


    public interface Listener {
        void onInputComplete(String input, int action);
    }

    public static void show(Activity activity, String message, String hint, int action,
                            Class<? extends TextInputDialog> clazz) {

        TextInputDialog dialog;
        try {
            dialog = clazz.newInstance();
        } catch (java.lang.InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot start dialog" + clazz.getSimpleName());
        }
        dialog.message = message;
        dialog.action = action;
        dialog.hint = hint;
        dialog.show(activity.getFragmentManager(), TAG);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (Listener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        String inputText = "";
        if (savedInstanceState != null) {
            message = savedInstanceState.getString(KEY_MESSAGE);
            hint = savedInstanceState.getString(KEY_HINT);
            inputText = savedInstanceState.getString(KEY_INPUT);
        }

        @SuppressLint("InflateParams")
        View dialogRoot = inflater.inflate(R.layout.dialog_text_input, null);
        TextView label = dialogRoot.findViewById(R.id.dialog_label);
        Button cancelButton = dialogRoot.findViewById(R.id.button_dialog_cancel);
        Button okButton = dialogRoot.findViewById(R.id.button_dialog_ok);
        label.setText(message);

        inputField = dialogRoot.findViewById(R.id.dialog_text_field);
        inputField.setHint(hint);
        inputField.setText(inputText);
        inputField.requestFocus();

        inputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                listener.onInputComplete(inputField.getText().toString(), action);
                dismiss();
                return true;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputField.getText().toString();
                if (onValidateInput(input)) {
                    listener.onInputComplete(input, action);
                    dismiss();
                }
            }
        });

        return dialogRoot;
    }

    public boolean onValidateInput(String input) {
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_MESSAGE, message);
        outState.putString(KEY_HINT, hint);
        outState.putString(KEY_INPUT, inputField.getText().toString());
    }
}
