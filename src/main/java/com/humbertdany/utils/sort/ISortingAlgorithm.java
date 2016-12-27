package com.humbertdany.utils.sort;

import java.util.Collection;

/**
 *
 * @author dhumbert
 */
public interface ISortingAlgorithm<T>{
	String getAlgorithmName();
	Collection<T> sort(final T[] toSort);
	Collection<T> sort(Collection<T> toSort);

}