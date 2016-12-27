import com.humbertdany.sarl.tsp.core.graph.*;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphTest extends ATest {

	public static void main(String args[]){

		final List<TspVertex> points = new ArrayList<>();

		final TspVertex belfort = new TspVertex("Belfort", new VertexInfo(1.0, 1.0));
		final TspVertex besancon = new TspVertex("Besancon", new VertexInfo(1.0, 2.0));
		final TspVertex champagnole = new TspVertex("Champagnole", new VertexInfo(2.0, 1.0));
		final TspVertex dijon = new TspVertex("Dijon", new VertexInfo(2.0, 2.0));
		final TspVertex geneve = new TspVertex("Genève", new VertexInfo(1.5, 1.5));

		points.addAll(Arrays.asList(belfort, besancon, champagnole, dijon, geneve));

		final TspGraph g = new TspGraph();

		g.addAllVertex(points);

		g.insertBiEdge(belfort, besancon, 50);
		g.insertBiEdge(besancon, champagnole, 25);
		g.insertBiEdge(champagnole, geneve, 15);
		g.insertBiEdge(belfort, geneve, 23);
		g.insertBiEdge(dijon, belfort, 50);
		g.insertBiEdge(dijon, champagnole, 40);

		g.setRootVertex(belfort);

		final List<Edge<VertexInfo>> incomingEdges = g.getRootVertex().getIncomingEdges();

		log(g.getRootVertex().toString());
		newLine();
		for(Edge<VertexInfo> e : incomingEdges){
			log(e.toString());
		}
		newLine();
		g.breadthFirstSearch(belfort, new Visitor<VertexInfo>() {
			@Override
			public void visit(Graph<VertexInfo> g, Vertex<VertexInfo> v) {
				log(v);
			}
		});
		newLine();
		log(g.getD3String());

	}

}