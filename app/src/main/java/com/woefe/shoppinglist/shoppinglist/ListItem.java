
package com.woefe.shoppinglist.shoppinglist;

public class ListItem {
    private boolean isChecked;
    private String description;
    private String quantity;

    public ListItem(boolean isChecked, String description, String quantity) {
        this.isChecked = isChecked;
        this.description = description;
        this.quantity = quantity;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    static class ListItemWithID extends ListItem {
        private final int id;

        public ListItemWithID(int id, ListItem item) {
            super(item.isChecked, item.description, item.quantity);
            this.id = id;
        }

        public int getId() {
            return id;
        }

    }
}
