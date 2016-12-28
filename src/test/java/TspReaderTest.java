import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.filereader.TspProblemReader;
import com.humbertdany.sarl.tsp.tspgraph.lib.TspCommonLibrary;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;

public class TspReaderTest extends ATest {

	public static void main(String args[]){
		TspProblemReader fr = new TspProblemReader();
		try {
			final TspGraph tspGraph = fr.readFromString(TspCommonLibrary.BERLIN_52);
			log(tspGraph);
			newLine();
		} catch (ParsingException e) {
			e.printStackTrace();
		}
	}


}
