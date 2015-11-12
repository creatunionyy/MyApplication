package com.library.core.event;

public abstract class BaseEventManager {

	public static interface OnEventRunner{
		public void onEventRun(Event event) throws Exception;
	}

	public static interface OnEventListener{
		public void onEventRunEnd(Event event);
	}

	public abstract void registerEventRunner(Event event, OnEventRunner runner);

	public abstract void addEventListener(Event event, OnEventListener listener);

	public abstract Event pushEvent(Event event, OnEventListener listener,OnEventRunner runner);

	public abstract Event runEvent(Event event);


}
