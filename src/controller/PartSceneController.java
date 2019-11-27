/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ims.SceneManager;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
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
    private TextField inputInvLevel;
    @FXML
    private TextField inputCost;
    @FXML
    private TextField inputMax;
    @FXML
    private TextField inputMin;
    @FXML
    private Text labelMax;
    @FXML
    private Text labelCost;
    @FXML
    private Text labelInv;
    @FXML
    private Text labelName;
    @FXML
    private TextField inputId;
    @FXML
    private Text labelId;
    @FXML
    private Text sceneTitle;
    @FXML
    private RadioButton selectInHouse;
    @FXML
    private RadioButton selectOutsourced;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private ToggleGroup partTypeGroup;
    @FXML
    private TextField inputCompany;
    @FXML
    private Text labelCompany;
    @FXML
    private TextField inputMachineId;
    @FXML
    private Text labelMachineId;
    @FXML
    private RowConstraints rowMachineId;
    @FXML 
    private TextArea fieldError;
    
    //Tracks Part Scene state
    private Integer modifyingIndex;
    //Tracks toggle state
    private Boolean isInHouse;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Setup ToggleGroup for the radio buttons that changes the state of the scene.
        partTypeGroup = new ToggleGroup();
        selectInHouse.setToggleGroup(partTypeGroup);
        selectOutsourced.setToggleGroup(partTypeGroup);
        selectInHouse.setUserData("inhouse");
        selectOutsourced.setUserData("outsourced");
    
        //Setup listeners
        setUpListeners();
        
        //Set initial state of the scene
        partTypeGroup.selectToggle(selectInHouse);
        handleToggle(partTypeGroup.getSelectedToggle());
    }    

    @FXML
    private void onSavePart(ActionEvent event) {
        
      
          if(validateandSaveInput()){
              exitScene();
          }
    }

    @FXML
    private void onCancel(ActionEvent event) {
        SceneManager.getInstance().unloadScene();
    }
    
    private void setUpListeners(){
        
        
        
        //Setup listeners for part type toggle
        partTypeGroup.selectedToggleProperty().addListener(
                (ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
                    if (partTypeGroup.getSelectedToggle() != null) {
                        handleToggle(partTypeGroup.getSelectedToggle());
                    }
                }
        );
        
  
   
        //Listener to validate Cost field
        inputCost.textProperty().addListener((observable, oldInput, newInput)->{
            Validator<Double> costValidator = new Validator(inputCost);
            //Add currency symbol if needed
            if(!newInput.contains("$")){
                inputCost.setText("$" + newInput);
            }    
            //Validate field
            
            

            
               
    });
           
        inputInvLevel.textProperty().addListener(new NumericValidator(inputInvLevel));  
        inputMin.textProperty().addListener(new NumericValidator(inputMin));
        inputMax.textProperty().addListener(new NumericValidator(inputMax));
        inputMachineId.textProperty().addListener(new NumericValidator(inputMachineId));
       
        
        
    }
    
    //Handles the event of changing part types
    private void handleToggle(Toggle selected){
        isInHouse = selected.getUserData().toString().equalsIgnoreCase("inhouse");
        togglePartType();
    }
    
   
    //Changes the scene based on the state.
    private void togglePartType(){
        labelMachineId.setVisible(isInHouse);
        inputMachineId.setVisible(isInHouse);
        labelCompany.setVisible(!isInHouse);
        inputCompany.setVisible(!isInHouse); 
    }
    
    private void exitScene(){
        SceneManager.getInstance().unloadScene();
    }
     
    public void loadAddPartScene(){
        sceneTitle.setText("Add Part");
        
    }
    
    private boolean validateandSaveInput(){
        Part part;
        boolean isValidated = false;
        Validator<String> nameValidator = new Validator(inputPartName);
        nameValidator.addValidation(Validator.Validators.NOTEMPTY);
        
         try{
            nameValidator.validate();
            }catch(ValidationException e){
                e.getField().getStyleClass().add("error");
                fieldError.setText(e.getMessage());
            }
            inputPartName.getStyleClass().removeAll("error");
            return isValidated;
        //Parse all fields to correct type
        
//        Validator<Number,TextField> numericValidator = (field)->{
//            Number value = 0;
//            try{
//                value = Integer.parseInt(field.getText());
//            }catch(Exception e){
//                field.getStyleClass().add("error");
//                throw new Exception("Value must be present and numeric");
//         
//            }
//            if(value.intValue() < 0){
//                field.getStyleClass().add("error");
//                throw new Exception("Value must be positive");
//      
//            }       
//            field.getStyleClass().removeAll("error");   
//            return value;  
//        };
        
//        Validator<Double,TextField> currencyValidator = (field)->{
//            Number currency;
//            try{
//                currency = NumberFormat.getCurrencyInstance(Locale.US).parse(field.getText());
//            }catch(Exception e){
//                field.getStyleClass().add("error");
//                throw e;
//            }
//            field.getStyleClass().removeAll("error"); 
//            return currency.doubleValue();
//        };
 
//        int stock = numericValidator.Validate(inputInvLevel).intValue();
//        double cost =  currencyValidator.Validate(inputCost);
//        int max = numericValidator.Validate(inputMax).intValue();
//        int min = numericValidator.Validate(inputMin).intValue();
//        Integer machineId = isInHouse ? numericValidator.Validate(inputMachineId).intValue(): null;
//        
        
//        int id;
//        if(inputId.getText().isEmpty()){
//            id = Inventory.GenerateId();
//        }else{
//            id = Integer.parseInt(inputId.getText());
//        }
//        
//        //Validate String Fields
//        if(inputPartName.getLength() < 1){
//            
//    }
        
//        //Create new part
//        if(isInHouse){
//            part = new InHouse(id,inputPartName.getText(),cost,stock,max,min,machineId);
//        }else{
//            part = new Outsourced(id,inputPartName.getText(),cost,stock,max,min,inputCompany.getText());
//        }
        
//        //Add or Update part
//        if(modifyingIndex == null){
//            //Add
//            Inventory.addPart(part);
//        }else{
//            //Update
//            Inventory.updatePart(modifyingIndex, part);
//        }
              
    }
    
    public void loadModifyPartScene(Part part,int index){
        
        //Index of the part being modified
        modifyingIndex = index;
       
        sceneTitle.setText("Modify Part");
        inputId.setText(String.valueOf(part.getId()));
        inputPartName.setText(part.getName());
        inputInvLevel.setText(String.valueOf(part.getStock()));
        inputCost.setText(NumberFormat.getCurrencyInstance().format(part.getPrice()));      
        inputMax.setText(String.valueOf(part.getMax()));
        inputMin.setText(String.valueOf(part.getMin()));
       
        //Set part specific info
       if(part instanceof InHouse)
       {
           inputMachineId.setText(String.valueOf(((InHouse)part).getMachineId()));
       }
       
       if(part instanceof Outsourced){
           inputCompany.setText(((Outsourced)part).getCompanyName());
       }
    }
    
    //Change Listener to validate numeric data
    private class NumericValidator implements ChangeListener<String>{
        private final TextField field;
        public NumericValidator(TextField textField){
            field = textField;
        }
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
            try{
//              validateNumericInput(newValue);
            }catch(Exception e){
                field.getStyleClass().add("error");
                fieldError.setText("Invalid Fields!\n" + e.getMessage());
                return;
            }
            fieldError.setText("");
            field.getStyleClass().removeAll("error");  
        }
    }
}
