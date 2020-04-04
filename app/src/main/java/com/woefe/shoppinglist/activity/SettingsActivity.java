
package com.woefe.shoppinglist.activity;

import android.os.Bundle;

import com.woefe.shoppinglist.shoppinglist.ShoppingListService;
public class SettingsActivity extends BinderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onServiceConnected(ShoppingListService.ShoppingListBinder binder) {

    }

    @Override
    protected void onServiceDisconnected(ShoppingListService.ShoppingListBinder binder) {

    }
}
