package com.humbertdany.sarl.tsp.mainui;

import java.util.ArrayList;
import java.util.UUID;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainUI extends Application {
	
	final private UUID uuid = UUID.randomUUID();
	
	private ArrayList<GuiListener> guiListeners = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	try {

		    // Load the stuff from JavaFX and the controller
		    final Parent root = FXMLLoader.load(getClass().getResource("/mainUi/sample.fxml"));

		    // Set the main frame info and display it
	        primaryStage.setTitle("TSP Ant Colony Solver");
	        primaryStage.setScene(new Scene(root, 500, 500));
		    primaryStage.setMinHeight(500);
		    primaryStage.setMinWidth(500);
	        primaryStage.show();
	        
	    	
		} catch (Exception e) {
			// Well, the app can't launch, what's going on?
			e.printStackTrace();
			System.exit(-1);
		}

 
    }
    
    @Override
    public void stop(){
        for(GuiListener l : guiListeners){
        	l.closing(this);
        }
    }
    
    public void onClosingEvent(final GuiListener l){
    	guiListeners.add(l);
    }
    
    public static void main(String[] args) {
		launch(args);
	}

}
