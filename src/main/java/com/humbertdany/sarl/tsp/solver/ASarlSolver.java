package com.humbertdany.sarl.tsp.solver;

import com.humbertdany.sarl.tsp.core.utils.Runner;
import com.humbertdany.sarl.tsp.solver.aco.EnvironmentListener;
import com.humbertdany.sarl.tsp.solver.generic.NewGraphState;
import com.humbertdany.sarl.tsp.solver.generic.StartSolvingEvent;
import com.humbertdany.sarl.tsp.solver.generic.StopSolvingEvent;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;

import io.sarl.lang.core.EventSpace;

import java.util.List;
import java.util.UUID;

abstract public class ASarlSolver extends ATspSolver implements EnvironmentListener {

	final private UUID uuid = UUID.randomUUID();

	private TspGraph graph;

	private EventSpace defaultLauncherSpace;

	private final Runner<Runnable> runner;

	public ASarlSolver(Runner<Runnable> runner){
		this.runner = runner;
	}

	final public void sarlReady(){
		if(this.defaultLauncherSpace != null){
			final StartSolvingEvent evt = new StartSolvingEvent();
			evt.graph = graph;
			this.defaultLauncherSpace.emit(evt);
		}
	}

	final public void setEventSpace(EventSpace defaultLauncherSpace){
		this.defaultLauncherSpace = defaultLauncherSpace;
	}

	final public UUID getUUID() {
		return uuid;
	}

	final public EventSpace getDefaultSpace(){
		return this.defaultLauncherSpace;
	}

	@Override
	final public void startSolving(TspGraph graph) {
		this.graph = graph;
		verifyGraph(this.graph);
		startSarlSolving();
	}

	@Override
	final public void newGraphState(final TspGraph g) {
		graph = g;
		this.notifyNewGraphState(g);
	}

	@Override
	final public void graphUpdated(TspGraph g) {
		graph = g;
		if(this.defaultLauncherSpace != null){
			final NewGraphState evt = new NewGraphState();
			evt.graph = g;
			this.defaultLauncherSpace.emit(evt);
		}
	}

	@Override
	final public void stopSolving() {
		if(getDefaultSpace() != null){
			getDefaultSpace().emit(new StopSolvingEvent());
		}
	}
	
	public final void runLater(final Runnable r){
		this.runner.run(r);
	}
	
	@Override
	public void newBestPath(List<TspVertex> flow, final double cost) {
		this.notifyNewBestPath(flow, cost);
	}


	// Abstract methods

	abstract protected void startSarlSolving();
	abstract protected void verifyGraph(TspGraph graph);

}
