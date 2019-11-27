/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

/**
 *
 * @author Joseph
 * @param <T>
 */
    public class ValidationResult<T>{
        private String msg;
        private boolean isValid;
        private T value;
        
        public ValidationResult(String msg,boolean result,T value){
            this.msg = msg;
            isValid = result;
            this.value = value;
        }
        
        public String getMsg(){
            return msg;
        }
        
        public boolean isValid(){
            return isValid;
        }
        
        public T getValue(){
            return value;
        }
   
    }
