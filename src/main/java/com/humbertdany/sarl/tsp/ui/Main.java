package com.humbertdany.sarl.tsp.ui;

import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.solver.aco.AntColonyTspSolver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

    	// build the TspSolver
	    // => We could imagine other solver type, not even related to SARL
	    final ATspSolver tspSolver = new AntColonyTspSolver();

	    // Load the stuff from JavaFX and the controller
	    final FXMLLoader fxmlLoader = new FXMLLoader();
	    final Parent root = fxmlLoader.load(getClass().getResource("sample.fxml").openStream());
	    final Controller ctrl = fxmlLoader.getController();

	    // Bind the Solver
	    ctrl.bindSolver(tspSolver);

	    // Set the main frame info and display it
        primaryStage.setTitle("TSP Ant Colony Solver");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
