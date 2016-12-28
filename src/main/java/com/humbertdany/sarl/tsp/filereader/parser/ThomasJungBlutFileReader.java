package com.humbertdany.sarl.tsp.filereader.parser;

import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://github.com/thomasjungblut/antcolonyopt
 */
public class ThomasJungBlutFileReader extends ATspProblemReader {

	public ThomasJungBlutFileReader(){

	}

	public final TspGraph readFromString(final String f) throws ParsingException {
		List<String> lines = Arrays.asList(f.split("\n"));
		final ArrayList<Record> records = new ArrayList<>();
		boolean readAhead = false;
		for(String line : lines){
			if (line.equals(END_OF_FILE)) {
				break;
			}
			if (readAhead) {
				String[] split = sweepNumbers(line.trim());
				records.add(new Record(Double.parseDouble(split[1].trim()), Double
						.parseDouble(split[2].trim())));
			}
			if (line.equals("NODE_COORD_SECTION")) {
				readAhead = true;
			}
		}
		final TspGraph graphFromRecords = this.getGraphFromRecords(records);
		if(graphFromRecords.size() <= 0){
			throw new ParsingException("The graph is empty");
		}
		return graphFromRecords;
	}

	private static String[] sweepNumbers(final String input) {
		final String[] arr = new String[3];
		int currentIndex = 0;
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if ((c) != 32) {
				for (int f = i + 1; f < input.length(); f++) {
					final char x = input.charAt(f);
					if ((x) == 32) {
						arr[currentIndex] = input.substring(i, f);
						currentIndex++;
						break;
					} else if (f == input.length() - 1) {
						arr[currentIndex] = input.substring(i, input.length());
						break;
					}
				}
				i = i + arr[currentIndex - 1].length();
			}
		}
		return arr;
	}

}