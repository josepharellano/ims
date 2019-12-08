/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Joseph
 */
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    
    public Product(){
        associatedParts = FXCollections.observableArrayList();
    }
    
    public Product(int id, String name, double price,int stock,int min,int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;  
    }
    
    //Constructor that includes associated parts
    public Product(int id, String name, double price,int stock,int min,int max,ObservableList<Part> associatedParts){
        this(id,name,price,stock,min,max);
        this.associatedParts = associatedParts;
        
    }
    
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    public void setMin(int min){
        this.min = min;
    }
    public void setMax(int max){
        this.max = max;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
    public int getStock(){
        return stock;
    }
    public int getMin(){
        return min;
    }
    public int getMax(){
        return max;
    }
    
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
        
    }
    public void deleteAssociatedPart(Part associatedPart){
        associatedParts.remove(associatedPart);
    }
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
    
    
    
    
}
