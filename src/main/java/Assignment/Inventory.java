package Assignment;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<InventoryItems> Inventory;

    public ArrayList<InventoryItems> getDescriptor() {
        return Inventory;
    }

    public void setDescriptor(ArrayList<InventoryItems> Inventory) {
        this.Inventory = Inventory;
    }
}
