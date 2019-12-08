/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Part.IPartState;
import Part.InHouseState;
import Part.OutsourcedState;
import ims.SceneManager;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.RowConstraints;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;
import validator.Validator;
import validator.ValidationException;

/**
 * FXML Controller class
 *
 * @author Joseph
 */
public class PartSceneController implements Initializable {
    @FXML
    private TextField inputPartName;
    @FXML
    private TextField inputStock;
    @FXML
    private TextField inputCost;
    @FXML
    private TextField inputMax;
    @FXML
    private TextField inputMin;
    @FXML
    private TextField inputId;
    @FXML
    private Label sceneTitle;
    @FXML
    private RadioButton selectInHouse;
    @FXML
    private RadioButton selectOutsourced;
    @FXML
    private ToggleGroup partTypeGroup;
    @FXML
    private TextField inputCompany;
    @FXML
    private Label labelCompany;
    @FXML
    private TextField inputMachineId;
    @FXML
    private Label labelMachineId;
    @FXML 
    private TextArea fieldError;
    
    //Tracks Part Scene state
    private Integer modifyingIndex;
    
    //Current state of form
    private IPartState state;
    
    //States of Part form
    InHouseState inHousePart;
    OutsourcedState outsourcedPart;

 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Initialize States
        inHousePart = new InHouseState(inputMachineId,labelMachineId);
        outsourcedPart = new OutsourcedState(inputCompany,labelCompany);
        
        
        //Setup ToggleGroup for the radio buttons that changes the state of the scene.
        partTypeGroup = new ToggleGroup();
        selectInHouse.setToggleGroup(partTypeGroup);
        selectOutsourced.setToggleGroup(partTypeGroup);
        selectInHouse.setUserData("inhouse");
        selectOutsourced.setUserData("outsourced");
    
        //Setup listeners
        setUpListeners();
      
    }    

    @FXML
    private void onSavePart(ActionEvent event) {
        //Validate fields
         if(validateInput()){
             int id;
             double cost;
             
             if(inputId.getText().isEmpty()){
                 //Create new ID
                 id = Inventory.GenerateId();
             }else{
                 id = Integer.parseInt(inputId.getText());
             }
             
             String name = inputPartName.getText();
             try{
                cost = NumberFormat.getCurrencyInstance().parse(inputCost.getText()).doubleValue();
            }catch(Exception e){
                    System.out.println(e.getMessage());
                    return;
                 }
             int stock = Integer.parseInt(inputStock.getText());
             int max = Integer.parseInt(inputMax.getText());
             int min = Integer.parseInt(inputMin.getText());
             
             Part part = state.savePart(id, name, cost, stock, max, min);
             
             if(modifyingIndex == null){
                 //Add new part
                 Inventory.addPart(part);
             }else{
                 //Modify current part
                 Inventory.updatePart(modifyingIndex, part);
             }
             
             exitScene();
         }
    }

    @FXML
    private void onCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancel changes and return to main screen?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            SceneManager.getInstance().unloadScene();
        }
        
    }
    
    private void setUpListeners(){
            
        //Setup listeners for part type toggle
        partTypeGroup.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
                    if (partTypeGroup.getSelectedToggle() != null) {
                        switch(partTypeGroup.getSelectedToggle().getUserData().toString()){
                            case "inhouse" : changeState(inHousePart); break;
                            case "outsourced" : changeState(outsourcedPart); break;
                            default : break;
                        }
                    }
                }
        );

        //Listener for Cost field
        inputCost.textProperty().addListener((observable, oldInput, newInput)->{
            //Add currency symbol if needed
            if(!newInput.contains("$")){
                inputCost.setText("$" + newInput);
            }    
    });
            
    }
    //Handles the event of changing form state
    private void changeState(IPartState newState){
        //Exit Current State if it exists
        if(state != null){
            state.exitState();
        }
        //Load State
        newState.loadState();
        //Set Current State
        state = newState;
    }
    
    private void exitScene(){
        SceneManager.getInstance().unloadScene();
    }
 
    //Validates Form Input
    private boolean  validateInput(){
        boolean validated = false;
        
        Validator nameValidator = new Validator(inputPartName);
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
        
        Validator costValidator = new Validator(inputCost);
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
        
        Validator machineIdValidator = new Validator(inputMachineId);
        machineIdValidator.addValidation(Validator.isEmpty);
        machineIdValidator.addValidation(Validator.isNum);
        machineIdValidator.addValidation(Validator.isPosNum);
        
        Validator companyValidator = new Validator(inputCompany);
        companyValidator.addValidation(Validator.isEmpty); 
         try{
            //Remove error styling for fields before validating
            fieldError.setText("");
            inputPartName.getStyleClass().removeAll("error");
            inputCost.getStyleClass().removeAll("error");
            inputMax.getStyleClass().removeAll("error");
            inputMin.getStyleClass().removeAll("error");
            inputStock.getStyleClass().removeAll("error");
            inputCompany.getStyleClass().removeAll("error");
            inputMachineId.getStyleClass().removeAll("error");
            
            //Run validations on each field
            nameValidator.validate();
            costValidator.validate();
            maxValidator.validate();
            minValidator.validate();
            //Run State Specific Validations
            state.validateFields();
            //Must validate inventory last since it depends on previous fields
            invValidator.validate();
            validated = true;
            }catch(ValidationException e){
                e.getField().getStyleClass().add("error");
                fieldError.setText(e.getMessage());
                return validated;
            }
            return validated;
    }
    
    //Loads Part Scene to Add new part
    public void loadScene(){
        sceneTitle.setText("Add Part");
        partTypeGroup.selectToggle(selectInHouse);
        changeState(inHousePart);
    }
     
    //Loads Part Scene with InHouse part
    public void loadScene(InHouse part,int index){
        sceneTitle.setText("Modify Part");
        partTypeGroup.selectToggle(selectInHouse);
        changeState(inHousePart);
        loadPartData(part,index);
    }
    
    //Loads Part Scene with Outsourced part
    public void loadScene(Outsourced part, int index){
        sceneTitle.setText("Modify Part");
        partTypeGroup.selectToggle(selectOutsourced);
        changeState(outsourcedPart);
        loadPartData(part,index);
    }
    
    private void loadPartData(Part part,int index){

        //Index of the part being modified
        modifyingIndex = index;
       
        inputId.setText(String.valueOf(part.getId()));
        inputPartName.setText(part.getName());
        inputStock.setText(String.valueOf(part.getStock()));
        inputCost.setText(NumberFormat.getCurrencyInstance().format(part.getPrice()));      
        inputMax.setText(String.valueOf(part.getMax()));
        inputMin.setText(String.valueOf(part.getMin()));
 
        //Set state specific fields
        state.setFields(part);
    }
}
