package Assignment;

import java.util.ArrayList;


//Created to load JSON file.

public class Inventory {
    private ArrayList<InventoryItems> Inventory;

    public ArrayList<InventoryItems> getDescriptor() {
        return Inventory;
    }

    public void setDescriptor(ArrayList<InventoryItems> Inventory) {
        this.Inventory = Inventory;
    }
}
