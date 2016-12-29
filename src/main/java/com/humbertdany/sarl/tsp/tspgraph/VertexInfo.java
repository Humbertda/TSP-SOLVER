package com.humbertdany.sarl.tsp.tspgraph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class VertexInfo {

	@JsonInclude
	private double x;

	@JsonInclude
	private double y;

	@JsonIgnore
	private double cost;

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

	public void setX(double x) {
		this.x = x;
		updateCost();
	}

	public void setY(double y) {
		this.y = y;
		updateCost();
	}

	private void updateCost(){

	}

	public String toString(){
		return "{x=" + x + " ; y=" + y + "}";
	}

}
