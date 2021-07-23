import Assignment.InventoryItems;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Luis Andres Acosta Mejia
 */

//This test class will be testing for all the functions of the program created.

class InventoryItemsTest {
    //New Inventory created for testing functions!
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
        x.setPrice("250");
        assertEquals("250",x.getPrice());
    }

    @Test
    void modifyName() {
        x.ModifyName("PC");
        assertEquals("PC",x.getName()); //Checking if we obtain this name after calling the modify function
    }

    @Test
    void modifySerialNumber() {
        x.ModifySerialNumber("1231231234");
        assertEquals("1231231234",x.getSerial()); //Checking if we obtain this serial number after calling the modify function.
    }

    @Test
    void modifyPrice() {
        x.ModifyPrice("550");
        assertEquals("550",x.getPrice()); //Checking if we obtain this price after calling the modify function
    }

    @Test
    void findName() {
        assertEquals("Samsung TV",x.FindName()); //Checking if we are able to find the name by calling this function
    }

    @Test
    void findSerialNumber() {
        assertEquals("1234567890",x.FindSerialNumber()); //Checking if we are able to find the serial number by calling this function
    }

    @Test
    void saveName() {
        assertEquals("Samsung TV",x.SaveName()); //Checking if we are saving the correct name by calling this function
    }

    @Test
    void saveSerialNumber() {
        assertEquals("1234567890",x.SaveSerialNumber()); //Checking if we are saving the correct serial number by calling this function
    }

    @Test
    void savePrice() {
        assertEquals("300",x.SavePrice()); //Checking if we are saving the correct price by calling this function
    }

    @Test
    void LoadName(){
        x.LoadName("Car");
        assertEquals("Car",x.getName());

    }
    @Test
    void LoadSerial(){
        x.LoadSerial("1231231230");
        assertEquals("1231231230",x.getSerial());

    }

    @Test
    void LoadPrice(){
        x.LoadPrice("500");
        assertEquals("500",x.getPrice());
    }
}