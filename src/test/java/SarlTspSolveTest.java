import com.humbertdany.sarl.tsp.solver.aco.AntColonyTspSolver;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspCommonLibrary;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspProblem;
import com.humbertdany.sarl.tsp.ui.tsppopup.TsplibListItem;

public class SarlTspSolveTest extends ATest {

	public static void main(String[] args){
		AntColonyTspSolver solver = new AntColonyTspSolver();
		TspGraph graph = TsplibListItem.fromTspProblem(new TspProblem("Berlin Test", TspCommonLibrary.BERLIN_52)).generateGraph();
		solver.startSolving(graph);

		solver.getParameters().setChecked(false);
	}

}
