package com.luzi82.lang;

import java.lang.ref.WeakReference;
import java.util.Observable;
import java.util.Observer;

public abstract class WeakObserver<H> implements Observer {

	public final WeakReference<H> ref;

	public WeakObserver(H host) {
		ref = new WeakReference<H>(host);
	}

	@Override
	public final void update(Observable o, Object arg) {
		H h = ref.get();
		if (h == null) {
			o.deleteObserver(this);
			return;
		}
		update(h, o, arg);
	}

	protected abstract void update(H h, Observable o, Object arg);

}
