/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;

/**
 *
 * @author Joseph
 */
public class Validator {
    
    TextField field;
    ArrayList<IValidator> validations;
    
    public static IValidator isEmpty;
    public static IValidator isCurrency;
    public static IValidator isNum;
    public static IValidator isPosNum;
    
    static {
        isEmpty = (field)->{
            boolean result = field.getText().length() > 0;
            if(!result){
                throw new ValidationException("Empty",field);
            }
        };
        isNum = (field)->{
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            boolean result = pattern.matcher(field.getText()).matches();
            if(!result){
                throw new ValidationException("Not a Number",field);
            }
        };
        isPosNum = (field)->{
            Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
            boolean result = pattern.matcher(field.getText()).matches();
            if(!result){
                throw new ValidationException("Not Positive",field);
            }
        };
        isCurrency = (field)->{
           Pattern pattern = Pattern.compile("(?:[$])\\s*\\d+(?:\\.\\d{2})?");
           boolean result = pattern.matcher(field.getText()).matches();
           if(!result){
                throw new ValidationException("Invalid Currency Format",field);
            }
        };
    }
      
    public Validator(TextField field){
        validations = new ArrayList<>();
        this.field = field;
     
    }
    
    public void validate() throws ValidationException {  
        for(IValidator validator : validations){
            try{
             validator.Validate(field);
            }catch(ValidationException e){
                throw e;
            }
        }
    }

    public void addValidation(IValidator validation){
        validations.add(validation);
    }
    
    public void addValidation(Validators validate){
        switch(validate){
            case ISEMPTY : validations.add(isEmpty); break;
            case ISNUM : validations.add(isNum); break;
            case ISCURRENCY : validations.add(isCurrency); break;
            case ISPOSNUM : validations.add(isPosNum); break;
            default : ;     
        }
    }
    
    public void removeValidation(IValidator validation){
        validations.remove(validation);
    }
 
    //Allows for the creation of custom validations
    public static boolean validate(IValidator operation) {
     return true;
    }
    
    public enum Validators{
        ISEMPTY,
        ISCURRENCY,
        ISNUM,
        ISPOSNUM,
    }
}
