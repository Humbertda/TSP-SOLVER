package com.humbertdany.sarl.tsp.solver.aco;

import com.humbertdany.sarl.tsp.core.params.ApplicationParametersObserver;
import com.humbertdany.sarl.tsp.core.ui.MAnchorPane;
import com.humbertdany.sarl.tsp.solver.ASarlSolver;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;
import com.humbertdany.sarl.tsp.solver.aco.sarl.*;
import com.humbertdany.sarl.tsp.solver.aco.ui.AcoGuiController;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import io.janusproject.Boot;
import io.janusproject.util.LoggerCreator;
import io.sarl.lang.core.EventSpace;

import java.util.UUID;
import java.util.logging.Level;

public class AntColonyTspSolver extends ASarlSolver implements ApplicationParametersObserver<AcoParameters>, EnvironmentListener {
	
	private AcoParameters parameters;

	public AntColonyTspSolver(){
		parameters = AcoParameters.buildDefault();
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
	protected String getSolverName() {
		return "SARL AcoSolver";
	}

	// Solving process

	@Override
	public void startSarlSolving() {
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


}
