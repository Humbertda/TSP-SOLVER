package com.humbertdany.sarl.tsp.filereader;

import com.humbertdany.sarl.tsp.filereader.parser.ITspFileReader;
import com.humbertdany.sarl.tsp.filereader.parser.ThomasJungBlutFileReader;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TspProblemReader {

	private final List<ITspFileReader> fileReaders = new ArrayList<>();

	public TspProblemReader(){
		fileReaders.addAll(Arrays.asList(
				new ThomasJungBlutFileReader()
		));
	}

	public TspGraph readFromString(String s) throws ParsingException{
		TspGraph res = null;
		for(ITspFileReader fr : this.fileReaders){
			try {
				res = fr.readFromString(s);
			} catch (ParsingException e){
				// Has been catched, but will try them all
			}
		}
		if(res!=null){
			return res;
		}
		throw new ParsingException("All the known ITspFileReader tested failed to read your file");
	}

	public TspGraph readFromFile(File f) throws ParsingException {
		final List<String> lines = new ArrayList<>();
		TspGraph res = null;
		try (final BufferedReader br = new BufferedReader(new FileReader(f))) {

			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}

			final StringBuilder sb = new StringBuilder();
			for(String s : lines){
				sb.append(s);
				sb.append("\n");
			}
			final String bString = sb.toString();
			for(ITspFileReader fr : this.fileReaders){
				try {
					res = fr.readFromString(bString);
				} catch (ParsingException e){
					// Has been catched, but will try them all
				}
			}
		} catch (IOException e) {
			throw new ParsingException("IOException received", e);
		}
		if(res!=null){
			return res;
		}
		throw new ParsingException("All the known ITspFileReader tested failed to read your file");
	}
}
