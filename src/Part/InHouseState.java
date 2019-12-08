/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Part;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.InHouse;
import model.Part;
import validator.ValidationException;
import validator.Validator;

/**
 *
 * @author Joseph
 */
public class InHouseState implements IPartState {
    private final TextField machineId;
    private final Label machineLabel;
    
    public InHouseState(TextField machineId,Label machineLabel){
        this.machineId = machineId;
        this.machineLabel = machineLabel;
        
        //Initialize as not visible
        machineId.setVisible(false);
        machineLabel.setVisible(false);
        
    }
    
    @Override 
    public void loadState(){
        //Show Fields
        machineId.setVisible(true);
        machineLabel.setVisible(true);
    }
    
    @Override 
    public void setFields(Part part){
        machineId.setText((String.valueOf(((InHouse)part).getMachineId())));
    }
   
    @Override 
    public void exitState(){
        //Hide Fields
        machineId.setVisible(false);
        machineLabel.setVisible(false);
    }
    
    @Override
    public void validateFields() throws ValidationException{
        
        Validator machineValidator = new Validator(machineId);
        machineValidator.addValidation(Validator.isEmpty);
        machineValidator.addValidation(Validator.isNum);
        machineValidator.addValidation(Validator.isPosNum);
        
        try{
            machineValidator.validate();
        }catch(ValidationException e){
            throw e;
        }
    }
    
    @Override
    public InHouse savePart(int id,String name,double cost,int stock,int max, int min){
        InHouse newPart = new InHouse(id,name,cost,stock,min,max,Integer.parseInt(machineId.getText()));
        return newPart;
    }
    
}
