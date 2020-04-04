package com.woefe.shoppinglist.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.woefe.shoppinglist.R;

public class EditBar {
    private static final String KEY_SAVED_DESCRIPTION = "SAVED_DESCRIPTION";
    private static final String KEY_SAVED_QUANTITY = "SAVED_QUANTITY";
    private static final String KEY_SAVED_MODE = "SAVED_MODE";
    private static final String KEY_SAVE_IS_VISIBLE = "SAVE_IS_VISIBLE";
    private final Context ctx;
    private final RelativeLayout layout;
    private final EditText descriptionText;
    private final EditText quantityText;
    private Mode mode;
    private EditBarListener listener;
    private final FloatingActionButton fab;
    private int position;

    public EditBar(View boundView, final Context ctx) {
        this.ctx = ctx;
        this.layout = boundView.findViewById(R.id.layout_add_item);
        ImageButton button = boundView.findViewById(R.id.button_add_new_item);
        this.descriptionText = boundView.findViewById(R.id.new_item_description);
        this.quantityText = boundView.findViewById(R.id.new_item_quantity);
        this.mode = Mode.ADD;

        layout.setVisibility(View.GONE);

        final Runnable confirmationAction = new Runnable() {
            @Override
            public void run() {
                String desc = descriptionText.getText().toString();
                String qty = quantityText.getText().toString();

                if (desc.equals("")) {
                    Toast.makeText(ctx, R.string.error_description_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mode == Mode.ADD) {
                    listener.onNewItem(desc, qty);
                    descriptionText.requestFocus();
                } else if (mode == Mode.EDIT) {
                    listener.onEditSave(position, desc, qty);
                }

                descriptionText.setText("");
                quantityText.setText("");
            }
        };

        quantityText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                confirmationAction.run();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationAction.run();
            }
        });
        fab = boundView.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                showAdd();
            }
        });
    }

    public void showEdit(int position, String description, String quantity) {
        this.position = position;
        prepare(Mode.EDIT, description, quantity);
        show();
    }

    public void showAdd() {
        prepare(Mode.ADD, "", "");
        show();
    }

    private void prepare(Mode mode, String description, String quantity) {
        this.mode = mode;
        descriptionText.setText(description);
        quantityText.setText(quantity);
    }

    public void enableAutoHideFAB(View view) {
        final GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
            private final int slop = ViewConfiguration.get(ctx).getScaledPagingTouchSlop();
            private float start = -1;
            private float triggerPosition = -1;

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (isNewEvent(e1)) {
                    start = e1.getY();
                }
                final float end = e2.getY();

                if (end - start > slop) {
                    showFAB();
                    start = end;
                } else if (end - start < -slop) {
                    hideFAB();
                    start = end;
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            private boolean isNewEvent(MotionEvent e1) {
                boolean isNewEvent = !(e1.getY() == triggerPosition);
                if (isNewEvent) {
                    triggerPosition = e1.getY();
                }
                return isNewEvent;
            }
        };

        final GestureDetector detector = new GestureDetector(ctx, gestureListener);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });
    }

    private void showFAB() {
        if (!isVisible()) {
            fab.show();
        }
    }

    private void hideFAB() {
        fab.hide();
    }

    private void show() {
        layout.setVisibility(View.VISIBLE);
        descriptionText.requestFocus();
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public void hide() {
        descriptionText.clearFocus();
        quantityText.clearFocus();
        layout.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        }
        fab.show();
        fab.requestFocus();
    }

    public boolean isVisible() {
        return layout.getVisibility() != View.GONE;
    }

    public void addEditBarListener(EditBarListener l) {
        listener = l;
    }

    public void removeEditBarListener(EditBarListener l) {
        if (l == listener) {
            listener = null;
        }
    }

    public void saveState(Bundle state) {
        state.putString(KEY_SAVED_DESCRIPTION, descriptionText.getText().toString());
        state.putString(KEY_SAVED_QUANTITY, quantityText.getText().toString());
        state.putBoolean(KEY_SAVE_IS_VISIBLE, isVisible());
        state.putSerializable(KEY_SAVED_MODE, mode);
    }

    public void restoreState(Bundle state) {
        String description = state.getString(KEY_SAVED_DESCRIPTION);
        String quantity = state.getString(KEY_SAVED_QUANTITY);
        Mode mode = (Mode) state.getSerializable(KEY_SAVED_MODE);
        if (state.getBoolean(KEY_SAVE_IS_VISIBLE)) {
            prepare(mode, description, quantity);
            layout.setVisibility(View.VISIBLE);
            fab.hide();
        }
    }

    public interface EditBarListener {
        void onEditSave(int position, String description, String quantity);

        void onNewItem(String description, String quantity);
    }

    private enum Mode {
        EDIT, ADD
    }


}
