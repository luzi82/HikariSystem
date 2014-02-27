package com.luzi82.lang;

import java.util.Observable;

public class GuriObservable<T> extends Observable {

	public T value;

	public void set(T value) {
		this.value = value;
		setChanged();
	}

	public void setNotify(T value) {
		set(value);
		notifyObservers();
	}

	public T get() {
		return value;
	}

}
