/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ims;

import controller.PartSceneController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Joseph
 */
public class SceneManager {
    //Create singleton for scenemanager
    private static SceneManager instance;
    private final Stage stage;
    
    //Dictionary of loaded Scene loaders
    private final HashMap<Views, FXMLLoader> loaders; 
    private final Stack<Scene> scenes;
    
    private SceneManager(Stage stage) throws IOException {
        loaders = new HashMap<>();
        scenes = new Stack();
        this.stage = stage;
        
        //Load in Initial Scene
        loadScene(Views.MAIN);
        //Display most recent loaded scene;
        displayScene();
       
    }
    
    //Only one SceneManager can be instantiated
    public static SceneManager getInstance(Stage stage) throws IOException {
        if(instance == null){
            instance = new SceneManager(stage);
        }
        return instance;
    }
    
    public static SceneManager getInstance(){
        if(instance == null){
            throw new IllegalArgumentException();
        }
        else{
            return instance;
        }
    }
    
    /**
     *
     * @param <T>
     * @param view
     * @return 
     * @throws IOException
     */
    public final <T> T loadScene(Views view) throws IOException{
        //Load in new scene and add it to the stack
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getView()));
        Parent root = loader.load();

        Scene scene = new Scene(root); 
        scene.setUserData(view);
        
        loaders.put(view, loader);
        scenes.push(scene);
        
        return loader.getController();
    }
    
    public void unloadScene(){
        
        //Remove scene from stack
        Scene scene = scenes.pop();
        loaders.remove((Views)scene.getUserData());
        
        //Load in previous scene
        if(scenes.size() > 0){
           stage.setScene(scenes.peek());
        }
        else{
            //Exit program
            System.exit(0);
        }
        
    }
    
    /**
     *
     */
    public final void displayScene(){
        stage.setScene(scenes.peek());
        stage.show();
    }
       
    //Returns a loaded scene controller
    public <T> T getSceneController(Views view){
        return loaders.get(view).getController();
    }
      
    public enum Views {
        MAIN("/view/MainScene.fxml"),
        PART("/view/PartScene.fxml"),
        PRODUCT("/view/ProductScene.fxml");
        private final String view;
        public String getView(){
            return this.view;
        }
        Views(String view){
            this.view = view;
        }
    }
}
