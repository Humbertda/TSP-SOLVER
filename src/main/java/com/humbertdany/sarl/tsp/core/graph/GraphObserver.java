package com.humbertdany.sarl.tsp.core.graph;

public interface GraphObserver<T extends Graph> {

	void graphUpdated(final T g);

}
