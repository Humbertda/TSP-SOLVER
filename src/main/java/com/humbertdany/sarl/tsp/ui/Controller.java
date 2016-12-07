package com.humbertdany.sarl.tsp.ui;

import com.humbertdany.sarl.tsp.solver.ATspSolver;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.ArrayList;

public class Controller {

	private static final String HTML_VIEW_FILENAME = "webView.html";

	@FXML
	private Pane paramPane;
	@FXML
	private WebView webViewer;

	private WebEngine webEngine;

	private ATspSolver solver;

	private JsApplication jsApp = new JsApplication();

	@FXML
	public void initialize() {
		webEngine = webViewer.getEngine();

		webEngine.load(Controller.class.getResource(Controller.HTML_VIEW_FILENAME).toExternalForm());
		webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if(newState == State.SUCCEEDED){
				JSObject window = (JSObject)webEngine.executeScript("window");
				window.setMember("javaApp", jsApp);
				webEngine.executeScript("init()");
			}
		});
	}

	public void bindSolver(final ATspSolver solver){
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
			System.out.println(arg);
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
		void sendNewMap(final String arg){
			// final AApplicationParameters params = solver.getParameters();
			for(String s : callbacks){
				webEngine.executeScript(s + "('" + arg +"')");
			}
		}

	}

}
