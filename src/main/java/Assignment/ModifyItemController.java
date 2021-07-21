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

public class ModifyItemController {

    @FXML
    public TextField ModifiedNameItem;
    public TextField ModifiedSerialItem;
    public TextField ModifiedPriceItem;

    private Alert alert = new Alert(Alert.AlertType.ERROR); //Here we are creating an alert to be used in any error interaction

    ObservableList<InventoryItems> Modify = FXCollections.observableArrayList();
    int index;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventory.fxml"));
    Parent root = loader.load();
    InventoryController controller = loader.getController();
;

    public ModifyItemController() throws IOException {
        //Receiving all the list information.
        controller.SendItemInformation(Modify);
        System.out.println(Modify);
        System.out.println(index);
    }

    @FXML
    public void ModifyItem(ActionEvent actionEvent) {

        //If the user don't modify the name, price or serial number; these values will remain the same unchanged.
        String NewName = Modify.get(index).getName(); //Starts with the initial name
        String NewPrice = Modify.get(index).getPrice(); //Starts with the initial price.
        String NewSerial = Modify.get(index).getSerial(); //Starts with the initial serial.

        boolean check = true;

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
         if(ModifiedSerialItem.getText().isEmpty() == false){
             NewSerial = ModifiedSerialItem.getText();
             check = true;

         }
         if(ModifiedNameItem.getText().isEmpty() && ModifiedPriceItem.getText().isEmpty() && ModifiedSerialItem.getText().isEmpty()){
             alert.setTitle("Error!");
             alert.setContentText("Error! There is nothing new to modify!");
             alert.showAndWait();
             check = false;
         }

         //If passes all the test cases, and there is not problems; we are ready to modify the item
         if(check) {
             //Creating an object of Inventory Items, in order to switch to the new values of this item
             InventoryItems x = new InventoryItems(NewName,NewSerial,NewPrice);
             x.ModifyName(NewName);
             x.ModifySerialNumber(NewSerial);
             x.ModifyPrice(NewPrice);
             controller.ModifyInformation(x, index);
             Dialog("The item was modified correctly!");
             ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
         }
    }//Working

    public void Close(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void ReceiveIndexToModify(int x){
        index = x;
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
}
