package com.woefe.shoppinglist;

import android.app.Application;
import android.widget.Toast;

import com.woefe.shoppinglist.shoppinglist.DirectoryStatus;


public class ShoppingListApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DirectoryStatus directoryStatus = new DirectoryStatus(getApplicationContext());

        if (directoryStatus.isFallback()) {
            int text = R.string.warn_ignore_directory;
            if (directoryStatus.getReason() == DirectoryStatus.Status.MISSING_PERMISSION) {
                text = R.string.warn_missing_permission;
            } else if (directoryStatus.getReason() == DirectoryStatus.Status.NOT_A_DIRECTORY) {
                text = R.string.warn_not_a_directory;
            } else if (directoryStatus.getReason() == DirectoryStatus.Status.CANNOT_WRITE) {
                text = R.string.warn_cannot_write;
            }
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }
}
