package com.humbertdany.sarl.tsp.ui.mainui;

import com.humbertdany.sarl.tsp.solver.ATspSolver;

public interface D3GraphDisplayable {

	String getD3String(final ATspSolver solver);

}
