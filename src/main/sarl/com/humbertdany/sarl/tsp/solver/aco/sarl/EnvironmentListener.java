package com.humbertdany.sarl.tsp.solver.aco.sarl;

import com.humbertdany.sarl.tsp.core.graph.Graph;

import io.sarl.lang.core.Identifiable;

public interface EnvironmentListener extends Identifiable {
	void newGraphState(final Graph g);
}