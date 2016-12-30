package com.humbertdany.sarl.tsp.solver;

import com.humbertdany.sarl.tsp.solver.aco.AntColonyTspSolver;
import com.humbertdany.sarl.tsp.solver.tester.AntColonySolverTester;

import java.util.ArrayList;
import java.util.List;

final public class TspSolverLibrary {

	/*** SOLVERS ****/

	public final AntColonySolverTester solverTest = new AntColonySolverTester();
	public final AntColonyTspSolver solverAcoSarl = new AntColonyTspSolver();

	/*** /SOLVERS ****/

	private TspSolverLibrary(){
		register(solverTest);
		register(solverAcoSarl);
	}

	public static TspSolverLibrary init(){
		return new TspSolverLibrary();
	}

	private ArrayList<ATspSolver> solverRegister = new ArrayList<>();

	protected void register(final ATspSolver solver){
		solverRegister.add(solver);
	}

	public List<ATspSolver> getSolvers(){
		return solverRegister;
	}

}
