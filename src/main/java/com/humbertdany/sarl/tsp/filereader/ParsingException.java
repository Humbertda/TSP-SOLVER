package com.humbertdany.sarl.tsp.filereader;

public class ParsingException extends Exception {

	public ParsingException(String msg, Throwable cause){
		super(msg, cause);
	}

	public ParsingException(String msg){
		super(msg);
	}

}
