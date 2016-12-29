import com.humbertdany.sarl.tsp.core.graph.Edge;
import com.humbertdany.sarl.tsp.core.graph.Graph;
import com.humbertdany.sarl.tsp.core.graph.Vertex;
import com.humbertdany.sarl.tsp.core.graph.Visitor;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphTest extends ATest {

	public static void main(String args[]){

		final List<TspVertex> points = new ArrayList<>();

		final TspVertex belfort = new TspVertex("Belfort", new VertexInfo(100.0, 100.0));
		final TspVertex besancon = new TspVertex("Besancon", new VertexInfo(100.0, 200.0));
		final TspVertex champagnole = new TspVertex("Champagnole", new VertexInfo(200.0, 100.0));
		final TspVertex dijon = new TspVertex("Dijon", new VertexInfo(200.0, 200.0));
		final TspVertex geneve = new TspVertex("Genève", new VertexInfo(100.5, 100.5));

		points.addAll(Arrays.asList(belfort, besancon, champagnole, dijon, geneve));

		final TspGraph g = new TspGraph();

		g.addAllVertex(points);

		g.insertBiEdge(belfort, besancon);
		g.insertBiEdge(besancon, champagnole);
		g.insertBiEdge(champagnole, geneve);
		g.insertBiEdge(belfort, geneve);
		g.insertBiEdge(dijon, belfort);
		g.insertBiEdge(dijon, champagnole);

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