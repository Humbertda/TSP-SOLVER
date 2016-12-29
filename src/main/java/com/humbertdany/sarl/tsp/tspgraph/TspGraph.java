package com.humbertdany.sarl.tsp.tspgraph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humbertdany.sarl.tsp.core.graph.Edge;
import com.humbertdany.sarl.tsp.core.graph.Graph;
import com.humbertdany.sarl.tsp.core.graph.Vertex;
import com.humbertdany.sarl.tsp.ui.mainui.D3GraphDisplayable;

import java.util.List;

public class TspGraph extends Graph<VertexInfo> implements D3GraphDisplayable {

	public boolean addAllVertex(List<TspVertex> vertexes){
		boolean res = true;
		for(TspVertex v : vertexes){
			boolean tmp = this.addVertex(v);
			if(!tmp){
				res = false;
			}
		}
		return res;
	}

	@Override
	public String getD3String() {
		ObjectMapper ow = new ObjectMapper();
		try {
			int counter;
			String json;
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			counter = 0;
			sb.append("\"cities\":[");
			for(Vertex<VertexInfo> vt : this.getVerticies()){
				TspVertex v = (TspVertex) vt;
				sb.append(ow.writeValueAsString(v));
				if(counter != this.getVerticies().size()-1){
					sb.append(", ");

				}
				counter++;
			}
			sb.append("]");
			sb.append(", \"edges\":[");
			counter = 0;
			for(Edge<VertexInfo> et : this.getEdges()){
				TspJsonEdge e = new TspJsonEdge(et);
				sb.append(ow.writeValueAsString(e));
				if(counter != this.getEdges().size()-1){

					sb.append(", ");
				}
				counter++;
			}
			sb.append("]");
			sb.append("}");
			json = sb.toString();
			return json;
		} catch (JsonProcessingException e) {
			logError(e.getMessage());
			return "[]";
		}
	}

	private static class TspJsonEdge {
		private TspVertex from;
		private TspVertex to;

		TspJsonEdge(Edge<VertexInfo> e){
			this.from = (TspVertex) e.getFrom();
			this.to = (TspVertex) e.getTo();
		}

		public TspVertex getFrom() {
			return from;
		}

		public void setFrom(TspVertex from) {
			this.from = from;
		}

		public TspVertex getTo() {
			return to;
		}

		public void setTo(TspVertex to) {
			this.to = to;
		}

	}
}
