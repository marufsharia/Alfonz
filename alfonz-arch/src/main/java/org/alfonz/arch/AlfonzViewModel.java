package org.alfonz.arch;

import org.alfonz.arch.event.Event;
import org.alfonz.arch.event.EventObserver;
import org.alfonz.arch.event.LiveBus;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

public abstract class AlfonzViewModel extends ViewModel implements Observable {
	private transient PropertyChangeRegistry mObservableCallbacks;
	private LiveBus mLiveBus = new LiveBus();

	@Override
	public synchronized void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
		if (mObservableCallbacks == null) {
			mObservableCallbacks = new PropertyChangeRegistry();
		}
		mObservableCallbacks.add(callback);
	}

	@Override
	public synchronized void removeOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
		if (mObservableCallbacks != null) {
			mObservableCallbacks.remove(callback);
		}
	}

	public synchronized void notifyChange() {
		if (mObservableCallbacks != null) {
			mObservableCallbacks.notifyCallbacks(this, 0, null);
		}
	}

	public void notifyPropertyChanged(int fieldId) {
		if (mObservableCallbacks != null) {
			mObservableCallbacks.notifyCallbacks(this, fieldId, null);
		}
	}

	public <T extends Event> void observeEvent(@NonNull LifecycleOwner lifecycleOwner, @NonNull Class<T> eventClass, @NonNull EventObserver<T> eventObserver) {
		mLiveBus.observe(lifecycleOwner, eventClass, eventObserver);
	}

	public <T extends Event> void removeEventObservers(@NonNull LifecycleOwner lifecycleOwner, @NonNull Class<T> eventClass) {
		mLiveBus.removeObservers(lifecycleOwner, eventClass);
	}

	public <T extends Event> void sendEvent(@NonNull T event) {
		mLiveBus.send(event);
	}
}
