package com.humbertdany.sarl.tsp.solver;


import com.humbertdany.sarl.tsp.core.params.AApplicationParameters;
import javafx.scene.layout.Pane;

abstract public class ATspSolver {

	// -- abstract methods

	public abstract AApplicationParameters getParameters();
	abstract public void buildGui(final Pane ctrl);
}
