package com.humbertdany.sarl.tsp.solver.aco;

import com.humbertdany.sarl.tsp.core.params.ApplicationParametersObserver;
import com.humbertdany.sarl.tsp.solver.ATspSolver;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;
import com.humbertdany.sarl.tsp.solver.aco.ui.AcoGuiController;
import javafx.scene.layout.Pane;

public class AntColonyTspSolver extends ATspSolver implements ApplicationParametersObserver<AcoParameters> {

	final private AcoParameters parameters;

	public AntColonyTspSolver(){
		parameters = AcoParameters.buildDefault();
		getParameters().watchParametersChange(this);
	}

	@Override
	public void buildGui(final Pane ctrl) {
		final AcoGuiController gCtrl = new AcoGuiController(this);
		gCtrl.build(ctrl);
	}

	public AcoParameters getParameters() {
		return parameters;
	}

	@Override
	public void parametersChanged(final AcoParameters newParams) {
		System.out.println(newParams);
	}

}
