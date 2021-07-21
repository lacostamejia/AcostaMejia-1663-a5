package Assignment;

import java.util.Map;

public class Inventory {
    private Map<String,InventoryItems> descriptor;

    public Map<String, InventoryItems> getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Map<String, InventoryItems> descriptor) {
        this.descriptor = descriptor;
    }
}
