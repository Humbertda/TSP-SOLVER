package com.humbertdany.sarl.tsp.tspgraph;

import com.fasterxml.jackson.annotation.JsonInclude;

public class VertexInfo {

	@JsonInclude
	private double x;

	@JsonInclude
	private double y;

	public VertexInfo(double x, double y){
		this.x = x;
		this.y = y;
	}

	public static VertexInfo initEmpty(){
		return new VertexInfo(0, 0);
	}

	@JsonInclude
	public double getY() {
		return y;
	}

	@JsonInclude
	public double getX() {
		return x;
	}

	public String toString(){
		return "{x=" + x + " ; y=" + y + "}";
	}
}
