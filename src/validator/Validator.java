/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/**
 *
 * @author Joseph
 */
public class Validator<T> {
    
    TextField field;
    T value;
    ArrayList<IValidator> validations;
    
    static IValidator notEmpty;
    static IValidator currency;
    
    static {
        notEmpty = (field)->{
            String error = null;
            boolean result = field.getText().length() > 0;
            if(!result){
                error = "Field is Empty";
            }
            return new ValidationResult<>(error,result,null);
        };    
    }
    
    
    
    public Validator(TextField field){
        validations = new ArrayList();
        this.field = field;
     
    }
    
    public void validate() throws ValidationException {
        ValidationResult result;
        for(IValidator validator : validations){
            result = validator.Validate(field);
            if(!result.isValid()){
                throw new ValidationException(result.getMsg(),field);
            }
        }
    }
    
    public void addValidation(IValidator validation){
        validations.add(validation);
    }
    
    public void addValidation(Validators validate){
        switch(validate){
            case NOTEMPTY : validations.add(notEmpty) ;
        }
    }
    
    public void removeValidation(IValidator validation){
        validations.remove(validation);
    }
   
    
    //Built In Validation
    public boolean isEmpty(String value){
        return value.length() == 0;
    }
    
    public double currencyValidation(TextField field) throws ValidationException {
        String value = field.getText().replaceAll(",","");
        Number currency;
        
        if(!isEmpty(value)){
            try{
                isEmpty(field.getText());   
                Pattern pattern = Pattern.compile("(?:[$])\\s*\\d+(?:\\.\\d{2})?");
                Matcher match = pattern.matcher(value);
                if(match.matches()){
                    currency = NumberFormat.getCurrencyInstance(Locale.US).parse(value);
                }else{
                    throw new Exception();
                }

                }catch(Exception e){
                    throw new ValidationException("Currency Format Invalid\nFormat $00.00",field);
                }
        }else{
            throw new ValidationException("Field Empty",field);
        }
        return currency.doubleValue();
    }
    
    public boolean isPositiveNum(Number value){
        return value.intValue() >= 0;
    }
    
    public Number validateAndParseNum(TextField field) throws ValidationException{
        
        Number value;
        try{
            value = NumberFormat.getInstance().parse(field.getText());
        }catch(Exception e){
            throw new ValidationException("Non Numeric Input",field);
        }
        
        if(!isPositiveNum(value)){
            throw new ValidationException("Value must be positive",field);
        }
        return value;
    }
    
    
    /**
     *
     * @param <T>
     * @param operation
     * @param field
     * @return
     * @throws validator.ValidationException
     */
//    public static <T> T validate(IValidator operation,T field) {
//        
//     return field;
//    }
    
    public enum Validators{
        NOTEMPTY,
        CURRENCY,
        POSITIVE,
    }
}
