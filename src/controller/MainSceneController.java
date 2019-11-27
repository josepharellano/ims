/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ims.SceneManager;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Inventory;
import model.Part;

/**
 * FXML Controller class
 *
 * @author Joseph
 */
public class MainSceneController implements Initializable {
    @FXML
    private Button exit;
    @FXML
    private Button addBtn;
    @FXML 
    private Button modifyBtn;
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
    private TableColumn productIDCol;
    @FXML
    private TableColumn productNameCol;
    @FXML
    private TableColumn productStockCol;
    @FXML
    private TableColumn productPriceCol;
    
    //
    private int partIndex;

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Populate TableViews
        setupPartsTable();
    }    

    @FXML
    private void onExitProgram(ActionEvent event) {
        SceneManager.getInstance().unloadScene();
    }

    @FXML
    private void onLoadPartScene(ActionEvent event) throws IOException{
        PartSceneController controller = SceneManager.getInstance().loadScene(SceneManager.Views.PART);
        if(event.getSource().equals(addBtn)){
            controller.loadAddPartScene();
        }
      
        if(event.getSource().equals(modifyBtn)){
            try{
                partIndex = partTable.getSelectionModel().getSelectedIndex();
                Part selected = (Part) partTable.getSelectionModel().getSelectedItem();
                controller.loadModifyPartScene(selected,partIndex);  
            }catch(NullPointerException e){
                //Unable to load in the part scene so must cleanup
                SceneManager.getInstance().unloadScene();      
                //Add User Feedback to GUI here
            }         
        }      
        SceneManager.getInstance().displayScene();
    }
    
    @FXML
    private void onPartDelete(ActionEvent event) {
        Part part = (Part) partTable.getSelectionModel().getSelectedItem();
        Inventory.deletePart(part);
    }

    @FXML
    private void onAddProduct(ActionEvent event) {
    }

    @FXML
    private void onModifyProduct(ActionEvent event) {
    }

    @FXML
    private void onProductDelete(ActionEvent event) {
        
    }
    
    private void setupPartsTable(){  
        partTable.setItems(Inventory.getAllParts());
  
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); 
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        
        //Custom formatting for the price cell
        partPriceCol.setCellFactory(column->{
            return new TableCell<TableView,Double>(){
                @Override
                protected void updateItem(Double price,boolean empty){
                    super.updateItem(price, empty);
                    
                    if(price == null || empty){
                        setText("");
                    }else{
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                        setText(currencyFormatter.format(price));
                    }
                }
                
            };
        });       
    }
 
  
}
