
package com.woefe.shoppinglist.shoppinglist;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ShoppingListMarshaller {
    public static void marshall(OutputStream stream, ShoppingList list) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream))) {
            writer.write("[ ");
            writer.write(list.getName());
            writer.write(" ]\n\n");

            for (ListItem item : list) {
                String quantity = item.getQuantity();
                String description = item.getDescription();

                if (item.isChecked()) {
                    writer.write("// ");
                }

                if (description != null) {
                    writer.write(description);
                }

                if (quantity != null && !quantity.equals("")) {
                    writer.write(" #");
                    writer.write(quantity);
                }

                writer.write("\n");
            }
        }
    }
}
