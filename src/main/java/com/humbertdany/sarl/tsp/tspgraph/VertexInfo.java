package com.humbertdany.sarl.tsp.tspgraph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.humbertdany.sarl.tsp.core.graph.VertexData;

public class VertexInfo extends VertexData {

	@JsonInclude
	private double x;

	@JsonInclude
	private double y;

	public VertexInfo(double x, double y){
		this.x = x;
		this.y = y;
	}

	@JsonInclude
	public double getY() {
		return y;
	}

	@JsonInclude
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.setXY(x, this.y);
	}

	public void setY(double y) {
		this.setXY(this.x, y);
	}

	public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
		this.notifyDataUpdate();
	}

	public String toString(){
		return "{x=" + x + " ; y=" + y + "}";
	}

}
