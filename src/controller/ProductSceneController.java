/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ims.CurrencyFormatCell;
import ims.SceneManager;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Inventory;
import model.Part;
import model.Product;
import validator.ValidationException;
import validator.Validator;

/**
 * FXML Controller class
 *
 * @author Joseph
 */
public class ProductSceneController implements Initializable {
    @FXML
    private TextField inputSearch;
    @FXML
    private TableView productPartsTable;
    @FXML
    private TextField inputMax;
    @FXML
    private TextField inputMin;
    @FXML
    private TextField inputId;
    @FXML
    private TextField inputName;
    @FXML
    private TextField inputStock;
    @FXML
    private TextField inputPrice;
    @FXML
    private Text sceneTitle;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn partsIdCol;
    @FXML
    private TableColumn partsNameCol;
    @FXML
    private TableColumn partsStockCol;
    @FXML
    private TableColumn partsCostCol;
    @FXML
    private TableColumn pPartsIdCol;
    @FXML
    private TableColumn pPartsNameCol;
    @FXML
    private TableColumn pPartsStockCol;
    @FXML
    private TableColumn pPartsCostCol;
    
    private ObservableList<Part> associatedParts;
    
    //Holds the Index of the current product being modified
    private Integer modifyingIndex;
    @FXML
    private TextField errorField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        associatedParts = FXCollections.observableArrayList();
        setupListeners();
        setupTables();
    }    

    @FXML
    private void onSearchPart(ActionEvent event) {
        String input = inputSearch.getText();
        
        //Return if input is empty
        if(input.equals("")){
            return;
        }
        try{
            int search = Integer.parseInt(inputSearch.getText());
            Part found = Inventory.lookupPart(search);
            if(found != null){
            partsTable.getSelectionModel().select(found);
            }
        }catch(Exception e){
            //ParseInt fails so try searching a string.
            Part found = Inventory.lookupPart(input);
            if(found != null){
                partsTable.getSelectionModel().select(Inventory.lookupPart(input));
            }       
    }
    }

    @FXML
    private void onAddPart(ActionEvent event) {
        Part part = partsTable.getSelectionModel().getSelectedItem();
        
        if(part == null){
            return;
        }
       
        associatedParts.add(part);
    }

    @FXML
    private void onDeletePart(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Delete part from product?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Part part = (Part)productPartsTable.getSelectionModel().getSelectedItem();
            associatedParts.remove(part);
        }
    }

    @FXML
    private void onSaveProduct(ActionEvent event) {
        if(validateInput()){
             int id;
             double price;
             
             if(inputId.getText().isEmpty()){
                 //Create new ID
                 id = Inventory.GenerateId();
             }else{
                 id = Integer.parseInt(inputId.getText());
             }
             
             String name = inputName.getText();
             try{
                price = NumberFormat.getCurrencyInstance().parse(inputPrice.getText()).doubleValue();
            }catch(Exception e){
                    System.out.println(e.getMessage());
                    return;
                 }
             int stock = Integer.parseInt(inputStock.getText());
             int max = Integer.parseInt(inputMax.getText());
             int min = Integer.parseInt(inputMin.getText());
             
             Product newProduct = new Product(id,name,price,stock,min,max,associatedParts);
             if(modifyingIndex == null){
                 //Add new part
                 Inventory.addProduct(newProduct);
             }else{
                 //Modify current part
                 Inventory.updateProduct(modifyingIndex, newProduct);
             }
             
             exitScene();
        }
    }

    @FXML
    private void onCancelProduct(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Cancel changes and return to main screen?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            SceneManager.getInstance().unloadScene();
        }
    }
    
    private void setupListeners(){
        //Listener for Cost field
        inputPrice.textProperty().addListener((observable, oldInput, newInput)->{
            //Add currency symbol if needed
            if(!newInput.contains("$")){
                inputPrice.setText("$" + newInput);
            }
        });
    }
    
    private void setupTables(){
        //Setup Part Inventory Table
        
        partsTable.setItems(FXCollections.observableArrayList(Inventory.getAllParts()));
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); 
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        //Set Price Column to display as Currency
        partsCostCol.setCellFactory((column)-> new CurrencyFormatCell());
        
        //Setup Products Part Components Table
        productPartsTable.setItems(associatedParts);
        pPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id")); 
        pPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        pPartsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        pPartsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        //Set Price Column to display as Currency
        pPartsCostCol.setCellFactory((column)-> new CurrencyFormatCell());
        
    }
    private boolean validateInput(){
         boolean validated = false;
        
        Validator nameValidator = new Validator(inputName);
        nameValidator.addValidation(Validator.isEmpty);
        
        Validator invValidator = new Validator(inputStock);
        invValidator.addValidation(Validator.isEmpty);
        invValidator.addValidation(Validator.isNum);
        invValidator.addValidation(Validator.isPosNum);
        //Custom validation to ensure stock level is between min and max
        invValidator.addValidation((field)->{
            int value = Integer.parseInt(field.getText());
            int max = Integer.parseInt(inputMax.getText());
            int min = Integer.parseInt(inputMin.getText());
            if(value <= max){
                if(value < min){
                    throw new ValidationException("Value must be more than Min",field);
                }
            }else{
                throw new ValidationException("Value must be less than Max",field);
            }
        });
        
        Validator costValidator = new Validator(inputPrice);
        costValidator.addValidation(Validator.isCurrency);
        
         
        Validator maxValidator = new Validator(inputMax);
        maxValidator.addValidation(Validator.isEmpty);
        maxValidator.addValidation(Validator.isNum);
        maxValidator.addValidation(Validator.isPosNum);
        
        Validator minValidator = new Validator(inputMin);
        minValidator.addValidation(Validator.isEmpty);
        minValidator.addValidation(Validator.isNum);
        minValidator.addValidation(Validator.isPosNum);
        //Custom validation to ensure min value is less than max
        minValidator.addValidation((field)->{
            int value = Integer.parseInt(field.getText());
            int max = Integer.parseInt(inputMax.getText()); 
            if(value > max){
                throw new ValidationException("Min must be less than Max",field);
            }
        });
    
         try{
            //Remove error styling for fields before validating
            errorField.setText("");
            inputName.getStyleClass().removeAll("error");
            inputPrice.getStyleClass().removeAll("error");
            inputMax.getStyleClass().removeAll("error");
            inputMin.getStyleClass().removeAll("error");
            inputStock.getStyleClass().removeAll("error");
            
            //Run validations on each field
            nameValidator.validate();
            costValidator.validate();
            maxValidator.validate();
            minValidator.validate();
            //Must validate inventory last since it depends on previous fields
            invValidator.validate();
            
            validated = true;
            
            }catch(ValidationException e){
                e.getField().getStyleClass().add("error");
                errorField.setText(e.getMessage());
                return validated;
            }
            return validated;
    }
    
    private void exitScene(){
        SceneManager.getInstance().unloadScene();
    }
    
    public void loadScene(){
        sceneTitle.setText("Add Product");
        
    }
    
    public void loadScene(Product product,int index){
        sceneTitle.setText("Modify Product");
        
        //Index of the part being modified
        modifyingIndex = index;
       
        //Populate fields of existing product info
        inputId.setText(String.valueOf(product.getId()));
        inputName.setText(product.getName());
        inputStock.setText(String.valueOf(product.getStock()));
        inputPrice.setText(NumberFormat.getCurrencyInstance().format(product.getPrice()));      
        inputMax.setText(String.valueOf(product.getMax()));
        inputMin.setText(String.valueOf(product.getMin()));
        associatedParts = FXCollections.observableArrayList(product.getAllAssociatedParts());
        productPartsTable.setItems(associatedParts);
    }
    
}
