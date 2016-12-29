package com.humbertdany.sarl.tsp.core.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * A named graph vertex with optional data.
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 * @param <T>
 */
public class Vertex<T> {

	@JsonIgnore
	private Graph<T> owner;

	@JsonIgnore
	private List<Edge<T>> incomingEdges;

	@JsonIgnore
	private List<Edge<T>> outgoingEdges;

	@JsonInclude
	private String name;

	@JsonIgnore
	private boolean mark;

	@JsonIgnore
	private int markState;

	@JsonInclude
	private T data;

	/**
	 * Calls this(null, null).
	 */
	@JsonIgnore
	public Vertex() {
		this(null, null);
	}

	/**
	 * Create a vertex with the given name and no data
	 *
	 * @param n
	 */
	@JsonIgnore
	public Vertex(String n) {
		this(n, null);
	}

	/**
	 * Create a Vertex with name n and given data
	 *
	 * @param n -
	 *          name of vertex
	 * @param data -
	 *          data associated with vertex
	 */
	@JsonIgnore
	public Vertex(String n, T data) {
		incomingEdges = new ArrayList<>();
		outgoingEdges = new ArrayList<>();
		name = n;
		mark = false;
		this.data = data;
	}

	/**
	 * @return the possibly null name of the vertex
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the possibly null data of the vertex
	 */
	@JsonInclude
	public T getData() {
		return this.data;
	}

	/**
	 * @param data
	 *          The data to set.
	 */
	@JsonIgnore
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Add an edge to the vertex. If edge.from is this vertex, its an outgoing
	 * edge. If edge.to is this vertex, its an incoming edge. If neither from or
	 * to is this vertex, the edge is not added.
	 *
	 * @param e -
	 *          the edge to add
	 * @return true if the edge was added, false otherwise
	 */
	@JsonIgnore
	public boolean addEdge(Edge<T> e) {
		if (e.getFrom() == this) {
			outgoingEdges.add(e);
			owner.notifyGraphChange();
		} else if (e.getTo() == this) {
			incomingEdges.add(e);
			owner.notifyGraphChange();
		} else
			return false;
		return true;
	}

	/**
	 * Add an outgoing edge ending at to.
	 *
	 * @param to -
	 *          the destination vertex
	 */
	@JsonIgnore
	public void addOutgoingEdge(Vertex<T> to) {
		Edge<T> out = new Edge<>(this, to);
		outgoingEdges.add(out);
		owner.notifyGraphChange();
	}

	/**
	 * Add an incoming edge starting at from
	 *
	 * @param from -
	 *          the starting vertex
	 */
	@JsonIgnore
	public void addIncomingEdge(Vertex<T> from) {
		Edge<T> out = new Edge<>(this, from);
		incomingEdges.add(out);
		owner.notifyGraphChange();
	}

	/**
	 * Check the vertex for either an incoming or outgoing edge mathcing e.
	 *
	 * @param e
	 *          the edge to check
	 * @return true it has an edge
	 */
	@JsonIgnore
	public boolean hasEdge(Edge<T> e) {
		if (e.getFrom() == this)
			return incomingEdges.contains(e);
		else if (e.getTo() == this)
			return outgoingEdges.contains(e);
		else
			return false;
	}

	/**
	 * Remove an edge from this vertex
	 *
	 * @param e -
	 *          the edge to remove
	 * @return true if the edge was removed, false if the edge was not connected
	 *         to this vertex
	 */
	@JsonIgnore
	public boolean remove(Edge<T> e) {
		if (e.getFrom() == this) {
			incomingEdges.remove(e);
			owner.notifyGraphChange();
		} else if (e.getTo() == this) {
			outgoingEdges.remove(e);
			owner.notifyGraphChange();
		} else
			return false;
		return true;
	}

	/**
	 *
	 * @return the count of incoming edges
	 */
	@JsonIgnore
	public int getIncomingEdgeCount() {
		return incomingEdges.size();
	}

	/**
	 * Get the ith incoming edge
	 *
	 * @param i
	 *          the index into incoming edges
	 * @return ith incoming edge
	 */
	@JsonIgnore
	public Edge<T> getIncomingEdge(int i) {
		return incomingEdges.get(i);
	}

	/**
	 * Get the incoming edges
	 *
	 * @return incoming edge list
	 */
	@JsonIgnore
	public List<Edge<T>> getIncomingEdges() {
		return this.incomingEdges;
	}

	/**
	 *
	 * @return the count of incoming edges
	 */
	@JsonIgnore
	public int getOutgoingEdgeCount() {
		return outgoingEdges.size();
	}

	/**
	 * Get the ith outgoing edge
	 *
	 * @param i
	 *          the index into outgoing edges
	 * @return ith outgoing edge
	 */
	@JsonIgnore
	public Edge<T> getOutgoingEdge(int i) {
		return outgoingEdges.get(i);
	}

	/**
	 * Get the outgoing edges
	 *
	 * @return outgoing edge list
	 */
	@JsonIgnore
	public List getOutgoingEdges() {
		return this.outgoingEdges;
	}

	/**
	 * Search the outgoing edges looking for an edge whose's edge.to == dest.
	 *
	 * @param dest
	 *          the destination
	 * @return the outgoing edge going to dest if one exists, null otherwise.
	 */
	@JsonIgnore
	public Edge<T> findEdge(Vertex<T> dest) {
		for (Edge<T> e : outgoingEdges) {
			if (e.getTo() == dest)
				return e;
		}
		return null;
	}

	@JsonIgnore
	void setGraphReference(Graph<T> t){
		this.owner = t;
	}

	/**
	 * Search the outgoing edges for a match to e.
	 *
	 * @param e -
	 *          the edge to check
	 * @return e if its a member of the outgoing edges, null otherwise.
	 */
	@JsonIgnore
	public Edge<T> findEdge(Edge<T> e) {
		if (outgoingEdges.contains(e))
			return e;
		else
			return null;
	}

	/**
	 * What is the cost from this vertext to the dest vertex.
	 *
	 * @param dest -
	 *          the destination vertex.
	 * @return Return Integer.MAX_VALUE if we have no edge to dest, 0 if dest is
	 *         this vertex, the cost of the outgoing edge otherwise.
	 */
	@JsonIgnore
	public double cost(Vertex<T> dest) {
		if (dest == this)
			return 0;

		Edge<T> e = findEdge(dest);
		double cost = Integer.MAX_VALUE;
		if (e != null)
			cost = e.getCost();
		return cost;
	}

	/**
	 * Is there an outgoing edge ending at dest.
	 *
	 * @param dest -
	 *          the vertex to check
	 * @return true if there is an outgoing edge ending at vertex, false
	 *         otherwise.
	 */
	@JsonIgnore
	public boolean hasEdge(Vertex<T> dest) {
		return (findEdge(dest) != null);
	}

	/**
	 * Has this vertex been marked during a visit
	 *
	 * @return true is visit has been called
	 */
	@JsonIgnore
	public boolean visited() {
		return mark;
	}

	/**
	 * Set the vertex mark flag.
	 *
	 */
	@JsonIgnore
	public void mark() {
		mark = true;
	}

	/**
	 * Set the mark state to state.
	 *
	 * @param state
	 *          the state
	 */
	@JsonIgnore
	public void setMarkState(int state) {
		markState = state;
	}

	/**
	 * Get the mark state value.
	 *
	 * @return the mark state
	 */
	@JsonIgnore
	public int getMarkState() {
		return markState;
	}

	/**
	 * Visit the vertex and set the mark flag to true.
	 *
	 */
	@JsonIgnore
	public void visit() {
		mark();
	}

	/**
	 * Clear the visited mark flag.
	 *
	 */
	@JsonIgnore
	public void clearMark() {
		mark = false;
	}

	/**
	 * @return a string form of the vertex with in and out edges.
	 */
	@JsonIgnore
	public String toString() {
		final StringBuilder tmp = new StringBuilder("Vertex(");
		tmp.append(name);
		tmp.append(", data=");
		tmp.append(data);
		tmp.append("), in:[");
		for (int i = 0; i < incomingEdges.size(); i++) {
			if (i > 0)
				tmp.append(',');
			tmp.append(getEdgeDesc(incomingEdges.get(i)));
		}
		tmp.append("], out:[");
		for (int i = 0; i < outgoingEdges.size(); i++) {
			if (i > 0)
				tmp.append(',');
			tmp.append(getEdgeDesc(outgoingEdges.get(i)));
		}
		tmp.append(']');
		return tmp.toString();
	}

	public double getCostTo(Vertex<T> v){
		return 0; // This should be implemented
	}

	@JsonIgnore
	private String getEdgeDesc(Edge<T> e){
		final StringBuilder tmp = new StringBuilder();
		tmp.append("{name=");
		tmp.append(e.getTo().name);
		tmp.append(", cost=");
		tmp.append(e.getCost());
		tmp.append('}');
		return tmp.toString();
	}
}