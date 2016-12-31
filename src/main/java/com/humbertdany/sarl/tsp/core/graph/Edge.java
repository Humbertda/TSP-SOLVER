package com.humbertdany.sarl.tsp.core.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A directed, weighted edge in a graph
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 * @param <T>
 */
public class Edge<T> {

	@JsonIgnore
	private Vertex<T> from;

	@JsonInclude
	private Vertex<T> to;

	@JsonIgnore
	private boolean mark;

	@JsonIgnore
	private EdgeData data;

	/**
	 * Create an edge between from and to with the given cost.
	 *
	 * @param from
	 *          the starting vertex
	 * @param to
	 *          the ending vertex
	 */
	@JsonIgnore
	public Edge(Vertex<T> from, Vertex<T> to, EdgeData data) {
		this.from = from;
		this.to = to;
		this.data = data;
		mark = false;
	}

	/**
	 * Get the ending vertex
	 *
	 * @return ending vertex
	 */
	@JsonInclude
	public Vertex<T> getTo() {
		return to;
	}

	/**
	 * Get the starting vertex
	 *
	 * @return starting vertex
	 */
	@JsonIgnore
	public Vertex<T> getFrom() {
		return from;
	}

	/**
	 * Get the cost of the edge
	 *
	 * @return cost of the edge
	 */
	@JsonInclude
	public double getCost() {
		return this.getFrom().getCostTo(this.getTo());
	}

	/**
	 * Set the mark flag of the edge
	 *
	 */
	@JsonIgnore
	public void mark() {
		mark = true;
	}

	/**
	 * Clear the edge mark flag
	 *
	 */
	@JsonIgnore
	public void clearMark() {
		mark = false;
	}

	/**
	 * Get the edge mark flag
	 *
	 * @return edge mark flag
	 */
	@JsonIgnore
	public boolean isMarked() {
		return mark;
	}

	/**
	 * Get the Edge data
	 * @return the data
	 */
	public EdgeData getData() {
		return data;
	}

	public void setData(EdgeData data) {
		this.data = data;
	}

	/**
	 * String rep of edge
	 *
	 * @return string rep with from/to vertex names and cost
	 */
	@JsonIgnore
	public String toString() {
		StringBuffer tmp = new StringBuffer("Edge[from: ");
		tmp.append(from.getName());
		tmp.append(",to: ");
		tmp.append(to.getName());
		tmp.append(", cost: ");
		tmp.append(getCost());
		tmp.append("]");
		return tmp.toString();
	}
}