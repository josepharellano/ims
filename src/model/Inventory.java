/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Joseph
 */
public class Inventory {
    private final static ObservableList<Part> allParts;
    private final static ObservableList<Product> allProducts;

    static {
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }
  
    public static void addPart(Part newPart){
        //Generate Id for part
        newPart.setId(allParts.size()+1);
        //Add part to allParts
        allParts.add(newPart);
    }
    
    public static void addProduct(Product newProduct){
        //Add product to allProducts
    }
    
//    public static Part lookupPart(int partId){
//        return new Part();
//    }
    
    public static Product lookupProduct(int productId){
        return new Product();
    }
    
    //Ask instructor: If the UML is wrong for the return types on the following
//    public static Part lookupPart(String productName){
//        return new Object();
//    }
    public static Product lookupProduct(String productName){
        return new Product();
    }
    
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);     
    }
    public static void updateProduct(int index,Product selectedProduct){
        
    }
    public static void deletePart(Part selectedPart){
        allParts.remove(selectedPart);
        
    }
    public static void deleteProduct(Product selectedProduct){
        
    }
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
    
    public static int GenerateId(){
        int id;
        if(allParts.size() > 0){
            id = allParts.get(allParts.size()-1).getId()+1;
        }else{
            id = 1;
        }
        return id;
    }
}
