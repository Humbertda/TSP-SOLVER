package com.humbertdany.sarl.tsp.filereader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.humbertdany.sarl.tsp.filereader.parser.ITspFileReader;
import com.humbertdany.sarl.tsp.filereader.parser.TsplibTspFileReader;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TspProblemReader {

	private Logger logger;

	private final List<ITspFileReader> fileReaders = new ArrayList<>();

	private static final String ERROR_MESSAGE = "All the known ITspFileReader tested failed to read your file";

	public TspProblemReader(){
		fileReaders.addAll(Arrays.asList(
				new TsplibTspFileReader()
		));
	}

	public TspGraph readFromString(String s) throws ParsingException{
		for(ITspFileReader fr : this.fileReaders){
			try {
				final TspGraph res = fr.readFromString(s);
				if(res != null){
					return res;
				}
			} catch (ParsingException e){
				// Has been catched, but will try them all
				logError(e.getMessage());
			}
		}
		throw new ParsingException(ERROR_MESSAGE);
	}

	/**
	 * !!! untested
	 * @param f the File to parse
	 * @return the TspGraph generated
	 * @throws ParsingException if unable to create TspGraph valid
	 */
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
					logError(e.getMessage());
				}
			}
		} catch (IOException e) {
			throw new ParsingException("IOException received", e);
		}
		if(res!=null){
			return res;
		}
		throw new ParsingException(ERROR_MESSAGE);
	}

	@JsonIgnore
	protected final void log(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.info("\n" + o.toString());
	}

	@JsonIgnore
	protected final void logError(final Object o){
		if(logger == null){
			initLogger();
		}
		logger.error("\n" + o.toString());
	}

	@JsonIgnore
	private void initLogger(){
		logger = LogManager.getLogger(this.getClass());
	}
}
