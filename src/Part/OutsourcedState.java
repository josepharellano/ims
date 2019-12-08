/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Part;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Outsourced;
import model.Part;
import validator.ValidationException;
import validator.Validator;

/**
 *
 * @author Joseph
 */
public class OutsourcedState implements IPartState {
    
    private final TextField companyName;
    private final Label companyLabel;
    
    public OutsourcedState(TextField companyName, Label companyLabel){
        this.companyName = companyName;
        this.companyLabel = companyLabel;
        
        //Initialize as hidden
        companyName.setVisible(false);
        companyLabel.setVisible(false);
    }
    
    @Override
    public void validateFields() throws ValidationException{
        Validator companyValidator = new Validator(companyName);
        companyValidator.addValidation(Validator.isEmpty);  
        try{
            companyValidator.validate();
        }catch(ValidationException e){
            throw e;
        }
    }  
    @Override
    public Outsourced savePart(int id,String name,double cost,int stock,int max, int min){
        Outsourced newPart = new Outsourced(id,name,cost,stock,min,max,companyName.getText());
        return newPart;
    }
    
    @Override
    public void loadState(){
        //Show Fields
        companyName.setVisible(true);
        companyLabel.setVisible(true);
    }
    
    @Override 
    public void setFields(Part part){
        companyName.setText(((Outsourced)part).getCompanyName());
    }
    
    @Override
    public void exitState(){
        //Hide Fields
        companyName.setVisible(false);
        companyLabel.setVisible(false);
    }
}
    

