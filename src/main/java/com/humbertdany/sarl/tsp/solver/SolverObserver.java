package com.humbertdany.sarl.tsp.solver;

import com.humbertdany.sarl.tsp.tspgraph.TspGraph;

public interface SolverObserver {

	void onTspProblemSolved();
	void onNewGraphState(TspGraph g);

}
