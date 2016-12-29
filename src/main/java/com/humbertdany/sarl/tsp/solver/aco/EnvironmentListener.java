package com.humbertdany.sarl.tsp.solver.aco;

import com.humbertdany.sarl.tsp.tspgraph.TspGraph;

import io.sarl.lang.core.Identifiable;

public interface EnvironmentListener extends Identifiable {
	void newGraphState(final TspGraph g);
}