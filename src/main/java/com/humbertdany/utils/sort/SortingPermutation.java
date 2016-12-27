package com.humbertdany.utils.sort;

import com.humbertdany.utils.factory.ArrayFactory;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author dhumbert
 */
public class SortingPermutation<T extends Comparable> extends ASortingAlgorithm<T>{

	public SortingPermutation(final ArrayFactory<T> arrayFactory){
		super(arrayFactory);
	}

	@Override
	public Collection<T> sort(T[] toSort) {
		bogoSort(toSort);
		return Arrays.asList(toSort);
	}


	/**
	 * Sorts array a[0..n-1] using Bogo sort
	 * @param a
	 */
	private void bogoSort(T[] a)
	{
		// if array is not sorted then shuffle the
		// array again
		while (!isSorted(a))
			shuffle(a);
	}

	/**
	 * To generate permuatation of the array
	 * @param a
	 */
	private void shuffle(T[] a)
	{
		// Math.random() returns a double positive
		// value, greater than or equal to 0.0 and
		// less than 1.0.
		for (int i=1; i < a.length; i++)
			swap(a, i, (int)(Math.random()*i));
	}

	@Override
	public String getAlgorithmName(){
		return "Permutation";
	}

}