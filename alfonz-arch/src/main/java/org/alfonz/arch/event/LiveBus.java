package org.alfonz.arch.event;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.lifecycle.LifecycleOwner;

import java.util.Map;

public class LiveBus {
	private final Map<Class<? extends Event>, LiveEvent<? extends Event>> mEventMap;

	public LiveBus() {
		mEventMap = new ArrayMap<>();
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> void observe(@NonNull LifecycleOwner lifecycleOwner, @NonNull Class<T> eventClass, @NonNull EventObserver<T> eventObserver) {
		LiveEvent<T> liveEvent = (LiveEvent<T>) mEventMap.get(eventClass);
		if (liveEvent == null) {
			liveEvent = initLiveEvent(eventClass);
		}
		liveEvent.observe(lifecycleOwner, eventObserver);
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> void removeObservers(@NonNull LifecycleOwner lifecycleOwner, @NonNull Class<T> eventClass) {
		LiveEvent<T> liveEvent = (LiveEvent<T>) mEventMap.get(eventClass);
		if (liveEvent != null) {
			liveEvent.removeObservers(lifecycleOwner);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Event> void send(@NonNull T event) {
		LiveEvent<T> liveEvent = (LiveEvent<T>) mEventMap.get(event.getClass());
		if (liveEvent == null) {
			liveEvent = initLiveEvent((Class<T>) event.getClass());
		}
		liveEvent.setValue(event);
	}

	@NonNull
	private <T extends Event> LiveEvent<T> initLiveEvent(Class<T> eventClass) {
		LiveEvent<T> liveEvent = new LiveEvent<>();
		mEventMap.put(eventClass, liveEvent);
		return liveEvent;
	}
}
