package com.humbertdany.sarl.tsp.mainui;

import com.humbertdany.sarl.tsp.core.graph.Graph;
import com.humbertdany.sarl.tsp.core.utils.Point;
import com.humbertdany.sarl.tsp.core.ui.JfxController;
import com.humbertdany.sarl.tsp.core.ui.MGridPane;
import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.filereader.TspProblemReader;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.solver.TspSolverLibrary;
import com.humbertdany.sarl.tsp.tspgraph.TspCommonLibrary;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.*;

public class Controller extends JfxController {

	private static final String HTML_VIEW_FILENAME = "/mainUi/webView.html";

	@FXML
	private Pane paramPane;
	@FXML
	private WebView webViewer;
	@FXML
	private ListView<ATspSolver> solverList;
	@FXML
	private Button startBtn;
	@FXML
	private Button backSolverBtn;

	private ATspSolver solver;

	private WebEngine webEngine;

	private JsApplication jsApp = new JsApplication();

	private final TspProblemReader reader = new TspProblemReader();


	@FXML
	private MGridPane selectionGPane;
	@FXML
	private MGridPane paramsGPane;

	@FXML
	public void initialize() {
		switchMode(false);

		final TspSolverLibrary tspSolverLibrary = TspSolverLibrary.init();

		webEngine = webViewer.getEngine();

		webEngine.load(this.getClass().getResource(Controller.HTML_VIEW_FILENAME).toExternalForm());
		webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if(newState == State.SUCCEEDED){

				JSObject window = (JSObject)webEngine.executeScript("window");
				window.setMember("javaApp", jsApp);
				webEngine.executeScript("init()");

				// TODO Make a real TSP selection
				try {
					final TspGraph tspGraph = reader.readFromString(TspCommonLibrary.BERLIN_52);
					jsApp.sendNewMap(tspGraph);
				} catch (ParsingException e) {
					e.printStackTrace();
				}

			}
		});

		// ListView generation
		final ObservableList<ATspSolver> list = FXCollections.observableArrayList();
		list.addAll(tspSolverLibrary.getSolvers());
		solverList.setItems(list);
		solverList.setOnMouseClicked(event -> {
			ATspSolver solver = solverList.getSelectionModel().getSelectedItem();
			if(solver != null){
				enterSolverMode(solver);
			}
		});

		this.bindButton(backSolverBtn, event -> {
			switchMode(false);
			solver = null;
		});

	}

	/**
	 * Method called when a Solver is selected by the user
	 * It will init the scene (make the buttons return appears and init the controler pane)
	 * @param solver The selected solver
	 */
	private void enterSolverMode(final ATspSolver solver){
		bindSolver(solver);
		switchMode(true);
	}

	private void switchMode(boolean isSolverMode) {
		solverList.setVisible(!isSolverMode);
		startBtn.setVisible(isSolverMode);
		backSolverBtn.setVisible(isSolverMode);
		paramsGPane.setVisible(isSolverMode);
		selectionGPane.setVisible(!isSolverMode);
	}

	public void bindSolver(final ATspSolver solver){
		paramPane.getChildren().clear();
		this.solver = solver;
		solver.buildGui(paramPane);
	}

	/**
	 * Class used to receive new information from the JS webview
	 * Basically, it will send the new layout for the TSP on each
	 * modification
	 */
	public class JsApplication {

		private ArrayList<String> callbacks = new ArrayList<>();

		/**
		 * Send back the new Map defined by the javascript
		 * After this, we should send the modifications
		 * to SARL so it can update it own values
		 * @param arg String
		 */
		public void sendNewTspMap(final String arg) {
			final String[] strings = arg.split(",");
			final List<Point> points = new ArrayList<>();
			for (int i = 0; i < strings.length; i = i+2) {
				final Point p = new Point(
						Double.parseDouble(strings[i]),
						Double.parseDouble(strings[i+1])
				);
				points.add(p);
			}
			// TODO Make a graph there and go on
		}

		/**
		 * Used to request notification when the map
		 * has been changed Java's side (when a new .tsp
		 * is loaded or when the solver is running)
		 * @param arg String
		 */
		public void onMapChange(final String arg){
			callbacks.add(arg);
		}

		// Bellow this line, all the functions are not called in JS

		/**
		 * Notify all the observers that a new map layout
		 * is live !
		 * @param arg String
		 */
		void sendNewMap(final D3GraphDisplayable arg){
			for(String s : callbacks){
				webEngine.executeScript(s + "('" + arg.getD3String() +"')");
			}
		}

	}

}