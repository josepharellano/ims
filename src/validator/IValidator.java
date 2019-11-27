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
 * @param <R>
 * @param <T>
 */
public interface IValidator{
    public ValidationResult Validate(TextField field);
}

