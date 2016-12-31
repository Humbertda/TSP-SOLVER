package com.humbertdany.sarl.tsp.core.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Graph<T> {

	@JsonIgnore
	private Logger logger;

	/** Color used to mark unvisited nodes */
	@JsonIgnore
	private static final int VISIT_COLOR_WHITE = 1;

	/** Color used to mark nodes as they are first visited in DFS order */
	@JsonIgnore
	private static final int VISIT_COLOR_GREY = 2;

	/** Color used to mark nodes after descendants are completely visited */
	@JsonIgnore
	private static final int VISIT_COLOR_BLACK = 3;

	/** Vector<Vertex> of graph verticies */
	private List<Vertex<T>> verticies;

	/** Vector<Edge> of edges in the graph */
	@JsonIgnore
	private List<Edge<T>> edges;

	/** The vertex identified as the root of the graph */
	@JsonIgnore
	private Vertex<T> rootVertex;

	@JsonIgnore
	private final List<GraphObserver> graphObservers = new ArrayList<>();

	/**
	 * Construct a new graph without any vertices or edges
	 */
	@JsonIgnore
	public Graph() {
		verticies = new ArrayList<>();
		edges = new ArrayList<>();
	}

	/**
	 * Are there any verticies in the graph
	 *
	 * @return true if there are no verticies in the graph
	 */
	@JsonIgnore
	public boolean isEmpty() {
		return verticies.size() == 0;
	}

	/**
	 * Add a vertex to the graph
	 *
	 * @param v
	 *          the Vertex to add
	 * @return true if the vertex was added, false if it was already in the graph.
	 */
	@JsonIgnore
	public boolean addVertex(Vertex<T> v) {
		boolean added = false;
		if (!verticies.contains(v)) {
			added = verticies.add(v);
			notifyGraphChange();
			v.setGraphReference(this); // allow to trigger graph change from vertex
		}
		return added;
	}

	/**
	 * Get the vertex count.
	 *
	 * @return the number of verticies in the graph.
	 */
	@JsonIgnore
	public int size() {
		return verticies.size();
	}

	/**
	 * Get the root vertex
	 *
	 * @return the root vertex if one is set, null if no vertex has been set as
	 *         the root.
	 */
	@JsonIgnore
	public Vertex<T> getRootVertex() {
		return rootVertex;
	}

	/**
	 * Set a root vertex. If root does no exist in the graph it is added.
	 *
	 * @param root -
	 *          the vertex to set as the root and optionally add if it does not
	 *          exist in the graph.
	 */
	@JsonIgnore
	public void setRootVertex(Vertex<T> root) {
		this.rootVertex = root;
		if (!verticies.contains(root))
			this.addVertex(root);
		notifyGraphChange();
	}

	/**
	 * Get the given Vertex.
	 *
	 * @param n
	 *          the index [0, size()-1] of the Vertex to access
	 * @return the nth Vertex
	 */
	@JsonIgnore
	public Vertex<T> getVertex(int n) {
		return verticies.get(n);
	}

	/**
	 * Get the graph verticies
	 *
	 * @return the graph verticies
	 */
	public List<Vertex<T>> getVerticies() {
		return this.verticies;
	}

	/**
	 * Insert a directed, weighted Edge<T> into the graph.
	 *
	 * @param from -
	 *          the Edge<T> starting vertex
	 *          the Edge<T> weight/cost
	 * @return true if the Edge<T> was added, false if from already has this Edge<T>
	 * @throws IllegalArgumentException
	 *           if from/to are not verticies in the graph
	 */
	@JsonIgnore
	public boolean addEdge(Vertex<T> from, Vertex<T> to, EdgeData d) throws IllegalArgumentException {
		if (!verticies.contains(from))
			throw new IllegalArgumentException("from is not in graph");
		if (!verticies.contains(to))
			throw new IllegalArgumentException("to is not in graph");

		Edge<T> e = new Edge<>(from, to, d);
		if (from.findEdge(to) != null)
			return false;
		else {
			from.addEdge(e);
			to.addEdge(e);
			edges.add(e);
			notifyGraphChange();
			return true;
		}
	}

	/**
	 * Get the graph edges
	 *
	 * @return the graph edges
	 */
	@JsonIgnore
	public List<Edge<T>> getEdges() {
		return this.edges;
	}

	/**
	 * Remove a vertex from the graph
	 *
	 * @param v
	 *          the Vertex to remove
	 * @return true if the Vertex was removed
	 */
	@JsonIgnore
	public boolean removeVertex(Vertex<T> v) {
		if (!verticies.contains(v))
			return false;

		verticies.remove(v);
		if (v == rootVertex)
			rootVertex = null;

		// Remove the edges associated with v
		for (int n = 0; n < v.getOutgoingEdgeCount(); n++) {
			Edge<T> e = v.getOutgoingEdge(n);
			v.remove(e);
			Vertex<T> to = e.getTo();
			to.remove(e);
			edges.remove(e);
		}
		for (int n = 0; n < v.getIncomingEdgeCount(); n++) {
			Edge<T> e = v.getIncomingEdge(n);
			v.remove(e);
			Vertex<T> predecessor = e.getFrom();
			predecessor.remove(e);
		}
		notifyGraphChange();
		return true;
	}

	/**
	 * Remove an Edge<T> from the graph
	 *
	 * @param from -
	 *          the Edge<T> starting vertex
	 * @param to -
	 *          the Edge<T> ending vertex
	 * @return true if the Edge<T> exists, false otherwise
	 */
	@JsonIgnore
	public boolean removeEdge(Vertex<T> from, Vertex<T> to) {
		Edge<T> e = from.findEdge(to);
		if (e == null)
			return false;
		else {
			from.remove(e);
			to.remove(e);
			edges.remove(e);
			notifyGraphChange();
			return true;
		}
	}

	/**
	 * Clear the mark state of all verticies in the graph by calling clearMark()
	 * on all verticies.
	 *
	 * @see Vertex#clearMark()
	 */
	@JsonIgnore
	public void clearMark() {
		for (Vertex<T> w : verticies)
			w.clearMark();
		notifyGraphChange();
	}

	/**
	 * Clear the mark state of all edges in the graph by calling clearMark() on
	 * all edges.
	 */
	@JsonIgnore
	public void clearEdges() {
		for (Edge<T> e : edges)
			e.clearMark();
		notifyGraphChange();
	}

	/**
	 * Perform a depth first serach using recursion.
	 *
	 * @param v -
	 *          the Vertex to start the search from
	 * @param visitor -
	 *          the vistor to inform prior to
	 * @see Visitor#visit(Graph, Vertex)
	 */
	@JsonIgnore
	public void depthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
		VisitorEX<T, RuntimeException> wrapper = (g, v1) -> {
			if (visitor != null)
				visitor.visit(g, v1);
		};
		this.depthFirstSearch(v, wrapper);
	}

	/**
	 * Perform a depth first serach using recursion. The search may be cut short
	 * if the visitor throws an exception.
	 *
	 * @param <E>
	 *
	 * @param v -
	 *          the Vertex to start the search from
	 * @param visitor -
	 *          the vistor to inform prior to
	 * @see Visitor#visit(Graph, Vertex)
	 * @throws E
	 *           if visitor.visit throws an exception
	 */
	@JsonIgnore
	public <E extends Exception> void depthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor) throws E {
		if (visitor != null)
			visitor.visit(this, v);
		v.visit();
		for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
			Edge<T> e = v.getOutgoingEdge(i);
			if (!e.getTo().visited()) {
				depthFirstSearch(e.getTo(), visitor);
			}
		}
	}

	/**
	 * Perform a breadth first search of this graph, starting at v.
	 *
	 * @param v -
	 *          the search starting point
	 * @param visitor -
	 *          the vistor whose vist method is called prior to visting a vertex.
	 */
	@JsonIgnore
	public void breadthFirstSearch(Vertex<T> v, final Visitor<T> visitor) {
		VisitorEX<T, RuntimeException> wrapper = (g, v1) -> {
			if (visitor != null)
				visitor.visit(g, v1);
		};
		this.breadthFirstSearch(v, wrapper);
	}

	/**
	 * Perform a breadth first search of this graph, starting at v. The vist may
	 * be cut short if visitor throws an exception during a vist callback.
	 *
	 * @param <E>
	 *
	 * @param v -
	 *          the search starting point
	 * @param visitor -
	 *          the vistor whose vist method is called prior to visting a vertex.
	 * @throws E
	 *           if vistor.visit throws an exception
	 */
	@JsonIgnore
	public <E extends Exception> void breadthFirstSearch(Vertex<T> v, VisitorEX<T, E> visitor)
			throws E {
		LinkedList<Vertex<T>> q = new LinkedList<Vertex<T>>();

		q.add(v);
		if (visitor != null)
			visitor.visit(this, v);
		v.visit();
		while (q.isEmpty() == false) {
			v = q.removeFirst();
			for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
				Edge<T> e = v.getOutgoingEdge(i);
				Vertex<T> to = e.getTo();
				if (!to.visited()) {
					q.add(to);
					if (visitor != null)
						visitor.visit(this, to);
					to.visit();
				}
			}
		}
	}

	/**
	 * Find the spanning tree using a DFS starting from v.
	 *
	 * @param v -
	 *          the vertex to start the search from
	 * @param visitor -
	 *          visitor invoked after each vertex is visited and an edge is added
	 *          to the tree.
	 */
	@JsonIgnore
	public void dfsSpanningTree(Vertex<T> v, DFSVisitor<T> visitor) {
		v.visit();
		if (visitor != null)
			visitor.visit(this, v);

		for (int i = 0; i < v.getOutgoingEdgeCount(); i++) {
			Edge<T> e = v.getOutgoingEdge(i);
			if (!e.getTo().visited()) {
				if (visitor != null)
					visitor.visit(this, v, e);
				e.mark();
				dfsSpanningTree(e.getTo(), visitor);
			}
		}
	}

	/**
	 * Search the verticies for one with name.
	 *
	 * @param name -
	 *          the vertex name
	 * @return the first vertex with a matching name, null if no matches are found
	 */
	@JsonIgnore
	public Vertex<T> findVertexByName(String name) {
		Vertex<T> match = null;
		for (Vertex<T> v : verticies) {
			if (name.equals(v.getName())) {
				match = v;
				break;
			}
		}
		return match;
	}

	/**
	 * Search the verticies for one with data.
	 *
	 * @param data -
	 *          the vertex data to match
	 * @param compare -
	 *          the comparator to perform the match
	 * @return the first vertex with a matching data, null if no matches are found
	 */
	@JsonIgnore
	public Vertex<T> findVertexByData(T data, Comparator<T> compare) {
		Vertex<T> match = null;
		for (Vertex<T> v : verticies) {
			if (compare.compare(data, v.getData()) == 0) {
				match = v;
				break;
			}
		}
		return match;
	}

	/**
	 * Search the graph for cycles. In order to detect cycles, we use a modified
	 * depth first search called a colored DFS. All nodes are initially marked
	 * white. When a node is encountered, it is marked grey, and when its
	 * descendants are completely visited, it is marked black. If a grey node is
	 * ever encountered, then there is a cycle.
	 *
	 * @return the edges that form cycles in the graph. The array will be empty if
	 *         there are no cycles.
	 */
	@JsonIgnore
	public Edge<T>[] findCycles() {
		ArrayList<Edge<T>> cycleEdges = new ArrayList<Edge<T>>();
		// Mark all verticies as white
		for (int n = 0; n < verticies.size(); n++) {
			Vertex<T> v = getVertex(n);
			v.setMarkState(VISIT_COLOR_WHITE);
		}
		for (int n = 0; n < verticies.size(); n++) {
			Vertex<T> v = getVertex(n);
			visit(v, cycleEdges);
		}

		Edge<T>[] cycles = new Edge[cycleEdges.size()];
		cycleEdges.toArray(cycles);
		return cycles;
	}

	@JsonIgnore
	private void visit(Vertex<T> v, ArrayList<Edge<T>> cycleEdges) {
		v.setMarkState(VISIT_COLOR_GREY);
		int count = v.getOutgoingEdgeCount();
		for (int n = 0; n < count; n++) {
			Edge<T> e = v.getOutgoingEdge(n);
			Vertex<T> u = e.getTo();
			if (u.getMarkState() == VISIT_COLOR_GREY) {
				// A cycle Edge<T>
				cycleEdges.add(e);
			} else if (u.getMarkState() == VISIT_COLOR_WHITE) {
				visit(u, cycleEdges);
			}
		}
		v.setMarkState(VISIT_COLOR_BLACK);
	}

	@JsonIgnore
	public String toString() {
		final StringBuilder tmp = new StringBuilder("Graph[");
		for (Vertex<T> v : verticies)
			tmp.append(v);
		tmp.append(']');
		return tmp.toString();
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

	// Observer

	@JsonIgnore
	public final void onGraphChange(GraphObserver obs){
		this.graphObservers.add(obs);
	}

	@JsonIgnore
	public final void offGraphChange(GraphObserver obs){
		final int i = this.graphObservers.indexOf(obs);
		if(i >= 0){
			this.graphObservers.remove(i);
		}
	}

	@JsonIgnore
	final void notifyGraphChange(){
		if(this.graphObservers.size() != 0){
			for(GraphObserver g : this.graphObservers){
				g.graphUpdated(this);
			}
		}
	}

}