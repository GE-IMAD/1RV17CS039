
package com.woefe.shoppinglist.shoppinglist;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Arrays;
import java.util.Comparator;

public class ShoppingListService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = ShoppingListService.class.getSimpleName();


    private ShoppingListsManager manager = null;
    private final IBinder binder = new ShoppingListBinder();
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind() called: " + intent.toString());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        manager = new ShoppingListsManager();
        manager.onStart(new DirectoryStatus(this).getDirectory());

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(TAG, "onUnbind() called: " + intent.toString());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        manager.onStop();
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        manager.onStop();
        manager.onStart(new DirectoryStatus(this).getDirectory());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand() called");
        return START_NOT_STICKY;
    }

    public class ShoppingListBinder extends Binder {

        public void addList(String listName) throws ShoppingListException {
            manager.addList(listName);
        }

        public void removeList(String listName) {
            manager.removeList(listName);
        }

        public ShoppingList getList(String listName) {
            return manager.getList(listName);
        }

        public boolean hasList(String listName) {
            return manager.hasList(listName);
        }

        public String[] getListNames() {
            String[] names = manager.getListNames().toArray(new String[0]);
            Arrays.sort(names, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            return names;
        }

        public int indexOf(String listName) {
            if (listName == null) {
                return -1;
            }
            return Arrays.binarySearch(getListNames(), listName);
        }

        public int size() {
            return manager.size();
        }

        public void onPermissionsGranted() {
            manager.onStop();
            manager.onStart(new DirectoryStatus(getApplicationContext()).getDirectory());
        }

        public void addListChangeListener(ListsChangeListener listener) {
            manager.setListChangeListener(listener);
        }

        public void removeListChangeListener(ListsChangeListener listener) {
            manager.removeListChangeListenerListener(listener);
        }
    }
}
