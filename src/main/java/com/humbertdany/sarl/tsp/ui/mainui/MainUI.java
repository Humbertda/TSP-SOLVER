package com.humbertdany.sarl.tsp.ui.mainui;

import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.filereader.TspProblemReader;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspCommonLibrary;
import com.humbertdany.sarl.tsp.ui.icon.AppIconLib;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MainUI extends Application {

	public static final String TEST_LAUNCH_ARGS = "lkhbzekij342$5TRGFDSsqdb-";

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
		    primaryStage.getIcons().add(AppIconLib.get250Image());
		    primaryStage.setMinHeight(500);
		    primaryStage.setMinWidth(650);
		    primaryStage.setWidth(900);
		    primaryStage.setHeight(650);
		    this.onClosingEvent(controller);

		    // TEST & DEV MODE ACTIVATION
		    //  The code here is not so pretty, should be redesigned later

		    for(String arg : getParameters().getRaw()){
			    if(arg.equals(TEST_LAUNCH_ARGS)){
			    	log("Entering MainUI Test|Dev mode");
				    controller.enterSolverMode(controller.getTspSolverLibrary().solverAcoSarl);
				    controller.openWebviewDebugger();
				    controller.onWebviewReady(() -> {
					    try {
						    final TspProblemReader fr = new TspProblemReader();
						    controller.newGraphSelected(fr.readFromString(TspCommonLibrary.BERLIN_52));
					    } catch (ParsingException e) {
						    logError(e.getMessage());
					    }
				    });

			    }
		    }

		    // Finally we can show the Pane
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
