/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ims.CurrencyFormatCell;
import ims.SceneManager;
import ims.SceneManager.Views;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author Joseph
 */
public class MainSceneController implements Initializable {
    @FXML
    private TableView partTable;
    @FXML
    private TableColumn partIdCol;
    @FXML
    private TableColumn partNameCol;
    @FXML
    private TableColumn partStockCol;
    @FXML
    private TableColumn partPriceCol;
    @FXML
    private TableView productsTable;
    @FXML
    private TableColumn productIdCol;
    @FXML
    private TableColumn productNameCol;
    @FXML
    private TableColumn productStockCol;
    @FXML
    private TableColumn productPriceCol;
    @FXML
    private TextField inputPartSearch;
    @FXML
    private TextField inputSearchProduct;
 

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Populate TableViews
        setupPartsTable();
        setupProductsTable();
    }    

    @FXML
    private void onExitProgram(ActionEvent event) {
        SceneManager.getInstance().unloadScene();
    }

    @FXML
    private void onModifyPart(ActionEvent event) throws IOException{
        Part selected;
        Integer partIndex;

        partIndex = partTable.getSelectionModel().getSelectedIndex();
        selected = (Part) partTable.getSelectionModel().getSelectedItem();  
        
        if(selected != null){
        onLoadScene(Views.PART,selected,partIndex);    
        }
    }
    
    @FXML
    private void onAddPart(ActionEvent event) throws IOException{
            onLoadScene(Views.PART,null,null);
    }
    
    @FXML
    private void onPartDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete part?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Part part = (Part) partTable.getSelectionModel().getSelectedItem();
            Inventory.deletePart(part);
        }
        
    }
    
    @FXML
    private void onSearchPart(ActionEvent event){ 
        
        String input = inputPartSearch.getText();
        
        //Return if input is empty
        if(input.equals("")){
            return;
        }
        try{
            int search = Integer.parseInt(inputPartSearch.getText());
            Part found = Inventory.lookupPart(search);
            if(found != null){
            partTable.getSelectionModel().select(found);
            }
        }catch(Exception e){
            //ParseInt fails so try searching a string.
            Part found = Inventory.lookupPart(input);
            if(found != null){
                partTable.getSelectionModel().select(found);
            }       
    }
    }
    
    @FXML
    private void onAddProduct(ActionEvent event) throws IOException{
        onLoadScene(Views.PRODUCT, null, null);
    }

    @FXML
    private void onModifyProduct(ActionEvent event) throws IOException{
        Product selected;
        Integer productIndex;
        
        productIndex = productsTable.getSelectionModel().getSelectedIndex();
        selected = (Product) productsTable.getSelectionModel().getSelectedItem();  
        
        if(selected != null){
        onLoadScene(Views.PRODUCT,selected,productIndex);
        }
    }

    @FXML
    private void onProductDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete product?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Product product = (Product)productsTable.getSelectionModel().getSelectedItem();
            if(product == null){
                return;
            }
            Inventory.deleteProduct(product);
        }
        
    }
    
    private void setupPartsTable(){  
        partTable.setItems(Inventory.getAllParts());
  
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); 
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        //Set Price Column to display as Currency
        partPriceCol.setCellFactory((column)-> new CurrencyFormatCell());
    }
    
    private void setupProductsTable(){
        productsTable.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); 
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        //Set Price Col to display as currency
        productPriceCol.setCellFactory((column)-> new CurrencyFormatCell());
    }
    
    /*
    * Param T scene controller
    * Param U Part or Product
    * Param index Index of part or product in respected ArrayList
    */
    private <T extends Initializable,U> void onLoadScene(Views sceneView, U item,Integer index) throws IOException{
       T controller = SceneManager.getInstance().loadScene(sceneView);
       
       try{
           if(item != null){
               //Load Modify Scene through reflection
               controller.getClass().getMethod("loadScene", new Class []{item.getClass() ,int.class}).invoke(controller,item,index);
           }else{
               //Load Add Scene through reflection
               controller.getClass().getMethod("loadScene").invoke(controller);
           }

       }catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
           System.out.println(e.getMessage());
       }
       
       SceneManager.getInstance().displayScene();

    }

    @FXML
    private void onSearchProduct(ActionEvent event) {
        String input = inputSearchProduct.getText();
        
        //Return if input is empty
        if(input.equals("")){
            return;
        }
        try{
            int search = Integer.parseInt(inputSearchProduct.getText());
            Product found = Inventory.lookupProduct(search);
            if(found != null){
            productsTable.getSelectionModel().select(found);
            }
        }catch(Exception e){
            //ParseInt fails so try searching a string.
            Product found = Inventory.lookupProduct(input);
            if(found != null){
                productsTable.getSelectionModel().select(found);
            }       
    }
    }
    
   
}
