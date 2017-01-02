package com.humbertdany.sarl.tsp.filereader.parser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humbertdany.sarl.tsp.core.helper.DistanceHelper;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;
import com.humbertdany.utils.factory.ArrayFactory;
import com.humbertdany.utils.sort.ISortingAlgorithm;
import com.humbertdany.utils.sort.SortingFusion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

abstract public class ATspProblemReader implements ITspFileReader {

	private Logger logger;

	final private ISortingAlgorithm<Elem> sortingAlgo = new SortingFusion<>(new ArrayFactory<Elem>() {
		@Override
		public Elem[] buildArray(int dimension) {
			return new Elem[dimension];
		}
	});

	final public TspGraph getGraphFromRecords(List<Record> records){

		final TspGraph graph = new TspGraph();
		final TspVertex[] vertices = new TspVertex[records.size()];

		int rIndex = 0;
		for (Record r : records) {
			// Generate the vertex linked to the record
			final TspVertex tspVertex = new TspVertex("City " + rIndex, new VertexInfo(r.x, r.y));
			vertices[rIndex] = tspVertex;
			graph.addVertex(tspVertex);
			rIndex++;
		}

		generateGraphWithFullLink(graph, vertices, records);
		graph.setRootVertex(vertices[0]);

		return graph;
	}
	
	private void generateGraphWithSmartLink(final TspGraph graph, final TspVertex[] vertices, final List<Record> records){
		List<Elem> temporaryCostArray;
		int rIndex = 0;
		for(TspVertex v : vertices){
			rIndex = 0;
			int bIndex = 0;
			temporaryCostArray = new ArrayList<>();
			for (Record r : records) {
				if(!Objects.equals(("City " + rIndex), v.getName())){
					double cost = DistanceHelper.calculateEuclideanDistance(r.x, r.y, v.getData().getX(), v.getData().getY());
					temporaryCostArray.add(new Elem(bIndex, cost));
					bIndex++;
				}
				rIndex++;
			}

			// only link the 3 more close Vertex, so we cant go every city from here
			final Collection<Elem> sort = sortingAlgo.sort(temporaryCostArray);
			final Elem[] sorted = new Elem[sort.size()];
			sort.toArray(sorted);
			for(int i = 0; i < 3; i++){
				final Elem e = sorted[i];
				this.addEdgeToGraph(graph, v, vertices[e.idInArray]);
			}
		}
	}
	
	private void generateGraphWithFullLink(final TspGraph graph, final TspVertex[] vertices, final List<Record> records){
		for(TspVertex v : vertices){
			for(TspVertex v2 : vertices){
				if(v2 != v){
					// Calculating most of the distance in advance
					DistanceHelper.calculateEuclideanDistance(v.getData().getX(), v.getData().getY(), v2.getData().getX(), v2.getData().getY());
					this.addEdgeToGraph(graph, v, v2);
				}
			}
		}
	}

	abstract void addEdgeToGraph(final TspGraph g, final TspVertex from, final TspVertex to);

	/**
	 * Private util class used to store
	 * an element (cost and id in array)
	 */
	private static class Elem implements Comparable<Elem> {
		private Integer idInArray;
		private Double cost;

		public Elem(int idInArray, double cost) {
			this.idInArray = idInArray;
			this.cost = cost;
		}

		@Override
		public int compareTo(final Elem o) {
			return cost.compareTo(o.cost);
		}
	}

	@JsonIgnore
	protected final void log(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.info("\n" + o.toString());
	}

	@JsonIgnore
	protected final void logError(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.error("\n" + o.toString());
	}

	@JsonIgnore
	private void initLogger(){
		logger = LogManager.getLogger(this.getClass());
	}

}
