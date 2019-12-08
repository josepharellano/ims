/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Part;

import model.Part;
import validator.ValidationException;

/**
 * 
 * @author Joseph
 */
public interface IPartState {
    
    //Validates State Specific Fields
    public void validateFields() throws ValidationException;
    //Hides the form elements for this state
    public void exitState();
    //Shows the form elements for this state
    public void loadState();
    //Set the specific fields for this state
    public void setFields(Part part);
    //Saves the part to inventory 
    public Part savePart(int id, String name, double cost, int stock,int max, int min); 
}
