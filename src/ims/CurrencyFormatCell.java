/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ims;

import java.text.NumberFormat;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;

/**
 *
 * @author Joseph
 */
 //Helper class to display a cell as currency.
    public class CurrencyFormatCell extends TableCell<TableView,Double>{
         @Override
         protected void updateItem(Double price,boolean empty){
            super.updateItem(price, empty);
            if(price == null || empty){
                setText("");
            }else{
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                setText(currencyFormatter.format(price));
                 }
            }
    }
