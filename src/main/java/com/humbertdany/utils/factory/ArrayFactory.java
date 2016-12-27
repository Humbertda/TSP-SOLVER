package com.humbertdany.utils.factory;

/**
 *
 * @author dhumbert
 */
abstract public class ArrayFactory<T> {
	abstract public T[] buildArray(int dimension);

	// ######
	// Classic Static
	// ######

	public static ArrayFactory<String> buildStringFactory(){
		return new ArrayFactory<String>() {
			@Override
			public String[] buildArray(int dimension) {
				return new String[dimension];
			}
		};
	}

	public static ArrayFactory<Integer> buildIntegerFactory(){
		return new ArrayFactory<Integer>() {
			@Override
			public Integer[] buildArray(int dimension) {
				return new Integer[dimension];
			}
		};
	}



}