package com.humbertdany.sarl.tsp.core.params;

import java.io.Serializable;
import java.util.ArrayList;

abstract public class AApplicationParameters implements Serializable {

	final private ArrayList<ApplicationParametersObserver> observers = new ArrayList<>();

	final public void watchParametersChange(final ApplicationParametersObserver observer){
		observers.add(observer);
	}

	protected void notify(final AApplicationParameters params){
		for(ApplicationParametersObserver o : observers){
			o.parametersChanged(params);
		}
	}

	protected AApplicationParameters(){
		// Nothing done here
	}

}
