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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Luis Andres Acosta Mejia
 */

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

    //Used the carry all the information of the items using InventoryItems class
     public static ObservableList<InventoryItems> list = FXCollections.observableArrayList();

     //Used to carry all the information of the items using Inventory Items class but only the ones we are looking to search for.
     public static ObservableList<InventoryItems> SearchItems = FXCollections.observableArrayList();

    @FXML
    public void SearchItem(ActionEvent event) {

        //Checking if the list is empty
        if(list.isEmpty()){
            alert.setTitle("Error!");
            alert.setContentText("Error! There isn't any item in the list to search");
            alert.showAndWait();
        }
        //Checking if the item to search text is empty
        else if(ItemToSearch.getText().isEmpty()){
            alert.setTitle("Error!");
            alert.setContentText("Error! Please insert a value to search for in the table.");
            alert.showAndWait();
        }
        //We are searching for name now.
        //Searching for name
        else if(SelectionSearch.getSelectionModel().getSelectedItem().equals("Name")){

            SearchItems.clear(); //We have to clear the list before using it in the other type of search.

            for(int i = 0; i < list.size(); i++){
                //We found the name
                if(list.get(i).FindName().equals(ItemToSearch.getText())){
                    SearchItems.setAll(list.get(i)); //We are adding it to the list
                }
            }
            //If the list is not empty; they found a value
            if(SearchItems.isEmpty() == false){
                table.setItems(SearchItems); //Then we are going to display the items searched
                table.refresh();
                Dialog("These are items found by this name on the inventory.");
            }
            //If not; there wasn't any item found with the name
            else{
                alert.setTitle("Error!");
                alert.setContentText("Error! There isn't any item in the inventory with this name '" + ItemToSearch.getText() + "'.");
                alert.showAndWait();
            }
            ItemToSearch.clear(); //Clear the input text.
        }

        //Searching for serial number
        else if(SelectionSearch.getSelectionModel().getSelectedItem().equals("Serial Number")){

            SearchItems.clear(); //We have to clear the list before using it in the other type of search.

            for(int i = 0; i < list.size(); i++){
                //We found the name
                if(list.get(i).FindSerialNumber().equals(ItemToSearch.getText())){
                    SearchItems.setAll(list.get(i)); //Adding to the list
                }
            }
            //We are able to display since there are items in the list found
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
        //Here the user will be able to reset the original list from the searching function
        table.setItems(list);
        table.refresh();
    }//Working

    @FXML
    public void ModifyItem(ActionEvent event) {

        //Loading new Window to modify the item
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
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//Working

    @FXML
    public void RemoveItem(ActionEvent actionEvent) {
        //Checking if the user didn't select any item of the inventory to remove
        if(table.getSelectionModel() == null){
            alert.setTitle("Error!");
            alert.setContentText("Error! Select an item first");
            alert.showAndWait();
        }
        //Checking if the list is empty
        else if(list.isEmpty()){
            alert.setTitle("Error!");
            alert.setContentText("Error! There isn't any item in the inventory to be deleted");
            alert.showAndWait();
        }
        //If passes all the cases; the we can proceed to delete this item.
        else{
            list.remove(table.getSelectionModel().getSelectedIndex()); //Removing it from the list
            table.refresh(); //Refreshing the list
            Dialog("The item was deleted successfully!");
        }
    }//Working

    @FXML
    public void NewItem(ActionEvent event) {

        //Loading a new window to add a new item
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewItem.fxml"));
            Parent root = (Parent) loader.load();

            //Working
           NewItemController controller = loader.getController();
           controller.ReceivePreviousItemInformation(list); //Sending the current list to check if they are duplicates.

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
                    Dialog("The Inventory was saved successfully on your computer! The name of the inventory is " + file.getName());

                }//Working

                //The user selected HTML
                //Fix the spaces to print in HTML
                else if(fileChooser.getSelectedExtensionFilter().getDescription().equals("HTML")){

                    PrintWriter BW = new PrintWriter(file);

                    //Here we are writing HTML code to setup the url and design
                    BW.printf("<html>");
                    BW.printf("<body>");
                    BW.printf("<h1 align = center> Inventory </h1>"); //Header of the website
                    BW.printf("<table align = center border = 1 style = background-color:#e0e0e0 cellpadding = 5 cellspacing = 5>"); //Table features such as border,  align to the center of the website and background color

                    //Here we are printing the headers of the table
                    BW.printf("<tr><th><font size = 5 >Price</th><th><font size = 5 >Serial Number</th><th><font size = 5 >Name</th><tr>");
                    for (int i = 0; i < list.size(); i++) {
                        //Here we are printing the other values to insert inside the table
                        BW.printf("<tr><td><font size = 4>" + list.get(i).SavePrice() + "</td><td><font size = 4>" + list.get(i).SaveSerialNumber()+ "</td><td><font size = 4>" + list.get(i).SaveName() + "</td></tr>");

                    }
                    BW.printf("</table>");
                    BW.printf("</body>");
                    BW.printf("</html>");

                    Dialog("The Inventory was saved successfully on your computer! The name of the inventory is " + file.getName());
                    BW.close();
                } //Working

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
                    Dialog("The Inventory was saved successfully on your computer! The name of the inventory is " + file.getName());


                }//Working

            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    } //Working
    @FXML
    public void LoadInventory(ActionEvent actionEvent) throws IOException {

        //Here we are getting the stage that we are currently using; therefore, we are going to create a new Window from it
        Window stage = MainWindow.getScene().getWindow();
        fileChooser.setTitle("Load To-do List");


            //Opening dialog to load a to-do list
            File file = fileChooser.showOpenDialog(stage);
            fileChooser.setInitialDirectory(file.getParentFile()); //Save the chosen directory
            FileReader reader = new FileReader(file);

            if(fileChooser.getSelectedExtensionFilter().getDescription().equals("TSV")){


                String nameReaded = ""; //Used to save the name that can come with spaces

                BufferedReader bf = new BufferedReader(reader);
                String st = bf.readLine(); //We don't read the first line

                list.clear(); //We clear the list so we are going to show a new inventory

                while((st = bf.readLine()) != null) {
                    String[] information = st.split("\\s+");
                    for(int i = 2; i < information.length; i++){
                        nameReaded = nameReaded + information[i] + " ";
                    }
                    //Here we are creating an instance of the class to load new values
                    InventoryItems x = new InventoryItems("","","");

                    x.LoadName(nameReaded); //Loading name
                    x.LoadSerial(information[1]); //Loading serial
                    x.LoadPrice(information[0]); //Loading price

                    list.add(x); //Adding this new class to the list

                    nameReaded = ""; //We clear the nameReaded variable to read the other name again
                }

                reader.close();
                Dialog("The Inventory was loaded correctly " + file.getName());

            }//Working

            else if(fileChooser.getSelectedExtensionFilter().getDescription().equals("HTML")){
                list.clear();

                Document htmlDoc = null;
                htmlDoc = Jsoup.parse(new File(file.getPath()), "ISO-8859-1");

                Element table = htmlDoc.select("table").get(0);
                Elements rows = table.select("tr");

                for(int i = 2; i < rows.size(); i++){
                    Element row = rows.get(i);
                    Elements cols = row.select("td");

                    //Here we are creating an instance of the class to load new values
                    InventoryItems x = new InventoryItems("","","");

                    x.LoadName(cols.get(2).text()); //Loading Name
                    x.LoadSerial(cols.get(1).text()); //Loading Serial
                    x.LoadPrice(cols.get(0).text()); //Loading Price

                    list.add(x); //Adding to the list
                }
                Dialog("The Inventory was loaded correctly " + file.getName());

            }//Working


            else if(fileChooser.getSelectedExtensionFilter().getDescription().equals("JSON")){
                list.clear();
                Gson gson = new Gson();
                Inventory inventory = gson.fromJson(reader,Inventory.class);
                for(int i = 0; i < inventory.getDescriptor().size(); i++){

                    //Here we are creating an instance of the class to load new values
                    InventoryItems x = new InventoryItems("","","");

                    //Here we are loading the name
                    String name  = inventory.getDescriptor().get(i).getName();

                    //Here we are loading the price
                    String price = inventory.getDescriptor().get(i).getPrice();

                    //Here we are loading the serial number
                    String serial = inventory.getDescriptor().get(i).getSerial();

                    //Here we are calling the methods of the InventoryItems class created to load
                    x.LoadName(name);
                    x.LoadSerial(serial);
                    x.LoadPrice(price);

                    //Now we are adding this Item to the list.
                    list.add(x);

                }
                Dialog("The Inventory was loaded correctly " + file.getName());
            }//Working

            table.refresh(); //We refresh the table to show the new values.


    } //Completed

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        //Here we are initialize the search box.

        //Here we are initializing the type of extensions used to save or load the inventory
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV", "*.tsv"), new FileChooser.ExtensionFilter("HTML","*.html"), new FileChooser.ExtensionFilter("JSON","*.json"));

        //We are setting first the Name to be the first option to search then te user can switch to serial number
        SelectionSearch.setValue("Name");
        SelectionSearch.setItems(itemsChoiceBox);

        //Here we are setting up the correct columns
        name.setCellValueFactory(new PropertyValueFactory<InventoryItems,String>("name"));
        serial.setCellValueFactory(new PropertyValueFactory<InventoryItems,String>("serial"));
        value.setCellValueFactory(new PropertyValueFactory<InventoryItems,Double>("price"));
        table.setItems(list);
        table.refresh();

    } //Completed

    //Receiving the information of new items created.
    public void ReceiveItemInformation(ObservableList <InventoryItems> x){ //Here is a function to do a communication between scenes which will receive the information from another class with all the lists created.
        list.addAll(x); //Add all the information of the list with the Inventory Items created

        table.setItems(list); //Setting the table

        table.refresh(); //Refreshing the table
    }//Completed

    //Sending information of the current list.
    public void SendItemInformation(ObservableList <InventoryItems> x){
        x.addAll(list); //Here we are sending all the information of the current list to the list of the other controller window.
    }//Working

    //Here we are sending the information of the current list and index of the item selected to modify.
    public void ModifyInformation(InventoryItems x, int index){
        list.set(index,x); //Here we are setting at an exact index the new item modified.
        table.refresh();
    }//Working

    //Used to display a message about new changes
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
