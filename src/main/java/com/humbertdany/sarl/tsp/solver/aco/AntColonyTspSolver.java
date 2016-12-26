package com.humbertdany.sarl.tsp.solver.aco;

import java.util.UUID;
import java.util.logging.Level;

import com.humbertdany.sarl.tsp.core.graph.Graph;
import com.humbertdany.sarl.tsp.core.params.ApplicationParametersObserver;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.solver.aco.sarl.*;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;
import com.humbertdany.sarl.tsp.solver.aco.ui.AcoGuiController;

import io.janusproject.Boot;
import io.janusproject.util.LoggerCreator;
import javafx.scene.layout.Pane;

public class AntColonyTspSolver extends ATspSolver implements ApplicationParametersObserver<AcoParameters>, EnvironmentListener {

	final private UUID uuid = UUID.randomUUID();
	
	final private AcoParameters parameters;
	
	private boolean appParametersUpToDate = false;

	public AntColonyTspSolver(){
		parameters = AcoParameters.buildDefault();
	}

	void solve(){
		// TODO implementation of this
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

	@Override
	public void buildGui(final Pane ctrl) {
		final AcoGuiController gCtrl = new AcoGuiController(this);
		gCtrl.build(ctrl);
	}

	public AcoParameters getParameters() {
		appParametersUpToDate = true;
		return parameters;
	}

	@Override
	public void parametersChanged(final AcoParameters newParams) {
		appParametersUpToDate = false; 
	}
	
	@Override
	public UUID getID() {
		return this.uuid; 
	}

	@Override
	public void newGraphState(final Graph g) {
		g.test();
	}
	
	public boolean isAppParametersUpToDate(){
		return appParametersUpToDate;
	}

	@Override
	protected String getSolverName() {
		return "SARL AcoSolver";
	}

}
