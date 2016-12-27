package com.humbertdany.sarl.tsp.filereader.parser;

import com.humbertdany.sarl.tsp.filereader.DistanceHelper;
import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;
import com.humbertdany.utils.factory.ArrayFactory;
import com.humbertdany.utils.sort.ISortingAlgorithm;
import com.humbertdany.utils.sort.SortingFusion;

import java.util.*;

/**
 * https://github.com/thomasjungblut/antcolonyopt
 */
public class ThomasJungBlutFileReader implements ITspFileReader {

	public ThomasJungBlutFileReader(){

	}

	public final TspGraph readFromString(final String f) throws ParsingException {
		List<String> lines = Arrays.asList(f.split("\n"));
		final ArrayList<Record> records = new ArrayList<>();
		boolean readAhead = false;
		for(String line : lines){
			if (line.equals("EOF")) {
				break;
			}

			if (readAhead) {
				String[] split = sweepNumbers(line.trim());
				records.add(new Record(Double.parseDouble(split[1].trim()), Double
						.parseDouble(split[2].trim())));
			}

			if (line.equals("NODE_COORD_SECTION")) {
				readAhead = true;
			}
		}

		final double[][] localMatrix = new double[records.size()][records.size()];
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

		final ISortingAlgorithm<Elem> sortingAlgo = new SortingFusion<>(new ArrayFactory<Elem>() {
			@Override
			public Elem[] buildArray(int dimension) {
				return new Elem[dimension];
			}
		});

		List<Elem> temporaryCostArray;
		for(TspVertex v : vertices){
			rIndex = 0;
			int bIndex = 0;
			temporaryCostArray = new ArrayList<>();
			for (Record r : records) {
				if(!Objects.equals(("City " + rIndex), v.getName())){
					int cost = DistanceHelper.calculateRoundEuclideanDistance(r.x, r.y, v.getData().getX(), v.getData().getY());
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
				graph.addEdge(v, vertices[e.idInArray], e.cost);
			}
		}

		return graph;
	}

	private static class Elem implements Comparable<Elem> {
		private Integer idInArray;
		private Integer cost;

		public Elem(int idInArray, int cost) {
			this.idInArray = idInArray;
			this.cost = cost;
		}

		@Override
		public int compareTo(Elem o) {
			return cost.compareTo(o.cost);
		}
	}

	private static String[] sweepNumbers(final String input) {
		final String[] arr = new String[3];
		int currentIndex = 0;
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if ((c) != 32) {
				for (int f = i + 1; f < input.length(); f++) {
					final char x = input.charAt(f);
					if ((x) == 32) {
						arr[currentIndex] = input.substring(i, f);
						currentIndex++;
						break;
					} else if (f == input.length() - 1) {
						arr[currentIndex] = input.substring(i, input.length());
						break;
					}
				}
				i = i + arr[currentIndex - 1].length();
			}
		}
		return arr;
	}

}