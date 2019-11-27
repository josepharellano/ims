/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import javafx.scene.control.TextField;

/**
 *
 * @author Joseph
 */
public class ValidationException extends Exception{
        private final TextField field;
        
        public ValidationException(String msg){
            super(msg);
            this.field = null;
        }
        
        public ValidationException(String msg,TextField field){
            super(msg);
            this.field = field;
        }
        
        public TextField getField(){
            return field;
        }
    }
