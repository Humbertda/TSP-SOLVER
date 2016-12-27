package com.humbertdany.utils.sort;

import com.humbertdany.utils.factory.ArrayFactory;

import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author dhumbert
 */
public class SortingFusion<T extends Comparable> extends ASortingAlgorithm<T>{

	public SortingFusion(final ArrayFactory<T> arrayFactory){
		super(arrayFactory);
	}

	@Override
	public Collection<T> sort(T[] toSort) {
		return Arrays.asList(this.mergeSort(toSort));
	}

	@Override
	public String getAlgorithmName(){
		return "Fusion";
	}

	private T[] mergeSort(T[] list)
	{
		//If list is empty; no need to do anything
		if (list.length <= 1) {
			return list;
		}

		//Split the array in half in two parts
		final ArrayFactory<T> factory = this.getArrayFactory();
		T[] first = factory.buildArray(list.length / 2);
		T[] second = factory.buildArray(list.length - first.length);
		System.arraycopy(list, 0, first, 0, first.length);
		System.arraycopy(list, first.length, second, 0, second.length);

		//Sort each half recursively
		mergeSort(first);
		mergeSort(second);

		//Merge both halves together, overwriting to original array
		this.merge(first, second, list);
		return list;
	}

	private void merge(T[] first, T[] second, T[] result)
	{
		//Index Position in first array - starting with first element
		int iFirst = 0;

		//Index Position in second array - starting with first element
		int iSecond = 0;

		//Index Position in merged array - starting with first position
		int iMerged = 0;

		//Compare elements at iFirst and iSecond,
		//and move smaller element at iMerged
		while (iFirst < first.length && iSecond < second.length)
		{
			if (first[iFirst].compareTo(second[iSecond]) < 0)
			{
				result[iMerged] = first[iFirst];
				iFirst++;
			}
			else
			{
				result[iMerged] = second[iSecond];
				iSecond++;
			}
			iMerged++;
		}
		//copy remaining elements from both halves - each half will have already sorted elements
		System.arraycopy(first, iFirst, result, iMerged, first.length - iFirst);
		System.arraycopy(second, iSecond, result, iMerged, second.length - iSecond);
	}


}