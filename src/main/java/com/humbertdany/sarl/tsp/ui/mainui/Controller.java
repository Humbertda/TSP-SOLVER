package com.humbertdany.sarl.tsp.ui.mainui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humbertdany.sarl.tsp.core.graph.Vertex;
import com.humbertdany.sarl.tsp.core.ui.JfxController;
import com.humbertdany.sarl.tsp.core.ui.MAnchorPane;
import com.humbertdany.sarl.tsp.core.ui.MGridPane;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.solver.SolverObserver;
import com.humbertdany.sarl.tsp.solver.TspSolverLibrary;
import com.humbertdany.sarl.tsp.solver.tester.AntColonySolverTester;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;
import com.humbertdany.sarl.tsp.ui.aboutpopup.AboutController;
import com.humbertdany.sarl.tsp.ui.tsppopup.PopupObserver;
import com.humbertdany.sarl.tsp.ui.tsppopup.TspPopupController;
import com.humbertdany.utils.jspipe.AJsApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller extends JfxController implements PopupObserver, SolverObserver, GuiListener {

	private static final String HTML_VIEW_FILENAME = "/mainUi/webView.html";

	@FXML
	private MAnchorPane paramPane;
	@FXML
	private WebView webViewer;
	@FXML
	private ListView<ATspSolver> solverList;
	@FXML
	private Button backSolverBtn;
	@FXML
	public Button startStopBtn;
	@FXML
	private Label statusLabel;

	private ATspSolver solver;

	private TspGraph tspGraph;

	private WebEngine webEngine;

	private JsApplication jsApp;
	
	private double bestCostSoFar = 0.0;
	
	private List<TspVertex> bestRunSoFar = new ArrayList<>(); 

	private final ObjectMapper mapper = new ObjectMapper();

	private final TspSolverLibrary tspSolverLibrary = TspSolverLibrary.init();

	private final List<WebviewReadyObserver> webviewReadyObservers = new ArrayList<>();

	@FXML
	private MGridPane selectionGPane;
	@FXML
	private MGridPane paramsGPane;

	@FXML
	public void initialize() {
		switchMode(false);

		startStopBtn.setText(BTN_START);
		webEngine = webViewer.getEngine();
		jsApp = new JsApplication(webEngine);

		webViewer.setCache(false);
		webEngine.load(this.getClass().getResource(Controller.HTML_VIEW_FILENAME).toExternalForm());
		webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			if(newState == State.SUCCEEDED){
				JSObject window = (JSObject)webEngine.executeScript("window");
				window.setMember("javaApp", jsApp);
				webEngine.executeScript("init(false)");
				tspGraph = new TspGraph();
				jsApp.sendNewMap(tspGraph);
				if(webviewReadyObservers.size() != 0){
					webviewReadyObservers.forEach(WebviewReadyObserver::webviewIsReady);
				}
			}
		});
		
		Timeline secondUpdater = new Timeline();
		secondUpdater.getKeyFrames().add(
				new KeyFrame(Duration.millis(100),  event -> {
					statusLabel.setText(getStatusText());
				})
		); 
		secondUpdater.setCycleCount(Timeline.INDEFINITE);
		secondUpdater.play();

		// ListView generation
		final ObservableList<ATspSolver> list = FXCollections.observableArrayList();
		list.addAll(tspSolverLibrary.getSolvers());
		solverList.setItems(list);
		solverList.setOnMouseClicked(event -> {
			ATspSolver selectedSvl = solverList.getSelectionModel().getSelectedItem();
			if(selectedSvl != null){
				enterSolverMode(selectedSvl);
			}
		});

		this.bindButton(backSolverBtn, event -> {
			switchMode(false);
			solver.offSolverDone(this);
			solver = null;
			jsApp.sendNewMap(tspGraph);
		});

	}

	/**
	 * Method called when a Solver is selected by the user
	 * It will init the scene (make the buttons return appears and init the controler pane)
	 * @param solver The selected solver
	 */
	void enterSolverMode(final ATspSolver solver){
		bindSolver(solver);
		switchMode(true);
	}

	private void switchMode(boolean isSolverMode) {
		solverList.setVisible(!isSolverMode);
		backSolverBtn.setVisible(isSolverMode);
		paramsGPane.setVisible(isSolverMode);
		selectionGPane.setVisible(!isSolverMode);
	}

	private void bindSolver(final ATspSolver solver){
		paramPane.getChildren().clear();
		this.solver = solver;
		jsApp.sendNewMap(this.tspGraph);
		solver.onSolverDone(this);
		solver.buildGui(paramPane);
	}

	/**
	 * Class used to receive new information from the JS webview
	 * Basically, it will send the new layout for the TSP on each
	 * modification
	 */
	public class JsApplication extends AJsApplication {

		private ArrayList<String> callbacksNewMap = new ArrayList<>();
		private ArrayList<String> callbacksUpdateMap = new ArrayList<>();

		public JsApplication(WebEngine we) {
			super(we);
		}

		/**
		 * Send back the new Map defined by the javascript
		 * After this, we should send the modifications
		 * to SARL so it can update it own values
		 * @param arg String
		 */
		public void sendNewTspMap(final String action, final String arg) {
			try {
				switch (action.toLowerCase()){
					case "create city":
						CityEntry cityEntry = mapper.readValue(arg, CityEntry.class);
						tspGraph.addVertex(new TspVertex("City in " + cityEntry.toString(), cityEntry.makeVertexInfo()));
						break;
					case "city updated":
						CityEntry cEntry = mapper.readValue(arg, CityEntry.class);
						final TspVertex vertexByData = (TspVertex) tspGraph.findVertexByName(cEntry.getName());
						vertexByData.getData().setXY(cEntry.getX(), cEntry.getY());
						break;
					case "link removed":
						PathEntry pathEntry = mapper.readValue(arg, PathEntry.class);
						final Vertex<VertexInfo> from = tspGraph.findVertexByName(pathEntry.getFrom().getName());
						final Vertex<VertexInfo> to = tspGraph.findVertexByName(pathEntry.getTo().getName());
						tspGraph.removeEdge(from, to);
						tspGraph.removeEdge(to, from);
						break;
					case "link city":
						CityEntry[] entries = mapper.readValue(arg, CityEntry[].class);
						if(entries[0] != null && entries[1] != null){
							tspGraph.addEdge(
									tspGraph.findVertexByName(entries[0].getName()),
									tspGraph.findVertexByName(entries[1].getName()),
									solver.makeEdgeData()
							);
						} else {
							logError("The two cities can't be found");
						}
						break;
					default:
						logError("The action '" + action + "' could not be recognized by Java App");
				}
				jsApp.sendNewMap(tspGraph);
			} catch (IOException e) {
				logError(e.getMessage());
			}
		}

		/**
		 * Used to request notification when the map
		 * has been changed Java's side (when a new .tsp
		 * is loaded or when the solver is running)
		 * @param arg String
		 */
		public void onMapChange(final String arg){
			callbacksUpdateMap.add(arg);
		}

		public void onNewMap(final String arg){
			callbacksNewMap.add(arg);
		}

		public void logMessage(final String arg){
			log("JS: " + arg);
		}
		
		// Bellow this line, all the functions are not called in JS

		/**
		 * Notify all the observers that a new map layout
		 * is live !
		 * @param arg String
		 */
		void sendNewMap(final D3GraphDisplayable arg){
			final ATspSolver tempSlv = solver == null ? new AntColonySolverTester() : solver;
			final String toSend = arg.getD3String(tempSlv);
			for(String s : callbacksNewMap){
				try {
					webEngine.executeScript(s + "('" + toSend +"')");
				} catch (JSException e){
					logError(e.getMessage() + "Map sent: " + toSend);
				}

			}
		}

		public void newBestPath(final List<TspVertex> vertices){
			if(vertices != null){
				try {
					int counter = 0;
					StringBuilder sb = new StringBuilder();
					sb.append("[");
					for(TspVertex v : vertices){
						sb.append(mapper.writeValueAsString(v));
						if(counter != vertices.size()-1){
							sb.append(", ");
						}
						counter++;
					}
					sb.append("]");
					webEngine.executeScript("newBestPath('" + sb.toString() +"')");
				} catch (JsonProcessingException e) {
					logError(e.getMessage());
				}
			} else {
				webEngine.executeScript("newBestPath('[]')");
			}
		}

		void updateMap(final D3GraphDisplayable arg){
			final String toSend = arg.getD3String(solver);
			for(String s : callbacksUpdateMap){
				try {
					webEngine.executeScript(s + "('" + toSend +"')");
				} catch (JSException e){
					logError(e.getMessage() + "Map sent: " + toSend);
				}

			}
		}

	}

	public TspSolverLibrary getTspSolverLibrary() {
		return tspSolverLibrary;
	}

	private void initPopupGraphChooser() throws IOException {
		TspPopupController controller = (TspPopupController) this.openModal("/tspChooserUi/popup.fxml", "Graph Selector");
		controller.addPopupObserver(this);
		controller.getStage().showAndWait();
	}

	public void openTspProblemChooser(){
		try {
			initPopupGraphChooser();
		} catch (IOException e) {
			logError(e.getMessage());
		}
	}

	public void openAbout(){
		try {
			AboutController controller = (AboutController) this.openModal("/aboutpopup/popup.fxml", "About", 300, 200);
			controller.getStage().showAndWait();
		} catch (IOException e) {
			logError(e.getMessage());
		}
	}

	@Override
	public void newGraphSelected(final TspGraph graph) {
		reset();
		tspGraph = graph;
		jsApp.sendNewMap(graph);
	}

	// Menu Bindings


	public void onCloseWindowRequest(ActionEvent evt) {
		stage.close();
	}

	public void onOpenTspProblemRequest(ActionEvent evt){
		openTspProblemChooser();
	}

	public void onAboutRequest(ActionEvent evt){
		openAbout();
	}

	// Action misc bindings

	private static final String BTN_START = "Start";
	private static final String BTN_STOP = "Stop";

	public void onResetBtn(ActionEvent evt){
		reset();
	}

	private void reset(){
		stopSolving();
		tspGraph = new TspGraph();
		jsApp.sendNewMap(tspGraph);
	}

	public void onStartStopBtnAction(ActionEvent evt){
		if(startStopBtn.getText().equals(BTN_START)){
			startSolving();
		} else {
			stopSolving();
		}
	}

	private void startSolving(){
		startStopBtn.setText(BTN_STOP);
		if(solver != null){
			tspGraph.onGraphChange(solver);
			solver.startSolving(tspGraph);
		}
	}

	private void stopSolving(){
		startStopBtn.setText(BTN_START);
		if(solver != null){
			tspGraph.offGraphChange(solver);
			solver.stopSolving();
			bestCostSoFar = 0.0;
			bestRunSoFar = null;
			jsApp.newBestPath(bestRunSoFar);
		}
	}

	@Override
	public void onTspProblemSolved() {
		stopSolving();
	}

	@Override
	public void onNewGraphState(TspGraph g) {
		//jsApp.updateMap(g);
	}

	@Override
	public void closing(MainUI ui) {
		this.reset();
	}
	
	@Override
	public void onNewBestPath(List<TspVertex> flow, final double cost){
		bestCostSoFar = cost;
		bestRunSoFar = flow;
		jsApp.newBestPath(bestRunSoFar);
	}
	
	private String getStatusText(){
		if(solver != null){
			return solver.getSolverName() + " > " + solver.getCurrentStatusInfo() + " | " + "Best run so far: " + bestCostSoFar; 
		} else {
			return "No solver selected yet."; 
		}
	}

	public void openWebviewDebugger(){
		this.jsApp.openFirebug();
	}

	public final void onWebviewReady(final WebviewReadyObserver obs){
		this.webviewReadyObservers.add(obs);
	}

}
