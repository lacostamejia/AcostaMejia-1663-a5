package Assignment;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    @FXML
    public Button NewItemBtn;
    public Button RemoveBtn;
    public ChoiceBox SelectionSearch;
    public AnchorPane MainWindow;
    public Menu FileMenu;
    public javafx.scene.control.ProgressBar ProgressBar;

    FileChooser fileChooser = new FileChooser();

    private Alert alert = new Alert(Alert.AlertType.ERROR); //Here we are creating an alert to be used in any error interaction

    ObservableList<String> itemsChoiceBox = FXCollections.observableArrayList("Name","Serial Number");

    @FXML
    public TextField ItemToSearch;

    @FXML
    public TableView<InventoryItems> table;

    @FXML
    public TableColumn<InventoryItems,Double> value;

    @FXML
    public TableColumn<InventoryItems, String> serial;

    @FXML
    public TableColumn<InventoryItems, String> name;

     public static ObservableList<InventoryItems> list = FXCollections.observableArrayList(new InventoryItems("Xbox One","AXB124AXY3","$399.00"),new InventoryItems("Samsung TV","S40AZBDE47","$599.99"));
     public static ObservableList<InventoryItems> SearchItems = FXCollections.observableArrayList();

    @FXML
    public void SearchItem(ActionEvent event) {
        //Checking if the item to search text is empty
        if(ItemToSearch.getText().isEmpty()){
            alert.setTitle("Error!");
            alert.setContentText("Error! Please insert a value to search for in the table.");
            alert.showAndWait();
        }
        //We are searching for name now.
        else if(SelectionSearch.getSelectionModel().getSelectedItem().equals("Name")){

            SearchItems.clear(); //We have to clear the list before using it in the other type of search.

            for(int i = 0; i < list.size(); i++){
                //We found the name
                if(list.get(i).FindName().equals(ItemToSearch.getText())){
                    SearchItems.setAll(list.get(i));
                }
            }
            if(SearchItems.isEmpty() == false){
                table.setItems(SearchItems);
                table.refresh();
                Dialog("These are items found by this name on the inventory.");
            }
            else{
                alert.setTitle("Error!");
                alert.setContentText("Error! There isn't any item in the inventory with this name '" + ItemToSearch.getText() + "'.");
                alert.showAndWait();
            }
            ItemToSearch.clear(); //Clear the input text.
        }
        else if(SelectionSearch.getSelectionModel().getSelectedItem().equals("Serial Number")){

            SearchItems.clear(); //We have to clear the list before using it in the other type of search.

            for(int i = 0; i < list.size(); i++){
                //We found the name
                if(list.get(i).FindSerialNumber().equals(ItemToSearch.getText())){
                    SearchItems.setAll(list.get(i));
                }
            }
            //We are able to search since there are items in the list
            if(SearchItems.isEmpty() == false){
                table.setItems(SearchItems);
                table.refresh();
                Dialog("These are items found by this name on the inventory.");
            }
            else{
                alert.setTitle("Error!");
                alert.setContentText("Error! There isn't any item in the inventory with this Serial Number '" + ItemToSearch.getText() + "'.");
                alert.showAndWait();
            }
            ItemToSearch.clear(); //Clear the input text.
        }
    }//Working

    @FXML
    public void ResetSearch(ActionEvent actionEvent) {
        table.setItems(list);
        table.refresh();
    }//Working

    @FXML
    public void ModifyItem(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyItem.fxml"));
            Parent root = (Parent) loader.load();

            //If the item is not selected
            if(table.getSelectionModel().getSelectedItem() == null){
                alert.setTitle("Error!");
                alert.setContentText("Error! Please select an item to modify first");
                alert.showAndWait();
            }
            else {
                //Sending the index selected to modify
                ModifyItemController controller = loader.getController();
                controller.ReceiveIndexToModify(table.getSelectionModel().getSelectedIndex());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modify Item");
                // if(Show_List();)
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//Working

    @FXML
    public void RemoveItem(ActionEvent actionEvent) {
        if(table.getSelectionModel() == null){
            alert.setTitle("Error!");
            alert.setContentText("Error! Select an item first");
            alert.showAndWait();
        }
        else{
            list.remove(table.getSelectionModel().getSelectedIndex()); //Removing it from the list
            table.refresh();
            Dialog("The item was deleted successfully!");
        }
    }//Working

    @FXML
    public void NewItem(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewItem.fxml"));
            Parent root = (Parent) loader.load();

            //Working
           NewItemController controller = loader.getController();
           controller.ReceivePreviousItemInformation(list);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add an new Item");
            // if(Show_List();)
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//Working

    @FXML
    public void SaveInventory(ActionEvent actionEvent) {
        if (list.isEmpty()) {
            alert.setTitle("Error!");
            alert.setContentText("Error! There isn't any item in the inventory to save.");
            alert.showAndWait();
        }
        //If the there is a list; then we can save items
        else {
            Window stage = MainWindow.getScene().getWindow();
            fileChooser.setTitle("Save Inventory");
            //Modify this part, we need to save in HTML, JSON, and TSV

            try {
                File file = fileChooser.showSaveDialog(stage); //Show Window to save item
                fileChooser.setInitialDirectory(file.getParentFile()); //Save the chosen directory


                //The user selected to write in TSV (tab-separated value); so we are going to export in this format
                if(fileChooser.getSelectedExtensionFilter().getDescription().equals("TSV")){
                    PrintWriter W = new PrintWriter(file); //Here we are going to write
                    //Value Serial Number // Name
                    W.printf("%-20s%-20s%-20s","Value","Serial Number","Name");
                    W.println();
                    //Now we are going to iterate to write each value to the text file desired.
                    for (int i = 0; i < list.size(); i++) {
                        W.printf("%-20s%-20s%-20s\n",list.get(i).SavePrice(),list.get(i).SaveSerialNumber(),list.get(i).SaveName());
                    }
                    W.close(); //Closing write
                    Dialog("The list was saved successfully on your computer! The name of the inventory is " + file.getName());

                }//Working

                //The user selected HTML
                //Fix the spaces to print in HTML
                else if(fileChooser.getSelectedExtensionFilter().getDescription().equals("HTML")){
                    PrintWriter BW = new PrintWriter(file);
                    BW.printf("<div><h1>%s          %s           %s</h1><div>","Value","Serial Number","Name");

                    for (int i = 0; i < list.size(); i++) {
                        BW.printf("<div><p>%s           %s          %s</p><div>\n",list.get(i).SavePrice(),list.get(i).SaveSerialNumber(),list.get(i).SaveName());
                    }

                    Dialog("The list was saved successfully on your computer! The name of the inventory is " + file.getName());
                    BW.close();
                }
                //The user selected JSON
                else if(fileChooser.getSelectedExtensionFilter().getDescription().equals("JSON")){
                    //We declared the variable to write
                    FileWriter W = new FileWriter(file);

                    //We create the JSON Object
                    JSONObject obj = new JSONObject();

                    //We add the list of inventory to the object
                    obj.put("Inventory",list); //Here we are adding the List object which contains al the items with their information
                    System.out.println(obj);
                    W.write(obj.toString()); //Writing the object

                    W.close(); //Close writer

                    //Used to test
                    /*
                    JSONArray jsonArray = obj.getJSONArray("Inventory");
                    for(int i = 0; i < jsonArray.length(); i++){
                           System.out.println(jsonArray.getJSONObject(i)) //Printing each item information
                    }
                    */

                }//Working


            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    }
    @FXML
    public void LoadInventory(ActionEvent actionEvent) throws FileNotFoundException {

        //Here we are getting the stage that we are currently using; therefore, we are going to create a new Window from it
        Window stage = MainWindow.getScene().getWindow();
        fileChooser.setTitle("Load To-do List");


            //Opening dialog to load a to-do list
            File file = fileChooser.showOpenDialog(stage);
            fileChooser.setInitialDirectory(file.getParentFile()); //Save the chosen directory
            FileReader reader = new FileReader(file);

            if(fileChooser.getSelectedExtensionFilter().getDescription().equals("TSV")){
                System.out.println("Hola");

            }
            else if(fileChooser.getSelectedExtensionFilter().getDescription().equals("HTML")){

            }
            else if(fileChooser.getSelectedExtensionFilter().getDescription().equals("JSON")){
                System.out.println("Hola");
                Gson gson = new Gson();
                InventoryItems inventory = gson.fromJson(reader,InventoryItems.class);
                System.out.println(inventory);
                //Fix getting null

            }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //Check bug of filtering the value

        /*
        //Creating a graphic (image)
        Image img = new Image(getClass().getResourceAsStream("/Assignment/AddItemImage.png"));
        ImageView view = new ImageView(img);
        view.setFitHeight(30);
        NewItemBtn.setText(null);
        view.setPreserveRatio(true);
        NewItemBtn.setGraphic(view);

         img = new Image(getClass().getResourceAsStream("/Assignment/RemoveItemImage.png"));
         view = new ImageView(img);
        view.setFitHeight(30);
        RemoveBtn.setText(null);
        view.setPreserveRatio(true);
        RemoveBtn.setGraphic(view);
        */

        //Here we are initialize the search box.

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV", "*.tsv"), new FileChooser.ExtensionFilter("HTML","*.html"), new FileChooser.ExtensionFilter("JSON","*.json"));

        SelectionSearch.setValue("Name");
        SelectionSearch.setItems(itemsChoiceBox);

        //Here we are setting up the correct columns
        name.setCellValueFactory(new PropertyValueFactory<InventoryItems,String>("name"));
        serial.setCellValueFactory(new PropertyValueFactory<InventoryItems,String>("serial"));
        value.setCellValueFactory(new PropertyValueFactory<InventoryItems,Double>("price"));
        table.setItems(list);
        table.refresh();

    }

    //Check this part to associate each item with the list created.
    public void ReceiveItemInformation(ObservableList <InventoryItems> x){ //Here is a function to do a communication between scenes which will receive the information from another class with all the lists created.
        list.addAll(x);
        table.setItems(list);
        table.refresh();
    }//Completed

    public void SendItemInformation(ObservableList <InventoryItems> x){
        x.addAll(list);
    }//Working

    public void ModifyInformation(InventoryItems x, int index){
        list.set(index,x);
        table.refresh();
    }//Working

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
