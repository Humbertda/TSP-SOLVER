import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.filereader.TspProblemReader;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspCommonLibrary;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspProblem;

public class TspReaderTest extends ATest {

	public static void main(String args[]){
		TspProblemReader fr = new TspProblemReader();
		try {
			for(TspProblem p : TspCommonLibrary.getAllCommonProblem()){
				final TspGraph tspGraph = fr.readFromString(p.getDesc());
				final StringBuilder sb = new StringBuilder();
				sb.append(p.getName()).append(" converted into : \n").append(tspGraph).append("\n");
				log(sb.toString());
			}
			newLine();
		} catch (ParsingException e) {
			final StringBuilder sb = new StringBuilder();
			sb.append(e.getMessage());
			if (e.getCause() != null){
				sb.append("\nCause: ");
				sb.append(e.getCause().getMessage());
			}
			logError(sb.toString());
		}
	}


}
