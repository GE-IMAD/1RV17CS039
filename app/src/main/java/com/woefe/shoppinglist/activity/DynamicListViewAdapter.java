
package com.woefe.shoppinglist.activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.woefe.shoppinglist.R;
import com.woefe.shoppinglist.shoppinglist.ListItem;
import com.woefe.shoppinglist.shoppinglist.ShoppingList;

public class DynamicListViewAdapter extends BaseAdapter {

    private final Activity activity;
    private ShoppingList shoppingList;
    private final ShoppingList.ShoppingListListener listener = new ShoppingList.ShoppingListListener() {
        @Override
        public void update() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    };

    public DynamicListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        if (shoppingList != null) {
            return shoppingList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (shoppingList != null) {
            return shoppingList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (shoppingList != null) {
            return shoppingList.getId(position);
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView description = view.findViewById(R.id.text_description);
        TextView quantity = view.findViewById(R.id.text_quantity);

        ListItem item = shoppingList.get(position);
        description.setText(item.getDescription());
        quantity.setText(item.getQuantity());

        if (item.isChecked()) {
            description.setTextColor(activity.getResources().getColor(R.color.textColorChecked));
            quantity.setTextColor(activity.getResources().getColor(R.color.textColorChecked));
        } else {
            description.setTextColor(activity.getResources().getColor(R.color.textColorDefault));
            quantity.setTextColor(activity.getResources().getColor(R.color.textColorDefault));
        }

        //view.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void disconnectShoppingList() {
        if (shoppingList != null) {
            this.shoppingList.removeListener(this.listener);
            this.shoppingList = null;
        }
        notifyDataSetChanged();
    }

    public void connectShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
        shoppingList.addListener(this.listener);
        notifyDataSetChanged();
    }

    public void onItemSwap(int startPosition, int endPosition) {
        if (shoppingList != null) {
            shoppingList.move(startPosition, endPosition);
        }
    }
}
