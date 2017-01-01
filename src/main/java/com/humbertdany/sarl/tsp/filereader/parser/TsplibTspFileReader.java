package com.humbertdany.sarl.tsp.filereader.parser;

import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.filereader.parser.tsplib.ATspDataTypeReader;
import com.humbertdany.sarl.tsp.filereader.parser.tsplib.TspDataTypeReaderAtt;
import com.humbertdany.sarl.tsp.filereader.parser.tsplib.TspDataTypeReaderEuc2D;
import com.humbertdany.sarl.tsp.filereader.parser.tsplib.TspDataTypeReaderGeo;
import com.humbertdany.sarl.tsp.tspgraph.TspEdgeData;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import com.humbertdany.sarl.tsp.tspgraph.TspVertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://github.com/thomasjungblut/antcolonyopt
 */
public class TsplibTspFileReader extends ATspProblemReader {

	public static final String DISPLAY_DATA_TYPES_STR = "EDGE_WEIGHT_TYPE";

	public TsplibTspFileReader(){

	}

	public final TspGraph readFromString(final String f) throws ParsingException {
		List<String> lines = Arrays.asList(f.split("\n"));
		final ArrayList<Record> records = new ArrayList<>();
		ATspDataTypeReader reader = null;
		boolean readAhead = false;
		final List<String> unknownParams = new ArrayList<>();

		try {
			for(String line : lines){

				if (line.equals(END_OF_FILE)) {

					break;

				} else if(reader == null){

					if(line.toLowerCase().contains(DISPLAY_DATA_TYPES_STR.toLowerCase())){
						final String[] split = line.split(":");
						if(split[1] != null){
							final String type = split[1].trim();
							switch (type){
								case "GEO":
									reader = new TspDataTypeReaderGeo();
									break;
								case "EUC_2D":
									reader = new TspDataTypeReaderEuc2D();
									break;
								case "ATT":
									reader = new TspDataTypeReaderAtt();
									break;
								default:
									throw new ParsingException("EDGE_WEIGHT_TYPE not recognized");
							}
							for(String s : unknownParams){
								reader.addUnknownParams(s);
							}
						}
					} else {
						unknownParams.add(line);
					}
				} else if (readAhead) {
					final Record read = reader.read(line);
					if(read != null){
						records.add(read);
					} else {
						throw new ParsingException("ATspDataTypeReader could not read the datas (not implemented)");
					}
				} else if (line.equals("NODE_COORD_SECTION")) {
					readAhead = true;
				} else {
					unknownParams.add(line);
					reader.addUnknownParams(line);
				}
			}
			final TspGraph graphFromRecords = this.getGraphFromRecords(records);
			if(graphFromRecords.size() <= 0){
				throw new ParsingException("The graph is empty");
			}
			return graphFromRecords;
		} catch (ParsingException e){
			final StringBuilder sb = new StringBuilder();
			sb.append("The file could not be read: ").append(e.getMessage());
			if (e.getCause() != null){
				sb.append("\nCause: ");
				sb.append(e.getCause().getMessage());
			}
			throw new ParsingException(sb.toString(), e);
		}
	}

	@Override
	void addEdgeToGraph(TspGraph g, TspVertex from, TspVertex to) {
		g.addEdge(from, to, null);
	}
}