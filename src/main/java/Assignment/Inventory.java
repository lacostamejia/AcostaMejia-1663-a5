package Assignment;

import java.util.ArrayList;

/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Luis Andres Acosta Mejia
 */


//Created to load JSON file into an array list and find each value then.

public class Inventory {

    //Creating an array list of InventoryItems which will be loaded.
    private ArrayList<InventoryItems> Inventory;

    //Creating a getDescriptor function that returns the Inventory
    public ArrayList<InventoryItems> getDescriptor() {
        return Inventory;
    }

    //This is the setter
    public void setDescriptor(ArrayList<InventoryItems> Inventory) {
        this.Inventory = Inventory;
    }
}
