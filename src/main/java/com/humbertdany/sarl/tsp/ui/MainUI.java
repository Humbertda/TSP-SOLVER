package com.humbertdany.sarl.tsp.ui;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.solver.aco.AntColonyTspSolver;
import com.humbertdany.sarl.tsp.solver.aco.sarl.EnvironmentListener;
import com.humbertdany.sarl.tsp.solver.aco.sarl.GuiListener;
import com.humbertdany.sarl.tsp.solver.aco.sarl.Launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import io.janusproject.Boot;
import io.janusproject.util.LoggerCreator;

public class MainUI extends Application {
	
	final private UUID uuid = UUID.randomUUID();
	private Controller ctrl;
	
	private ArrayList<GuiListener> guiListeners = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	try {
	    	
	    	// build the TspSolver
		    // => We could imagine other solver type, not even related to SARL
		    final ATspSolver tspSolver = new AntColonyTspSolver();

		    // Load the stuff from JavaFX and the controller
		    final FXMLLoader fxmlLoader = new FXMLLoader();
		    final Parent root = fxmlLoader.load(getClass().getResource("sample.fxml").openStream());
		    ctrl = fxmlLoader.getController();

		    // Bind the Solver
		    ctrl.bindSolver(tspSolver);

		    // Set the main frame info and display it
	        primaryStage.setTitle("TSP Ant Colony Solver");
	        primaryStage.setScene(new Scene(root, 500, 500));
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
    
    public Controller getController(){
    	return ctrl;
    }
    
    public void onClosingEvent(final GuiListener l){
    	guiListeners.add(l);
    }
    
    public static void main(String[] args) {
		launch(args);
	}

}
