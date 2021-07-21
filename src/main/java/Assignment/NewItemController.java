package Assignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class NewItemController {

    String price;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventory.fxml"));
    Parent root = loader.load();
    InventoryController controller = loader.getController();

    //We have to create an Observable list to add all these values
    //private static  ObservableList<InventoryItems> list = new ObservableList<>() {
    //};

    private Alert alert = new Alert(Alert.AlertType.ERROR); //Here we are creating an alert to be used in any error interaction


    @FXML
    public TextField NewItemName;
    public TextField NewItemSerialNumber;
    public TextField NewItemPrice;
    public Button BtnClose;

    boolean cont = false; //Used to check if all the possible error cases pass.

    //Used to save all the items new created
    ObservableList<InventoryItems> items = FXCollections.observableArrayList(); //Used to save all the tasks created in this controller
    ObservableList<InventoryItems> previousitems = FXCollections.observableArrayList(); //Used to save all the tasks created from the previous main controller

    public NewItemController() throws IOException {
        //Here we are calling the function of the main controller to send all the previous items to the list
        controller.SendItemInformation(previousitems);
    }

    @FXML
    public void AddNewItem(ActionEvent event) {

        if (NewItemName.getText().isEmpty()) {
            alert.setTitle("Error!");
            alert.setContentText("Error! The name field is empty");
            alert.showAndWait();

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
        //Working, check how to implement the new list items added.
        else if(CheckItemPreviousRepeatedSerial() == false){
            alert.setTitle("Error!");
            alert.setContentText("Error! There is an item in the list already that has this serial number");
            alert.showAndWait();
            NewItemSerialNumber.clear();
        }
        else {
                //If we don't detect any problems; then we are able to do this back
                items.add(new InventoryItems(NewItemName.getText(), NewItemSerialNumber.getText(), price = "$" + NewItemPrice.getText()));

              /*  //Checking if there was a new item added with the same serial number
                if(CheckItemNewRepeatedSerial() == false){
                    alert.setTitle("Error!");
                    alert.setContentText("Error! There is an item that you just added to the list, which already has this serial number");
                    alert.showAndWait();
                    NewItemSerialNumber.clear();
                }
                //Then we passed all the cases of possible errors.
                else {

               */
                    Dialog("The Item was added succesfully!");
                    System.out.println(items.toString());
                    NewItemName.clear();
                    NewItemPrice.clear();
                    NewItemSerialNumber.clear();
                }
        }
    @FXML
    public void Close(ActionEvent actionEvent) throws IOException {

        //Sending back the information to the main controller
        controller.ReceiveItemInformation(items);

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
    }
    //Check this function
    public boolean CheckItemNewRepeatedSerial(){
        for(int i=0;i<items.size();i++){
            //Checking if the previous items or the new added items has the same serial number.
            if(items.get(i).getSerial().equals(NewItemSerialNumber.getText())){
                items.remove(i); //We removed from the items created so it won't be added.
                return false;
            }
        }
        return true;
    }


    //Here we are receiving the information of the previous list.
    public void ReceivePreviousItemInformation(ObservableList <InventoryItems> x){ //Here is a function to do a communication between scenes which will receive the information from another class with all the lists created.
        previousitems.addAll(x);
    }//Completed

}

