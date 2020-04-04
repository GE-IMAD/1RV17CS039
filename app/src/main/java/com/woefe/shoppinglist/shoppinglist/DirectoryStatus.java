
package com.woefe.shoppinglist.shoppinglist;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.woefe.shoppinglist.activity.SettingsFragment;

import java.io.File;

public class DirectoryStatus {
    public enum Status {IS_OK, MISSING_PERMISSION, NOT_A_DIRECTORY, CANNOT_WRITE}

    private static final String DEFAULT_DIRECTORY = "ShoppingLists";
    private Status reason;
    private String directory;

    public DirectoryStatus(Context ctx) {
        //ctx = ctx.getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String directory = sharedPreferences.getString(SettingsFragment.KEY_DIRECTORY_LOCATION, "").trim();
        String defaultDir = ctx.getFileStreamPath(DEFAULT_DIRECTORY).getAbsolutePath();
        int permission = ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        File file = new File(directory);

        if (directory.equals("")) {
            init(Status.IS_OK, defaultDir);
        } else if (permission != PackageManager.PERMISSION_GRANTED) {
            init(Status.MISSING_PERMISSION, defaultDir);
        } else if (!file.isDirectory()) {
            init(Status.NOT_A_DIRECTORY, defaultDir);
        } else if (!file.canWrite()) {
            init(Status.CANNOT_WRITE, defaultDir);
        } else {
            init(Status.IS_OK, directory);
        }
    }

    private void init(Status reason, String directory) {
        this.reason = reason;
        this.directory = directory;
        new File(directory).mkdirs();
    }

    public boolean isFallback() {
        return reason != Status.IS_OK;
    }

    public String getDirectory() {
        return directory;
    }

    public Status getReason() {
        return reason;
    }
}
