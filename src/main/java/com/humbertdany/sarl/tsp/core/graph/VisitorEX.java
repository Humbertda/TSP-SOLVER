package com.humbertdany.sarl.tsp.core.graph;

public interface VisitorEX<T, E extends Exception> {
	/**
	 * Called by the graph traversal methods when a vertex is first visited.
	 *
	 * @param g -
	 *          the graph
	 * @param v -
	 *          the vertex being visited.
	 * @throws E
	 *           exception for any error
	 */
	public void visit(Graph<T> g, Vertex<T> v) throws E;
}