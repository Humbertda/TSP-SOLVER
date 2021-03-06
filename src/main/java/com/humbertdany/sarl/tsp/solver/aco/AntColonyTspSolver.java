package com.humbertdany.sarl.tsp.solver.aco;

import com.humbertdany.sarl.tsp.core.graph.Edge;
import com.humbertdany.sarl.tsp.core.params.ApplicationParametersObserver;
import com.humbertdany.sarl.tsp.core.ui.MAnchorPane;
import com.humbertdany.sarl.tsp.core.utils.Runner;
import com.humbertdany.sarl.tsp.solver.ASarlSolver;
import com.humbertdany.sarl.tsp.solver.EnvironmentListener;
import com.humbertdany.sarl.tsp.solver.aco.listener.SarlAcoListener;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;
import com.humbertdany.sarl.tsp.solver.aco.sarl.Launcher;
import com.humbertdany.sarl.tsp.solver.aco.sarl.NewTspProblemParameters;
import com.humbertdany.sarl.tsp.solver.aco.sarl.SarlAcoProcessUpdate;
import com.humbertdany.sarl.tsp.solver.aco.ui.AcoGuiController;
import com.humbertdany.sarl.tsp.solver.aco.utils.AcoTspEdgeData;
import com.humbertdany.sarl.tsp.tspgraph.TspEdgeData;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.VertexInfo;
import io.janusproject.Boot;
import io.janusproject.util.LoggerCreator;

import java.util.UUID;
import java.util.logging.Level;

public class AntColonyTspSolver extends ASarlSolver implements ApplicationParametersObserver<AcoParameters>, EnvironmentListener, SarlAcoListener {
	
	private AcoParameters parameters;
	
	private int currentTick = 0;
	
	private boolean isTesting;
	
	public AntColonyTspSolver(Runner<Runnable> r, AcoParameters p, boolean isTesting){
		super(r);
		this.isTesting = isTesting;
		parameters = p;
		parameters.watchParametersChange(this);
	}

	public AntColonyTspSolver(Runner<Runnable> r, AcoParameters p){
		this(r, p, false); 
	}
	
	public AntColonyTspSolver(Runner<Runnable> r){
		this(r, AcoParameters.buildDefault()); 
	}

	@Override
	public void buildGui(final MAnchorPane ctrl) {
		final AcoGuiController gCtrl = new AcoGuiController(this);
		gCtrl.build(ctrl);
	}

	public AcoParameters getParameters() {
		return parameters;
	}

	@Override
	public void parametersChanged(final AcoParameters p) {
		this.parameters = p;
		if(getDefaultSpace() != null){
			final NewTspProblemParameters newParams = new NewTspProblemParameters();
			newParams.params = p;
			getDefaultSpace().emit(newParams);
		}
	}
	
	@Override
	public UUID getID() {
		return this.getUUID();
	}

	@Override
	public String getSolverName() {
		return "SARL AcoSolver";
	}

	@Override
	public String getColorFor(Edge<VertexInfo> e) {
		return "rgba(170, 170, 170, 0.1)";
	}

	@Override
	public TspEdgeData makeEdgeData() {
		return new AcoTspEdgeData(this.parameters);
	}

	// Solving process

	@Override
	public void startSarlSolving() {
		Boot.setOffline(true);
		Boot.setVerboseLevel(LoggerCreator.toInt(Level.INFO));
		try {
			Boot.startJanus(
					(Class) null,
					Launcher.class,
					this, 
					this.isTesting
			);
		} catch (Exception e) {
			System.exit(-1);
		}
	}

	protected void verifyGraph(TspGraph graph) {
		for(Edge<VertexInfo> e : graph.getEdges()){
			if(e.getData() == null){
				e.setData(new AcoTspEdgeData(this.parameters));
			}
		}
	}

	@Override
	public void onNewTick(int tickNumber) {
		currentTick = tickNumber;
	}

	@Override
	protected void mainEventSpaceReceived() {
		SarlAcoProcessUpdate evt = new SarlAcoProcessUpdate();
		evt.obs = this;
		this.getDefaultSpace().emit(evt);
	}

	@Override
	public String getCurrentStatusInfo() {
		return "Current Tick: " + currentTick + " ";
	}


}
