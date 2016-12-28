package com.humbertdany.sarl.tsp.ui.mainui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MainUI extends Application {

	private Logger logger;
	
	private ArrayList<GuiListener> guiListeners = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	try {

		    // Load the stuff from JavaFX and the controller
		    final FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainUi/sample.fxml"));
		    final Parent root = loader.load();
		    Controller controller = loader.getController();
		    controller.setStage(primaryStage);

		    // Set the main frame info and display it
	        primaryStage.setTitle("TSP Ant Colony Solver");
	        primaryStage.setScene(new Scene(root, 500, 500));
		    primaryStage.setMinHeight(500);
		    primaryStage.setMinWidth(650);
	        primaryStage.show();


		} catch (Exception e) {
			// Well, the app can't launch, what's going on?
			logError(e.getMessage());
			System.exit(-1);
		}
 
    }

	private void initLogger(){
		logger = LogManager.getLogger(this.getClass());
	}

	protected final void log(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.info("\n" + o.toString());
	}

	protected final void logError(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.error("\n" + o.toString());
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
