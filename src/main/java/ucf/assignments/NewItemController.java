package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Luis Andres Acosta Mejia
 */


public class NewItemController {

    //This is the controller of the new window created to add new items to the Inventory

    String price;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventory.fxml"));
    Parent root = loader.load();
    InventoryController controller = loader.getController();


    private Alert alert = new Alert(Alert.AlertType.ERROR); //Here we are creating an alert to be used in any error interaction


    @FXML
    public TextField NewItemName;
    public TextField NewItemSerialNumber;
    public TextField NewItemPrice;
    public Button BtnClose;

    boolean cont = false; //Used to check if all the possible error cases pass.

    //Used to save all the items new created
    ObservableList<InventoryItems> items = FXCollections.observableArrayList(); //Used to save all the tasks created in this controller
    ObservableList<InventoryItems> actualItems = FXCollections.observableArrayList(); //Used to save all the current tasks created in this controller
    ObservableList<InventoryItems> previousitems = FXCollections.observableArrayList(); //Used to save all the tasks created from the previous main controller

    public NewItemController() throws IOException {
        //Here we are calling the function of the main controller to send all the previous items to the list
        controller.SendItemInformation(previousitems);
    }

    @FXML
    public void AddNewItem(ActionEvent event) {

        //Here are some test cases used in order to identify some issues

        //Check if the name is empty
        if (NewItemName.getText().isEmpty()) {
            alert.setTitle("Error!");
            alert.setContentText("Error! The name field is empty");
            alert.showAndWait();

            //CHecking if the lenght of the name is less than 2 and more than 256 characters.
        } else if (NewItemName.getText().length() < 2 || NewItemName.getText().length() > 256) {
            alert.setTitle("Error!");
            alert.setContentText("Error! The name of the item has to be between 2 and 256 characters.");
            alert.showAndWait();

        //Checking if the Serial number field is empty.
        } else if (NewItemSerialNumber.getText().isEmpty()) {
            alert.setTitle("Error!");
            alert.setContentText("Error! The serial number of the item is empty");
            alert.showAndWait();
        }
        //Checking if the Serial number has the pattern (XXXXXXXXXX) 10 numbers/digits
        else if(NewItemSerialNumber.getText().length() != 10){
            alert.setTitle("Error!");
            alert.setContentText("Error! The serial number has to have the pattern 'XXXXXXXXXX' (10 digits that can be numbers/letters)");
            alert.showAndWait();
            NewItemSerialNumber.clear(); //We clear this field, so the user can input a new serial number
        }

        //Checking if the price is empty
        else if (NewItemPrice.getText().isEmpty()) {
            alert.setTitle("Error!");
            alert.setContentText("Error! The price field is empty");
            alert.showAndWait();
        }
        //Here we are checking if the price has characters and not numbers.
        else if(NewItemPrice.getText().matches("[a-zA-Z]")){
            alert.setTitle("Error!");
            alert.setContentText("Error! The price field has characters; it has to be only numbers");
            alert.showAndWait();
            NewItemPrice.clear();
        }
        //Checking if there is an item already in the inventory with this serial number
        else if(CheckItemPreviousRepeatedSerial() == false){
            alert.setTitle("Error!");
            alert.setContentText("Error! There is an item in the list already that has this serial number");
            alert.showAndWait();
            NewItemSerialNumber.clear();
        }
        else if(CheckItemNewRepeatedSerial() == false){
            alert.setTitle("Error!");
            alert.setContentText("Error! There is an item in the list already that has this serial number");
            alert.showAndWait();
            NewItemSerialNumber.clear();
        }
        //Checking if the serial number has special symbols
        else if(SymbolsInSerial(NewItemSerialNumber.getText())){
            alert.setTitle("Error!");
            alert.setContentText("Error! There is an item in the list already has this serial number");
            alert.showAndWait();
            NewItemSerialNumber.clear();
        }
        else {
                //If we don't detect any problems; then we are able to do this back
                    items.add(new InventoryItems(NewItemName.getText(), NewItemSerialNumber.getText(), price = "$" + NewItemPrice.getText())); //Creating new Item information
                    actualItems.addAll(items); //Used to save all the actual items, in order to check for repeated serial numbers just added.
                    Dialog("The Item was added succesfully!");

                    controller.ReceiveItemInformation(items); //Sending immediately the item information

                    items.clear();
                    NewItemName.clear();
                    NewItemPrice.clear();
                    NewItemSerialNumber.clear();
                }
        } //Completed

    @FXML
    public void Close(ActionEvent actionEvent) throws IOException {
        //Closing the actual window.
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }//Completed

    public void Dialog(String x){ //This is a function to call a dialog!
        //Creating a dialog
        Dialog<String> dialog = new Dialog<String>();
        //Setting the title
        dialog.setTitle("!NEW Changes!");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        dialog.setContentText(x);
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.showAndWait(); //Showing the dialog
    } //Completed

    //Function to check if the items have the same serial
    public boolean CheckItemPreviousRepeatedSerial(){

        for(int i=0;i<previousitems.size();i++){

            //Checking if the previous items or the new added items has the same serial number.
            if(previousitems.get(i).getSerial().equals(NewItemSerialNumber.getText())){
                return false;
            }
        }
        return true;
    }//Completed

    public boolean CheckItemNewRepeatedSerial(){
        for(int i=0;i<actualItems.size();i++){
            //Checking if the previous items or the new added items has the same serial number.
            if(actualItems.get(i).getSerial().equals(NewItemSerialNumber.getText())){
                actualItems.remove(i); //We removed from the items created so it won't be added.
                return false;
            }
        }
        return true;
    } //Completed

    public boolean SymbolsInSerial(String x){

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]"); //Checking if has this pattern of a symbol or special sign.
        Matcher matcher = pattern.matcher(x);
        return matcher.find(); //Returning yes if found a Symbol
    }//Completed

    //Here we are receiving the information of the previous list.
    public void ReceivePreviousItemInformation(ObservableList <InventoryItems> x){ //Here is a function to do a communication between scenes which will receive the information from another class with all the lists created.
        previousitems.addAll(x);
    }//Completed

}

