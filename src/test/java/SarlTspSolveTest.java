import java.util.List;

import com.humbertdany.sarl.tsp.solver.SolverObserver;
import com.humbertdany.sarl.tsp.solver.aco.AntColonyTspSolver;
import com.humbertdany.sarl.tsp.solver.aco.params.AcoParameters;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspCommonLibrary;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspProblem;
import com.humbertdany.sarl.tsp.ui.tsppopup.TsplibListItem;

public class SarlTspSolveTest extends ATest {

	public static void main(String[] args){
		AcoParameters testParams = AcoParameters.buildDefault();
		testParams.setMsBetweenTick(500);
		AntColonyTspSolver solver = new AntColonyTspSolver(runnable -> {
			runnable.run();
		}, testParams);
		TspGraph graph = TsplibListItem.fromTspProblem(new TspProblem("Sarl Aco Test", TspCommonLibrary.EASY_CHALLENGE)).generateGraph();
		solver.onSolverDone(new SolverObserver(){
			@Override
			public void onTspProblemSolved() {
				// This wont happen in ACO
			}
			@Override
			public void onNewGraphState(TspGraph g) {
				// We dont care of it in tests
			}
			@Override
			public void onNewBestPath(List<TspVertex> flow, double cost) {
				log("New best cost " + cost); 
			}
		});
		solver.startSolving(graph);
	}

}
