package com.humbertdany.sarl.tsp.solver;

import java.util.List;

import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;

import io.sarl.lang.core.Identifiable;

public interface EnvironmentListener extends Identifiable {
	void newGraphState(final TspGraph g);
	void newBestPath(final List<TspVertex> flow, final double cost); 
}