package com.humbertdany.sarl.tsp.solver.tester;

import com.humbertdany.sarl.tsp.core.graph.Edge;
import com.humbertdany.sarl.tsp.core.params.AApplicationParameters;
import com.humbertdany.sarl.tsp.core.ui.MAnchorPane;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.tspgraph.TspEdgeData;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AntColonySolverTester extends ATspSolver {

	@Override
	public AApplicationParameters getParameters() {
		return null;
	}

	@Override
	public void buildGui(final MAnchorPane ctrl) {
		final TextFlow tf = new TextFlow();
		tf.getChildren().addAll(
				new Text("This is a solver tester, "),
				new Text("Used to debug and develop."),
				new Text("It does not solve TSP, it only send the events to the Logger")
		);
		ctrl.add(tf, 5.0);
	}

	@Override
	protected String getSolverName() {
		return "Tester";
	}

	@Override
	public void startSolving(TspGraph graph) {
		log("the Tester solver received a startSolving request for the following graph (as JS)");
		log(graph.getD3String(this));
		this.notifyNewGraphState(graph);
		this.notifySolverDone();
	}

	@Override
	public void stopSolving() {
		log("Stop solving");
	}

	@Override
	public String getColorFor(Edge<VertexInfo> e) {
		return "blue";
	}

	@Override
	public TspEdgeData makeEdgeData() {
		return new TspEdgeData();
	}

	@Override
	public void graphUpdated(TspGraph g) {
		log("A new Graph State has been defined (new city, new path, etc.), it is now ! \n" + g.toString());
	}
}
