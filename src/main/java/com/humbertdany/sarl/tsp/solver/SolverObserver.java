package com.humbertdany.sarl.tsp.solver;

import java.util.List;

import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;

public interface SolverObserver {

	void onTspProblemSolved();
	void onNewGraphState(TspGraph g);
	void onNewBestPath(List<TspVertex> flow); 

}
