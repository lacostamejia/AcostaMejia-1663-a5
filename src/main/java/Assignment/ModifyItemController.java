package Assignment;

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

public class ModifyItemController {

    //This controller is used to Modify a specific item on the table.

    @FXML
    public TextField ModifiedNameItem;
    public TextField ModifiedSerialItem;
    public TextField ModifiedPriceItem;

    private Alert alert = new Alert(Alert.AlertType.ERROR); //Here we are creating an alert to be used in any error interaction

    ObservableList<InventoryItems> Modify = FXCollections.observableArrayList(); //Carries all the previous items, and the item modified.
    int index; //Carries the index of the item to be modified

    //Loading information of the main window
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventory.fxml"));
    Parent root = loader.load(); //Loading the parent rood to obtain the previous values
    InventoryController controller = loader.getController(); //Calling the controller of the main window
;

    public ModifyItemController() throws IOException {
        //Receiving all the previous list information, we need this since we have to verify there isn't any duplicate with the serial number
        controller.SendItemInformation(Modify);
    } //Completed

    @FXML
    public void ModifyItem(ActionEvent actionEvent) {

        //If the user don't modify the name, price or serial number; these values will remain the same unchanged.
        String NewName = Modify.get(index).getName(); //Starts with the initial name
        String NewPrice = Modify.get(index).getPrice(); //Starts with the initial price.
        String NewSerial = Modify.get(index).getSerial(); //Starts with the initial serial.

        boolean check = true; //This is variable used to check if there is an error case in our verification of the name, serial, and price.

        //Let's check if the user type some value to modify just the name
        if(ModifiedNameItem.getText().isEmpty() == false){
            if((ModifiedNameItem.getText().length() < 2 || ModifiedNameItem.getText().length() > 256)) {
                alert.setTitle("Error!");
                alert.setContentText("Error! The name of the item has to be between 2 and 256 characters.");
                alert.showAndWait();
                ModifiedNameItem.clear();
                check = false;
            }
            //If there isn't any error in the name; let save this new name
            else{
                NewName = ModifiedNameItem.getText();
                check = true;
            }
        }
        if(ModifiedPriceItem.getText().isEmpty() == false){
            if(ModifiedPriceItem.getText().matches("[a-zA-Z]")) {
                alert.setTitle("Error!");
                alert.setContentText("Error! The price field has characters; it has to be only numbers");
                alert.showAndWait();
                ModifiedPriceItem.clear();
                check = false;
            }
            else{
                NewPrice = "$" + ModifiedPriceItem.getText();
                check = true;
            }
        }
        if(ModifiedSerialItem.getText().isEmpty() == false) {
            //Checking if the serial number has the format XXXXXXXXX (10 digits letters or digits).

            if (ModifiedSerialItem.getText().length() != 10) {
                alert.setTitle("Error!");
                alert.setContentText("Error! The serial number of the item has to have the format XXXXXXXXX (10 digits letters or digits).");
                alert.showAndWait();
                ModifiedNameItem.clear();
                check = false;
            }
            //Checking if the Serial number has a symbol, which is an error.
            else if(SymbolsInSerial(ModifiedSerialItem.getText())){
                alert.setTitle("Error!");
                alert.setContentText("Error! The serial number can't contain special characters (Symbols).");
                alert.showAndWait();
                ModifiedSerialItem.clear();
                check = false;
            }
            else {
                //Here we are checking if the serial number already exists in an item
                for (int i = 0; i < Modify.size(); i++) {
                    if (Modify.get(i).getSerial().equals(ModifiedSerialItem.getText())) {
                        alert.setTitle("Error!");
                        alert.setContentText("Error! This serial number already exists in an item, please change it.");
                        alert.showAndWait();
                        ModifiedSerialItem.clear();
                        check = false;
                        break; //If we found a duplicate, we are going to break the searching for duplicates.
                    }
                    //If it doesn't exist yet, we are going to save this serial number
                    else {
                        NewSerial = ModifiedSerialItem.getText();
                        check = true;
                    }
                }
            }
        }
        else if(ModifiedNameItem.getText().isEmpty() && ModifiedPriceItem.getText().isEmpty() && ModifiedSerialItem.getText().isEmpty()){
            alert.setTitle("Error!");
            alert.setContentText("Error! There is nothing new to modify!");
            alert.showAndWait();
            check = false;
        }

        //If passes all the test cases, and there is not problems; we are ready to modify the item
        else if (check) {
            //Creating an object of Inventory Items, in order to switch to the new values of this item
            InventoryItems x = new InventoryItems(NewName,NewSerial,NewPrice);

            //Modifying the name, serial number and price
            x.ModifyName(NewName);
            x.ModifySerialNumber(NewSerial);
            x.ModifyPrice(NewPrice);

            //Calling the method of this function, which will modify an exact item at an exact index of the list
            controller.ModifyInformation(x, index);
            Dialog("The item was modified correctly!");
            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
        }
    }//Completed

    @FXML
    public void Close(ActionEvent actionEvent) {
        //Closing the actual window.
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    //Function to receive the index of the item to be modified in the list
    public void ReceiveIndexToModify(int x){
        index = x;
    }//Completed

    //Function to send new changes messages
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

    //Function to check symbols in the serial number
    public boolean SymbolsInSerial(String x){

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]"); //This is the pattern to check if there is a symbol or special sign in the serial number
        Matcher matcher = pattern.matcher(x);
        return matcher.find(); //Returning yes if found a Symbol
    }//Completed
}
