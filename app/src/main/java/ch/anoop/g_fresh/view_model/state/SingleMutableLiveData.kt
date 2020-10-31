package ch.anoop.g_fresh.view_model.state

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A observable that sends only new updates, also aware of lifecycle
 */
class SingleMutableLiveData<T> : MutableLiveData<T>() {

    // State of the result weather completed or still in-progress
    private val isInProgress = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        // Observe internally
        super.observe(owner, { t ->
            if (isInProgress.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        isInProgress.set(true)
        super.setValue(t)
    }

    /**
     * Used for making fresh calls, reset
     */
    @MainThread
    fun newCall() {
        value = null
    }
}
