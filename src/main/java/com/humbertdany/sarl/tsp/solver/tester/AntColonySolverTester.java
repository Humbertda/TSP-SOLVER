package com.humbertdany.sarl.tsp.solver.tester;

import com.humbertdany.sarl.tsp.core.params.AApplicationParameters;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import javafx.scene.layout.Pane;

public class AntColonySolverTester extends ATspSolver {

	@Override
	public AApplicationParameters getParameters() {
		return null;
	}

	@Override
	public void buildGui(final Pane ctrl) {

	}

	@Override
	protected String getSolverName() {
		return "Tester";
	}

	@Override
	public void startSolving(TspGraph graph) {
		log("the Tester solver received a startSolving request for the following graph (as JS)");
		log(graph.getD3String());
		this.notifyNewGraphState(graph);
		this.notifySolverDone();
	}

	@Override
	public void stopSolving() {
		log("Stop solving");
	}

}
