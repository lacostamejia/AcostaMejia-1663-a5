import Assignment.InventoryItems;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemsTest {
    //New Inventory created for testing!
    InventoryItems x = new InventoryItems("Samsung TV","1234567890","300");

    @Test
    void getName() {
        assertEquals("Samsung TV",x.getName());
    }

    @Test
    void getPrice() {
        assertEquals("300",x.getPrice());
    }

    @Test
    void getSerial() {
        assertEquals("1234567890",x.getSerial());
    }

    @Test
    void setName() {
        x.setName("Watch");
        assertEquals("Watch",x.getName());
    }

    @Test
    void setSerial() {
        x.setSerial("1234561234");
        assertEquals("1234561234",x.getSerial());
    }

    @Test
    void setPrice() {
    }

    @Test
    void modifyName() {
    }

    @Test
    void modifySerialNumber() {
    }

    @Test
    void modifyPrice() {
    }

    @Test
    void findName() {
    }

    @Test
    void findSerialNumber() {
    }

    @Test
    void saveName() {
    }

    @Test
    void saveSerialNumber() {
    }

    @Test
    void savePrice() {
    }
}