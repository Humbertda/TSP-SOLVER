package com.humbertdany.sarl.tsp.solver.aco;

import java.util.UUID;
import java.util.logging.Level;

import com.humbertdany.sarl.tsp.core.params.ApplicationParametersObserver;
import com.humbertdany.sarl.tsp.core.ui.MAnchorPane;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.solver.aco.sarl.*;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;
import com.humbertdany.sarl.tsp.solver.aco.ui.AcoGuiController;

import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import io.janusproject.Boot;
import io.janusproject.util.LoggerCreator;
import io.sarl.lang.core.EventSpace;

public class AntColonyTspSolver extends ATspSolver implements ApplicationParametersObserver<AcoParameters>, EnvironmentListener {

	final private UUID uuid = UUID.randomUUID();
	
	private AcoParameters parameters;

	private EventSpace defaultLauncherSpace;
	
	private TspGraph graph;
	
	private boolean appParametersUpToDate = false;

	public AntColonyTspSolver(){
		parameters = AcoParameters.buildDefault();
	}

	@Override
	public void buildGui(final MAnchorPane ctrl) {
		final AcoGuiController gCtrl = new AcoGuiController(this);
		gCtrl.build(ctrl);
	}

	public AcoParameters getParameters() {
		appParametersUpToDate = true;
		return parameters;
	}

	@Override
	public void parametersChanged(final AcoParameters p) {
		this.parameters = p;
		if(defaultLauncherSpace != null){
			final NewTspProblemParameters newParams = new NewTspProblemParameters();
			newParams.params = p;
			defaultLauncherSpace.emit(newParams);
		}
	}
	
	@Override
	public UUID getID() {
		return this.uuid; 
	}
	
	public boolean isAppParametersUpToDate(){
		return appParametersUpToDate;
	}

	@Override
	protected String getSolverName() {
		return "SARL AcoSolver";
	}

	// Solving process

	@Override
	public void startSolving(TspGraph graph) {
		this.graph = graph;
		getParameters().watchParametersChange(this);
		Boot.setOffline(true);
		Boot.setVerboseLevel(LoggerCreator.toInt(Level.INFO));
		try {
			Boot.startJanus(
					(Class) null,
					Launcher.class,
					this
			);
		} catch (Exception e) {
			System.exit(-1);
		}
	}
	
	public void sarlReady(){
		if(this.defaultLauncherSpace != null){
			final StartSolvingEvent evt = new StartSolvingEvent(); 
			evt.graph = graph;
			this.defaultLauncherSpace.emit(evt);
		}
	}

	public void setEventSpace(EventSpace defaultLauncherSpace){
		this.defaultLauncherSpace = defaultLauncherSpace;
	}

	@Override
	public void stopSolving() {
		if(this.defaultLauncherSpace != null){
			this.defaultLauncherSpace.emit(new StopSolvingEvent());
		}
	}

	@Override
	public void newGraphState(final TspGraph g) {
		graph = g;
		this.notifyNewGraphState(g);
	}

	@Override
	public void graphUpdated(TspGraph g) {
		graph = g;
		if(this.defaultLauncherSpace != null){
			final NewGraphState evt = new NewGraphState();
			evt.graph = g;
			this.defaultLauncherSpace.emit(evt);
		}
	}
}
