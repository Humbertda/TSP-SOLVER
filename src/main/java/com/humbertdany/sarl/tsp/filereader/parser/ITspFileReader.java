package com.humbertdany.sarl.tsp.filereader.parser;


import com.humbertdany.sarl.tsp.filereader.ParsingException;
import com.humbertdany.sarl.tsp.tspgraph.TspGraph;

public interface ITspFileReader {

	String END_OF_FILE = "EOF";

	TspGraph readFromString(final String f) throws ParsingException;

}
