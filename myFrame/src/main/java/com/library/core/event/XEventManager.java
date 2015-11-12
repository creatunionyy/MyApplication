package com.library.core.event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @Description:
 * @Functions:
 * @Author:
 * @Date: 2015-07-02
 */
public class XEventManager extends BaseEventManager {

	private static XEventManager sInstance;
	private static final int WHAT_EVENT_NOTIFY = 1;
	private static final int WHAT_EVENT_PUSH = 2;
	private static final int WHAT_EVENT_END = 3;
	private ExecutorService mExecutorService;

	private Map<Event, Event> mMapRunningEvent = new ConcurrentHashMap<Event, Event>();

	private Map<Event, List<BaseEventManager.OnEventRunner>> mMapRunner = new ConcurrentHashMap<Event, List<BaseEventManager.OnEventRunner>>();

	private Map<Event, List<BaseEventManager.OnEventListener>> mMapListener = new ConcurrentHashMap<Event, List<BaseEventManager.OnEventListener>>();
	private List<Event> mListEventNotify = new LinkedList<Event>();
	private boolean mIsEventNotifying;

	public static XEventManager getInstance() {
		if (sInstance == null) {
			sInstance = new XEventManager();
		}
		return sInstance;
	}

	private XEventManager() {
		mExecutorService = Executors.newCachedThreadPool();
	}

	private static Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			final int nWhat = msg.what;
			switch (nWhat) {
			case WHAT_EVENT_PUSH:
				final Event event = (Event) msg.obj;
				if (!sInstance.isEventRunning(event)) {
					sInstance.mExecutorService.execute(new Runnable() {
						@Override
						public void run() {
							sInstance.processEvent(event);
							mHandler.sendMessage(mHandler.obtainMessage(
									WHAT_EVENT_END, event));
						}
					});
				}
				break;
			case WHAT_EVENT_END:
				sInstance.onEventRunEnd((Event) msg.obj);
				break;
			case WHAT_EVENT_NOTIFY:
				sInstance.doNotify((Event) msg.obj);
				break;
			}
		}
	};

	@Override
	public Event pushEvent(Event event, OnEventListener listener,
			OnEventRunner runner) {
		registerEventRunner(event, runner);
		addEventListener(event, listener);
		mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_PUSH, event));
		return event;
	}

	@Override
	public void registerEventRunner(Event event, OnEventRunner runner) {
		if (!isRunnerContained(event.getEventCode())) {
			List<OnEventRunner> runners = new LinkedList<OnEventRunner>();
			mMapRunner.put(event, runners);
			runners.add(runner);
		}
	}

	@Override
	public void addEventListener(Event event, OnEventListener listener) {
		if (!isListenerContained(event.getEventCode())) {
			List<OnEventListener> listeners = new LinkedList<OnEventListener>();
			mMapListener.put(event, listeners);
			listeners.add(listener);
		}
	}

	@Override
	public Event runEvent(Event event) {
		processEvent(event);
		mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_END, event));
		return event;
	}

	protected boolean processEvent(Event event) {
		if (isEventRunning(event)) {
			return false;
		}
		mMapRunningEvent.put(event, event);
		try {
			List<OnEventRunner> runners = mMapRunner.get(event);
			if (runners != null) {
				for (OnEventRunner runner : runners) {
					runner.onEventRun(event);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			event.setFailException(e);
		} finally {
			mMapRunningEvent.remove(event);
		}
		return true;
	}

	protected void onEventRunEnd(Event event) {
		if (mIsEventNotifying) {
			mListEventNotify.add(event);
		} else {
			doNotify(event);
		}
	}

	private void doNotify(Event event) {
		mIsEventNotifying = true;
		final List<OnEventListener> listeners = mMapListener.get(event);
		if (listeners != null && listeners.size() > 0) {
			for (OnEventListener listener : listeners) {
				try {
					listener.onEventRunEnd(event);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mMapListener.remove(event);
		}
		mIsEventNotifying = false;
		if (mListEventNotify.size() > 0) {
			Event eventNotify = mListEventNotify.get(0);
			mListEventNotify.remove(0);
			mHandler.sendMessage(mHandler.obtainMessage(WHAT_EVENT_NOTIFY,
					eventNotify));
		}
	}

	public boolean isEventRunning(Event e) {
		return mMapRunningEvent.containsKey(e);
	}

	public boolean isRunnerContained(int eventCode) {
		return mMapRunner.containsKey(eventCode);
	}

	public boolean isListenerContained(int eventCode) {
		return mMapListener.containsKey(eventCode);
	}

}
