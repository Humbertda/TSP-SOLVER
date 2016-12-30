package com.humbertdany.sarl.tsp.core.utils;

public interface Runner<T extends Runnable> {
	void run(T runnable);
}
