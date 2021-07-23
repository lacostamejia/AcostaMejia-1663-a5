package Assignment;

import java.util.ArrayList;

/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Luis Andres Acosta Mejia
 */


//Created to load JSON file into an array list and find each value then.

public class Inventory {
    private ArrayList<InventoryItems> Inventory;

    public ArrayList<InventoryItems> getDescriptor() {
        return Inventory;
    }

    public void setDescriptor(ArrayList<InventoryItems> Inventory) {
        this.Inventory = Inventory;
    }
}
