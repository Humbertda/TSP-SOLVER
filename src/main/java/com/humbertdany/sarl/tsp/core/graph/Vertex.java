package com.humbertdany.sarl.tsp.core.graph;

import java.util.HashSet;

class Vertex implements Comparable<Vertex> {

	private final HashSet<Vertex> edges;
	private final double x;
	private final double y;

	public Vertex(double x, double y) {
		edges = new HashSet<>();
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Vertex o) {
		return Integer.compare(this.edges.size(), o.edges.size());
	}

	public String toString(){
		return "{x=" + x + "; y=" + y + "}" + edges;
	}
}