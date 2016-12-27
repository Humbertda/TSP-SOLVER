package com.humbertdany.sarl.tsp.tspgraph;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.humbertdany.sarl.tsp.core.graph.Vertex;

public class TspVertex extends Vertex<VertexInfo> {

	public TspVertex(final String name, final VertexInfo info){
		super(name, info);
	}

	public TspVertex(final String name){
		this(name, VertexInfo.initEmpty());
	}

	@JsonInclude
	public VertexInfo getVertexInfo(){
		return this.getData();
	}

}
