package com.humbertdany.sarl.tsp.solver.tester;

import com.humbertdany.sarl.tsp.core.params.AApplicationParameters;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
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

}
