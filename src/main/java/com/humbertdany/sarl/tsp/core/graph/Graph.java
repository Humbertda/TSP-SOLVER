package com.humbertdany.sarl.tsp.core.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Graph implements Serializable {

	final private PriorityQueue<Vertex> vertexes;

	public Graph(final List<Point> pointList){
		vertexes = new PriorityQueue<>(10, Vertex::compareTo);
		final ArrayList<Vertex> vs = new ArrayList<>();
		for(Point p : pointList){
			vs.add(new Vertex(p.getX(), p.getY()));
		}
		vertexes.addAll(vs);
	}

	public Graph() {
		this(new ArrayList<>());
	}

	public Vertex maxDegreeVertex() {
		return vertexes.peek();
	}

	public String toString(){
		return vertexes.toString();
	}
	
}