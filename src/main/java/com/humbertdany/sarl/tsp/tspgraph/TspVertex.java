package com.humbertdany.sarl.tsp.tspgraph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.humbertdany.sarl.tsp.core.graph.Vertex;
import com.humbertdany.sarl.tsp.core.helper.DistanceHelper;

public class TspVertex extends Vertex<VertexInfo> {

	public TspVertex(final String name, final VertexInfo info){
		super(name, info);
	}

	@JsonInclude
	public VertexInfo getVertexInfo(){
		return this.getData();
	}

	@Override
	public double getCostTo(Vertex<VertexInfo> o){
		// Their cost only depends from Distance
		return DistanceHelper.calculateEuclideanDistance(
				this.getData().getX(), this.getData().getY(),
				o.getData().getX(), o.getData().getY()
		);
	}

}
