package com.humbertdany.sarl.tsp.core.params;

public interface ApplicationParametersObserver<T extends AApplicationParameters> {

	public void parametersChanged(final T newParams);

}
