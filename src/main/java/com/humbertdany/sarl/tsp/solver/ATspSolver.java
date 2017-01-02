package com.humbertdany.sarl.tsp.solver;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humbertdany.sarl.tsp.core.graph.Edge;
import com.humbertdany.sarl.tsp.core.graph.EdgeData;
import com.humbertdany.sarl.tsp.core.graph.GraphObserver;
import com.humbertdany.sarl.tsp.core.params.AApplicationParameters;
import com.humbertdany.sarl.tsp.core.ui.MAnchorPane;
import com.humbertdany.sarl.tsp.tspgraph.TspEdgeData;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

abstract public class ATspSolver implements GraphObserver<TspGraph> {

	private Logger logger;

	private ArrayList<SolverObserver> solverObservers = new ArrayList<>();

	// -- abstract methods

	public abstract AApplicationParameters getParameters();

	abstract public void buildGui(final MAnchorPane ctrl);

	final public String toString(){
		return this.getSolverName() == null ? "Null" : this.getSolverName();
	}

	public abstract String getSolverName();
	public abstract String getCurrentStatusInfo();

	// Solving process

	public abstract void startSolving(TspGraph graph);
	public abstract void stopSolving();

	// Logger

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

	public final void onSolverDone(SolverObserver obs){
		this.solverObservers.add(obs);
	}

	public final void offSolverDone(SolverObserver obs){
		this.solverObservers.remove(this.solverObservers.indexOf(obs));
	}

	protected final void notifySolverDone(){
		if(this.solverObservers.size() != 0){
			this.solverObservers.forEach(SolverObserver::onTspProblemSolved);
		}
	}

	protected final void notifyNewGraphState(final TspGraph g){
		if(this.solverObservers.size() != 0){
			for(SolverObserver obs : this.solverObservers){
				obs.onNewGraphState(g);
			}
		}
	}
	
	protected final void notifyNewBestPath(final List<TspVertex> flow, final double cost){
		if(this.solverObservers.size() != 0){
			for(SolverObserver obs : this.solverObservers){
				obs.onNewBestPath(flow, cost);
			}
		}
	}
	
	public abstract String getColorFor(Edge<VertexInfo> e);

	public abstract TspEdgeData makeEdgeData();
}
